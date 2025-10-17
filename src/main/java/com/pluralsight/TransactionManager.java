package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    // Date and time formats
    private static final DateTimeFormatter date_Format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter time_Format = DateTimeFormatter.ofPattern("hh:mm a");

    // Load transactions from a file
    public static List<Transaction> loadTransactionFromFile(String filename) {
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) return transactions;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
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

    // Save a transaction to a file
    public static void saveTransaction(String filename, Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());
        }
    }

    // Display transactions
    public static void displayTransaction(List<Transaction> transactions, String type) {
        double totalDeposits = 0;
        double totalPayments = 0;

        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }
        System.out.println("\n---- Ledger ----");
        for (int i = transactions.size() - 1; i >= 0; i--) {
            Transaction t = transactions.get(i);

            if (type.equals("ALL") || (type.equals("DEPOSIT") && t.getAmount() > 0)
                    || (type.equals("PAYMENT") && t.getAmount() < 0)) {
                System.out.println(t);
                if (t.getAmount() > 0) totalDeposits += t.getAmount();
                else totalPayments += t.getAmount();
            }
        }

        System.out.println("\nTotals:");
        System.out.println("Total Deposits: $" + totalDeposits);
        System.out.println("Total Payments: $" + totalPayments);
    }

    // Filter by vendor
    public static List<Transaction> filterByVendor(List<Transaction> transactions, String vendor) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                result.add(t);
            }
        }
        return result;
    }

    // Filter transactions Month-To-Date
    public static List<Transaction> filterByMonthToDate(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate today = LocalDate.now();

        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(startOfMonth) && !t.getDate().isAfter(today)) {
                result.add(t);
            }
        }
        return result;
    }

    // Filter transactions Previous Month
    public static List<Transaction> filterByPreviousMonth(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        LocalDate firstDayPrevMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayPrevMonth = firstDayPrevMonth.withDayOfMonth(firstDayPrevMonth.lengthOfMonth());

        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(firstDayPrevMonth) && !t.getDate().isAfter(lastDayPrevMonth)) {
                result.add(t);
            }
        }
        return result;
    }

    // Filter transactions Year-To-Date
    public static List<Transaction> filterByYearToDate(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        LocalDate startOfYear = LocalDate.now().withDayOfYear(1);
        LocalDate today = LocalDate.now();

        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(startOfYear) && !t.getDate().isAfter(today)) {
                result.add(t);
            }
        }
        return result;
    }

    // Filter transactions Previous Year
    public static List<Transaction> filterByPreviousYear(List<Transaction> transactions) {
        List<Transaction> result = new ArrayList<>();
        int lastYear = LocalDate.now().getYear() - 1;
        LocalDate startPrevYear = LocalDate.of(lastYear, 1, 1);
        LocalDate endPrevYear = LocalDate.of(lastYear, 12, 31);

        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(startPrevYear) && !t.getDate().isAfter(endPrevYear)) {
                result.add(t);
            }
        }
        return result;
    }
}