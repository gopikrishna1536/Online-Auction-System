package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Comparable Historical Sales Fair Market Value (FMV) Estimator.
 */
public class FairMarketValuePredictor {

    public static BigDecimal estimateFMV(List<BigDecimal> pastSalePrices) {
        if (pastSalePrices == null || pastSalePrices.isEmpty()) {
            throw new IllegalArgumentException("Past sales prices cannot be empty");
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal p : pastSalePrices) {
            sum = sum.add(p);
        }

        return sum.divide(BigDecimal.valueOf(pastSalePrices.size()), 2, RoundingMode.HALF_UP);
    }
}
