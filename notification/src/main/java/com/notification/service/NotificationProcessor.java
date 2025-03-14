package com.notification.service;

import com.notification.model.Notification;
import com.notification.model.UserPreference;
import com.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationProcessor {

    private final UserPreferenceService userPreferenceService;
    private final EmailDeliveryService emailDeliveryService;
    private final SMSDeliveryService smsDeliveryService;
    private final WhatsAppDeliveryService whatsAppDeliveryService;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void process(Notification notification) {
        try {
            // Save notification
            notificationRepository.save(notification);

            // Get user preferences
            UserPreference preferences = userPreferenceService.getUserPreferences(notification.getUserId());

            if (preferences == null) {
                log.warn("No preferences found for user: {}", notification.getUserId());
                return;
            }

            // Route to appropriate channels based on user preferences
            if (preferences.isEmailEnabled()) {
                emailDeliveryService.send(notification);
            }
            
            if (preferences.isSmsEnabled()) {
                smsDeliveryService.send(notification);
            }
            
            if (preferences.isWhatsappEnabled()) {
                whatsAppDeliveryService.send(notification);
            }

            log.info("Successfully processed notification for user: {}", 
                    notification.getUserId());

        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage(), e);
            notification.setStatus("FAILED");
            notification.setRetryCount(notification.getRetryCount() + 1);
            notificationRepository.save(notification);
            throw e;
        }
    }
}
