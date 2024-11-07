import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Dimension;
import SQLiteConnection  .*;



public class ExpenseTracker
{
    JFrame jFExpenseTracker;
    JButton jBRecord,jBAnalysis,jBBudget,jBCategory;
    Record r;
    Analysis a;
    Budget b;
    Category c;
    JPanel p;
    static int width;
    static int height;


    //Default Constructor For Expense Tracker
    public ExpenseTracker()
    {
        jFExpenseTracker = new JFrame("Expense Tracker");
        jFExpenseTracker.setDefaultCloseOperation(jFExpenseTracker.EXIT_ON_CLOSE);
        jFExpenseTracker.setLayout(new BoxLayout(jFExpenseTracker.getContentPane(), BoxLayout.Y_AXIS));
        //jFExpenseTracker.setLayout(new BoxLayout(jFExpenseTracker, BoxLayout.Y_AXIS));
        jFExpenseTracker.getContentPane().add(getjPExpenseTracker());
        SQLiteConnection.initSQLite();
        r = new Record();
        a = new Analysis();
        b = new Budget();
        c = new Category();
        p = new JPanel();
        p.add(r.getRecordPanel());

        //Category c = new Category();
        //JPanel p = c.getCategoryPanel();
        p.setPreferredSize(new Dimension(1910,968));
        jFExpenseTracker.add(p);
        jFExpenseTracker.setVisible(true);
        jFExpenseTracker.setBounds(0,0,600,650);
    }

    

    public JPanel getjPExpenseTracker()
    {
        //Creating Objects 
        JPanel jPET = new JPanel();
        
        jBRecord = new JButton("Record");
        jBAnalysis = new JButton("Analysis");
        jBBudget = new JButton("Budget");
        jBCategory = new JButton("Category");
        
        //Add Action Listeners to the buttons to switch between JPanel's
        ActionListener eTL = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton source = (JButton)e.getSource();
                p.removeAll();
                if(source == jBRecord)
                {
                    p.add(r.getRecordPanel());
                }
                else if(source == jBAnalysis)
                {
                    p.add(a.getAnalysisPanel());
                }
                else if(source == jBBudget)
                {
                    p.add(b.getBudgetPanel());
                }
                else if(source == jBCategory)
                {
                    p.add(c.getCategoryPanel());
                }
                p.revalidate();
                p.repaint(); 
            }
        };
        jBRecord.addActionListener(eTL);
        jBAnalysis.addActionListener(eTL);
        jBBudget.addActionListener(eTL);
        jBCategory.addActionListener(eTL);
        
        //Set Layout to the Panel
        jPET.setLayout(new GridLayout(1,4));
        
        //Add Components to the Panel
        jPET.add(jBRecord);
        jPET.add(jBAnalysis);
        jPET.add(jBBudget);
        jPET.add(jBCategory);
        
        //Set size to the JPanel
        jPET.setPreferredSize(new Dimension(1910,40));

        //Return the JPanel
        return jPET;
        
    }


    public static void main(String[] args)
    {
        ExpenseTracker e = new ExpenseTracker();
    }
}