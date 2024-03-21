package com.example.demo.common.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

// from https://console.twilio.com/?frameUrl=%2Fconsole%3Fx-target-region%3Dus1&newCustomer=true

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
public class TwilioMessageSender implements MessageSender<Message> {
    private String accountSid;
    private String authToken;
    private String trialNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    @Override
    public Message send(String to, String content) {
        PhoneNumber toPhoneNumber = new PhoneNumber(to);
        PhoneNumber fromPhoneNumber = new PhoneNumber(trialNumber);

        return Message
                .creator(toPhoneNumber, fromPhoneNumber,
                        content)
                .create();
    }
}
