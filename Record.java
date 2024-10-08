import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class Record{
    JPanel jPRecord;
    JButton NewRecord;
    JTable records;
    JDialog recordInput;
    DefaultTableModel tableModel;

    public Record() {
        // Initialize JPanel and set its layout
        jPRecord = new JPanel();
        jPRecord.setLayout(new BoxLayout(jPRecord, BoxLayout.Y_AXIS));

        // Initialize JButton
        NewRecord = new JButton("Add New Record");
        ActionListener addNewRecord = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                recordInput = new JDialog();
                recordInput.setTitle("Enter Record Details");
                recordInput.setSize(300, 400);
                recordInput.setLocationRelativeTo(jPRecord);
                recordInput.add(newRecordPanel());
                recordInput.setModal(true);
                recordInput.setVisible(true);
            }
        };

        NewRecord.addActionListener(addNewRecord);

        // Create column headers for the table
        String[] header = {"Name", "Category", "Date", "Amount"};

        // Data for the table
        String[][] recordsString = {};

        // Initialize table model and set data
        tableModel = new DefaultTableModel(recordsString, header);

        // Initialize JTable with the table model
        records = new JTable(tableModel);

        // Add table to JScrollPane
        JScrollPane scrollTable = new JScrollPane(records);

        // Add components to JPanel
        jPRecord.add(NewRecord);
        jPRecord.add(scrollTable);
    }

    // Method to return the JPanel
    public JPanel getRecordPanel() {
        return jPRecord;
    }
    
    
    // JPanel to add New Record
    JPanel newRecordPanel()
    {
        JPanel newRecord = new JPanel();
        newRecord.setLayout(null);
        JTextField name = new JTextField("Name ");
        name.setBounds(50,50,100,30);

        JRadioButton income = new JRadioButton("Income");
        income.setBounds(50,100,100,30);

        JRadioButton expense = new JRadioButton("Expense");
        expense.setBounds(150,100,100,30);
        
        ButtonGroup GP = new ButtonGroup();
        GP.add(income);
        GP.add(expense);
        JTextField category = new JTextField("Category");
        category.setBounds(50,150,100,30);

        JTextField amount = new JTextField("Amount");
        amount.setBounds(50,200,100,30);

        JButton save = new JButton("Save");
        save.setBounds(50,250,100,30);

        JButton close = new JButton("Close");
        close.setBounds(150,250,100,30);

        ActionListener saveRecord = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton source = (JButton)e.getSource();
                if(source == save)
                {
                    String recordName = name.getText();
                    String recordCategory = category.getText();
                    String  recordAmount = amount.getText();
                    // New record to be added
                    String[] newRecordAdd = {recordName, recordCategory, "17.6.2024", recordAmount};
                    
                    // Add new row to the table model
                    tableModel.addRow(newRecordAdd);
                }
                else if(source == close)
                {
                    recordInput.dispose();
                }
                
                        
            }
        };
        save.addActionListener(saveRecord);
        close.addActionListener(saveRecord);
        
        
        
        //Add Elements to newRecord Panel
        newRecord.add(name);
        newRecord.add(income);
        newRecord.add(expense);
        newRecord.add(category);
        newRecord.add(amount);
        newRecord.add(save);
        newRecord.add(close);
        
        return newRecord;
    }
}