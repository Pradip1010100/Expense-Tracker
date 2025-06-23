import javax.swing.*;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
// import SQLiteConnection.*;
import java.awt.GridBagLayout;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.Font;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ExpenseTracker
{
    static JFrame jFExpenseTracker;
    JButton jBRecord,jBAnalysis,jBBudget,jBCategory;
    static Record r;
    static Analysis a;
    static Budget b;
    static Category c;
    // static ExpenseTracker expenseTracker;
    static int xAxis;
    static int yAxis;
    static int width;
    static int height;
    private static ExpenseTracker instance;
    
    // Add these field declarations
    private JPanel summaryPanel;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private static final DecimalFormat currencyFormat = new DecimalFormat("₹#,##0.00");
    
    private JPanel mainPanel;
    private JPanel navigationPanel;
    
    //Default Constructor For Expense Tracker
    public ExpenseTracker()
    {
        instance = this;
        //SQLiteConnection.dropAllTables();       //NOTE : If you want to delete all table then remove comments (Development Use)
        jFExpenseTracker = new JFrame("Expense Tracker");
        jFExpenseTracker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFExpenseTracker.setLayout(new BorderLayout());
        jFExpenseTracker.setSize(800, 600);
        jFExpenseTracker.setLocationRelativeTo(null);
        
        // Initialize database
        SQLiteConnection.initSQLite();

        // Initialize components
        mainPanel = new JPanel(new BorderLayout());
        navigationPanel = createNavigationPanel();

        // Create and add summary panel at the top
        JPanel summaryPanel = createSummaryPanel();
        jFExpenseTracker.add(summaryPanel, BorderLayout.NORTH);

        // Initialize all panels
        r = new Record();
        a = new Analysis();
        b = new Budget();
        c = new Category();

        // Set initial view to Record panel
        mainPanel.add(r.getRecordPanel(), BorderLayout.CENTER);

        // Add panels to frame
        jFExpenseTracker.add(mainPanel, BorderLayout.CENTER);
        jFExpenseTracker.add(navigationPanel, BorderLayout.SOUTH);
        
        jFExpenseTracker.setVisible(true);
        jFExpenseTracker.getContentPane().setBackground(Color.decode("#BDBDBD"));
        
        // Initialize summary
        updateMonthlySummary();
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.decode("#28262f"));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 10, 0, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Income Panel
        JPanel incomePanel = new JPanel(new BorderLayout(5, 5));
        incomePanel.setBackground(Color.decode("#2ecc71"));
        incomePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel incomeTitle = new JLabel("Monthly Income");
        incomeTitle.setForeground(Color.WHITE);
        incomeTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        incomeLabel = new JLabel("₹0.00"); // Initialize the class field
        incomeLabel.setForeground(Color.WHITE);
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        incomePanel.add(incomeTitle, BorderLayout.NORTH);
        incomePanel.add(incomeLabel, BorderLayout.CENTER);

        // Expense Panel
        JPanel expensePanel = new JPanel(new BorderLayout(5, 5));
        expensePanel.setBackground(Color.decode("#e74c3c"));
        expensePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel expenseTitle = new JLabel("Monthly Expenses");
        expenseTitle.setForeground(Color.WHITE);
        expenseTitle.setFont(new Font("Arial", Font.BOLD, 14));
        
        expenseLabel = new JLabel("₹0.00"); // Initialize the class field
        expenseLabel.setForeground(Color.WHITE);
        expenseLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        expensePanel.add(expenseTitle, BorderLayout.NORTH);
        expensePanel.add(expenseLabel, BorderLayout.CENTER);

        // Add panels to main summary panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        panel.add(incomePanel, gbc);

        gbc.gridx = 1;
        panel.add(expensePanel, gbc);

        return panel;
    }

    public void updateMonthlySummary() {
        String[][] records = SQLiteConnection.getRecords();
        double monthlyIncome = 0;
        double monthlyExpense = 0;
        
        // Get current month and year
        LocalDateTime now = LocalDateTime.now();
        String currentMonthYear = now.format(DateTimeFormatter.ofPattern("MM-yyyy"));
        
        for (String[] record : records) {
            String recordDate = record[2];
            String recordMonthYear = recordDate.split(" ")[1].substring(3);
            
            if (recordMonthYear.equals(currentMonthYear)) {
                double amount = Double.parseDouble(record[3]);
                if (record[4].equalsIgnoreCase("Income")) {
                    monthlyIncome += amount;
                } else {
                    monthlyExpense += amount;
                }
            }
        }
        
        incomeLabel.setText(currencyFormat.format(monthlyIncome));
        expenseLabel.setText(currencyFormat.format(monthlyExpense));
    }

    public static void refreshSummary() {
        if (instance != null) {
            instance.updateMonthlySummary();
        }
    }

    public static void frameResetSize()
    {
        xAxis = jFExpenseTracker.getX();
        yAxis = jFExpenseTracker.getY();
        width = jFExpenseTracker.getWidth();
        height = jFExpenseTracker.getHeight();
    }

    static int allocateCenterByWidth(int ContainerWidth)
    {
        frameResetSize();
        return ExpenseTracker.xAxis + (ExpenseTracker.width - ContainerWidth)/2;
    }

    static int allocateCenterByHeight(int ContainerHeight)
    {
        frameResetSize();
        return ExpenseTracker.yAxis + (ExpenseTracker.height - ContainerHeight)/2;
    }
    private void animateColor(JButton button, Color targetColor) {
        Timer timer = new Timer(20, null);
        ActionListener animation = new ActionListener() {
            float progress = 0.0f;
            Color startColor = button.getBackground();
    
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 0.1f; // Speed of animation (adjust for smoothness)
                if (progress > 1.0f) {
                    progress = 1.0f;
                    ((Timer) e.getSource()).stop();
                }
                int red = (int) ((1 - progress) * startColor.getRed() + progress * targetColor.getRed());
                int green = (int) ((1 - progress) * startColor.getGreen() + progress * targetColor.getGreen());
                int blue = (int) ((1 - progress) * startColor.getBlue() + progress * targetColor.getBlue());
                button.setBackground(new Color(red, green, blue));
            }
        };
        timer.addActionListener(animation);
        timer.start();
    }
    
    // Add hover effect with animation
    MouseAdapter hoverEffect = new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            if (!source.getBackground().equals(Color.decode("#5b72ee"))) {
                animateColor(source, Color.decode("#757de8")); // Light blue on hover
            }
        }
    
        @Override
        public void mouseExited(MouseEvent e) {
            JButton source = (JButton) e.getSource();
            if (!source.getBackground().equals(Color.decode("#5b72ee"))) {
                animateColor(source, Color.decode("#585f74")); // Default color
            }
        }
    };

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        navPanel.setBackground(new Color(40, 38, 47));
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create navigation buttons
        jBRecord = createNavButton("Records");
        jBAnalysis = createNavButton("Analysis");
        jBBudget = createNavButton("Budget");
        jBCategory = createNavButton("Category");

        // Add action listeners
        jBRecord.addActionListener(e -> switchToPanel(r.getRecordPanel()));
        jBAnalysis.addActionListener(e -> switchToPanel(a.getAnalysisPanel()));
        jBBudget.addActionListener(e -> switchToPanel(b.getBudgetPanel()));
        jBCategory.addActionListener(e -> switchToPanel(c.getCategoryPanel()));

        // Add buttons to panel
        navPanel.add(jBRecord);
        navPanel.add(jBAnalysis);
        navPanel.add(jBBudget);
        navPanel.add(jBCategory);

        return navPanel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(88, 95, 116));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(117, 125, 232));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(88, 95, 116));
            }
        });

        return button;
    }

    private void switchToPanel(JPanel panel) {
        mainPanel.removeAll();
        mainPanel.add(panel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static void main(String[] args)
    {
        ExpenseTracker e = new ExpenseTracker();
    }
}
