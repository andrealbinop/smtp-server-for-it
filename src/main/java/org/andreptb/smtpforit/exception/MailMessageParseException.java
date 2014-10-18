package org.andreptb.smtpforit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MailMessageParseException extends  RuntimeException {

	public MailMessageParseException(Throwable cause) {
		super("Mail message parse failed", cause);
	}

	public MailMessageParseException(String message) {
		super(message);
	}
}
