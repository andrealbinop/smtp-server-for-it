package org.andreptb.smtpforit.rest;

import org.andreptb.smtpforit.dto.MessageDetails;
import org.andreptb.smtpforit.dto.MessageMetadata;
import org.andreptb.smtpforit.provider.MailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.Arrays;

@RestController
@RequestMapping("/email")
public class EmailMessageEndpoint {

    @Autowired
    private MailMessageService server;

    @RequestMapping("/{index}")
    public @ResponseBody MessageMetadata email(@PathVariable int index) {
        return server.getMessageDetails(index).getMessageMetadata();
    }

	@RequestMapping("/body/{index}")
	public ResponseEntity<byte[]> emailBody(@PathVariable int index) {
        MessageDetails messageDetails = server.getMessageDetails(index);
		byte[] body = messageDetails.getBody();
		HttpHeaders headers = new HttpHeaders();
		MessageMetadata messageMetadata = messageDetails.getMessageMetadata();
		headers.setContentType(MediaType.parseMediaType(messageMetadata.getContentType() + ";charset=" + messageMetadata.getCharset()));
		headers.setContentLength(body.length);
		return new ResponseEntity(body, headers, HttpStatus.OK);
	}
}
