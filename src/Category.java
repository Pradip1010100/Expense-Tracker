import javax.swing.*;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import SQLiteConnection.*;

public class Category {
    JPanel jPCategory;
    JPanel incomeCategory;
    JPanel expenseCategory;
    public Category()
    {
        jPCategory = new JPanel();
        jPCategory.setLayout(new BoxLayout(jPCategory,BoxLayout.Y_AXIS));
        // jPCategory.setPreferredSize(new Dimension(1000,1000)); 
        // jPCategory.setBackground(Color.DARK_GRAY);
        // jPCategory.setMaximumSize(new Dimension(600,500));
        // JPanel incomeExpense  = new JPanel();
        // incomeExpense.setLayout(new BoxLayout(incomeExpense,BoxLayout.X_AXIS));
        // incomeExpense.setBackground(Color.LIGHT_GRAY);
        JPanel ieP = new JPanel();
        ieP.setLayout(new BoxLayout(ieP, BoxLayout.X_AXIS));
        ieP.setBackground(Color.LIGHT_GRAY);
        ieP.setMaximumSize(new Dimension(600,500));
        
        

        incomeCategory = new JPanel(){
            @Override
            public Dimension getMinimumSize(){
                return new Dimension(200,40);
            }
        };
        incomeCategory.setLayout(new BoxLayout(incomeCategory,BoxLayout.Y_AXIS));
        // incomeCategory.setAlignmentX(Component.LEFT_ALIGNMENT);
        // incomeCategory.setAlignmentY(Component.TOP_ALIGNMENT);
        incomeCategory.setMinimumSize(new Dimension(0,100));
        incomeCategory.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel incomeLabel = new JLabel("Income Categories");
        incomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        incomeLabel.setFont(new Font("Arial",Font.BOLD,14));
        incomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        incomeCategory.add(incomeLabel);
        JScrollPane scrollPaneI = new JScrollPane(incomeCategory);
        scrollPaneI.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneI.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        

        expenseCategory = new JPanel(){
            @Override
            public Dimension getMinimumSize(){
                return new Dimension(200,40);
            }
        };
        expenseCategory.setLayout(new BoxLayout(expenseCategory,BoxLayout.Y_AXIS));
        // expenseCategory.setAlignmentX(Component.RIGHT_ALIGNMENT);
        //expenseCategory.setAlignmentY(Component.TOP_ALIGNMENT);
        expenseCategory.setMinimumSize(new Dimension(0,100));
        expenseCategory.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel expenseLabel = new JLabel("Expense Categories   ");
        expenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        expenseLabel.setFont(new Font("Arial",Font.BOLD,14));
        expenseLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        expenseCategory.add(expenseLabel);

        JScrollPane scrollPaneE = new JScrollPane(expenseCategory);
        scrollPaneE.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneE.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        ieP.add(scrollPaneI);
        ieP.add(scrollPaneE);
        // JScrollPane expenseScroll = new JScrollPane(expenseCategory);
        // incomeScroll.setSize(500,100);

        // incomeExpense.add(incomeScroll);
        // incomeExpense.add(expenseScroll);

        // incomeExpense.add(incomeCategory);
        // incomeExpense.add(expenseCategory);

        

        JButton newCategory = new JButton("New Category   "); 
        ActionListener newC = new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JDialog categoryInput = new JDialog();
                categoryInput.setTitle("Enter Category Details");
                categoryInput.setSize(300,400);
                categoryInput.setLayout(null);

                JTextField name = new JTextField("Name");
                name.setBounds(50,50,100,30);
                JRadioButton income = new JRadioButton("Income");
                income.setBounds(50,100,100,30);

                JRadioButton expense = new JRadioButton("Expense");
                expense.setBounds(150,100,100,30);
                
                ButtonGroup GP = new ButtonGroup();
                GP.add(income);
                GP.add(expense);
                JButton save = new JButton("Save");
                save.setBounds(50,150,100,30);
                JButton close  = new JButton("Close");
                close.setBounds(150,150,100,30);
                ActionListener SaveAL = new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        JButton bSource = (JButton)e.getSource();
                        if(bSource == save)
                        {
                            String Name = name.getText();
                            JLabel labelName = new JLabel("  "+Name+"  ");
                            labelName.setFont(new Font("Arial",Font.PLAIN,15));
                            labelName.setOpaque(true);
                            //labelName.setBackground(Color.getHSBColor(30,30, 3));
                            JButton edit = new JButton("...");
                            JPanel p = new JPanel();
                            p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
                            // p.setMinimumSize(new Dimension(100,30));
                            p.add(labelName);
                            p.add(edit);
                            p.setVisible(true);
                            //p.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            String type = "";
                            if(income.isSelected())
                            {
                                type = income.getText();
                                incomeCategory.add(p);
                            }
                            else if(expense.isSelected())
                            {
                                type = expense.getText();
                                expenseCategory.add(p);
                            }
                            jPCategory.revalidate();
                            jPCategory.repaint();
                            SQLiteConnection.addCategory(Name,type);
                        }
                        else if(bSource == close)
                        {
                            categoryInput.dispose();
                        }
                                        
                    }
                };
                save.addActionListener(SaveAL);
                close.addActionListener(SaveAL);


                categoryInput.add(name);
                categoryInput.add(income);
                categoryInput.add(expense);
                categoryInput.add(save);
                categoryInput.add(close);
                categoryInput.setModal(true);
                categoryInput.setVisible(true);
            }
        };
        newCategory.addActionListener(newC);
        newCategory.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPCategory.add(newCategory);
        ieP.setAlignmentX(Container.CENTER_ALIGNMENT);
        jPCategory.add(ieP);
        // jPCategory.add(newCategory,BorderLayout.PAGE_START);
        // jPCategory.add(incomeCategory,BorderLayout.EAST);
        // jPCategory.add(expenseCategory,BorderLayout.WEST);
        // jPCategory.add(incomeExpense);
    }
    
    public JPanel getCategoryPanel()
    { 
        return jPCategory;
    }
    
}