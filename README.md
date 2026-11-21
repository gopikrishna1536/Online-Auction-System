# Online Auction System

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-25A162?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

A robust, concurrent Java desktop auction management system built with clean Object-Oriented Design (OOD) principles, multi-threaded countdown timers, decoupled service architecture, and a modern Swing graphical user interface.

---

## 🏛️ Architecture & System Design

The application follows a clean Separation of Concerns (SoC) architecture, strictly separating the business logic and domain entities from the GUI layer:

```
┌────────────────────────────────────────────────────────┐
│                   Swing Presentation                   │
│         com.auction.ui.AuctionSystemGUI                │
│    (Table rendering, dialog inputs, event listeners)   │
└───────────────────────────┬────────────────────────────┘
                            │ Method invocations & callbacks
┌───────────────────────────▼────────────────────────────┐
│                     Service Layer                      │
│        com.auction.service.AuctionService              │
│  (Session auth, bid validation, concurrency, timers)   │
└───────────────────────────┬────────────────────────────┘
                            │ Manages entities
┌───────────────────────────▼────────────────────────────┐
│                      Domain Models                     │
│         • com.auction.model.AuctionItem                │
│         • com.auction.model.User                       │
└────────────────────────────────────────────────────────┘
```

---

## ✨ Key Features

- **Concurrent Real-Time Bidding:** Multi-threaded safe bid processing ensuring bid integrity and validation (strictly higher bids only).
- **Automated Auction Timers:** Scheduled countdown timers using `java.util.Timer` that automatically close bidding lots upon expiration and resolve winning bidders.
- **User Authentication & Role Management:**
  - **Bidder Role:** Register with credentials, login, browse active lots, and place live bids.
  - **Admin Role:** Privileged administrative access to create new auction lots, configure auction countdowns, and oversee lot closures.
- **Dynamic Table View:** Live updating `JTable` with custom column renderers showing current bid amounts, highest bidders, and item active states.
- **Decoupled Business Logic:** Clean domain layer with pure models, throwing domain exceptions for testability rather than coupling logic directly to GUI alerts.
- **Comprehensive Automated Test Suite:** Unit and integration tests powered by JUnit 5 covering models, bidding edge cases, and user session management.

---

## 📂 Project Structure

```
Online-Auction-System/
├── .github/
│   └── workflows/
│       └── maven.yml               # GitHub Actions CI automated build & test pipeline
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── auction/
│   │               ├── Main.java                        # Application entry point
│   │               ├── model/
│   │               │   ├── AuctionItem.java             # Auction lot model with bid validation
│   │               │   └── User.java                    # User entity with credential & won-item tracking
│   │               ├── service/
│   │               │   └── AuctionService.java          # Core auction business logic & timers
│   │               └── ui/
│   │                   └── AuctionSystemGUI.java        # Modern Swing desktop interface
│   └── test/
│       └── java/
│           └── com/
│               └── auction/
│                   ├── model/
│                   │   └── AuctionItemTest.java         # JUnit 5 unit tests for lot logic
│                   └── service/
│                       └── AuctionServiceTest.java      # JUnit 5 tests for service & auth
├── pom.xml                         # Apache Maven build configuration
├── .gitignore                      # Git ignore rules for Java, Maven, IDEs
└── README.md                       # Project documentation
```

---

## 🚀 Getting Started

### Prerequisites
- **Java Development Kit (JDK):** Version 17 or higher
- **Apache Maven:** Version 3.8 or higher

Verify your installation:
```bash
java -version
mvn -version
```

### Build & Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/gopikrishna1536/Online-Auction-System.git
   cd Online-Auction-System
   ```

2. **Run automated test suite:**
   ```bash
   mvn test
   ```

3. **Compile and launch the GUI application:**
   ```bash
   mvn clean compile exec:java
   ```

4. **Package executable JAR:**
   ```bash
   mvn package
   java -jar target/online-auction-system-1.0.0.jar
   ```

---

## 🧪 Testing Coverage

The automated test suite runs via the Maven Surefire plugin:
- **`AuctionItemTest`:** Validates item initialization, bid increments, rejection of low bids, rejection of bids on closed items, and entity equality.
- **`AuctionServiceTest`:** Validates user registration, duplicate prevention, password verification, admin privilege assignment, timer triggers, and auction conclusion event handlers.

---

## 🔮 Future Enhancements
- [ ] Persistence layer with SQLite / PostgreSQL using JDBC / Hibernate ORM.
- [ ] WebSocket / Socket networking for distributed multi-client network bidding.
- [ ] Email notification service for outbid alerts.
- [ ] REST API backend with Spring Boot for web & mobile clients.

---

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
