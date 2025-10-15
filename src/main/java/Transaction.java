import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {

    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(LocalDate date, double amount, String vendor, String description, LocalTime time) {
        this.date = date;
        this.amount = amount;
        this.vendor = vendor;
        this.description = description;
        this.time = time;
    }

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

    public String toString() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }
}

