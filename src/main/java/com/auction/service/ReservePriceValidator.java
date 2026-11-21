package com.auction.service;

import java.math.BigDecimal;

/**
 * Reserve Price Threshold Protection.
 */
public class ReservePriceValidator {

    public static boolean isReserveMet(BigDecimal highestBid, BigDecimal secretReservePrice) {
        if (secretReservePrice == null || secretReservePrice.compareTo(BigDecimal.ZERO) <= 0) {
            return true; // No reserve set -> always met
        }
        if (highestBid == null) {
            return false;
        }
        return highestBid.compareTo(secretReservePrice) >= 0;
    }
}
