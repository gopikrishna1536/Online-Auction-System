package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ShippingCostCalculatorTest {

    @Test
    void testShippingWithInsurance() {
        // 2 kg actual, 20x20x20 = 8000/5000 = 1.6 kg dim -> billable 2 kg * $10 = $20 freight + 1.5% on $1000 = $15 -> $35
        BigDecimal cost = ShippingCostCalculator.calculateShipping(
                2.0, 20, 20, 20, new BigDecimal("1000.00"), new BigDecimal("10.00"));
        assertEquals(new BigDecimal("35.00"), cost);
    }
}
