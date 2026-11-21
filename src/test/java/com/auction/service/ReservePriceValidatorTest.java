package com.auction.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ReservePriceValidatorTest {

    @Test
    void testReserveEvaluation() {
        BigDecimal reserve = new BigDecimal("500.00");
        assertFalse(ReservePriceValidator.isReserveMet(new BigDecimal("499.99"), reserve));
        assertTrue(ReservePriceValidator.isReserveMet(new BigDecimal("500.00"), reserve));
        assertTrue(ReservePriceValidator.isReserveMet(new BigDecimal("550.00"), reserve));
    }
}
