package com.auction.service;

import com.auction.model.AuctionItem;
import com.auction.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuctionService Business Logic Tests")
class AuctionServiceTest {
    private AuctionService service;

    @BeforeEach
    void setUp() {
        service = new AuctionService();
    }

    @Test
    @DisplayName("Should successfully register a new user and reject duplicates")
    void shouldRegisterUser() {
        boolean first = service.registerUser("johndoe", "secret123", "1234567890", "123 Main St");
        assertTrue(first);

        // Duplicate registration with case insensitivity
        boolean duplicate = service.registerUser("JohnDoe", "anotherpass", "9999999999", "Other St");
        assertFalse(duplicate);
    }

    @Test
    @DisplayName("Should authenticate valid users and reject wrong passwords")
    void shouldAuthenticateUsers() {
        service.registerUser("bidder1", "pass123", "555-0100", "City");

        Optional<User> success = service.login("bidder1", "pass123");
        assertTrue(success.isPresent());
        assertTrue(service.isLoggedIn());
        assertEquals("bidder1", service.getCurrentUser().getUsername());

        Optional<User> wrongPass = service.login("bidder1", "wrong");
        assertTrue(wrongPass.isEmpty());
    }

    @Test
    @DisplayName("Admin account should be pre-seeded with admin privileges")
    void shouldVerifyAdminLogin() {
        Optional<User> admin = service.login("admin", "admin");
        assertTrue(admin.isPresent());
        assertTrue(admin.get().isAdmin());
        assertTrue(service.isAdmin());
    }

    @Test
    @DisplayName("Adding new item should succeed if ID is unique")
    void shouldAddItem() {
        AuctionItem item = new AuctionItem(999, "Signed Guitar", 500.0);
        assertTrue(service.addItem(item));

        // Duplicate ID should fail
        AuctionItem duplicate = new AuctionItem(999, "Another Guitar", 600.0);
        assertFalse(service.addItem(duplicate));
    }

    @Test
    @DisplayName("Should require login to place bids")
    void shouldRequireLoginForBidding() {
        assertThrows(IllegalStateException.class, () -> service.placeBid(1, 1000.0));
    }

    @Test
    @DisplayName("Logged in user can place valid bids")
    void shouldAllowLoggedInUserToBid() {
        service.registerUser("bidder_alex", "pass", "123", "Address");
        service.login("bidder_alex", "pass");

        boolean placed = service.placeBid(1, 500.0);
        assertTrue(placed);

        AuctionItem item = service.findItemById(1).orElseThrow();
        assertEquals(500.0, item.getCurrentBid());
        assertEquals("bidder_alex", item.getHighestBidder());
    }

    @Test
    @DisplayName("Ending auction closes all items and notifies listeners")
    void shouldEndAuctionAndNotify() {
        service.registerUser("winner_dan", "pass", "123", "Address");
        service.login("winner_dan", "pass");
        service.placeBid(1, 999.0);

        AtomicBoolean notified = new AtomicBoolean(false);
        service.addAuctionEndListener(winners -> {
            notified.set(true);
            assertFalse(winners.isEmpty());
        });

        service.endAuction();

        assertTrue(notified.get());
        assertFalse(service.findItemById(1).orElseThrow().isActive());
    }

    @Test
    @DisplayName("Should search items by keyword across repository")
    void shouldSearchItemsByKeyword() {
        var results = service.searchItems("Vase");
        assertEquals(1, results.size());
        assertEquals("Antique Vase", results.get(0).getName());

        var emptyResults = service.searchItems("NonExistentWidget");
        assertTrue(emptyResults.isEmpty());
    }

    @Test
    @DisplayName("Should filter items by price range across repository")
    void shouldFilterItemsByPriceRange() {
        var results = service.filterItemsByPrice(100.0, 500.0);
        // Antique Vase ($150), First Edition Rare Book ($450)
        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("Should record immutable audit ledger entry when bid is successfully placed")
    void shouldRecordAuditLedgerOnSuccessfulBid() {
        service.registerUser("audited_bidder", "pass", "123", "City");
        service.login("audited_bidder", "pass");

        boolean placed = service.placeBid(1, 400.0);
        assertTrue(placed);

        var auditEntries = service.getAuditService().getLedgerForItem(1);
        assertEquals(1, auditEntries.size());
        assertEquals("audited_bidder", auditEntries.get(0).getBidderUsername());
        assertEquals(400.0, auditEntries.get(0).getBidAmount());
    }
}
