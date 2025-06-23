import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import java.sql.DriverManager;

public class Budget {
    private JPanel jPBudget;
    private JPanel mainPanel;
    static int YINC = 0;
    
    public Budget() {
        jPBudget = new JPanel();
        jPBudget.setLayout(new BorderLayout());
        jPBudget.setBackground(Color.decode("#28262f"));
        
        // Initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.decode("#28262f"));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton setBudget = new JButton("Set Budget");
        setBudget.setBackground(Color.decode("#FFFFFF"));
        setBudget.setPreferredSize(new Dimension(1, 50));

        ActionListener sB = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                getBudgetInfo();
            }
        };
        setBudget.addActionListener(sB);

        JScrollPane scrollBudgets = new JScrollPane(mainPanel);
        scrollBudgets.setForeground(Color.CYAN);
        scrollBudgets.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollBudgets.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPBudget.add(setBudget, BorderLayout.PAGE_START);
        jPBudget.add(scrollBudgets, BorderLayout.CENTER);
        initBudget();
    }

    void initBudget()
    {
        mainPanel.removeAll();
        String[][] budgetSet = SQLiteConnection.getBudgets();
        for (int i = 0; i < budgetSet.length; i++) {
            addBudget(budgetSet[i][0],budgetSet[i][1],Double.parseDouble(budgetSet[i][2]),Double.parseDouble(budgetSet[i][3]));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void refreshBudgetPanel() {
        mainPanel.removeAll();
        String[][] budgetSet = SQLiteConnection.getBudgets();
        for (int i = 0; i < budgetSet.length; i++) {
            addBudget(budgetSet[i][0], budgetSet[i][1], Double.parseDouble(budgetSet[i][2]), Double.parseDouble(budgetSet[i][3]));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    void addBudget(String category, String type, double limit, double spent) {
        addBudgetItem(category, type, limit, spent);
    }

    void getBudgetInfo()
    {
        JDialog info = new JDialog();
        info.setTitle("Set Budget");
        info.setSize(300, 220);
        int dX = ExpenseTracker.allocateCenterByWidth(info.getWidth());
        int dY = ExpenseTracker.allocateCenterByHeight(info.getHeight());
        info.setLocation(dX, dY);
        info.setLayout(null);
        info.getContentPane().setBackground(Color.decode("#28262f"));

        String[][] categorySet = SQLiteConnection.getCategories();
        List<String> categoryNames = new ArrayList<String>();
        for (int i = 0; i < categorySet.length; i++) {
            if(categorySet[i][1].equals("Expense"))
            {
                categoryNames.add(categorySet[i][0]);
            }
        }
        
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setBounds(40, 40, 80, 30);
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JComboBox<String> selectC = new JComboBox<String>(categoryNames.toArray(new String[0]));
        selectC.setBounds(120, 40, 140, 30);
        selectC.setBackground(Color.decode("#585f74"));
        selectC.setForeground(Color.WHITE);
        selectC.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel budgetLabel = new JLabel("Budget:");
        budgetLabel.setBounds(40, 80, 80, 30);
        budgetLabel.setForeground(Color.WHITE);
        budgetLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JTextField budget = new JTextField();
        budget.setBounds(120, 80, 140, 30);
        budget.setBackground(Color.decode("#585f74"));
        budget.setForeground(Color.WHITE);
        budget.setFont(new Font("Arial", Font.PLAIN, 14));
        budget.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JButton save = new JButton("Save");
        save.setBounds(40, 120, 100, 30);
        save.setBackground(Color.decode("#5b72ee"));
        save.setForeground(Color.WHITE);
        save.setFont(new Font("Arial", Font.BOLD, 14));
        save.setFocusPainted(false);
        save.setBorderPainted(false);
        
        JButton close = new JButton("Close");
        close.setBounds(160, 120, 100, 30);
        close.setBackground(Color.decode("#585f74"));
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 14));
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        
        ActionListener SaveClose = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JButton source = (JButton) e.getSource();
                if(source == save)
                {
                    try {
                        double limit = Double.parseDouble(budget.getText());
                        String category = (String)selectC.getSelectedItem();
                        Object[] cInfo = SQLiteConnection.getCategoryIdTypeSpent(category);
                        int id = (int)cInfo[0];
                        double spent = (double)cInfo[2];
                        SQLiteConnection.addBudget(id,limit);
                        addBudget(category, (String) cInfo[1], limit, spent);
                        info.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(info, 
                            "Please enter a valid number for the budget",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
                else if(source == close)
                {
                    info.dispose();
                }
            }
        };
        
        save.addActionListener(SaveClose);
        close.addActionListener(SaveClose);
        
        // Add hover effects
        MouseAdapter saveHover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                save.setBackground(Color.decode("#757de8"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                save.setBackground(Color.decode("#5b72ee"));
            }
        };
        
        MouseAdapter closeHover = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                close.setBackground(Color.decode("#757de8"));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                close.setBackground(Color.decode("#585f74"));
            }
        };
        
        save.addMouseListener(saveHover);
        close.addMouseListener(closeHover);
        
        info.add(categoryLabel);
        info.add(selectC);
        info.add(budgetLabel);
        info.add(budget);
        info.add(save);
        info.add(close);
        info.setModal(true);
        info.setVisible(true);
    }

    public JPanel getBudgetPanel()
    {
        return jPBudget;
    }

    private void addBudgetItem(String category, String type, double limit, double spent) {
        JPanel itemPanel = new JPanel(new BorderLayout(10, 0));
        itemPanel.setBackground(Color.decode("#28262f"));
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        itemPanel.setPreferredSize(new Dimension(600, 60));  // Fixed size for each item
        itemPanel.setMaximumSize(new Dimension(600, 60));    // Prevent expansion
        itemPanel.setMinimumSize(new Dimension(600, 60));    // Prevent shrinking

        // Create context menu
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Budget");
        JMenuItem deleteItem = new JMenuItem("Delete Budget");
        
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        // Add mouse listener for right-click
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    contextMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // Add action listeners for menu items
        editItem.addActionListener(e -> {
            String newLimitStr = JOptionPane.showInputDialog(
                itemPanel,
                "Enter new budget limit:",
                "Edit Budget",
                JOptionPane.PLAIN_MESSAGE
            );
            
            if (newLimitStr != null && !newLimitStr.trim().isEmpty()) {
                try {
                    double newLimit = Double.parseDouble(newLimitStr);
                    if (newLimit >= 0) {
                        // Get category ID
                        Object[] categoryInfo = SQLiteConnection.getCategoryIdTypeSpent(category);
                        int categoryId = (int) categoryInfo[0];
                        SQLiteConnection.addBudget(categoryId, newLimit);
                        refreshBudgetPanel();
                    } else {
                        JOptionPane.showMessageDialog(
                            itemPanel,
                            "Budget limit cannot be negative",
                            "Invalid Input",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        itemPanel,
                        "Please enter a valid number",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                itemPanel,
                "Are you sure you want to delete this budget?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                // Get category ID
                Object[] categoryInfo = SQLiteConnection.getCategoryIdTypeSpent(category);
                int categoryId = (int) categoryInfo[0];
                
                // Delete the budget
                String deleteQuery = "DELETE FROM budgets WHERE cId = ?;";
                try (Connection connected = DriverManager.getConnection(SQLiteConnection.url);
                     PreparedStatement stmt = connected.prepareStatement(deleteQuery)) {
                    
                    stmt.setInt(1, categoryId);
                    stmt.executeUpdate();
                    System.out.println("Budget deleted successfully");
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
                
                refreshBudgetPanel();
            }
        });

        // Category name and type
        JLabel categoryLabel = new JLabel(category + " (" + type + ")");
        categoryLabel.setForeground(Color.WHITE);
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setPreferredSize(new Dimension(150, 40));  // Fixed width for label
        itemPanel.add(categoryLabel, BorderLayout.WEST);

        // Progress bar for budget visualization
        JProgressBar progressBar = new JProgressBar(0, (int) limit);
        progressBar.setValue((int) spent);
        progressBar.setStringPainted(true);
        progressBar.setString(String.format("₹%.2f / ₹%.2f", spent, limit));
        progressBar.setForeground(spent > limit ? Color.decode("#e74c3c") : Color.decode("#2ecc71"));
        progressBar.setBackground(Color.decode("#3a3844"));
        progressBar.setBorderPainted(false);
        progressBar.setPreferredSize(new Dimension(400, 25));  // Fixed width for progress bar
        itemPanel.add(progressBar, BorderLayout.CENTER);

        // Add the item panel to the main panel
        mainPanel.add(itemPanel);
    }
}