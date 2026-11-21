package com.auction.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a registered user or administrator within the Auction System.
 */
public class User {
    private final String username;
    private String password;
    private String mobile;
    private String address;
    private boolean admin;
    private final List<AuctionItem> wonItems;

    public User(String username, String password, String mobile, String address) {
        this(username, password, mobile, address, false);
    }

    public User(String username, String password, String mobile, String address, boolean admin) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        this.username = username.trim();
        this.password = password;
        this.mobile = mobile != null ? mobile.trim() : "";
        this.address = address != null ? address.trim() : "";
        this.admin = admin;
        this.wonItems = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String candidatePassword) {
        return candidatePassword != null && this.password.equals(candidatePassword);
    }

    public void setPassword(String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("New password cannot be empty.");
        }
        this.password = newPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public synchronized void addWonItem(AuctionItem item) {
        if (item != null && !wonItems.contains(item)) {
            wonItems.add(item);
        }
    }

    public synchronized List<AuctionItem> getWonItems() {
        return Collections.unmodifiableList(wonItems);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username.toLowerCase(), user.username.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username.toLowerCase());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", admin=" + admin +
                ", wonItemsCount=" + wonItems.size() +
                '}';
    }
}
