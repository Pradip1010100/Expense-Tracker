import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Record{
    JPanel jPRecord;
    JButton NewRecord;
    JTable records;
    JDialog recordInput;
    DefaultTableModel tableModel;

    public Record() {
        // Initialize JPanel and set its layout
        jPRecord = new JPanel();
        jPRecord.setLayout(new BorderLayout());
        jPRecord.setBackground(Color.decode("#BDBDBD"));
        // Initialize JButton
        NewRecord = new JButton("Add New Record");
        NewRecord.setBackground(Color.decode("#00E676"));
        ActionListener addNewRecord = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                recordInput = new JDialog();
                recordInput.setTitle("Enter Record Details");
                recordInput.setSize(300, 320);
                int dX = ExpenseTracker.allocateCenterByWidth(recordInput.getWidth());
                int dY = ExpenseTracker.allocateCenterByHeight(recordInput.getHeight());
                recordInput.setLocation(dX, dY);
                recordInput.add(newRecordPanel());
                recordInput.setModal(true);
                recordInput.setVisible(true);
            }
        };
        NewRecord.addActionListener(addNewRecord);
        // Create column headers for the table
        String[] header = {"Name", "Category", "Date", "Amount","Type"};
        // Data for the table
        String[][] recordsString = {};
        // Initialize table model and set data
        tableModel = new DefaultTableModel(recordsString, header){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes all cells non-editable
            }
        };
        // Initialize JTable with the table model
        records = new JTable(tableModel);
        records.getTableHeader().setBackground(Color.decode("#F96E2A"));
        // Add table to JScrollPane
        JScrollPane scrollTable = new JScrollPane(records);
        scrollTable.getViewport().setBackground(Color.decode("#EAD8B1"));
        // Add components to JPanel
        jPRecord.add(NewRecord,BorderLayout.PAGE_START);
        jPRecord.add(scrollTable,BorderLayout.CENTER);
        initRecord();
    }

    void initRecord()
    {
        String[][] resultSet = SQLiteConnection.getRecords();
        for (int i = 0; i < resultSet.length; i++) {
            addRecord(resultSet[i]);
        }
    }

    //Initialize the Records Panel 
    void addRecord(String[] newRecordInfo)
    {
        tableModel.addRow(newRecordInfo);
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
        JLabel enterName = new JLabel("Name : ");
        enterName.setBounds(50,50,60,30);
        JTextField name = new JTextField("");
        name.setBounds(120,50,100,30);

        JLabel enterCategory = new JLabel("Category : ");
        enterCategory.setBounds(50,100,60,30);
        String[][] categorySet = SQLiteConnection.getCategories();
        String[] categoryNames = new String[categorySet.length];
        for (int i = 0; i < categorySet.length; i++) {
            categoryNames[i] = categorySet[i][0];
        }
        JComboBox<String> selectC = new JComboBox<String>(categoryNames);
        // JTextField category = new JTextField("");
        selectC.setBounds(120,100,100,30);

        JLabel enterAmount = new JLabel("Amount : ");
        enterAmount.setBounds(50,150,60,30);
        JTextField amount = new JTextField("0");
        amount.setBounds(120,150,100,30);

        JButton save = new JButton("Save");
        save.setBounds(50,200,100,30);

        JButton close = new JButton("Close");
        close.setBounds(150,200,100,30);

        ActionListener saveRecord = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JButton source = (JButton)e.getSource();
                if(source == save)
                {
                    String recordName = name.getText();
                    String recordCategory = (String)selectC.getSelectedItem();
                    String type = "";
                    int t = -1;
                    for (int i = 0; i < categorySet.length; i++) {
                        if(categoryNames[i].equals(recordCategory))
                        {
                            type = categorySet[i][1];
                            t = i+1;
                        }
                    }

                    String  ramount = amount.getText();
                    double recordAmount = Double.parseDouble(ramount); 
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                    String recordDateTime = localDateTime.format(formatter);
                    // New record to be added
                    String[] newRecordInfo = {recordName, recordCategory,recordDateTime,ramount,type};
                    SQLiteConnection.addRecord(recordName, recordCategory, recordAmount, recordDateTime,t);
                    // Add new row to the table model
                    addRecord(newRecordInfo);
                    ExpenseTracker.b.refreshBudgetPanel();
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
        newRecord.add(enterName);
        newRecord.add(name);
        newRecord.add(enterCategory);
        newRecord.add(selectC);
        newRecord.add(enterAmount);
        newRecord.add(amount);
        newRecord.add(save);
        newRecord.add(close);
        
        return newRecord;
    }
}