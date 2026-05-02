# 🏦 Harish Smart Bank - Premium Digital Banking Platform

Harish Smart Bank is a sophisticated, production-ready digital banking application designed for the modern era. It features a high-end **Corporate Blue & Midnight Navy** interface, secure account management, and real-time transaction tracking.

![Bank Hero](https://images.unsplash.com/photo-1501167786227-4cba60f6d58f?ixlib=rb-4.0.3&auto=format&fit=crop&w=2070&q=80)

## ✨ Key Features

### 👤 User Features
*   **Secure Dashboard**: Overview of account balance, active cards, and recent activity.
*   **Real-time Transactions**: Perform deposits, withdrawals, and instant person-to-person transfers.
*   **Card Management**: View and manage your virtual debit and credit cards with an elite visual design.
*   **Loan Portal**: Apply for personal and business loans directly from the dashboard.
*   **Transaction History**: Detailed, color-coded history of all income and expenses.

### 🔑 Admin Features
*   **System Oversight**: Monitor total managed assets and verified accounts.
*   **Account Governance**: Block/Unblock suspicious accounts or terminate inactive ones.
*   **Card Clearing**: Specialized tool to clear card counts for re-issuance.
*   **Loan Underwriting**: Approve or reject pending loan requests with one click.
*   **Advanced Search**: Locate any account in the system by name or account number.

## 🛠️ Technology Stack

### Frontend
*   **React.js**: For a dynamic and responsive single-page application experience.
*   **Vanilla CSS**: Custom premium glassmorphic design system with midnight theme optimizations.
*   **Inter Font**: Optimized for high-contrast, professional banking typography.

### Backend
*   **Java Spring Boot**: Robust, scalable RESTful API architecture.
*   **MySQL**: Reliable relational database for persistent financial data.
*   **Spring Data JPA**: Efficient database interaction and object mapping.

## 🚀 Getting Started

### Prerequisites
*   Java 17 or higher
*   Node.js & npm
*   MySQL Server

### Installation

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/YOUR_USERNAME/Harish-Smart-Bank.git
    cd Harish-Smart-Bank
    ```

2.  **Database Setup**:
    *   Create a database named `harishsmartbank`.
    *   Execute the `setup_db.sql` script to initialize tables.
    *   Update `banking/src/main/resources/application.properties` with your MySQL credentials.

3.  **Run Backend**:
    ```bash
    cd banking
    ./mvnw spring-boot:run
    ```

4.  **Run Frontend**:
    ```bash
    cd bank-frontend
    npm install
    npm start
    ```

## 🔒 Security
Harish Smart Bank implements strict financial governance, including a **One-Card-Per-Account** policy and secure account blocking mechanisms to protect user assets.

## 📄 License
This project is for educational purposes. All rights reserved.

---
*Designed with ❤️ for a premium banking experience.*
