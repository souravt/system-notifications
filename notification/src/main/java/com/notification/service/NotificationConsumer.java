package com.notification.service;

import com.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final NotificationProcessor notificationProcessor;
    private final DeduplicationService deduplicationService;

    @RabbitListener(queues = "notifications")
    public void consumeMessage(Notification notification) {
        try {
            log.info("Received notification for user: {}", notification.getUserId());
            
            if (deduplicationService.isDuplicate(notification)) {
                log.info("Duplicate notification detected, skipping processing");
                return;
            }

            notificationProcessor.process(notification);
        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage(), e);
            // TODO: Implement retry mechanism
        }
    }
}
