package com.auction.service;

import java.math.BigDecimal;

/**
 * Buyer-Seller Escrow Payment Lifecycle State Machine.
 */
public class EscrowPaymentService {

    public enum EscrowStatus {
        AWAITING_FUNDS,
        FUNDS_HELD,
        DISPATCHED,
        DELIVERED,
        RELEASED_TO_SELLER,
        REFUNDED_TO_BUYER
    }

    public static EscrowStatus transition(EscrowStatus current, String event) {
        switch (current) {
            case AWAITING_FUNDS:
                if ("PAYMENT_CONFIRMED".equals(event)) return EscrowStatus.FUNDS_HELD;
                break;
            case FUNDS_HELD:
                if ("ITEM_SHIPPED".equals(event)) return EscrowStatus.DISPATCHED;
                if ("CANCELLED_BY_SELLER".equals(event)) return EscrowStatus.REFUNDED_TO_BUYER;
                break;
            case DISPATCHED:
                if ("DELIVERY_CONFIRMED".equals(event)) return EscrowStatus.DELIVERED;
                break;
            case DELIVERED:
                if ("BUYER_ACCEPTED".equals(event) || "AUTO_RELEASE_TIMEOUT".equals(event)) {
                    return EscrowStatus.RELEASED_TO_SELLER;
                }
                break;
            default:
                break;
        }
        return current;
    }
}
