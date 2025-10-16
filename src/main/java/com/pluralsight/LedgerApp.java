package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class LedgerApp {
    // Scanner for user input
    static Scanner scanner = new Scanner(System.in);

    // CSV file name for saving/loading transactions
    static final String file_Name = "transaction.csv";

    // Load all transactions at startup
    static List<Transaction> transactions = TransactionManager.loadTransactionFromFile(file_Name);

    public static void main(String[] args) {
        boolean endProgram = false;

        // Main application loop
        while (!endProgram) {
            endProgram = displayHomeScreen();
        }

    }
    // Displays the main menu (Home Screen)
    public static boolean displayHomeScreen () {
        System.out.println("==========================================");
        System.out.println("  💰 Welcome to Najib's LedgerProApp 💰     ");
        System.out.println("    Track Deposits, Payments & Reports");
        System.out.println("==========================================");

        String options = """
                1) Add Deposit
                2) Make Payment
                3) Ledger
                4) Exit
                """;
        int choice = getNumericChoice(options);

        switch (choice) {
            case 1:
                addTransaction(true);  // Deposit
                break;
            case 2:
                addTransaction(false);  // Payment
                break;
            case 3:
                displayLedgerScreen();  // View Ledger menu
                break;
            case 4:
                return true;  // Exit program
            default:
                System.out.println("Invalid choice.");

        }
        return false;
    }

    // Handles adding a deposit or payment transaction
    public static void addTransaction(boolean isDeposit) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // If payment, make amount negative
        if (!isDeposit) amount = -Math.abs(amount);

        // Create and save transaction
        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        transactions.add(transaction);
        TransactionManager.saveTransaction(file_Name, transaction);

        System.out.println("Transaction added successfully.");
    }

    // Displays the Ledger menu with transaction filters
    public static void displayLedgerScreen() {
        boolean inLedger = true;
        while (inLedger) {
            String options = """
                    1) All Transaction
                    2) Deposits
                    3) Payments
                    4) Reports
                    5) Back to Home
                    """;
            int choice = getNumericChoice(options);

            switch (choice) {
                case 1:
                    TransactionManager.displayTransaction(transactions, "ALL");
                    break;
                case 2:
                    TransactionManager.displayTransaction(transactions, "DEPOSIT");
                    break;
                case 3:
                    TransactionManager.displayTransaction(transactions, "PAYMENT");
                    break;
                case 4:
                    displayReportsMenu();  // Jump to Reports
                    break;
                case 5:
                    inLedger = false;  // Return to home
                    break;
                default:
                    System.out.println("Invalid Choice.");

            }
        }
    }

    // Reports menu — placeholder for date-based reports
    public static void displayReportsMenu() {
        boolean inReports = true;
        while(inReports) {
            String options = """
                    1) Month To Date
                    2) Previous Month
                    3) Year To Date
                    4) Previous Year
                    5) Search by Vendor
                    0) Back
                    """;
            int choice = getNumericChoice(options);

            switch (choice) {
                case 1:
                    System.out.println("MTD report ");
                    break;
                case 2:
                    System.out.println("Previous Month report ");
                case 3:
                    System.out.println("YTD report ");
                    break;
                case 4:
                    System.out.println("Previous Year report ");
                    break;
                case 5:
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine();
                    List<Transaction> filtered = TransactionManager.filterByVendor(transactions, vendor);
                    TransactionManager.displayTransaction(filtered, "ALL");
                    break;
                case 0:
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid choice.");

            }
        }
    }

    // Helper method for consistent numeric input handling
    public static int getNumericChoice(String options) {
        System.out.println(options);
        return Integer.parseInt(scanner.nextLine());
    }
}

