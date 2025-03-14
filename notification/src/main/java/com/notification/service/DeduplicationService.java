package com.notification.service;

import com.notification.model.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeduplicationService {
    
    private static final String DEDUP_KEY_PREFIX = "notification:dedup:";
    private static final Duration DEDUP_WINDOW = Duration.ofHours(24);
    
    private final RedisTemplate<String, String> redisTemplate;
    
    public boolean isDuplicate(Notification notification) {
        String dedupKey = generateDedupKey(notification);
        Boolean wasAbsent = redisTemplate.opsForValue()
            .setIfAbsent(dedupKey, "1", DEDUP_WINDOW);
        
        return !Boolean.TRUE.equals(wasAbsent);
    }
    
    public String generateDedupKey(Notification notification) {
        return DEDUP_KEY_PREFIX + 
               Objects.hash(notification.getUserId(), 
                          notification.getType(), 
                          notification.getContent());
    }
}
