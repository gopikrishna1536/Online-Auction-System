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
}
