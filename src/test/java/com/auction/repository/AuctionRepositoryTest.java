package com.auction.repository;

import com.auction.model.AuctionItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AuctionRepository Interface & Implementation Tests")
class AuctionRepositoryTest {
    private AuctionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryAuctionRepository();
        repository.save(new AuctionItem(1, "Vintage Rolex", 5000.0));
        repository.save(new AuctionItem(2, "Antique Vase", 1200.0));
        repository.save(new AuctionItem(3, "Modern Art Painting", 8500.0));
    }

    @Test
    @DisplayName("Should find item by its unique ID")
    void shouldFindById() {
        Optional<AuctionItem> item = repository.findById(2);
        assertTrue(item.isPresent());
        assertEquals("Antique Vase", item.get().getItemName());
    }

    @Test
    @DisplayName("Should return empty optional when item ID is not found")
    void shouldReturnEmptyForNonExistentId() {
        Optional<AuctionItem> item = repository.findById(999);
        assertTrue(item.isEmpty());
    }

    @Test
    @DisplayName("Should search items by partial case-insensitive name")
    void shouldSearchByName() {
        List<AuctionItem> results = repository.searchByName("rolex");
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getItemId());

        List<AuctionItem> multiResults = repository.searchByName("a");
        assertTrue(multiResults.size() >= 2);
    }

    @Test
    @DisplayName("Should filter items within price range")
    void shouldFindByPriceRange() {
        List<AuctionItem> filtered = repository.findByPriceRange(1000.0, 6000.0);
        assertEquals(2, filtered.size());
    }

    @Test
    @DisplayName("Should delete item by ID and reduce count")
    void shouldDeleteById() {
        assertEquals(3, repository.count());
        assertTrue(repository.deleteById(1));
        assertEquals(2, repository.count());
        assertTrue(repository.findById(1).isEmpty());
    }
}
