
package com.github.andreptb.smtpserverforit.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.james.mime4j.dom.*;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.MultipartImpl;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import com.github.andreptb.smtpserverforit.dto.MessageDetails;
import com.github.andreptb.smtpserverforit.dto.MessageMetadata;
import com.github.andreptb.smtpserverforit.exception.MailMessageNotFound;
import com.github.andreptb.smtpserverforit.exception.MailMessageParseException;

@Component
public class MailMessageService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private Wiser wiser;

	@Value("${smtp.port:25}")
	private int port;

	@PostConstruct
	void start() {
		this.wiser = new Wiser(this.port);
		this.wiser.start();
	}

	@PreDestroy
	void stop() {
		this.wiser.stop();
		this.logger.info("SMTP server stop (was listening on port '{}')", this.port);
	}

	public MessageDetails getMessageDetails(int index) {
		return createMessageDetails(index);
	}

	private MessageDetails createMessageDetails(int index) {
		MessageDetails messageDetails = new MessageDetails();
		try {
			Message message = parseMessage(index);
			messageDetails.setMessageMetadata(createMessageMetadata(message, index));
			messageDetails.setBody(createMessageBody(message));
		} catch (IOException | RuntimeException e) {
			throw new MailMessageParseException(e);
		}
		return messageDetails;
	}

	private MessageMetadata createMessageMetadata(Message message, Object id) {
		MessageMetadata messageMetadata = new MessageMetadata();
		messageMetadata.setId(Objects.toString(id));
		messageMetadata.setFrom(parseMailAddress(message.getFrom()));
		messageMetadata.setTo(parseMailAddress(message.getTo()));
		messageMetadata.setCc(parseMailAddress(message.getCc()));
		messageMetadata.setBcc(parseMailAddress(message.getBcc()));
		messageMetadata.setSubject(message.getSubject());
		messageMetadata.setContentType(MediaType.TEXT_HTML_VALUE);
		messageMetadata.setCharset(message.getCharset());
		messageMetadata.setDate(message.getDate());
		return messageMetadata;
	}

	private List<String> parseMailAddress(AddressList addressList) {
		if (addressList == null) {
			return null;
		}
		return parseMailAddress(addressList.flatten());
	}

	private List<String> parseMailAddress(List<Mailbox> mailboxes) {
		return mailboxes.stream().map(Mailbox::getAddress).collect(Collectors.toList());
	}

	private boolean writeToFirstSingleBody(Disposable body, OutputStream out) throws IOException {
		if(body instanceof SingleBody) {
			((SingleBody) body).writeTo(out);
			return true;
		}
		if(body instanceof MultipartImpl) {
			for(Entity childBody : ((MultipartImpl) body).getBodyParts()) {
				if(writeToFirstSingleBody(childBody, out)) {
					return true;
				}
			}
		}
		if(body instanceof BodyPart) {
			return writeToFirstSingleBody(((BodyPart) body).getBody(), out);
		}
		return false;
	}

	private byte[] createMessageBody(Message message) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if(!writeToFirstSingleBody(message.getBody(), out)) {
			throw new MailMessageParseException("Message body not found");
		}
		return out.toByteArray();
	}

	private Message parseMessage(int index) throws IOException {
		return new DefaultMessageBuilder().parseMessage(new ByteArrayInputStream(getWiserMessage(index).getData()));
	}

	private WiserMessage getWiserMessage(int index) {
		List<WiserMessage> messages = this.wiser.getMessages();
		int indexToGet = index;
		if (index < 0) {
			indexToGet = messages.size() + index;
		}
		if (indexToGet < 0 || messages.size() <= indexToGet) {
			throw new MailMessageNotFound(index);
		}
		return messages.get(indexToGet);
	}
}
