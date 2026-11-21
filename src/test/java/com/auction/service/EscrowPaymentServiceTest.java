package com.auction.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EscrowPaymentServiceTest {

    @Test
    void testEscrowLifecycle() {
        var status = EscrowPaymentService.EscrowStatus.AWAITING_FUNDS;
        status = EscrowPaymentService.transition(status, "PAYMENT_CONFIRMED");
        assertEquals(EscrowPaymentService.EscrowStatus.FUNDS_HELD, status);

        status = EscrowPaymentService.transition(status, "ITEM_SHIPPED");
        assertEquals(EscrowPaymentService.EscrowStatus.DISPATCHED, status);

        status = EscrowPaymentService.transition(status, "DELIVERY_CONFIRMED");
        assertEquals(EscrowPaymentService.EscrowStatus.DELIVERED, status);

        status = EscrowPaymentService.transition(status, "BUYER_ACCEPTED");
        assertEquals(EscrowPaymentService.EscrowStatus.RELEASED_TO_SELLER, status);
    }
}
