package com.auction.repository;

import com.auction.model.AuctionItem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Thread-safe in-memory implementation of AuctionRepository.
 */
public class InMemoryAuctionRepository implements AuctionRepository {
    private final Map<Integer, AuctionItem> storage = new ConcurrentHashMap<>();

    @Override
    public boolean save(AuctionItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot save null item.");
        }
        storage.put(item.getItemId(), item);
        return true;
    }

    @Override
    public Optional<AuctionItem> findById(int itemId) {
        return Optional.ofNullable(storage.get(itemId));
    }

    @Override
    public List<AuctionItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<AuctionItem> findByActive(boolean active) {
        return storage.values().stream()
                .filter(item -> item.isActive() == active)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionItem> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String lower = keyword.trim().toLowerCase();
        return storage.values().stream()
                .filter(item -> item.getItemName().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    @Override
    public List<AuctionItem> findByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < minPrice) {
            throw new IllegalArgumentException("Invalid price range parameters.");
        }
        return storage.values().stream()
                .filter(item -> item.getCurrentBid() >= minPrice && item.getCurrentBid() <= maxPrice)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(int itemId) {
        return storage.remove(itemId) != null;
    }

    @Override
    public int count() {
        return storage.size();
    }
}
