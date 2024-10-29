import javax.swing.*;
import javax.swing.border.Border;

import java.awt.Color;
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
        
        JPanel incomeExpense  = new JPanel();
        incomeExpense.setLayout(new BoxLayout(incomeExpense,BoxLayout.X_AXIS));

        
        incomeCategory = new JPanel();
        incomeCategory.setLayout(new BoxLayout(incomeCategory,BoxLayout.Y_AXIS));
        incomeCategory.setSize(500,100);
        // JScrollPane incomeScroll = new JScrollPane(incomeCategory);
        // incomeScroll.setSize(500,100);

        expenseCategory = new JPanel();
        expenseCategory.setLayout(new BoxLayout(expenseCategory,BoxLayout.Y_AXIS));
        expenseCategory.setSize(500,100);
        // JScrollPane expenseScroll = new JScrollPane(expenseCategory);
        // incomeScroll.setSize(500,100);

        // incomeExpense.add(incomeScroll);
        // incomeExpense.add(expenseScroll);

        incomeExpense.add(incomeCategory);
        incomeExpense.add(expenseCategory);

        JButton newCategory = new JButton("New Category"); 
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
                            JLabel labelName = new JLabel(Name);
                            labelName.setBounds(0,0,100,40);
                            labelName.setFont(new Font("Arial",Font.PLAIN,15));
                            labelName.setHorizontalTextPosition(SwingConstants.CENTER);;
                            labelName.setOpaque(true);
                            labelName.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                            labelName.setBackground(Color.magenta);
                            String type = "";
                            if(income.isSelected())
                            {
                                type = income.getText();
                                incomeCategory.add(labelName);
                            }
                            else if(expense.isSelected())
                            {
                                type = expense.getText();
                                expenseCategory.add(labelName);
                            }
                            incomeExpense.revalidate();
                            incomeExpense.repaint();
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
        jPCategory.add(newCategory);
        jPCategory.add(incomeExpense);
    }
    
    public JPanel getCategoryPanel()
    {
        return jPCategory;
    }
    
}