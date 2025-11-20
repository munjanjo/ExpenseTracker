import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database.init();

        ExpenseDAO dao = new ExpenseDAO();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== EXPENSE TRACKER ===");
            System.out.println("1) Add expense");
            System.out.println("2) Show all expenses");
            System.out.println("3) Delete expense");
            System.out.println("4) Show total expense");
            System.out.println("0) Exit");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {

                    System.out.print("Description: ");
                    String description = scanner.nextLine();

                    System.out.print("Amount: ");
                    double amount;
                    try {
                        amount = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount.");
                        break;
                    }

                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();

                    System.out.print("Category: ");
                    String category = scanner.nextLine();

                    Expense expense = new Expense(description, amount, date, category);
                    dao.addExpense(expense);
                }

                case "2" -> {

                    List<Expense> all = dao.getAllExpenses();
                    if (all.isEmpty()) {
                        System.out.println("No expenses found.");
                    } else {
                        System.out.println("\nID | Date       | Category | Description | Amount");
                        System.out.println("--------------------------------------------------");
                        for (Expense e : all) {
                            System.out.println(e);
                        }
                    }
                }

                case "3" -> {

                    System.out.print("Enter ID to delete: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine());
                        dao.deleteExpenseById(id);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID.");
                    }
                }

                case "4" -> {

                    double total = dao.getTotalAmountSpent();
                    System.out.println("Total expense: " + total);
                }

                case "0" -> {

                    running = false;
                    System.out.println("Exiting...");
                }

                default -> System.out.println("Unknown option, try again.");
            }
        }

        scanner.close();
    }
}
