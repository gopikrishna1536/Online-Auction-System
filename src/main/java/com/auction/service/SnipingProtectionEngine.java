package com.auction.service;

import java.time.Duration;
import java.time.Instant;

/**
 * Anti-Sniping Soft-Close Extension Algorithm.
 */
public class SnipingProtectionEngine {

    public static Instant evaluateSoftClose(
            Instant scheduledEnd,
            Instant bidTime,
            Duration snipeWindow,
            Duration extensionDuration) {

        if (bidTime.isAfter(scheduledEnd)) {
            return scheduledEnd;
        }

        Duration remainingTime = Duration.between(bidTime, scheduledEnd);
        if (remainingTime.compareTo(snipeWindow) <= 0) {
            // Extend from bid time by extension duration
            return bidTime.plus(extensionDuration);
        }

        return scheduledEnd;
    }
}
