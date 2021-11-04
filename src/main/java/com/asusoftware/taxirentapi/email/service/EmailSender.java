package com.asusoftware.taxirentapi.email.service;

public interface EmailSender {
    void send(String to, String email);
}
