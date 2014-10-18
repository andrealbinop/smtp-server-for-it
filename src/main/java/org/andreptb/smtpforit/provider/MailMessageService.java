package org.andreptb.smtpforit.provider;

import org.andreptb.smtpforit.dto.MessageDetails;
import org.andreptb.smtpforit.dto.MessageMetadata;
import org.andreptb.smtpforit.exception.MailMessageNotFound;
import org.andreptb.smtpforit.exception.MailMessageParseException;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Body;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.SingleBody;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.AddressList;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class MailMessageService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Wiser wiser;

    @Value("${smtp.port:25}")
    private int port;

    @PostConstruct
    void start() {
        this.wiser = new Wiser(port);
        this.wiser.start();
    }

    @PreDestroy
    void stop() {
        this.wiser.stop();
        logger.info("SMTP server stop (was listening on port '{}')", port);
    }

	public MessageDetails getLastMessageDetails() {
		return getMessageDetails(wiser.getMessages().size() - 1);
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
		} catch(IOException | RuntimeException e) {
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
		messageMetadata.setContentType(message.getMimeType());
		messageMetadata.setCharset(message.getCharset());
		messageMetadata.setDate(message.getDate());
		return messageMetadata;
	}

	private List<String> parseMailAddress(AddressList addressList) {
		if(addressList == null) {
			return null;
		}
		return parseMailAddress(addressList.flatten());
	}

	private List<String> parseMailAddress(List<Mailbox> mailboxes) {
		return mailboxes.stream().map(Mailbox::getAddress).collect(Collectors.toList());
	}

	private byte[] createMessageBody(Message message) throws IOException {
		Body body = message.getBody();
		if(body instanceof SingleBody) {
			try (InputStream in = ((SingleBody) body).getInputStream()) {
				return IOUtils.toByteArray(in);
			}
		}
		throw new MailMessageParseException("Message body not found");
	}

	private Message parseMessage(int index) throws IOException {
		return new DefaultMessageBuilder().parseMessage(new ByteArrayInputStream(getWiserMessage(index).getData()));
	}

	private WiserMessage getWiserMessage(int index) {
		if(this.wiser.getMessages().size() <= index) {
			throw new MailMessageNotFound(index);
		}
		return this.wiser.getMessages().get(index);
	}
}
