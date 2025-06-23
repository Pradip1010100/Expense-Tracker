import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
// import SQLiteConnection.*; 

public class Category {
    JPanel jPCategory;
    JPanel incomeCategory;
    JPanel expenseCategory;
    public Category()
    {

        jPCategory = new JPanel();
        jPCategory.setLayout(new BorderLayout()); 
        JPanel ieP = new JPanel();
        ieP.setLayout(new BoxLayout(ieP, BoxLayout.X_AXIS));

        JPanel incomeMainPanel = new JPanel();
        incomeMainPanel.setLayout(new BorderLayout());
        incomeMainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel il = new JPanel();
        JLabel incomeLabel = new JLabel("Income Categories");
        incomeLabel.setFont(new Font("Arial",Font.BOLD,18));
        il.setPreferredSize(new Dimension(1, 50));
        
        il.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        il.add(incomeLabel);
        il.setBackground(Color.decode("#585f74"));
        incomeLabel.setForeground(Color.decode("#FFFFFF"));

        // Income Category Panel
        incomeCategory = new JPanel();
        incomeCategory.setLayout(new BoxLayout(incomeCategory,BoxLayout.Y_AXIS)); 
        incomeCategory.setBackground(Color.decode("#28262F"));
        JScrollPane scrollPaneI = new JScrollPane(incomeCategory);
        scrollPaneI.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneI.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        JPanel expenseMainPanel = new JPanel();
        expenseMainPanel.setLayout(new BorderLayout());
        expenseMainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //Expense Category Panel
        
        JPanel el = new JPanel();
        JLabel expenseLabel = new JLabel("Expense Categories   ");
        expenseLabel.setFont(new Font("Arial",Font.BOLD,18));
        expenseLabel.setForeground(Color.decode("#ffffff"));
        el.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        el.add(expenseLabel);
        el.setBackground(Color.decode("#585f74"));
        el.setPreferredSize(new Dimension(1, 50));

        expenseCategory = new JPanel();
        expenseCategory.setLayout(new BoxLayout(expenseCategory,BoxLayout.Y_AXIS));
        expenseCategory.setBackground(Color.decode("#28262F"));
        JScrollPane scrollPaneE = new JScrollPane(expenseCategory);
        scrollPaneE.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneE.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        incomeMainPanel.add(il,BorderLayout.PAGE_START);
        incomeMainPanel.add(scrollPaneI,BorderLayout.CENTER);
        expenseMainPanel.add(el,BorderLayout.PAGE_START);
        expenseMainPanel.add(scrollPaneE,BorderLayout.CENTER);
        ieP.add(incomeMainPanel);
        ieP.add(expenseMainPanel);

        JButton newCategory = new JButton("New Category   "); 
        newCategory.setBackground(Color.decode("#FFFFFF"));
        newCategory.setPreferredSize(new Dimension(1, 50));
        ActionListener newC = new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getCategoryInfo();
            }
        };
        newCategory.addActionListener(newC);
        jPCategory.add(newCategory,BorderLayout.PAGE_START);
        jPCategory.add(ieP,BorderLayout.CENTER);
        initCategory();
    }

    void initCategory(){
        String[][] categorySet = SQLiteConnection.getCategories();
        for (int i = 0; i < categorySet.length; i++) {
            addCategory(categorySet[i][0], categorySet[i][1]);
        }
    }

        //JDialog to get Category information to add new Category
    void getCategoryInfo()
    {
        JDialog categoryInput = new JDialog();
        categoryInput.setTitle("Enter Category Details");
        categoryInput.setSize(300,220);
        int dX = ExpenseTracker.allocateCenterByWidth(categoryInput.getWidth());
        int dY = ExpenseTracker.allocateCenterByHeight(categoryInput.getHeight());
        categoryInput.setLocation(dX, dY);
        categoryInput.setLayout(null);

        JLabel eName = new JLabel("Name  : ");
        eName.setBounds(50,40,60,30);
        JTextField name = new JTextField("");
        name.setBounds(110,40,140,30);
        JRadioButton income = new JRadioButton("Income");
        income.setBounds(50,80,100,30);
        JRadioButton expense = new JRadioButton("Expense");
        expense.setBounds(150,80,100,30);
        ButtonGroup GP = new ButtonGroup();
        GP.add(income);
        GP.add(expense);
        JButton save = new JButton("Save");
        save.setBounds(50,120,100,30);
        JButton close  = new JButton("Close");
        close.setBounds(150,120,100,30);
        ActionListener SaveAL = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton bSource = (JButton)e.getSource();
                if(bSource == save)
                {
                    String Name = name.getText();
                    String type = "";
                    if(income.isSelected())
                    {
                        type = income.getText();
                    }
                    else if(expense.isSelected())
                    {
                        type = expense.getText();
                    }
                    SQLiteConnection.addCategory(Name,type);
                    addCategory(Name, type);
                }
                else if(bSource == close)
                {
                    categoryInput.dispose();
                }
            }
        };
        save.addActionListener(SaveAL);
        close.addActionListener(SaveAL);
        categoryInput.add(eName);
        categoryInput.add(name);
        categoryInput.add(income);
        categoryInput.add(expense);
        categoryInput.add(save);
        categoryInput.add(close);
        categoryInput.setModal(true);
        categoryInput.setVisible(true);
    }

    //Add Category to the jPCategory Panel (incomeCategory or expenseCategory)
    void addCategory(String Name,String type)
    {
        JPanel l = new JPanel();
        l.setLayout(new BorderLayout());
        l.setBackground(Color.decode("#FFFFFF"));
        l.setPreferredSize(new Dimension(10,50));
        l.setMaximumSize(new Dimension(1000,30));
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel labelName = new JLabel("  "+Name+"  ");
        labelName.setFont(new Font("Arial",Font.PLAIN,15));
        labelName.setOpaque(true);
        labelName.setPreferredSize(new Dimension(200,30));
        labelName.setBackground(Color.decode("#FFFFFF"));

        // Create context menu
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Category");
        JMenuItem deleteItem = new JMenuItem("Delete Category");
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        // Add mouse listener for right-click
        MouseAdapter mouseAdapter = new MouseAdapter() {
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
        };

        // Add mouse listeners to both panel and label
        l.addMouseListener(mouseAdapter);
        labelName.addMouseListener(mouseAdapter);

        // Add action listeners for menu items
        editItem.addActionListener(e -> {
            JDialog editDialog = new JDialog();
            editDialog.setTitle("Edit Category");
            editDialog.setSize(300, 220);
            int dX = ExpenseTracker.allocateCenterByWidth(editDialog.getWidth());
            int dY = ExpenseTracker.allocateCenterByHeight(editDialog.getHeight());
            editDialog.setLocation(dX, dY);
            editDialog.setLayout(null);

            JLabel eName = new JLabel("Name  : ");
            eName.setBounds(50, 40, 60, 30);
            JTextField name = new JTextField(Name);
            name.setBounds(110, 40, 140, 30);

            JRadioButton income = new JRadioButton("Income");
            income.setBounds(50, 80, 100, 30);
            JRadioButton expense = new JRadioButton("Expense");
            expense.setBounds(150, 80, 100, 30);
            
            // Set initial selection based on current type
            if (type.equals("Income")) {
                income.setSelected(true);
            } else {
                expense.setSelected(true);
            }

            ButtonGroup GP = new ButtonGroup();
            GP.add(income);
            GP.add(expense);

            JButton save = new JButton("Save");
            save.setBounds(50, 120, 100, 30);
            JButton close = new JButton("Close");
            close.setBounds(150, 120, 100, 30);

            ActionListener SaveClose = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton source = (JButton) e.getSource();
                    if (source == save) {
                        String newName = name.getText();
                        String newType = income.isSelected() ? "Income" : "Expense";
                        
                        // Update category in database
                        SQLiteConnection.updateCategory(Name, newName, newType);
                        
                        // Refresh the category panels
                        incomeCategory.removeAll();
                        expenseCategory.removeAll();
                        initCategory();
                    } else if (source == close) {
                        editDialog.dispose();
                    }
                }
            };

            save.addActionListener(SaveClose);
            close.addActionListener(SaveClose);

            editDialog.add(eName);
            editDialog.add(name);
            editDialog.add(income);
            editDialog.add(expense);
            editDialog.add(save);
            editDialog.add(close);
            editDialog.setModal(true);
            editDialog.setVisible(true);
        });

        deleteItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                jPCategory,
                "Are you sure you want to delete this category?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                SQLiteConnection.deleteCategory(Name);
                l.setVisible(false);
                jPCategory.revalidate();
                jPCategory.repaint();
            }
        });

        l.add(labelName, BorderLayout.CENTER);

        if(type.equals("Income"))
        {
            incomeCategory.add(l);
        }
        else if(type.equals("Expense"))
        {
            expenseCategory.add(l);
        }
        jPCategory.revalidate();
        jPCategory.repaint();
    }

    //return to Main JPanel i.e (ExpenseTracker)
    public JPanel getCategoryPanel()
    { 
        return jPCategory;
    }
}