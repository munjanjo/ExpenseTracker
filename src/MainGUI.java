import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MainGUI {

    private JFrame mainFrame;

    private JTextField descField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField categoryField;

    private JTable expensesTable;
    private DefaultTableModel tableModel;


    private JLabel totalExpensesLabel = new JLabel();


    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    private static final String[] COLUMN_NAMES =
            {"ID", "Description", "Amount", "Date", "Category"};

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.createAndShowUI();
        });
    }

    private void createAndShowUI() {
        mainFrame = new JFrame("Expenses Tracker");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 800);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setLayout(new BorderLayout());

        mainFrame.add(createTitlePanel(), BorderLayout.NORTH);
        mainFrame.add(createMainPanel(), BorderLayout.CENTER);

        mainFrame.setVisible(true);
    }

    private JPanel createTitlePanel() {
        JLabel titleLabel = new JLabel("Expenses Tracker");
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createInputPanel());
        mainPanel.add(createButtonsPanel());
        mainPanel.add(createTablePanel());
        mainPanel.add(createTotalPanel());

        return mainPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        descField = new JTextField(15);
        amountField = new JTextField(10);
        dateField = new JTextField(10);
        categoryField = new JTextField(10);

        inputPanel.add(createLabeledField("Description:", descField));
        inputPanel.add(createLabeledField("Amount:", amountField));
        inputPanel.add(createLabeledField("Date (YYYY-MM-DD):", dateField));
        inputPanel.add(createLabeledField("Category:", categoryField));

        return inputPanel;
    }

    private JPanel createLabeledField(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 20));
        field.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        panel.add(field);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JPanel createButtonsPanel() {
        JButton addButton = new JButton("Add");
        JButton removeButton = new JButton("Remove");
        JButton refreshButton = new JButton("Refresh");
        JButton updateButton = new JButton("Update");
        JButton clearButton = new JButton("Clear");

        Font btnFont = new Font("Times New Roman", Font.BOLD, 20);
        addButton.setFont(btnFont);
        removeButton.setFont(btnFont);
        refreshButton.setFont(btnFont);
        updateButton.setFont(btnFont);
        clearButton.setFont(btnFont);

        addButton.addActionListener(e -> onAdd());
        removeButton.addActionListener(e -> onRemove());
        refreshButton.addActionListener(e -> refreshTable());
        updateButton.addActionListener(e -> onUpdate());
        clearButton.addActionListener(e -> clearInputs());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return buttonPanel;
    }


    private JPanel createTablePanel() {
        JPanel expensesPanel = new JPanel();
        expensesPanel.setLayout(new BoxLayout(expensesPanel, BoxLayout.Y_AXIS));

        JLabel expensesLabel = new JLabel("Expenses:");
        expensesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        expensesLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        expensesLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
        expensesTable = new JTable(tableModel);
        expensesTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && expensesTable.getSelectedRow() != -1) {
                int row = expensesTable.getSelectedRow();

                descField.setText(tableModel.getValueAt(row, 1).toString());
                amountField.setText(tableModel.getValueAt(row, 2).toString());
                dateField.setText(tableModel.getValueAt(row, 3).toString());
                categoryField.setText(tableModel.getValueAt(row, 4).toString());
            }
        });


        JScrollPane tableScroll = new JScrollPane(expensesTable);
        tableScroll.setPreferredSize(new Dimension(800, 300));

        expensesPanel.add(expensesLabel);
        expensesPanel.add(tableScroll);


        refreshTable();

        return expensesPanel;
    }

    private JPanel createTotalPanel() {
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalExpensesLabel.setFont(new Font("Times New Roman", Font.BOLD, 22));
        double totalExpenses = expenseDAO.getTotalAmountSpent();
        totalExpensesLabel.setText("Total Expenses: "+totalExpenses);
        totalPanel.add(totalExpensesLabel);
        return totalPanel;
    }

    private void refreshTable() {
        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);

        for (Expense expense : expenseDAO.getAllExpenses()) {
            tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getDescription(),
                    expense.getAmount(),
                    expense.getDate(),
                    expense.getCategory()
            });
        }

        double total = expenseDAO.getTotalAmountSpent();
        totalExpensesLabel.setText("Total Expenses: " + total);
    }
    private void onUpdate() {
        int selectedRow = expensesTable.getSelectedRow();
        if(selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an expense");
            return;
        }
        int id=Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        Expense updated=buildExpenseFromInputs();
        if (updated == null) {return;}
        updated.setId(id);
        expenseDAO.updateExpense(updated);
        JOptionPane.showMessageDialog(null, "Expenses Updated");
        refreshTable();

    }

    private void onAdd() {
        Expense expense = buildExpenseFromInputs();
        if (expense == null) {
            return;
        }

        expenseDAO.addExpense(expense);
        JOptionPane.showMessageDialog(mainFrame, "Expense added successfully!");

        clearInputs();
        refreshTable();
    }

    private void onRemove() {
        int selectedRow = expensesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(mainFrame, "Please select an expense!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Object value = expensesTable.getValueAt(selectedRow, 0);
        if (value == null) {
            JOptionPane.showMessageDialog(mainFrame, "Please select an expense!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(value.toString());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Invalid ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                mainFrame,
                "Delete expense with ID " + id + "?",
                "Confirm delete",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        expenseDAO.deleteExpenseById(id);
        refreshTable();
    }

    private Expense buildExpenseFromInputs() {
        String desc = descField.getText().trim();
        String amountText = amountField.getText().trim();
        String date = dateField.getText().trim();
        String category = categoryField.getText().trim();

        if (desc.isEmpty() || amountText.isEmpty() || date.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Amount must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }


        return new Expense(desc, amount, date, category);
    }

    private void clearInputs() {
        descField.setText("");
        amountField.setText("");
        dateField.setText("");
        categoryField.setText("");
    }
}
