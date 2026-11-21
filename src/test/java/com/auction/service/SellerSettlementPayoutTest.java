package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class SellerSettlementPayoutTest {

    @Test
    void testSettlementPayout() {
        var summary = SellerSettlementPayout.calculatePayout(new BigDecimal("1000.00"), new BigDecimal("10.0"));
        assertEquals(new BigDecimal("100.00"), summary.platformFee); // 10%
        assertEquals(new BigDecimal("29.30"), summary.paymentProcessingFee); // 2.9% + 0.30
        assertEquals(new BigDecimal("870.70"), summary.netPayout);
    }
}
