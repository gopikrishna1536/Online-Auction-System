package com.auction.model;

import java.util.Objects;

/**
 * Represents an individual auction lot/item available for bidding.
 */
public class AuctionItem {
    private final int itemId;
    private final String itemName;
    private double currentBid;
    private String highestBidder;
    private boolean active;

    public AuctionItem(int itemId, String itemName) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be positive.");
        }
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        this.itemId = itemId;
        this.itemName = itemName.trim();
        this.currentBid = 0.0;
        this.highestBidder = "None";
        this.active = true;
    }

    public AuctionItem(int itemId, String itemName, double startingBid) {
        this(itemId, itemName);
        if (startingBid < 0.0) {
            throw new IllegalArgumentException("Starting bid cannot be negative.");
        }
        this.currentBid = startingBid;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public synchronized double getCurrentBid() {
        return currentBid;
    }

    public synchronized String getHighestBidder() {
        return highestBidder;
    }

    public synchronized boolean isActive() {
        return active;
    }

    public synchronized void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Places a bid on the item if the bid amount is strictly greater than the current bid
     * and the item is active.
     *
     * @param bidderName username of the bidder
     * @param bidAmount  amount offered
     * @return true if bid was accepted
     * @throws IllegalStateException    if auction on this item is closed
     * @throws IllegalArgumentException if bidder is invalid or bid amount is not higher
     */
    public synchronized boolean placeBid(String bidderName, double bidAmount) {
        if (!active) {
            throw new IllegalStateException("Auction for item '" + itemName + "' is closed.");
        }
        if (bidderName == null || bidderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bidder name cannot be empty.");
        }
        if (bidAmount <= currentBid) {
            throw new IllegalArgumentException("Bid must be strictly greater than current bid ($" + currentBid + ").");
        }

        this.currentBid = bidAmount;
        this.highestBidder = bidderName.trim();
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionItem that = (AuctionItem) o;
        return itemId == that.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }

    @Override
    public String toString() {
        return "AuctionItem{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", currentBid=" + currentBid +
                ", highestBidder='" + highestBidder + '\'' +
                ", active=" + active +
                '}';
    }
}
