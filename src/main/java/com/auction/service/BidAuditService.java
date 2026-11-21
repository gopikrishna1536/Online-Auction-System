package com.auction.service;

import com.auction.model.BidLedgerEntry;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Service managing immutable audit trails for auction bids and detecting bot / velocity anomalies.
 */
public class BidAuditService {
    private final List<BidLedgerEntry> auditLedger = new CopyOnWriteArrayList<>();

    public synchronized void recordBid(int itemId, String bidderUsername, double amount, String clientIp) {
        long now = System.currentTimeMillis();
        boolean suspicious = detectRapidBiddingVelocity(bidderUsername, 4, 3000, now);
        BidLedgerEntry entry = new BidLedgerEntry(itemId, bidderUsername, amount, now, clientIp, suspicious);
        auditLedger.add(entry);
    }

    public synchronized void recordBid(BidLedgerEntry entry) {
        if (entry == null) {
            throw new IllegalArgumentException("Audit entry cannot be null.");
        }
        auditLedger.add(entry);
    }

    public List<BidLedgerEntry> getLedgerForItem(int itemId) {
        return auditLedger.stream()
                .filter(e -> e.getItemId() == itemId)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<BidLedgerEntry> getLedgerForBidder(String bidderUsername) {
        if (bidderUsername == null) return Collections.emptyList();
        return auditLedger.stream()
                .filter(e -> e.getBidderUsername().equalsIgnoreCase(bidderUsername.trim()))
                .collect(Collectors.toUnmodifiableList());
    }

    public synchronized boolean detectRapidBiddingVelocity(String bidderUsername, int thresholdCount, long windowMillis, long referenceTimestamp) {
        if (bidderUsername == null) return false;
        long windowStart = referenceTimestamp - windowMillis;

        long recentBids = auditLedger.stream()
                .filter(e -> e.getBidderUsername().equalsIgnoreCase(bidderUsername.trim()))
                .filter(e -> e.getTimestamp() >= windowStart && e.getTimestamp() <= referenceTimestamp)
                .count();

        return recentBids >= thresholdCount;
    }

    public int getTotalAuditCount() {
        return auditLedger.size();
    }

    public List<BidLedgerEntry> getFlaggedSuspiciousBids() {
        return auditLedger.stream()
                .filter(BidLedgerEntry::isFlaggedSuspicious)
                .collect(Collectors.toUnmodifiableList());
    }
}
