package com.auction.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BidderReputationServiceTest {

    @Test
    void testTrustedBidder() {
        var score = BidderReputationService.calculateScore(10, 0, 0);
        assertTrue(score.score >= 80);
        assertTrue(score.isTrusted);
        assertEquals("EXCELLENT", score.riskCategory);
    }

    @Test
    void testDefaultPenalty() {
        var score = BidderReputationService.calculateScore(5, 2, 0);
        assertFalse(score.isTrusted);
        assertEquals("HIGH_RISK", score.riskCategory);
    }
}
