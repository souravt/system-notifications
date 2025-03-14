package com.notification.service;

import com.notification.model.Notification;
import com.notification.model.UserPreference;
import com.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationProcessorTest {

    @Mock
    private UserPreferenceService userPreferenceService;
    @Mock
    private EmailDeliveryService emailDeliveryService;
    @Mock
    private SMSDeliveryService smsDeliveryService;
    @Mock
    private WhatsAppDeliveryService whatsAppDeliveryService;
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationProcessor notificationProcessor;

    private Notification testNotification;
    private UserPreference testPreference;

    @BeforeEach
    void setUp() {
        testNotification = new Notification();
        testNotification.setUserId("test-user");
        testNotification.setType("TEST_NOTIFICATION");
        testNotification.setContent("Test content");

        testPreference = new UserPreference();
        testPreference.setUserId("test-user");
        testPreference.setEmailEnabled(true);
        testPreference.setEmailAddress("test@example.com");
    }

    @Test
    void whenProcessNotification_withEmailEnabled_shouldSendEmail() {
        // Arrange
        when(userPreferenceService.getUserPreferences(anyString()))
            .thenReturn(testPreference);

        // Act
        notificationProcessor.process(testNotification);

        // Assert
        verify(emailDeliveryService, times(1)).send(testNotification);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void whenProcessNotification_withNoPreferences_shouldNotSendNotifications() {
        // Arrange
        when(userPreferenceService.getUserPreferences(anyString()))
            .thenReturn(null);

        // Act
        notificationProcessor.process(testNotification);

        // Assert
        verify(emailDeliveryService, never()).send(any());
        verify(smsDeliveryService, never()).send(any());
        verify(whatsAppDeliveryService, never()).send(any());
    }

    @Test
    void whenProcessNotification_withError_shouldSaveFailedStatus() {
        // Arrange
        when(userPreferenceService.getUserPreferences(anyString()))
            .thenReturn(testPreference);
        doThrow(new RuntimeException("Test error"))
            .when(emailDeliveryService).send(any());

        // Act
        try {
            notificationProcessor.process(testNotification);
        } catch (Exception e) {
            // Expected exception
        }

        // Assert
        verify(notificationRepository, times(1))
            .save(argThat(notification -> 
                "FAILED".equals(notification.getStatus())));
    }
}
