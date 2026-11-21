package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Seller Net Settlement Payout Calculator.
 */
public class SellerSettlementPayout {

    public static class PayoutSummary {
        public final BigDecimal grossSale;
        public final BigDecimal platformFee;
        public final BigDecimal paymentProcessingFee;
        public final BigDecimal netPayout;

        public PayoutSummary(BigDecimal grossSale, BigDecimal platformFee, BigDecimal paymentProcessingFee, BigDecimal netPayout) {
            this.grossSale = grossSale;
            this.platformFee = platformFee;
            this.paymentProcessingFee = paymentProcessingFee;
            this.netPayout = netPayout;
        }
    }

    public static PayoutSummary calculatePayout(BigDecimal grossHammerPrice, BigDecimal commissionPercent) {
        BigDecimal platformFee = grossHammerPrice.multiply(commissionPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));
        // 2.9% + $0.30 standard gateway fee
        BigDecimal gatewayFee = grossHammerPrice.multiply(new BigDecimal("0.029")).add(new BigDecimal("0.30"));

        BigDecimal net = grossHammerPrice.subtract(platformFee).subtract(gatewayFee);

        return new PayoutSummary(
                grossHammerPrice.setScale(2, RoundingMode.HALF_UP),
                platformFee.setScale(2, RoundingMode.HALF_UP),
                gatewayFee.setScale(2, RoundingMode.HALF_UP),
                net.setScale(2, RoundingMode.HALF_UP));
    }
}
