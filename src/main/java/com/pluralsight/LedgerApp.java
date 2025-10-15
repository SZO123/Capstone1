package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static final String file_Name = "transaction.csv";
    static List<Transaction> transactions = TransactionManager.loadTransactionFromFile(file_Name);

    public static void main(String[] args) {
        boolean endProgram = false;
        while (!endProgram) {
            endProgram = displayHomeScreen();
        }

    }

    public static boolean displayHomeScreen () {
        String options = """
                1) Add Deposit
                2) Make Payment
                3) Ledger
                4) Exit
                """;
        int choice = getNumericChoice(options);

        switch (choice) {
            case 1:
                addTransaction(true);
                break;
            case 2:
                addTransaction(false);
                break;
            case 3:
                displayLedgerScreen();
                break;
            case 4:
                return true;
            default:
                System.out.println("Invalid choice.");

        }
        return false;
    }

    public static void addTransaction(boolean isDeposit) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();
        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = Double.parseDouble(scanner.nextLine());
        if (!isDeposit) amount = -Math.abs(amount);

        Transaction transaction = new Transaction(LocalDate.now(), LocalTime.now(), description, vendor, amount);
        transactions.add(transaction);
        TransactionManager.saveTransaction(file_Name, transaction);

        System.out.println("Transaction added successfully.");
    }

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
                    displayReportsMenu();
                    break;
                case 5:
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid Choice.");

            }
        }
    }

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
                    System.out.println("YTD report");
                    break;
                case 4:
                    System.out.println("Previous Year report");
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

    public static int getNumericChoice(String options) {
        System.out.println(options);
        return Integer.parseInt(scanner.nextLine());
    }
}

