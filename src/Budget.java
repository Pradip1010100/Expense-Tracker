
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Budget {
    JPanel jPBudget;
    JPanel budgets;
    static int YINC = 0;
    public Budget()
    {
        jPBudget = new JPanel();
        budgets = new JPanel();
        jPBudget.setLayout(new BorderLayout());

        budgets.setLayout(new BoxLayout(budgets,BoxLayout.Y_AXIS));
        budgets.setAlignmentX(Component.CENTER_ALIGNMENT);
        budgets.setBackground(Color.decode("#ECFFE6"));

        JButton setBudget = new JButton("Set Budget");
        setBudget.setBackground(Color.decode("#00E676"));

        ActionListener sB = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                getBudgetInfo();
            }
        };
        setBudget.addActionListener(sB);

        JScrollPane scrollBudgets = new JScrollPane(budgets);
        scrollBudgets.setForeground(Color.CYAN);
        scrollBudgets.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollBudgets.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPBudget.add(setBudget,BorderLayout.PAGE_START);
        jPBudget.add(scrollBudgets,BorderLayout.CENTER);
        initBudget();
    }

    void initBudget()
    {
        String[][] budgetSet = SQLiteConnection.getBudgets();
        for (int i = 0; i < budgetSet.length; i++) {
            addBudget(budgetSet[i][0],Double.parseDouble(budgetSet[i][2]),Double.parseDouble(budgetSet[i][3]));
        }
    }

    void refreshBudgetPanel()
    {
        budgets.removeAll();
        initBudget();
    }

void addBudget(String category,double limit,double spent)
    {
        //Name ,Type ,Limit ,Spent
        JPanel b = new JPanel();
        b.setLayout(new BorderLayout());
        b.setPreferredSize(new Dimension(300,60));
        b.setMaximumSize(new Dimension(2000,60));
        JLabel categoryName = new JLabel("Name : "+category);
        JLabel categoryLimit = new JLabel("Limit : " +limit+"₹");
        JLabel categorySpent = new JLabel("Spent : "+spent+"₹");
        JButton edit = new JButton("...");
        edit.setBackground(Color.decode("#00E676"));
        edit.setPreferredSize(new Dimension(50,40));

        JPanel mainAddPanel = new JPanel();
        mainAddPanel.setLayout(new BorderLayout());
        mainAddPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mainAddPanel.setPreferredSize(new Dimension(350,60));
        mainAddPanel.setMaximumSize(new Dimension(2000,60));
        if(limit>spent){
            b.setBackground(Color.decode("#9DDE8B"));
            mainAddPanel.setBackground(Color.decode("#9DDE8B"));
        }
        else{
            b.setBackground(Color.decode("#F44336"));
            mainAddPanel.setBackground(Color.decode("#F44336")); 
        }
        b.add(categoryName,BorderLayout.PAGE_START);
        b.add(categoryLimit,BorderLayout.WEST);
        b.add(categorySpent,BorderLayout.EAST);
        
        
        mainAddPanel.add(b,BorderLayout.LINE_START);
        mainAddPanel.add(edit,BorderLayout.LINE_END);
        budgets.add(mainAddPanel);
        budgets.revalidate();
        budgets.repaint();
    }

    void getBudgetInfo()
    {
        JDialog info = new JDialog();
        info.setSize(260,220);
        int dX = ExpenseTracker.allocateCenterByWidth(info.getWidth());
        int dY = ExpenseTracker.allocateCenterByHeight(info.getHeight());
        info.setLocation(dX, dY);
        info.setLayout(null);
        info.setTitle("Enter Budget");

        String[][] categorySet = SQLiteConnection.getCategories();
        List<String> categoryNames = new ArrayList<String>();
        for (int i = 0; i < categorySet.length; i++) {
            if(categorySet[i][1].equals("Expense"))
            {
                categoryNames.add(categorySet[i][0]);
            }
        }
        JComboBox<String> selectC = new JComboBox<String>(categoryNames.toArray(new String[0]));
        selectC.setBounds(40, 40, 160, 30);
        JTextField budget = new JTextField();
        budget.setHorizontalAlignment(SwingConstants.RIGHT);
        budget.setBounds(40,80,160,30);
        JButton save = new JButton("Save");
        save.setBounds(40,120,80,30);
        JButton close = new JButton("Close");
        close.setBounds(120,120,80,30);
        ActionListener SaveClose = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JButton source = (JButton) e.getSource();
                if(source == save)
                {
                    double limit = Double.parseDouble(budget.getText());
                    String category = (String)selectC.getSelectedItem();
                    Object[] cInfo = SQLiteConnection.getCategoryIdTypeSpent(category);
                    int  id = (int)cInfo[0];
                    double spent = (double)cInfo[2];
                    SQLiteConnection.addBudget(id,limit);
                    addBudget(category,limit,spent);
                }
                else if(source == close)
                {
                    info.dispose();
                }
            }
        };
        save.addActionListener(SaveClose);
        close.addActionListener(SaveClose);

        info.add(selectC);
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
}