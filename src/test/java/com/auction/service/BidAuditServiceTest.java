package com.auction.service;

import com.auction.model.BidLedgerEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BidAuditService Immutable Ledger & Bot Detection Tests")
class BidAuditServiceTest {
    private BidAuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new BidAuditService();
    }

    @Test
    @DisplayName("Should record and query immutable bid ledger entries by item")
    void shouldRecordAndRetrieveEntries() {
        BidLedgerEntry entry1 = new BidLedgerEntry(101, "alice", 150.0, 1000L, "192.168.1.1", false);
        BidLedgerEntry entry2 = new BidLedgerEntry(101, "bob", 175.0, 2000L, "192.168.1.2", false);
        BidLedgerEntry entry3 = new BidLedgerEntry(102, "charlie", 300.0, 3000L, "192.168.1.3", false);

        auditService.recordBid(entry1);
        auditService.recordBid(entry2);
        auditService.recordBid(entry3);

        assertEquals(3, auditService.getTotalAuditCount());

        List<BidLedgerEntry> item101Ledger = auditService.getLedgerForItem(101);
        assertEquals(2, item101Ledger.size());
        assertEquals("alice", item101Ledger.get(0).getBidderUsername());
        assertEquals("bob", item101Ledger.get(1).getBidderUsername());
    }

    @Test
    @DisplayName("Should detect rapid bot velocity when burst threshold is exceeded")
    void shouldDetectRapidBiddingVelocity() {
        long baseTime = 10000L;
        // User 'sniper_bot' places 4 rapid bids within 1.5 seconds
        auditService.recordBid(new BidLedgerEntry(1, "sniper_bot", 100.0, baseTime, "10.0.0.1", false));
        auditService.recordBid(new BidLedgerEntry(1, "sniper_bot", 120.0, baseTime + 200, "10.0.0.1", false));
        auditService.recordBid(new BidLedgerEntry(1, "sniper_bot", 140.0, baseTime + 500, "10.0.0.1", false));
        auditService.recordBid(new BidLedgerEntry(1, "sniper_bot", 160.0, baseTime + 800, "10.0.0.1", false));

        boolean isSuspicious = auditService.detectRapidBiddingVelocity("sniper_bot", 3, 2000L, baseTime + 800);
        assertTrue(isSuspicious);

        // Different benign bidder has only 1 bid
        boolean benign = auditService.detectRapidBiddingVelocity("benign_user", 3, 2000L, baseTime + 800);
        assertFalse(benign);
    }

    @Test
    @DisplayName("Entry should reject negative amounts or empty bidders")
    void shouldValidateEntryParameters() {
        assertThrows(IllegalArgumentException.class, () -> new BidLedgerEntry(1, "", 100.0, 1000L, "127.0.0.1", false));
        assertThrows(IllegalArgumentException.class, () -> new BidLedgerEntry(1, "user", -50.0, 1000L, "127.0.0.1", false));
    }
}
