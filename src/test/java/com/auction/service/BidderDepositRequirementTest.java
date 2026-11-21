package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class BidderDepositRequirementTest {

    @Test
    void testDepositCheck() {
        BigDecimal bid = new BigDecimal("100000.00");
        BigDecimal requirement = new BigDecimal("10.0"); // 10% requires 10,000

        assertTrue(BidderDepositRequirement.hasSufficientDeposit(bid, new BigDecimal("10000.00"), requirement));
        assertFalse(BidderDepositRequirement.hasSufficientDeposit(bid, new BigDecimal("9999.00"), requirement));
    }
}
