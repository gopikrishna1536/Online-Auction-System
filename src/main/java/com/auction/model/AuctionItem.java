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
    private double minimumIncrement;

    public AuctionItem(int itemId, String itemName) {
        this(itemId, itemName, 0.0, 1.0);
    }

    public AuctionItem(int itemId, String itemName, double startingBid) {
        this(itemId, itemName, startingBid, 1.0);
    }

    public AuctionItem(int itemId, String itemName, double startingBid, double minimumIncrement) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("Item ID must be positive.");
        }
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty.");
        }
        if (startingBid < 0.0) {
            throw new IllegalArgumentException("Starting bid cannot be negative.");
        }
        if (minimumIncrement <= 0.0) {
            throw new IllegalArgumentException("Minimum increment must be positive.");
        }
        this.itemId = itemId;
        this.itemName = itemName.trim();
        this.currentBid = startingBid;
        this.highestBidder = "None";
        this.active = true;
        this.minimumIncrement = minimumIncrement;
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

    public synchronized double getMinimumIncrement() {
        return minimumIncrement;
    }

    public synchronized void setMinimumIncrement(double minimumIncrement) {
        if (minimumIncrement <= 0.0) {
            throw new IllegalArgumentException("Minimum increment must be positive.");
        }
        this.minimumIncrement = minimumIncrement;
    }

    public synchronized boolean placeBid(String bidderName, double bidAmount) {
        if (!active) {
            throw new IllegalStateException("Auction for item '" + itemName + "' is closed.");
        }
        if (bidderName == null || bidderName.trim().isEmpty()) {
            throw new IllegalArgumentException("Bidder name cannot be empty.");
        }
        double minRequired = currentBid + minimumIncrement;
        if (bidAmount < minRequired) {
            throw new IllegalArgumentException(String.format("Bid must be at least $%.2f (minimum increment is $%.2f over current bid $%.2f).", minRequired, minimumIncrement, currentBid));
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
