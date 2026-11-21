package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class DutchAuctionServiceTest {

    @Test
    void testPriceDecrementHalfway() {
        Instant start = Instant.parse("2026-12-01T10:00:00Z");
        Instant current = Instant.parse("2026-12-01T10:30:00Z"); // 30 min in
        Duration duration = Duration.ofHours(1); // 60 min total

        BigDecimal startPrice = new BigDecimal("1000.00");
        BigDecimal floorPrice = new BigDecimal("400.00");

        // Halfway between 1000 and 400 is 700
        BigDecimal price = DutchAuctionService.calculateCurrentPrice(
                startPrice, floorPrice, start, current, duration, new BigDecimal("10.00"));

        assertEquals(new BigDecimal("700.00"), price);
    }
}
