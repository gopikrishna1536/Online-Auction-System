package com.auction.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuctionItem Model Tests")
class AuctionItemTest {

    @Test
    @DisplayName("Should initialize item with default bid and active state")
    void shouldInitializeCorrectly() {
        AuctionItem item = new AuctionItem(1, "Vintage Watch");
        assertEquals(1, item.getItemId());
        assertEquals("Vintage Watch", item.getItemName());
        assertEquals(0.0, item.getCurrentBid());
        assertEquals("None", item.getHighestBidder());
        assertTrue(item.isActive());
    }

    @Test
    @DisplayName("Should reject invalid item ID or blank name")
    void shouldRejectInvalidConstructorArguments() {
        assertThrows(IllegalArgumentException.class, () -> new AuctionItem(0, "Invalid ID"));
        assertThrows(IllegalArgumentException.class, () -> new AuctionItem(-5, "Negative ID"));
        assertThrows(IllegalArgumentException.class, () -> new AuctionItem(1, "   "));
        assertThrows(IllegalArgumentException.class, () -> new AuctionItem(1, null));
    }

    @Test
    @DisplayName("Should place bid successfully when higher than current bid")
    void shouldAcceptHigherBid() {
        AuctionItem item = new AuctionItem(10, "Rare Painting", 100.0);
        boolean result = item.placeBid("alice", 150.0);

        assertTrue(result);
        assertEquals(150.0, item.getCurrentBid());
        assertEquals("alice", item.getHighestBidder());
    }

    @Test
    @DisplayName("Should reject bid if amount is less than or equal to current bid")
    void shouldRejectLowerOrEqualBid() {
        AuctionItem item = new AuctionItem(10, "Rare Painting", 100.0);

        assertThrows(IllegalArgumentException.class, () -> item.placeBid("bob", 100.0));
        assertThrows(IllegalArgumentException.class, () -> item.placeBid("bob", 90.0));
        assertEquals(100.0, item.getCurrentBid());
        assertEquals("None", item.getHighestBidder());
    }

    @Test
    @DisplayName("Should strictly enforce custom minimum bid increments")
    void shouldEnforceMinimumBidIncrement() {
        // Item with $100 starting bid and $15 minimum increment
        AuctionItem item = new AuctionItem(25, "Vintage Sculpture", 100.0, 15.0);

        // $110 should be rejected because min required is 100 + 15 = 115
        assertThrows(IllegalArgumentException.class, () -> item.placeBid("dan", 110.0));

        // Exactly 115 should be accepted
        assertTrue(item.placeBid("dan", 115.0));
        assertEquals(115.0, item.getCurrentBid());

        // Next bid must be at least 115 + 15 = 130
        assertThrows(IllegalArgumentException.class, () -> item.placeBid("elena", 125.0));
        assertTrue(item.placeBid("elena", 130.0));
        assertEquals(130.0, item.getCurrentBid());
    }

    @Test
    @DisplayName("Should reject bid if item is closed/inactive")
    void shouldRejectBidOnInactiveItem() {
        AuctionItem item = new AuctionItem(10, "Rare Painting", 100.0);
        item.setActive(false);

        assertThrows(IllegalStateException.class, () -> item.placeBid("charlie", 200.0));
    }

    @Test
    @DisplayName("Items with same ID should be considered equal")
    void shouldVerifyEqualityById() {
        AuctionItem item1 = new AuctionItem(42, "Item A");
        AuctionItem item2 = new AuctionItem(42, "Item B");

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
    }
}
