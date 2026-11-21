package com.auction.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

/**
 * Descending Clock Dutch Auction Engine.
 * Decrements asking price at scheduled intervals until accepted or reserve floor is reached.
 */
public class DutchAuctionService {

    public static BigDecimal calculateCurrentPrice(
            BigDecimal startPrice,
            BigDecimal reserveFloorPrice,
            Instant auctionStartTime,
            Instant currentTime,
            Duration totalDuration,
            BigDecimal decrementStep) {

        if (startPrice.compareTo(reserveFloorPrice) < 0) {
            throw new IllegalArgumentException("Start price cannot be lower than reserve floor");
        }

        if (currentTime.isBefore(auctionStartTime)) {
            return startPrice;
        }

        long elapsedSeconds = Duration.between(auctionStartTime, currentTime).getSeconds();
        long totalSeconds = totalDuration.getSeconds();

        if (elapsedSeconds >= totalSeconds) {
            return reserveFloorPrice;
        }

        // Linear price decay: Price = StartPrice - ((StartPrice - Floor) * (elapsed / total))
        BigDecimal priceSpread = startPrice.subtract(reserveFloorPrice);
        BigDecimal elapsedFraction = BigDecimal.valueOf(elapsedSeconds)
                .divide(BigDecimal.valueOf(totalSeconds), 4, RoundingMode.HALF_UP);

        BigDecimal priceReduction = priceSpread.multiply(elapsedFraction);
        BigDecimal currentPrice = startPrice.subtract(priceReduction);

        if (currentPrice.compareTo(reserveFloorPrice) < 0) {
            return reserveFloorPrice;
        }

        return currentPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
