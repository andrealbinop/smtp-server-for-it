package org.andreptb.smtpforit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MailMessageNotFound extends RuntimeException {

    public MailMessageNotFound(int index) {
        super("No Message found with id: " + index);
    }
}
