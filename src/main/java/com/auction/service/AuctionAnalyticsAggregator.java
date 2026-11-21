package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Auction Concurrency & Price Discovery Metrics Aggregator.
 */
public class AuctionAnalyticsAggregator {

    public static class Metrics {
        public final int totalBids;
        public final BigDecimal priceSpread;
        public final BigDecimal percentageAppreciation;

        public Metrics(int totalBids, BigDecimal priceSpread, BigDecimal percentageAppreciation) {
            this.totalBids = totalBids;
            this.priceSpread = priceSpread;
            this.percentageAppreciation = percentageAppreciation;
        }
    }

    public static Metrics computeMetrics(BigDecimal startingPrice, BigDecimal finalPrice, int bidCount) {
        BigDecimal spread = finalPrice.subtract(startingPrice);
        BigDecimal pct = startingPrice.compareTo(BigDecimal.ZERO) > 0
                ? spread.divide(startingPrice, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                : BigDecimal.ZERO;

        return new Metrics(bidCount, spread, pct.setScale(2, RoundingMode.HALF_UP));
    }
}
