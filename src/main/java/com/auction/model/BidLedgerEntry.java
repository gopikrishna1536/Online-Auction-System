package com.auction.model;

import java.util.Objects;

/**
 * Immutable audit record representing an individual bid transaction.
 */
public final class BidLedgerEntry {
    private final int itemId;
    private final String bidderUsername;
    private final double bidAmount;
    private final long timestamp;
    private final String clientIp;
    private final boolean flaggedSuspicious;

    public BidLedgerEntry(int itemId, String bidderUsername, double bidAmount, long timestamp, String clientIp, boolean flaggedSuspicious) {
        if (bidderUsername == null || bidderUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Bidder username cannot be empty.");
        }
        if (bidAmount <= 0) {
            throw new IllegalArgumentException("Bid amount must be strictly positive.");
        }
        this.itemId = itemId;
        this.bidderUsername = bidderUsername.trim();
        this.bidAmount = bidAmount;
        this.timestamp = timestamp;
        this.clientIp = clientIp != null ? clientIp : "127.0.0.1";
        this.flaggedSuspicious = flaggedSuspicious;
    }

    public int getItemId() {
        return itemId;
    }

    public String getBidderUsername() {
        return bidderUsername;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public boolean isFlaggedSuspicious() {
        return flaggedSuspicious;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BidLedgerEntry)) return false;
        BidLedgerEntry that = (BidLedgerEntry) o;
        return itemId == that.itemId &&
                Double.compare(that.bidAmount, bidAmount) == 0 &&
                timestamp == that.timestamp &&
                flaggedSuspicious == that.flaggedSuspicious &&
                Objects.equals(bidderUsername, that.bidderUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId, bidderUsername, bidAmount, timestamp, flaggedSuspicious);
    }

    @Override
    public String toString() {
        return "BidLedgerEntry{" +
                "itemId=" + itemId +
                ", bidderUsername='" + bidderUsername + '\'' +
                ", bidAmount=" + bidAmount +
                ", timestamp=" + timestamp +
                ", clientIp='" + clientIp + '\'' +
                ", flaggedSuspicious=" + flaggedSuspicious +
                '}';
    }
}
