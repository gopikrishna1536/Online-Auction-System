package com.auction.repository;

import com.auction.model.AuctionItem;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for persisting and querying auction lots.
 */
public interface AuctionRepository {
    boolean save(AuctionItem item);
    Optional<AuctionItem> findById(int itemId);
    List<AuctionItem> findAll();
    List<AuctionItem> findByActive(boolean active);
    List<AuctionItem> searchByName(String keyword);
    List<AuctionItem> findByPriceRange(double minPrice, double maxPrice);
    boolean deleteById(int itemId);
    int count();
}
