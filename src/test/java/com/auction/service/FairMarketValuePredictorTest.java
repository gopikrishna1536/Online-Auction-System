package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FairMarketValuePredictorTest {

    @Test
    void testFMVAverage() {
        var sales = List.of(new BigDecimal("100.00"), new BigDecimal("120.00"), new BigDecimal("110.00"));
        BigDecimal fmv = FairMarketValuePredictor.estimateFMV(sales);
        assertEquals(new BigDecimal("110.00"), fmv);
    }
}
