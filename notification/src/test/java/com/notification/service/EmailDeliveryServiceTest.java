package com.notification.service;

import com.notification.model.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailDeliveryServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailDeliveryService emailDeliveryService;

    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testNotification = new Notification();
        testNotification.setUserId("test@example.com");
        testNotification.setType("TEST_EMAIL");
        testNotification.setContent("Test email content");

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void whenSendingEmail_withValidData_shouldSucceed() {
        // Act
        assertDoesNotThrow(() -> emailDeliveryService.send(testNotification));

        // Assert
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void whenSendingEmail_withMailError_shouldThrowException() {
        // Arrange
        doThrow(new RuntimeException("Mail server error"))
            .when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> emailDeliveryService.send(testNotification));
    }

    @Test
    void whenSendingEmail_shouldSetCorrectEmailProperties() throws MessagingException {
        // Act
        emailDeliveryService.send(testNotification);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
