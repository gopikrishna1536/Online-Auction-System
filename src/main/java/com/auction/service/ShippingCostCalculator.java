package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Dimensional Weight Shipping Cost & Parcel Insurance Calculator.
 */
public class ShippingCostCalculator {

    public static BigDecimal calculateShipping(
            double actualWeightKg,
            double lengthCm, double widthCm, double heightCm,
            BigDecimal itemDeclaredValue,
            BigDecimal baseRatePerKg) {

        // Volumetric weight divisor: 5000 (IATA standard)
        double dimensionalWeightKg = (lengthCm * widthCm * heightCm) / 5000.0;
        double billableWeight = Math.max(actualWeightKg, dimensionalWeightKg);

        BigDecimal freightCost = baseRatePerKg.multiply(BigDecimal.valueOf(billableWeight));
        // Insurance fee: 1.5% of declared item value
        BigDecimal insurance = itemDeclaredValue.multiply(new BigDecimal("0.015"));

        return freightCost.add(insurance).setScale(2, RoundingMode.HALF_UP);
    }
}
