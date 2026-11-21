package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Bidder Trust & Credit Risk Scorecard.
 */
public class BidderReputationService {

    public static class ReputationScore {
        public final int score; // 0 to 100
        public final boolean isTrusted;
        public final String riskCategory;

        public ReputationScore(int score, boolean isTrusted, String riskCategory) {
            this.score = score;
            this.isTrusted = isTrusted;
            this.riskCategory = riskCategory;
        }
    }

    public static ReputationScore calculateScore(int completedPurchases, int paymentDefaults, int retractedBids) {
        if (completedPurchases < 0 || paymentDefaults < 0 || retractedBids < 0) {
            throw new IllegalArgumentException("Inputs must be non-negative");
        }

        int score = 50; // Base score
        score += Math.min(40, completedPurchases * 4); // Up to +40 for history
        score -= (paymentDefaults * 25);               // Severe penalty for payment failure
        score -= (retractedBids * 10);                 // Penalty for retraction

        score = Math.max(0, Math.min(100, score));

        boolean trusted = score >= 70 && paymentDefaults == 0;
        String category = score >= 80 ? "EXCELLENT" : (score >= 60 ? "MODERATE" : "HIGH_RISK");

        return new ReputationScore(score, trusted, category);
    }
}
