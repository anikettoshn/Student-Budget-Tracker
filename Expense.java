import java.time.LocalDate;

public class Expense implements Comparable<Expense> {
    private LocalDate date;
    private String description;
    private String category;
    private double amount;

    public Expense(LocalDate date, String description, String category, double amount) {
        this.date = date;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }

    // Replace commas so CSV columns do not break when saved
    public String toCSV() {
        String cleanDesc = description.replace(",", " ");
        return date + "," + cleanDesc + "," + category + "," + amount;
    }

    // Allows Collections.sort() to sort expenses newest-first
    @Override
    public int compareTo(Expense other) {
        return other.date.compareTo(this.date);
    }
}