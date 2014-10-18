package org.andreptb.smtpforit.rest;

import org.andreptb.smtpforit.dto.MailMessage;
import org.andreptb.smtpforit.provider.MailMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailMessageEndpoint {

    @Autowired
    private MailMessageService server;

    @RequestMapping("/last")
    public @ResponseBody MailMessage lastEmail() {
        return server.getLastMessage();
    }

    @RequestMapping("/{index}")
    public @ResponseBody MailMessage emailByIndex(@PathVariable int index) {
        return server.getMessage(index);
    }
}
