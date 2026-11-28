import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static String[] categories = {"Food", "Books", "Housing", "Transport", "Other"};
    private static Scanner in = new Scanner(System.in);
    private static ExpenseManager manager = new ExpenseManager();
    private static NumberFormat money = NumberFormat.getCurrencyInstance();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            System.out.println("\n--- STUDENT BUDGET TRACKER ---");
            System.out.println("1. Add Expense");
            System.out.println("2. Calculate Total Spent and Remaining Budget");
            System.out.println("3. Display Expenses by Category");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");

            int choice = readInt();

            if (choice == 1) {
                addExpense();
            } else if (choice == 2) {
                calculateBudget();
            } else if (choice == 3) {
                displayByCategory();
            } else if (choice == 4) {
                System.out.println("Exiting Student Budget Tracker. Goodbye!");
                running = false;
            } else {
                System.out.println("Invalid choice. Please enter 1, 2, 3, or 4.");
            }
        }
        in.close();
    }

    private static void addExpense() {
        System.out.print("Date (YYYY-MM-DD, or press Enter for today): ");
        String dateInput = in.nextLine().trim();
        LocalDate date;
        try {
            if (dateInput.isEmpty()) {
                date = LocalDate.now();
            } else {
                date = LocalDate.parse(dateInput);
            }
        } catch (Exception e) {
            System.out.println("Invalid date. Use YYYY-MM-DD.");
            return;
        }

        System.out.print("Description: ");
        String desc = in.nextLine();
        if (desc.trim().isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        String category = pickCategory();
        if (category.equals("")) {
            return;
        }

        System.out.print("Amount ($): ");
        double amount = readDouble();
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        manager.addExpense(new Expense(date, desc, category, amount));
        System.out.println("Expense added and saved to CSV!");
    }

    private static void calculateBudget() {
        System.out.println("\nCurrent Monthly Budget: " + money.format(manager.getMonthlyBudget()));
        System.out.print("Press Enter to keep this budget, or enter a new budget ($): ");
        String input = in.nextLine().trim();
        if (!input.isEmpty()) {
            try {
                double newBudget = Double.parseDouble(input);
                if (newBudget >= 0) {
                    manager.setMonthlyBudget(newBudget);
                } else {
                    System.out.println("Budget cannot be negative. Keeping previous budget.");
                }
            } catch (Exception e) {
                System.out.println("Invalid number. Keeping previous budget.");
            }
        }

        System.out.println("\n--- BUDGET SUMMARY ---");
        System.out.println("Monthly Budget:   " + money.format(manager.getMonthlyBudget()));
        System.out.println("Total Spent:      " + money.format(manager.getTotalSpent()));
        System.out.println("Remaining Budget: " + money.format(manager.getRemainingBudget()));
    }

    private static void displayByCategory() {
        System.out.println("\nSelect a category to display:");
        System.out.println("0. All Categories");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Choose option (0-5): ");
        int choice = readInt();

        if (choice < 0 || choice > categories.length) {
            System.out.println("Invalid category choice.");
            return;
        }

        ArrayList<Expense> list = manager.getExpenses();
        boolean found = false;

        if (choice == 0) {
            System.out.println("\n--- ALL EXPENSES ---");
            for (int i = 0; i < list.size(); i++) {
                Expense e = list.get(i);
                System.out.println((i + 1) + ". " + e.getDate() + " | " + e.getCategory() + " | " + money.format(e.getAmount()) + " | " + e.getDescription());
                found = true;
            }
        } else {
            String selected = categories[choice - 1];
            System.out.println("\n--- EXPENSES IN " + selected.toUpperCase() + " ---");
            for (int i = 0; i < list.size(); i++) {
                Expense e = list.get(i);
                if (e.getCategory().equals(selected)) {
                    System.out.println((i + 1) + ". " + e.getDate() + " | " + money.format(e.getAmount()) + " | " + e.getDescription());
                    found = true;
                }
            }
        }

        if (!found) {
            System.out.println("No expenses found.");
        }
    }

    private static String pickCategory() {
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        System.out.print("Choose category (1-5): ");
        int choice = readInt();

        if (choice >= 1 && choice <= categories.length) {
            return categories[choice - 1];
        } else {
            System.out.println("Invalid category.");
            return "";
        }
    }

    // Reads whole lines before parsing so Scanner never skips the next input
    private static int readInt() {
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static double readDouble() {
        try {
            return Double.parseDouble(in.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}