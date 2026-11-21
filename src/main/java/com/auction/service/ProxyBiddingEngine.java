package com.auction.service;

import java.math.BigDecimal;

/**
 * Proxy Bidding Agent Engine.
 */
public class ProxyBiddingEngine {

    public static class ProxyResult {
        public final BigDecimal winningBid;
        public final String winningBidderId;

        public ProxyResult(BigDecimal winningBid, String winningBidderId) {
            this.winningBid = winningBid;
            this.winningBidderId = winningBidderId;
        }
    }

    public static ProxyResult resolveBid(
            String bidderA, BigDecimal maxA,
            String bidderB, BigDecimal maxB,
            BigDecimal minIncrement) {

        if (maxA.compareTo(maxB) > 0) {
            // Bidder A wins with min increment above B's max, up to A's max
            BigDecimal winPrice = maxB.add(minIncrement);
            if (winPrice.compareTo(maxA) > 0) winPrice = maxA;
            return new ProxyResult(winPrice, bidderA);
        } else if (maxB.compareTo(maxA) > 0) {
            BigDecimal winPrice = maxA.add(minIncrement);
            if (winPrice.compareTo(maxB) > 0) winPrice = maxB;
            return new ProxyResult(winPrice, bidderB);
        } else {
            // Tie goes to earlier bidder (assume bidder A)
            return new ProxyResult(maxA, bidderA);
        }
    }
}
