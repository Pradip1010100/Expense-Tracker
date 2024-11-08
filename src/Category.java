import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
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
        jPCategory.setLayout(new BoxLayout(jPCategory,BoxLayout.Y_AXIS));

        JPanel ieP = new JPanel();
        ieP.setLayout(new BoxLayout(ieP, BoxLayout.X_AXIS));
        ieP.setBackground(Color.LIGHT_GRAY);
        ieP.setMaximumSize(new Dimension(600,500));

        incomeCategory = new JPanel(){
            @Override
            public Dimension getMinimumSize(){
                return new Dimension(300,40);
            }
        };
        incomeCategory.setLayout(new BoxLayout(incomeCategory,BoxLayout.Y_AXIS));
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

        JButton newCategory = new JButton("New Category   "); 
        ActionListener newC = new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                getCategoryInfo();
            }
        };
        newCategory.addActionListener(newC);
        newCategory.setAlignmentX(Component.CENTER_ALIGNMENT);
        jPCategory.add(newCategory);
        ieP.setAlignmentX(Container.CENTER_ALIGNMENT);
        jPCategory.add(ieP);
    }

    void getCategoryInfo()
    {
        JDialog categoryInput = new JDialog();
        categoryInput.setTitle("Enter Category Details");
        categoryInput.setSize(300,220);
        ExpenseTracker.frameResetSize();        //set Location to the center
        int dX = ExpenseTracker.xAxis + (ExpenseTracker.width - categoryInput.getWidth())/2;
        int dY = ExpenseTracker.yAxis + (ExpenseTracker.height - categoryInput.getHeight())/2;
        categoryInput.setLocation(dX, dY);
        categoryInput.setLayout(null);

        JLabel eName = new JLabel("Name  : ");
        eName.setBounds(50,40,60,30);

        JTextField name = new JTextField("Name");
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
                    JLabel labelName = new JLabel("  "+Name+"  ");
                    labelName.setFont(new Font("Arial",Font.PLAIN,15));
                    labelName.setOpaque(true);
                    JButton edit = new JButton("...");
                    JPanel p = new JPanel();
                    p.setLayout(new BoxLayout(p,BoxLayout.X_AXIS));
                    p.add(labelName,JPanel.LEFT_ALIGNMENT);
                    p.add(edit,JPanel.RIGHT_ALIGNMENT);
                    p.setVisible(true);
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
        categoryInput.add(eName);
        categoryInput.add(name);
        categoryInput.add(income);
        categoryInput.add(expense);
        categoryInput.add(save);
        categoryInput.add(close);
        categoryInput.setModal(true);
        categoryInput.setVisible(true);
    }

    public JPanel getCategoryPanel()
    { 
        return jPCategory;
    }
}