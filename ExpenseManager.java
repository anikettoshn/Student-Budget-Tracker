import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class ExpenseManager {
    private ArrayList<Expense> expenses = new ArrayList<Expense>();
    private double monthlyBudget = 1000.0;

    public ExpenseManager() {
        loadBudget();
        loadFromCSV();
    }

    public void addExpense(Expense exp) {
        expenses.add(exp);
        Collections.sort(expenses);
        saveToCSV();
    }

    public double getTotalSpent() {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            total = total + expenses.get(i).getAmount();
        }
        return total;
    }

    public double getTotalByCategory(String category) {
        double total = 0;
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getCategory().equals(category)) {
                total = total + expenses.get(i).getAmount();
            }
        }
        return total;
    }

    public double getRemainingBudget() {
        return monthlyBudget - getTotalSpent();
    }

    public double getMonthlyBudget() { return monthlyBudget; }
    public ArrayList<Expense> getExpenses() { return expenses; }

    public void setMonthlyBudget(double budget) {
        monthlyBudget = budget;
        try {
            FileWriter writer = new FileWriter("budget.txt");
            writer.write(budget + "\n");
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save budget.");
        }
    }

    private void loadBudget() {
        try {
            File file = new File("budget.txt");
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                if (line != null) {
                    monthlyBudget = Double.parseDouble(line.trim());
                }
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("Could not load budget.");
        }
    }

    public void saveToCSV() {
        try {
            FileWriter writer = new FileWriter("expenses.csv");
            writer.write("Date,Description,Category,Amount\n");
            for (int i = 0; i < expenses.size(); i++) {
                writer.write(expenses.get(i).toCSV() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Could not save CSV.");
        }
    }

    public void loadFromCSV() {
        try {
            File file = new File("expenses.csv");
            if (!file.exists()) {
                saveToCSV();
                return;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine(); // Skip header row

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    LocalDate date = LocalDate.parse(parts[0].trim());
                    String desc = parts[1].trim();
                    String cat = parts[2].trim();
                    double amt = Double.parseDouble(parts[3].trim());
                    expenses.add(new Expense(date, desc, cat, amt));
                }
            }
            Collections.sort(expenses);
            reader.close();
        } catch (Exception e) {
            System.out.println("Could not load CSV.");
        }
    }
}
