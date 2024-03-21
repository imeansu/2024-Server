package com.example.demo.common.service;

public interface MessageSender<T> {

    public T send(String to, String message);
}
