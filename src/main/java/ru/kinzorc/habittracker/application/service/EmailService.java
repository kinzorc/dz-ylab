package ru.kinzorc.habittracker.application.service;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(String recipient, String subject, String text) throws MessagingException;
}
