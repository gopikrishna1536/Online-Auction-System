package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class AuctionAnalyticsAggregatorTest {

    @Test
    void testMetrics() {
        var m = AuctionAnalyticsAggregator.computeMetrics(new BigDecimal("100.00"), new BigDecimal("150.00"), 12);
        assertEquals(12, m.totalBids);
        assertEquals(new BigDecimal("50.00"), m.priceSpread);
        assertEquals(new BigDecimal("50.00"), m.percentageAppreciation);
    }
}
