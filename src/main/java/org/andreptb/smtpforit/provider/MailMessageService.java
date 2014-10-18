package org.andreptb.smtpforit.provider;

import org.andreptb.smtpforit.dto.MailMessage;
import org.andreptb.smtpforit.exception.MailMessageNotFound;
import org.apache.commons.io.IOUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.address.Address;
import org.apache.james.mime4j.dom.address.Mailbox;
import org.apache.james.mime4j.dom.address.MailboxList;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.message.MessageImpl;
import org.apache.james.mime4j.stream.EntityState;
import org.apache.james.mime4j.stream.Field;
import org.apache.james.mime4j.stream.MimeTokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

    public MailMessage getLastMessage() {
        return getMessage(wiser.getMessages().size() - 1);
    }

    public MailMessage getMessage(int index) {
        if(this.wiser.getMessages().size() <= index) {
            throw new MailMessageNotFound(index);
        }
        try {
            return parseMessageData(index, this.wiser.getMessages().get(index).getData());
        } catch(IOException | MimeException e) {
            throw new IllegalStateException("Unexpected exception parsing message with id " + index);
        }
    }

    private MailMessage parseMessageData(int index, byte[] messageData) throws IOException, MimeException {
        Message message = new DefaultMessageBuilder().parseMessage(new ByteArrayInputStream(messageData));
        MailMessage mailMessage = new MailMessage();
        mailMessage.setId(Integer.toString(index));
        mailMessage.setFrom(message.getFrom().stream().map(Mailbox::getAddress).collect(Collectors.toList()));
        mailMessage.setTo(message.getTo().flatten().stream().map(Mailbox::getAddress).collect(Collectors.toList()));
        mailMessage.setCc(message.getCc().flatten().stream().map(Mailbox::getAddress).collect(Collectors.toList()));
        mailMessage.setBcc(message.getFrom().stream().map(Mailbox::getAddress).collect(Collectors.toList()));
        mailMessage.setSubject(message.getSubject());
        mailMessage.setDate(message.getDate());
        return mailMessage;
    }
}
