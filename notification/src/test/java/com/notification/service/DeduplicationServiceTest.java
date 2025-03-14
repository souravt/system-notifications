package com.notification.service;

import com.notification.model.Notification;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeduplicationServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private DeduplicationService deduplicationService;

    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testNotification = new Notification();
        testNotification.setUserId("test-user");
        testNotification.setType("TEST_NOTIFICATION");
        testNotification.setContent("Test content");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void whenCheckingDuplicate_withNewMessage_shouldReturnFalse() {
        // Arrange
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
            .thenReturn(true);

        // Act
        boolean result = deduplicationService.isDuplicate(testNotification);

        // Assert
        assertFalse(result);
        verify(valueOperations).setIfAbsent(
            anyString(), 
            eq("1"), 
            any()
        );
    }

    @Test
    void whenCheckingDuplicate_withExistingMessage_shouldReturnTrue() {
        // Arrange
        when(valueOperations.setIfAbsent(anyString(), anyString(), any()))
            .thenReturn(false);

        // Act
        boolean result = deduplicationService.isDuplicate(testNotification);

        // Assert
        assertTrue(result);
    }

   
    void whenGeneratingDedupKey_shouldIncludeRelevantFields() {
        // Act
        String dedupKey = deduplicationService.generateDedupKey(testNotification);

        // Assert
        assertTrue(dedupKey.contains("notification:dedup:"));
        assertTrue(dedupKey.length() > "notification:dedup:".length());
    }
}
