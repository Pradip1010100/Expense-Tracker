import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HomePage {
    private JFrame homeFrame;
    private JLabel totalIncomeLabel;
    private JLabel totalExpenseLabel;
    private static final DecimalFormat currencyFormat = new DecimalFormat("₹#,##0.00");
    
    public HomePage() {
        homeFrame = new JFrame("Expense Tracker - Home");
        homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homeFrame.setSize(500, 400);
        homeFrame.setLocationRelativeTo(null);
    
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#28262f"));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Title
        JLabel titleLabel = new JLabel("Expense Tracker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));

        // Summary Panel
        JPanel summaryPanel = createSummaryPanel();
        summaryPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(summaryPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        // View Records Button
        JButton viewRecordsButton = new JButton("View Records");
        viewRecordsButton.setFont(new Font("Arial", Font.BOLD, 14));
        viewRecordsButton.setBackground(Color.decode("#5b72ee"));
        viewRecordsButton.setForeground(Color.WHITE);
        viewRecordsButton.setFocusPainted(false);
        viewRecordsButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        viewRecordsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        viewRecordsButton.addActionListener(e -> {
            homeFrame.dispose();
            new ExpenseTracker();
        });
        
        mainPanel.add(viewRecordsButton);

        homeFrame.add(mainPanel);
        homeFrame.setVisible(true);
        
        // Initial update
        updateSummary();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 20));
        panel.setBackground(Color.decode("#28262f"));

        // Income Panel
        JPanel incomePanel = new JPanel();
        incomePanel.setBackground(Color.decode("#2ecc71"));
        incomePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        incomePanel.setLayout(new BoxLayout(incomePanel, BoxLayout.Y_AXIS));
        
        JLabel incomeTitleLabel = new JLabel("Total Income");
        incomeTitleLabel.setForeground(Color.WHITE);
        incomeTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        incomeTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        totalIncomeLabel = new JLabel("₹0.00");
        totalIncomeLabel.setForeground(Color.WHITE);
        totalIncomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalIncomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        incomePanel.add(incomeTitleLabel);
        incomePanel.add(Box.createVerticalStrut(10));
        incomePanel.add(totalIncomeLabel);

        // Expense Panel
        JPanel expensePanel = new JPanel();
        expensePanel.setBackground(Color.decode("#e74c3c"));
        expensePanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        expensePanel.setLayout(new BoxLayout(expensePanel, BoxLayout.Y_AXIS));
        
        JLabel expenseTitleLabel = new JLabel("Total Expense");
        expenseTitleLabel.setForeground(Color.WHITE);
        expenseTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        expenseTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        totalExpenseLabel = new JLabel("₹0.00");
        totalExpenseLabel.setForeground(Color.WHITE);
        totalExpenseLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalExpenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        expensePanel.add(expenseTitleLabel);
        expensePanel.add(Box.createVerticalStrut(10));
        expensePanel.add(totalExpenseLabel);

        panel.add(incomePanel);
        panel.add(expensePanel);

        return panel;
    }

    public void updateSummary() {
        String[][] records = SQLiteConnection.getRecords();
        double totalIncome = 0;
        double totalExpense = 0;

        for (String[] record : records) {
            double amount = Double.parseDouble(record[3]);
            if (record[4].equalsIgnoreCase("Income")) {
                totalIncome += amount;
            } else {
                totalExpense += amount;
            }
        }

        totalIncomeLabel.setText(currencyFormat.format(totalIncome));
        totalExpenseLabel.setText(currencyFormat.format(totalExpense));
    }

    public static void main(String[] args) {
        // Set up database connection
        SQLiteConnection.initSQLite();
        
        // Start the application with the home page
        SwingUtilities.invokeLater(() -> new HomePage());
    }
} 