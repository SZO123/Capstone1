package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {

    // Transaction details
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    // 12-hour display format
    private static final DateTimeFormatter displayTimeFormat = DateTimeFormatter.ofPattern("hh:mm a");

    // Constructor to create a transaction
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.amount = amount;
        this.vendor = vendor;
        this.description = description;
        this.time = time;
    }

    // Getters for transaction details
    public LocalDate getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getTime() {
        return time;
    }

    // Convert transaction to a string for saving or displaying
    public String toString() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        return date + "|" + time.format(displayTimeFormat) + "|" + description + "|" + vendor + "|" + amount;
    }
}

