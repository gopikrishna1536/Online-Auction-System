package com.auction.service;

import com.auction.model.AuctionItem;
import com.auction.model.User;
import com.auction.repository.AuctionRepository;
import com.auction.repository.InMemoryAuctionRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Service managing auction lifecycle, item catalog, user sessions, bids, and countdown timers.
 */
public class AuctionService {
    private final AuctionRepository itemRepository;
    private final Map<String, User> users;
    private User currentUser;
    private Timer timer;
    private int timeRemaining;
    private boolean auctionRunning;

    // Listeners for reactive UI notifications
    private final List<Consumer<Integer>> timerTickListeners = new CopyOnWriteArrayList<>();
    private final List<Consumer<Map<AuctionItem, String>>> auctionEndListeners = new CopyOnWriteArrayList<>();

    public AuctionService() {
        this(new InMemoryAuctionRepository());
    }

    public AuctionService(AuctionRepository repository) {
        this.itemRepository = Objects.requireNonNull(repository, "Repository cannot be null.");
        this.users = new ConcurrentHashMap<>();
        this.currentUser = null;
        this.timeRemaining = 0;
        this.auctionRunning = false;

        seedDefaultData();
    }

    private void seedDefaultData() {
        // Default admin account
        users.put("admin", new User("admin", "admin", "000-000-0000", "Headquarters", true));

        // Initial auction lots
        addItem(new AuctionItem(1, "Antique Vase", 150.0));
        addItem(new AuctionItem(2, "Vintage 1967 Mustang", 25000.0));
        addItem(new AuctionItem(3, "Painting by Van Gogh Replica", 1200.0));
        addItem(new AuctionItem(4, "Rolex Submariner Watch", 8500.0));
        addItem(new AuctionItem(5, "Diamond Solitaire Necklace", 5400.0));
        addItem(new AuctionItem(6, "First Edition Rare Book", 450.0));
        addItem(new AuctionItem(7, "Luxury Motor Yacht", 180000.0));
    }

    public synchronized boolean registerUser(String username, String password, String mobile, String address) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required.");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required.");
        }
        String key = username.trim().toLowerCase();
        if (users.containsKey(key)) {
            return false;
        }
        users.put(key, new User(username.trim(), password, mobile, address, false));
        return true;
    }

    public synchronized Optional<User> login(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        String key = username.trim().toLowerCase();
        User user = users.get(key);
        if (user != null && user.checkPassword(password)) {
            this.currentUser = user;
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public synchronized void logout() {
        this.currentUser = null;
    }

    public synchronized User getCurrentUser() {
        return currentUser;
    }

    public synchronized boolean isLoggedIn() {
        return currentUser != null;
    }

    public synchronized boolean isAdmin() {
        return currentUser != null && currentUser.isAdmin();
    }

    public synchronized boolean addItem(AuctionItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        return itemRepository.save(item);
    }

    public Optional<AuctionItem> findItemById(int itemId) {
        return itemRepository.findById(itemId);
    }

    public List<AuctionItem> getAllItems() {
        return itemRepository.findAll();
    }

    public List<AuctionItem> searchItems(String keyword) {
        return itemRepository.searchByName(keyword);
    }

    public List<AuctionItem> filterItemsByPrice(double minPrice, double maxPrice) {
        return itemRepository.findByPriceRange(minPrice, maxPrice);
    }

    public AuctionRepository getRepository() {
        return itemRepository;
    }

    public synchronized boolean placeBid(int itemId, double bidAmount) {
        if (currentUser == null) {
            throw new IllegalStateException("Authentication required to place a bid.");
        }
        AuctionItem item = findItemById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Item with ID " + itemId + " not found."));

        return item.placeBid(currentUser.getUsername(), bidAmount);
    }

    public synchronized void startAuctionTimer(int durationSeconds) {
        if (durationSeconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        if (timer != null) {
            timer.cancel();
        }

        this.timeRemaining = durationSeconds;
        this.auctionRunning = true;
        this.timer = new Timer("AuctionTimerThread", true);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                synchronized (AuctionService.this) {
                    timeRemaining--;
                    for (Consumer<Integer> listener : timerTickListeners) {
                        try {
                            listener.accept(timeRemaining);
                        } catch (Exception ignored) {}
                    }

                    if (timeRemaining <= 0) {
                        endAuction();
                    }
                }
            }
        }, 1000, 1000);
    }

    public synchronized void endAuction() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        this.auctionRunning = false;

        Map<AuctionItem, String> winners = new LinkedHashMap<>();
        for (AuctionItem item : itemRepository.findAll()) {
            item.setActive(false);
            String winner = item.getHighestBidder();
            winners.put(item, winner);

            if (winner != null && !winner.equalsIgnoreCase("None")) {
                User user = users.get(winner.toLowerCase());
                if (user != null) {
                    user.addWonItem(item);
                }
            }
        }

        for (Consumer<Map<AuctionItem, String>> listener : auctionEndListeners) {
            try {
                listener.accept(winners);
            } catch (Exception ignored) {}
        }
    }

    public synchronized int getTimeRemaining() {
        return timeRemaining;
    }

    public synchronized boolean isAuctionRunning() {
        return auctionRunning;
    }

    public void addTimerTickListener(Consumer<Integer> listener) {
        timerTickListeners.add(listener);
    }

    public void addAuctionEndListener(Consumer<Map<AuctionItem, String>> listener) {
        auctionEndListeners.add(listener);
    }
}
