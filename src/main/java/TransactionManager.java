import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {

    private static final DateTimeFormatter date_Format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter time_Format = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static List<Transaction> loadTransactionFromFile (String filename) {
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
                transactions.add(new Transaction(date, amount, description, vendor, time));
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return transactions;
    }

    public static void saveTransaction(String filename, Transaction transaction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename,true))) {
            writer.write(transaction.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing transaction: " + e.getMessage());

            }
        }

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
                if (type.equals("ALL") || (type.equals("DEPOSIT") && t.getAmount() > 0) || (type.equals("PAYMENT") && t.getAmount() < 0)) {
                    System.out.println(t);
                    if (t.getAmount() > 0) totalDeposits += t.getAmount();
                    else totalPayments += t.getAmount();
                }
            }

            System.out.println("\nTotals:");
            System.out.println("Total Deposits: $" + totalDeposits);
            System.out.println("Total Payments: $" + totalPayments);
        }

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
