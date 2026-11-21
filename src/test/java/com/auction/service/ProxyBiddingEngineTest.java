package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProxyBiddingEngineTest {

    @Test
    void testProxyResolution() {
        var res = ProxyBiddingEngine.resolveBid(
                "Alice", new BigDecimal("500.00"),
                "Bob", new BigDecimal("350.00"),
                new BigDecimal("25.00"));

        assertEquals("Alice", res.winningBidderId);
        assertEquals(new BigDecimal("375.00"), res.winningBid);
    }
}
