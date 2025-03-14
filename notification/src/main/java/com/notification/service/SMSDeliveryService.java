package com.notification.service;

import com.notification.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SMSDeliveryService {
    
    public void send(Notification notification) {
        try {
            // TODO: Implement actual SMS sending logic using a provider like Twilio
            log.info("SMS notification sent (mock) to user: {}", notification.getUserId());
        } catch (Exception e) {
            log.error("Failed to send SMS to user: {}", notification.getUserId(), e);
            throw new RuntimeException("SMS delivery failed", e);
        }
    }
}
