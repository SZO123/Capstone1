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
    private static final DateTimeFormatter time_Format = DateTimeFormatter.ofPattern("HH:mm");


    // Load transactions from a file safely
    public static List<Transaction> loadTransactionFromFile(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filename);

        // Return empty list if file not found
        if (!file.exists()) {
            System.out.println("No file found (" + filename + "). A new one will be created later.");
            return transactions;
        }

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat12 = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter timeFormat24 = DateTimeFormatter.ofPattern("HH:mm");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // skip header if present
            while ((line = reader.readLine()) != null) {
                // Support both comma and pipe separators
                String[] parts = line.split("[,|]");
                if (parts.length < 5) continue; // skip bad lines

                LocalDate date = LocalDate.parse(parts[0].trim(), dateFormat);

                // Try both time formats (e.g. "08:29 PM" or "20:29")
                LocalTime time;
                String timeStr = parts[1].trim();
                try {
                    time = LocalTime.parse(timeStr, timeFormat24);
                } catch (Exception e) {
                    time = LocalTime.parse(timeStr, timeFormat12);
                }

                String description = parts[2].trim();
                String vendor = parts[3].trim();

                // Handle possible currency symbols in amount
                String amtStr = parts[4].replace("$", "").replace(",", "").trim();
                double amount = Double.parseDouble(amtStr);

                transactions.add(new Transaction(date, time, description, vendor, amount));
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Parsing error: " + e.getMessage());
        }


        return transactions;
    }


    // Save a transaction to the file
    public static void saveTransaction(String filename, Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction:" + e.getMessage());

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
