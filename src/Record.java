import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


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
        jPRecord.setBackground(Color.decode("#173B45"));
        // Initialize JButton
        NewRecord = new JButton("Add New Record");
        NewRecord.setPreferredSize(new Dimension(1, 50));
        NewRecord.setBackground(Color.decode("#FFFFFF"));
        
        ActionListener addNewRecord = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                recordInput = new JDialog();
                recordInput.setBackground(Color.decode("#000000"));
                recordInput.setTitle("Enter Record Details");
                
                recordInput.setSize(400, 320);
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
        records.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16)); // Change "Arial" and size as needed

        records.getTableHeader().setBackground(Color.decode("#5b72ee"));
        records.getTableHeader().setForeground(Color.decode("#FFFFFF"));
        records.getTableHeader().setPreferredSize(new Dimension(records.getWidth(), 40)); // Set header height to 40
        records.setFont(new Font("Arial", Font.PLAIN, 14)); // Change "Arial" and size as needed
        
        // Create context menu for right-click operations
        JPopupMenu contextMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Edit Record");
        JMenuItem deleteItem = new JMenuItem("Delete Record");
        
        contextMenu.add(editItem);
        contextMenu.add(deleteItem);

        // Add mouse listener for right-click
        records.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = records.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        records.setRowSelectionInterval(row, row);
                        contextMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = records.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        records.setRowSelectionInterval(row, row);
                        contextMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });

        // Add action listeners for menu items
        editItem.addActionListener(e -> {
            int selectedRow = records.getSelectedRow();
            if (selectedRow >= 0) {
                String[] recordData = new String[5];
                for (int i = 0; i < 5; i++) {
                    recordData[i] = (String) records.getValueAt(selectedRow, i);
                }
                showEditDialog(recordData, selectedRow);
            }
        });

        deleteItem.addActionListener(e -> {
            int selectedRow = records.getSelectedRow();
            if (selectedRow >= 0) {
                String recordName = (String) records.getValueAt(selectedRow, 0);
                String recordCategory = (String) records.getValueAt(selectedRow, 1);
                String recordDate = (String) records.getValueAt(selectedRow, 2);
                
                int confirm = JOptionPane.showConfirmDialog(
                    jPRecord,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    SQLiteConnection.deleteRecord(recordName, recordCategory, recordDate);
                    tableModel.removeRow(selectedRow);
                    ExpenseTracker.b.refreshBudgetPanel();
                    ExpenseTracker.refreshSummary();
                }
            }
        });

        // Create a renderer that centers text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Apply the renderer to all columns
        for (int i = 0; i < records.getColumnCount(); i++) {
            records.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        records.setRowHeight(50);
          // Ensures no spacing between cells
         // Set font to Arial, size 16

        
         // Change font size to 18px

        // Add table to JScrollPane
        JScrollPane scrollTable = new JScrollPane(records);
        scrollTable.getViewport().setBackground(Color.decode("#28262f"));
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
        ExpenseTracker.refreshSummary();
    }
    
    // Method to return the JPanel
    public JPanel getRecordPanel() {
        return jPRecord;
    }
    
    // Method to filter categories based on type
    private String[] filterCategoriesByType(String[][] categorySet, String type) {
        int count = 0;
        // First count matching categories
        for (int i = 0; i < categorySet.length; i++) {
            if (categorySet[i][1].equals(type)) {
                count++;
            }
        }
        
        // Create array of matching categories
        String[] filteredCategories = new String[count];
        int index = 0;
        for (int i = 0; i < categorySet.length; i++) {
            if (categorySet[i][1].equals(type)) {
                filteredCategories[index++] = categorySet[i][0];
            }
        }
        
        return filteredCategories;
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

        JLabel enterType = new JLabel("Type : ");
        enterType.setBounds(50,100,60,30);
        String[] types = {"Income", "Expense"};
        JComboBox<String> selectType = new JComboBox<String>(types);
        selectType.setBounds(120,100,100,30);

        JLabel enterCategory = new JLabel("Category : ");
        enterCategory.setBounds(50,150,60,30);
        String[][] categorySet = SQLiteConnection.getCategories();
        JComboBox<String> selectC = new JComboBox<String>();
        selectC.setBounds(120,150,100,30);

        // Add listener to update categories based on type selection
        selectType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String)selectType.getSelectedItem();
                selectC.removeAllItems();
                
                String[] filteredCategories = filterCategoriesByType(categorySet, selectedType);
                if (filteredCategories.length == 0) {
                    selectC.addItem("No categories available");
                } else {
                    for (String category : filteredCategories) {
                        selectC.addItem(category);
                    }
                }
            }
        });

        // Trigger initial category update
        selectType.setSelectedIndex(0);

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
                    String recordCategory = (String)selectC.getSelectedItem();
                    String recordType = (String)selectType.getSelectedItem();
                    
                    // Don't proceed if no valid category is selected
                    if (recordCategory.equals("No categories available")) {
                        JOptionPane.showMessageDialog(recordInput, 
                            "Please add a category for " + recordType + " first", 
                            "No Categories Available", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int t = -1;
                    for (int i = 0; i < categorySet.length; i++) {
                        if(categorySet[i][0].equals(recordCategory))
                        {
                            t = i+1;
                        }
                    }

                    String  ramount = amount.getText();
                    double recordAmount = Double.parseDouble(ramount); 
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
                    String recordDateTime = localDateTime.format(formatter);
                    // New record to be added
                    String[] newRecordInfo = {recordName, recordCategory,recordDateTime,ramount,recordType};
                    SQLiteConnection.addRecord(recordName, recordType, recordAmount, recordDateTime,t);
                    // Add new row to the table model
                    addRecord(newRecordInfo);
                    ExpenseTracker.b.refreshBudgetPanel();
                    ExpenseTracker.refreshSummary();
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
        newRecord.add(enterType);
        newRecord.add(selectType);
        newRecord.add(enterCategory);
        newRecord.add(selectC);
        newRecord.add(enterAmount);
        newRecord.add(amount);
        newRecord.add(save);
        newRecord.add(close);
        
        return newRecord;
    }

    // Method to show edit dialog
    private void showEditDialog(String[] recordData, int row) {
        JDialog editDialog = new JDialog();
        editDialog.setTitle("Edit Record");
        editDialog.setSize(400, 320);
        int dX = ExpenseTracker.allocateCenterByWidth(editDialog.getWidth());
        int dY = ExpenseTracker.allocateCenterByHeight(editDialog.getHeight());
        editDialog.setLocation(dX, dY);

        JPanel editPanel = new JPanel();
        editPanel.setLayout(null);

        JLabel enterName = new JLabel("Name : ");
        enterName.setBounds(50,50,60,30);
        JTextField name = new JTextField(recordData[0]);
        name.setBounds(120,50,100,30);

        JLabel enterType = new JLabel("Type : ");
        enterType.setBounds(50,100,60,30);
        String[] types = {"Income", "Expense"};
        JComboBox<String> selectType = new JComboBox<String>(types);
        selectType.setBounds(120,100,100,30);
        selectType.setSelectedItem(recordData[4]);

        JLabel enterCategory = new JLabel("Category : ");
        enterCategory.setBounds(50,150,60,30);
        String[][] categorySet = SQLiteConnection.getCategories();
        JComboBox<String> selectC = new JComboBox<String>();
        selectC.setBounds(120,150,100,30);

        // Add listener to update categories based on type selection
        selectType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedType = (String)selectType.getSelectedItem();
                selectC.removeAllItems();
                
                String[] filteredCategories = filterCategoriesByType(categorySet, selectedType);
                if (filteredCategories.length == 0) {
                    selectC.addItem("No categories available");
                } else {
                    for (String category : filteredCategories) {
                        selectC.addItem(category);
                    }
                }
            }
        });

        // Trigger initial category update
        selectType.setSelectedIndex(0);
        selectC.setSelectedItem(recordData[1]);

        JLabel enterAmount = new JLabel("Amount : ");
        enterAmount.setBounds(50,200,60,30);
        JTextField amount = new JTextField(recordData[3]);
        amount.setBounds(120,200,100,30);

        JButton save = new JButton("Save");
        save.setBounds(50,250,100,30);

        JButton cancel = new JButton("Cancel");
        cancel.setBounds(150,250,100,30);

        ActionListener saveEdit = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                if(source == save) {
                    String recordName = name.getText();
                    String recordCategory = (String)selectC.getSelectedItem();
                    String recordType = (String)selectType.getSelectedItem();
                    
                    if (recordCategory.equals("No categories available")) {
                        JOptionPane.showMessageDialog(editDialog, 
                            "Please add a category for " + recordType + " first", 
                            "No Categories Available", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    int t = -1;
                    for (int i = 0; i < categorySet.length; i++) {
                        if(categorySet[i][0].equals(recordCategory)) {
                            t = i+1;
                        }
                    }

                    String ramount = amount.getText();
                    double recordAmount = Double.parseDouble(ramount);
                    
                    // Update the record in database
                    SQLiteConnection.updateRecord(recordData[0], recordData[1], recordData[2],
                                               recordName, recordCategory, recordAmount);
                    
                    // Update the table
                    tableModel.setValueAt(recordName, row, 0);
                    tableModel.setValueAt(recordCategory, row, 1);
                    tableModel.setValueAt(recordData[2], row, 2); // Keep original date
                    tableModel.setValueAt(ramount, row, 3);
                    tableModel.setValueAt(recordType, row, 4);
                    
                    ExpenseTracker.b.refreshBudgetPanel();
                    ExpenseTracker.refreshSummary();
                    editDialog.dispose();
                } else if(source == cancel) {
                    editDialog.dispose();
                }
            }
        };
        
        save.addActionListener(saveEdit);
        cancel.addActionListener(saveEdit);

        editPanel.add(enterName);
        editPanel.add(name);
        editPanel.add(enterType);
        editPanel.add(selectType);
        editPanel.add(enterCategory);
        editPanel.add(selectC);
        editPanel.add(enterAmount);
        editPanel.add(amount);
        editPanel.add(save);
        editPanel.add(cancel);

        editDialog.add(editPanel);
        editDialog.setModal(true);
        editDialog.setVisible(true);
    }
}