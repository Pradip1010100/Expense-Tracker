import javax.swing.*;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
// import SQLiteConnection.*;
import java.awt.GridBagLayout;



public class ExpenseTracker
{
    static JFrame jFExpenseTracker;
    JButton jBRecord,jBAnalysis,jBBudget,jBCategory;
    Record r;
    Analysis a;
    Budget b;
    Category c;
    // static ExpenseTracker expenseTracker;
    static int xAxis;
    static int yAxis;
    static int width;
    static int height;


    //Default Constructor For Expense Tracker
    public ExpenseTracker()
    {
        jFExpenseTracker = new JFrame("Expense Tracker");
        jFExpenseTracker.setDefaultCloseOperation(3);    //jFExpenseTracker.EXIT_ON_CLOSE
        jFExpenseTracker.getContentPane().setLayout(new BorderLayout());
        //jFExpenseTracker.setLayout(new BoxLayout(jFExpenseTracker, BoxLayout.Y_AXIS));
        jFExpenseTracker.setBounds(0,0,600,650);
        
        jFExpenseTracker.getContentPane().add(getjPExpenseTracker(),BorderLayout.NORTH);
        SQLiteConnection.initSQLite();
        r = new Record();
        a = new Analysis();
        b = new Budget();
        c = new Category();
        jFExpenseTracker.add(r.getRecordPanel(),BorderLayout.CENTER);
        jFExpenseTracker.setVisible(true);
        jFExpenseTracker.getContentPane().setBackground(Color.decode("#BDBDBD"));
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
        return ExpenseTracker.xAxis + (ExpenseTracker.height - ContainerHeight)/2;
    }

    public JPanel getjPExpenseTracker()
    {
        //Creating Objects 
        JPanel jPET = new JPanel();
        //Set Layout to the Panel
        jPET.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.gridwidth = 1;
        //gbc.gridheight = 1;
        // gbc.ipadx = 100;
        gbc.ipady = 20;
        //Adding Components to the Panel
        jBRecord = new JButton("Record");
        jBRecord.setBackground(Color.decode("#29B6F6"));
        gbc.gridx = 0;
        gbc.gridy = 0;
        jPET.add(jBRecord,gbc);
        jBAnalysis = new JButton("Analysis");
        jBAnalysis.setBackground(Color.decode("#29B6F6"));
        gbc.gridx = 1;
        gbc.gridy = 0;
        jPET.add(jBAnalysis,gbc);
        jBBudget = new JButton("Budget");
        jBBudget.setBackground(Color.decode("#29B6F6"));
        gbc.gridx = 2;
        gbc.gridy = 0;
        jPET.add(jBBudget,gbc);
        jBCategory = new JButton("Category");
        jBCategory.setBackground(Color.decode("#29B6F6"));
        gbc.gridx = 3;
        gbc.gridy = 0;
        jPET.add(jBCategory,gbc);
        
        //Add Action Listeners to the buttons to switch between JPanel's
        ActionListener eTL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton source = (JButton)e.getSource();
                System.out.println(jFExpenseTracker.getContentPane().getComponentCount());
                jFExpenseTracker.getContentPane().remove(1);
                // jFExpenseTracker.getComponent(1);

                if(source == jBRecord)
                {
                    jFExpenseTracker.add(r.getRecordPanel(),BorderLayout.CENTER);
                }
                else if(source == jBAnalysis)
                {
                    jFExpenseTracker.add(a.getAnalysisPanel(),BorderLayout.CENTER);
                }
                else if(source == jBBudget)
                {
                    jFExpenseTracker.add(b.getBudgetPanel(),BorderLayout.CENTER);
                }
                else if(source == jBCategory)
                {
                    jFExpenseTracker.add(c.getCategoryPanel(),BorderLayout.CENTER);
                }
                jFExpenseTracker.revalidate();
                jFExpenseTracker.repaint(); 
            }
        };
        jBRecord.addActionListener(eTL);
        jBAnalysis.addActionListener(eTL);
        jBBudget.addActionListener(eTL);
        jBCategory.addActionListener(eTL);
        
        jPET.setBackground(Color.MAGENTA);
        //Return the JPanel
        return jPET;
        
    }


    public static void main(String[] args)
    {
        ExpenseTracker e = new ExpenseTracker();
        // expenseTracker = e;
    }
}
