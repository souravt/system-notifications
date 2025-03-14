package com.notification.service;

import com.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailDeliveryService {
    
    private final JavaMailSender mailSender;
    
    public void send(Notification notification) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            // Set email properties
            helper.setFrom("${spring.mail.username}");
            helper.setTo(notification.getUserId()); // Assuming userId is the email address
            helper.setSubject(notification.getType());
            helper.setText(notification.getContent());
            
            mailSender.send(message);
            log.info("Email sent successfully to user: {}", notification.getUserId());
        } catch (Exception e) {
            log.error("Failed to send email to user: {}", notification.getUserId(), e);
            throw new RuntimeException("Email delivery failed", e);
        }
    }
}
