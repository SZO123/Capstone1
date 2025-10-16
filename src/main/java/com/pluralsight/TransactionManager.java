package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    // Date and time formats for saving/loading
    private static final DateTimeFormatter date_Format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter time_Format = DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS");

    // Load transactions from a file
    public static List<Transaction> loadTransactionFromFile (String filename) {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return transactions; // Return empty list if file not found

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // Split line by '|'
                LocalDate date = LocalDate.parse(parts[0], date_Format);
                LocalTime time = LocalTime.parse(parts[1], time_Format);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);
                transactions.add(new Transaction(date, time, description, vendor, amount));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }

    // Save a transaction to the file
    public static void saveTransaction(String filename, Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());

            }
        }

        // Display transactions, filtered by type
        public static void displayTransaction(List<Transaction> transactions, String type) {
            double totalDeposits = 0;
            double totalPayments = 0;

            if (transactions.isEmpty()) {
                System.out.println("No transactions available.");
                return;
            }
            System.out.println("\n---- Ledger ----");
            for (int i = transactions.size() - 1; i >= 0; i--) {  // Show newest first
                Transaction t = transactions.get(i);

                // Check if transaction matches type
                if (type.equals("ALL") || (type.equals("DEPOSIT") && t.getAmount() > 0) || (type.equals("PAYMENT") && t.getAmount() < 0)) {
                    System.out.println(t);  // Print transaction

                    // Add to totals
                    if (t.getAmount() > 0) totalDeposits += t.getAmount();
                    else totalPayments += t.getAmount();
                }
            }

            // Print totals
            System.out.println("\nTotals:");
            System.out.println("Total Deposits: $" + totalDeposits);
            System.out.println("Total Payments: $" + totalPayments);
        }

        // Filter transactions by vendor name
        public static List<Transaction> filterByVendor(List<Transaction> transactions, String vendor) {
            List<Transaction> result = new ArrayList<>();
            for (Transaction t: transactions) {
                if (t.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                    result.add(t);
                }
            }
            return result;
    }
}
