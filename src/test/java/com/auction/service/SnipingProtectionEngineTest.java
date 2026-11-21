package com.auction.service;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class SnipingProtectionEngineTest {

    @Test
    void testSoftCloseExtensionTriggered() {
        Instant end = Instant.parse("2026-12-01T12:00:00Z");
        Instant bid = Instant.parse("2026-12-01T11:58:30Z"); // 1m 30s remaining
        Duration window = Duration.ofMinutes(2);             // 2 min snipe window
        Duration ext = Duration.ofMinutes(5);                // 5 min extension

        Instant newEnd = SnipingProtectionEngine.evaluateSoftClose(end, bid, window, ext);
        assertEquals(Instant.parse("2026-12-01T12:03:30Z"), newEnd);
    }
}
