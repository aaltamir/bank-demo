package com.ariel.bankdemo;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

/**
 * Just a helper service to allow better testing when using time
 */
@Service
public class TimeService {
    public LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }
}
