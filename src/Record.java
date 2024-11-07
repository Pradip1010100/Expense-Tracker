import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        JLabel enterName = new JLabel("Name : ");
        enterName.setBounds(50,50,60,30);
        JTextField name = new JTextField("");
        name.setBounds(120,50,100,30);

        JRadioButton income = new JRadioButton("Income");
        income.setBounds(50,100,100,30);

        JRadioButton expense = new JRadioButton("Expense");
        expense.setBounds(150,100,100,30);
        
        ButtonGroup GP = new ButtonGroup();
        GP.add(income);
        GP.add(expense);
        JLabel enterCategory = new JLabel("Category : ");
        enterCategory.setBounds(50,150,60,30);
        JTextField category = new JTextField("");
        category.setBounds(120,150,100,30);

        JLabel enterAmount = new JLabel("Amount : ");
        enterAmount.setBounds(50,200,60,30);
        JTextField amount = new JTextField("0");
        amount.setBounds(120,200,100,30);

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
                    String  ramount = amount.getText();
                    double recordAmount = Double.parseDouble(ramount); 

                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                    String recordDateTime = localDateTime.format(formatter);
                    int t =0;
                    String type ="";
                    if(income.isSelected())
                    {
                        type = income.getText();
                        t =1;
                    }
                    else if(expense.isSelected())
                    {
                        type = expense.getText();
                        t = 2;
                    }

                    // New record to be added
                    String[] newRecordAdd = {recordName, recordCategory,recordDateTime,ramount,type};
                    SQLiteConnection.addRecord(recordName, recordCategory, recordAmount, recordDateTime,t);
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
        newRecord.add(enterName);
        newRecord.add(name);
        newRecord.add(income);
        newRecord.add(expense);
        newRecord.add(enterCategory);
        newRecord.add(category);
        newRecord.add(enterAmount);
        newRecord.add(amount);
        newRecord.add(save);
        newRecord.add(close);
        
        return newRecord;
    }
}