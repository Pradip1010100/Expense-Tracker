import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        jPBudget.setLayout(new BoxLayout(jPBudget, BoxLayout.Y_AXIS));

        // budgets.setLayout(new BoxLayout(budgets, BoxLayout.Y_AXIS));
        budgets.setLayout(new GridBagLayout());
        budgets.setAlignmentX(Component.CENTER_ALIGNMENT);
        // budgets.setBackground(Color.DARK_GRAY);

        JButton setBudget = new JButton("Set Budget");
        setBudget.setBackground(Color.decode("#76FF03"));
        setBudget.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        jPBudget.add(setBudget);
        jPBudget.add(scrollBudgets);
        initBudget();
    }

    void initBudget()
    {
        String[][] budgetSet = SQLiteConnection.getBudgets();
        for (int i = 0; i < budgetSet.length; i++) {
            addBudget(budgetSet[i][0],budgetSet[i][1],Double.parseDouble(budgetSet[i][2]),Double.parseDouble(budgetSet[i][3]));
        }
    }

void addBudget(String category,String type,double limit,double spent)
    {
        //Name ,Type ,Limit ,Spent
        JPanel b = new JPanel();
        b.setLayout(new GridBagLayout());
        b.setBackground(Color.LIGHT_GRAY);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel categoryName = new JLabel("Name : "+category);
        JLabel categoryType =  new JLabel("Type : "+type);
        JLabel categoryLimit = new JLabel("Limit : " +limit+"₹");
        JLabel categorySpent = new JLabel("Spent : "+spent+"₹");

        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.gridwidth = 1;
        // gbc.gridheight = 1;
        gbc.ipadx = 60;
        // gbc.ipady = 12;
        gbc.gridx = 0;
        gbc.gridy = 0;
        b.add(categoryName,gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        b.add(categoryType,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        b.add(categoryLimit,gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        b.add(categorySpent,gbc);
        gbc.ipadx = 0;
        gbc.ipady = 30;
        gbc.gridx = 0;
        gbc.gridy = YINC++;
        budgets.add(b,gbc);
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
        String[] categoryNames = new String[categorySet.length];
        for (int i = 0; i < categorySet.length; i++) {
            categoryNames[i] = categorySet[i][0]; 
        }
        JComboBox<String> selectC = new JComboBox<String>(categoryNames);
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
                    String type = (String)cInfo[1];
                    double spent = (double)cInfo[2];
                    SQLiteConnection.addBudget(id,limit);
                    addBudget(category,type,limit,spent);
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