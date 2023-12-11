package com.epam.xstack.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_TIME_DURATION = 30 * 1000;

    private final LoadingCache<String, Integer> attemptsCache;

    public LoginAttemptService() {
        super();
        attemptsCache = CacheBuilder.newBuilder().
                expireAfterWrite(LOCK_TIME_DURATION, TimeUnit.MILLISECONDS).build(new CacheLoader<>() {

                    public Integer load(@Nonnull String key) {
                        return 0;
                    }
                });
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    public void loginSucceeded(HttpServletRequest request) {
        attemptsCache.invalidate(getClientIP(request));
    }

    public void loginFailed(HttpServletRequest request) {
        String key = getClientIP(request);
        int attempts = 0;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException ignored) {
        }
        attempts++;
        attemptsCache.put(key, attempts);
        log.error("ATTEMPT " + key + " " + attempts);
    }

    public boolean isBlocked(HttpServletRequest request) {
        try {
            return attemptsCache.get(getClientIP(request)) >= MAX_ATTEMPTS;
        } catch (ExecutionException e) {
            return false;
        }
    }


}

