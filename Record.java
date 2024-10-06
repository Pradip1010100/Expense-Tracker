import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Record {
    JPanel jPRecord;
    JButton addNewRecord;
    JTable records;
    DefaultTableModel tableModel;

    public Record() {
        // Initialize JPanel and set its layout
        jPRecord = new JPanel();
        jPRecord.setLayout(new BoxLayout(jPRecord, BoxLayout.Y_AXIS));

        // Initialize JButton
        addNewRecord = new JButton("Add New Record");

        // Create column headers for the table
        String[] header = {"Name", "Category", "Date", "Amount"};

        // Data for the table
        String[][] recordsString = {
            {"Phone Recharge", "Bills", "19.8.2024", "249"},
            {"Pizza", "Food", "18.8.2024", "200"},
            {"Dentist", "Health", "18.8.2024", "600"}
        };

        // Initialize table model and set data
        tableModel = new DefaultTableModel(recordsString, header);

        // Initialize JTable with the table model
        records = new JTable(tableModel);

        // Add table to JScrollPane
        JScrollPane scrollTable = new JScrollPane(records);

        // Add components to JPanel
        jPRecord.add(addNewRecord);
        jPRecord.add(scrollTable);
    }

    // Method to return the JPanel
    public JPanel getRecordPanel() {
        return jPRecord;
    }

    JPanel getRecordInfo()
    {
        JPanel recordInfo = new JPanel();
        JTextField recordName = new JTextField("Enter Record Name ");
        JRadioButton income = new JRadioButton("Income");
        JRadioButton expense = new JRadioButton("Expense");
        ButtonGroup incomeOrExpense = new ButtonGroup();
        incomeOrExpense.add(income);
        incomeOrExpense.add(expense);
        JTextField amount = new JTextField("Enter Amount");
        JButton save = new JButton("Save");
        JButton close = new JButton("Close");


        recordInfo.add(recordname);
        recordInfo.add(income);
        recordInfo.add(expense);
        recordInfo.add(amount);
        recordInfo.add(save);
        recordInfo.add(close);
        return recordInfo;
    }

    // Method to add a new record to the table
    public void addRecord() {
        // New record to be added
        String[] newRecord = {"Vadapav", "Food", "17.6.2024", "50"};
        
        // Add new row to the table model
        tableModel.addRow(newRecord);
    }
}