## Capstone1 – Java Ledger App

A simple **Java CLI Ledger** that records deposits and payments and displays reports.

## How it works
- **Home Menu** (in `LedgerApp.java`):  
  1) Add Deposit  
  2) Make Payment  
  3) Ledger  
  4) Exit

- **Ledger Menu**:  
  1) All Transactions  
  2) Deposits  
  3) Payments  
  4) Reports  
  5) Back to Home

- **Reports Menu**:  
  1) Month To Date  
  2) Previous Month  
  3) Year To Date  
  4) Previous Year  
  5) Search by Vendor  
  0) Back

Data is persisted in a local CSV-like text file named **`transaction.csv`** as pipe-delimited rows:
```
YYYY-MM-DD|HH:MM:SS|description|vendor|amount
```
Deposits are stored as positive amounts; payments as negative amounts.

## Source Layout
- `src/main/java/com/pluralsight/LedgerApp.java` – menus, input handling, and screen flow  
- `src/main/java/com/pluralsight/Transaction.java` – POJO for a single transaction (date, time, description, vendor, amount)  
- `src/main/java/com/pluralsight/TransactionManager.java` – file I/O, parsing/formatting dates, saving, loading, filtering, and totals  
- `pom.xml` – Maven project descriptor

## Build & Run (Maven)
```bash
# from the repo root
mvn -q -e -DskipTests package
java -cp target/Capstone1-1.0-SNAPSHOT.jar com.pluralsight.LedgerApp
```

If running from an IDE, set the main class to **`com.pluralsight.LedgerApp`**.

## Usage
- Choose **1** to add a deposit (amount > 0).  
- Choose **2** to make a payment; enter description, vendor, and amount (stored as negative).  
- Choose **3** for the Ledger/Reports screens.  
- Totals for **Deposits** and **Payments** are shown at the bottom of each ledger view.

## Known Behaviors
- Input is read from `Scanner`; invalid numeric input will throw `NumberFormatException` if not handled at the call site.
- File is appended when saving; ensure `transaction.csv` is writable in the working directory.
- Filtering by vendor is case-insensitive substring match.

## Future Improvements 
- Validate numeric input with retry prompts.
- Extract constants for menus and messages.
- Add unit tests for `TransactionManager` parsing and filters.
- Replace flat-file with H2/SQLite and add running balance.

