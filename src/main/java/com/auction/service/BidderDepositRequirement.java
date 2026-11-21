package com.auction.service;

import java.math.BigDecimal;

/**
 * High-Value Auction Earnest Money Deposit (EMD) Verification.
 */
public class BidderDepositRequirement {

    public static boolean hasSufficientDeposit(BigDecimal proposedBid, BigDecimal heldDeposit, BigDecimal emdPercentRequirement) {
        if (proposedBid == null || proposedBid.compareTo(BigDecimal.ZERO) <= 0) return false;
        if (heldDeposit == null) return false;

        BigDecimal requiredDeposit = proposedBid.multiply(emdPercentRequirement.divide(new BigDecimal("100")));
        return heldDeposit.compareTo(requiredDeposit) >= 0;
    }
}
