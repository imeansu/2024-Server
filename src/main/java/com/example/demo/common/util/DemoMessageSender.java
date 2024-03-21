package com.example.demo.common.util;

import com.example.demo.common.service.MessageSender;

public class DemoMessageSender implements MessageSender<String> {
    @Override
    public String send(String to, String message) {
        return "DemoMessageSender: " + message + " to " + to;
    }
}
