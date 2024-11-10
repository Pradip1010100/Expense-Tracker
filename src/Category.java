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
    static int icPanelWidth = 0;
    static int icPanelHeight = 0;
    static int iePanelWidth = 0;
    static int iePanelHeight = 0;
    public Category()
    {

        jPCategory = new JPanel();
        jPCategory.setLayout(new BoxLayout(jPCategory,BoxLayout.Y_AXIS));
        jPCategory.setBackground(Color.decode("#f0f0f0"));

        JPanel ieP = new JPanel();
        ieP.setLayout(new BoxLayout(ieP, BoxLayout.X_AXIS));
        ieP.setBackground(Color.LIGHT_GRAY);
        ieP.setMaximumSize(new Dimension(600,500));

        incomeCategory = new JPanel();
        // incomeCategory.setLayout(new BoxLayout(incomeCategory,BoxLayout.Y_AXIS));
        incomeCategory.setLayout(null);
        // incomeCategory.setMaximumSize(new Dimension(100,100));
        incomeCategory.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel incomeLabel = new JLabel("Income Categories");
        incomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        icPanelHeight = incomeLabel.getHeight();
        incomeLabel.setFont(new Font("Arial",Font.BOLD,14));
        incomeLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        incomeCategory.add(incomeLabel);

        JScrollPane scrollPaneI = new JScrollPane(incomeCategory);
        scrollPaneI.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneI.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        

        expenseCategory = new JPanel();
        // expenseCategory.setLayout(new BoxLayout(expenseCategory,BoxLayout.Y_AXIS));
        expenseCategory.setLayout(null);
        // expenseCategory.setMinimumSize(new Dimension(100,100));
        expenseCategory.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel expenseLabel = new JLabel("Expense Categories   ");
        expenseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        expenseLabel.setFont(new Font("Arial",Font.BOLD,14));
        expenseLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        iePanelHeight = expenseLabel.getHeight();
        expenseCategory.add(expenseLabel);

        JScrollPane scrollPaneE = new JScrollPane(expenseCategory);
        scrollPaneE.setForeground(Color.CYAN);
        scrollPaneE.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPaneE.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        ieP.add(scrollPaneI);
        ieP.add(scrollPaneE);

        JButton newCategory = new JButton("New Category   "); 
        newCategory.setBackground(Color.decode("#76FF03"));
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
        // JPanel l = new JPanel();
        // l.setLayout(new BoxLayout(l,BoxLayout.Y_AXIS));
        // l.setBackground(Color.CYAN);
        // JPanel b = new JPanel();
        // b.setLayout(new BoxLayout(b,BoxLayout.Y_AXIS));
        // b.setBackground(Color.DARK_GRAY);
        JLabel labelName = new JLabel("  "+Name+"  ");
        labelName.setFont(new Font("Arial",Font.PLAIN,15));
        labelName.setOpaque(true);
        JButton edit = new JButton("...");
        // l.add(labelName);  //,JPanel.LEFT_ALIGNMENT
        // b.add(edit);   //,JPanel.RIGHT_ALIGNMENT

        // JPanel lb = new JPanel();
        // lb.setLayout(new BoxLayout(lb,BoxLayout.X_AXIS));
        // lb.setBackground(Color.LIGHT_GRAY);
        // lb.add(l);
        // lb.add(b);
        if(type.equals("Income"))
        {
            // incomeCategory.add(lb);
            labelName.setBounds(0,icPanelHeight,100,30);
            edit.setBounds(200,icPanelHeight,40,30);
            icPanelHeight += 30;
            incomeCategory.add(labelName);
            incomeCategory.add(edit);
        }
        else if(type.equals("Expense"))
        {
            // expenseCategory.add(lb);
            labelName.setBounds(0,iePanelHeight,100,30);
            edit.setBounds(200,iePanelHeight,40,30);
            iePanelHeight += 30;
            expenseCategory.add(labelName);
            expenseCategory.add(edit);
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