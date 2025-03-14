package com.notification.service;

import com.notification.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WhatsAppDeliveryService {
    
    public void send(Notification notification) {
        try {
            // TODO: Implement actual WhatsApp sending logic using WhatsApp Business API
            log.info("WhatsApp message sent (mock) to user: {}", notification.getUserId());
        } catch (Exception e) {
            log.error("Failed to send WhatsApp message to user: {}", notification.getUserId(), e);
            throw new RuntimeException("WhatsApp delivery failed", e);
        }
    }
}
