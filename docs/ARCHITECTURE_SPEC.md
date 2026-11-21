# Online Auction System - Architecture & OOP Design Specifications

Enterprise Java 17 desktop auction platform adhering to clean architectural patterns.

## Design Patterns Applied
1. **Repository Pattern**: Decoupled in-memory storage (`AuctionRepository`) isolating state persistence from GUI view logic.
2. **State Pattern**: Finite state machine governing item statuses (`ACTIVE`, `PENDING`, `ENDED`, `CANCELLED`).
3. **Observer Pattern**: Real-time bid update event dispatcher pushing ticker prices to auction participants.
4. **Proxy Bidding Pattern**: Autonomous bidding agent increments bids dynamically up to pre-authorized ceilings.
