package com.asusoftware.taxirentapi.email.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Override
    @Async // per non bloccare il client, e un metodo asincrono
    public void send(String to, String email) {
      try {
          MimeMessage mimeMessage = mailSender.createMimeMessage();
          MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
          mimeMessageHelper.setText(email, true);
          mimeMessageHelper.setTo(to);
          mimeMessageHelper.setSubject("Confirm your email");
          mimeMessageHelper.setFrom("taxi.rent.service.global@gmail.com");
          mailSender.send(mimeMessage);
      } catch (MessagingException e) {
          LOGGER.error("Failed to send email!", e);
          throw new IllegalStateException("Failed to send email!");
      }
    }
}
