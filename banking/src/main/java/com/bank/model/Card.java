package com.bank.model;

public class Card {
    private int id;
    private String cardNumber;
    private String accountNumber;
    private String holderName;
    private String expiryDate;
    private String cvv;
    private String pin;
    private String cardType; // DEBIT or CREDIT
    private String status;   // ACTIVE or BLOCKED

    public Card() {}

    public Card(String cardNumber, String accountNumber, String holderName, String expiryDate, String cvv, String pin, String cardType) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.pin = pin;
        this.cardType = cardType;
        this.status = "ACTIVE";
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getPin() { return pin; }
    public void setPin(String pin) { this.pin = pin; }

    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
