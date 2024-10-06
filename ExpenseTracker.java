import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.Dimension;

//Action Listener Class for the JButttons to switch the JPanel 
class ExpenseTrackerListener implements ActionListener
{
    JButton jBRecord,jBAnalysis,jBBudget,jBCategory;
    public ExpenseTrackerListener(JButton jBRecord,JButton jBAnalysis,JButton jBBudget,JButton jBCategory)
    {
        this.jBRecord = jBRecord;
        this.jBAnalysis = jBAnalysis;
        this.jBBudget = jBBudget;
        this.jBCategory = jBCategory;
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton source = (JButton)e.getSource();
        
        jBRecord.setFocusPainted(false);
        jBRecord.setBorderPainted(false);
        jBAnalysis.setFocusPainted(false);
        jBAnalysis.setBorderPainted(false);
        jBBudget.setFocusPainted(false);

        jBBudget.setBorderPainted(false);
        jBCategory.setFocusPainted(false);
        jBCategory.setBorderPainted(false);
            
        source.setFocusPainted(true);
        source.setBorderPainted(true);
    }
}


public class ExpenseTracker
{
    JFrame jFExpenseTracker;
    JButton jBRecord,jBAnalysis,jBBudget,jBCategory;
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
        Record r = new Record();
        JPanel p = r.getRecordPanel();

        
        p.setPreferredSize(new Dimension(1910,968));
        jFExpenseTracker.add(p);
        jFExpenseTracker.setVisible(true);
        jFExpenseTracker.setBounds(0,0,500,500);
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
        ExpenseTrackerListener eTL = new ExpenseTrackerListener(jBRecord,jBAnalysis,jBBudget,jBCategory);
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
        ExpenseTracker ET = new ExpenseTracker();
    }
}