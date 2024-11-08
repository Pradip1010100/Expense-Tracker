import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Budget {
    JPanel jPBudget;
    JPanel budgets;
    public Budget()
    {
        jPBudget = new JPanel();
        budgets = new JPanel();
        JButton setBudget = new JButton("Set Budget");
        jPBudget.setLayout(new BoxLayout(jPBudget, BoxLayout.Y_AXIS));
        budgets.setLayout(new BoxLayout(budgets, BoxLayout.Y_AXIS));

        ActionListener sB = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                getBudgetInfo();
            }
        };
        setBudget.addActionListener(sB);

        jPBudget.add(setBudget);
        jPBudget.add(budgets);
    }

    void getBudgetInfo()
    {
        JDialog info = new JDialog();
        info.setSize(260,220);

        ExpenseTracker.frameResetSize();        //set Location to the center
        int dX = ExpenseTracker.xAxis + (ExpenseTracker.width - info.getWidth())/2;
        int dY = ExpenseTracker.yAxis + (ExpenseTracker.height - info.getHeight())/2;
        info.setLocation(dX, dY);
        info.setLayout(null);
        info.setTitle("Enter Budget");

        String[] categorySet = SQLiteConnection.getCategories();
        JComboBox<String> selectC = new JComboBox<String>(categorySet);
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
                    double amount = Double.parseDouble(budget.getText());
                    SQLiteConnection.addBudget(1,amount);
                    JPanel b = new JPanel();
                    b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));
                    JLabel budgetName = new JLabel((String)selectC.getSelectedItem());
                    JLabel budgetAmount = new JLabel("Budget: $" + budget.getText());
                    b.add(budgetName);
                    b.add(budgetAmount);
                    budgets.add(b);
                    budgets.revalidate();
                    budgets.repaint();
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