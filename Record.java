
import java.awt.*;    
import javax.swing.*;    
    
public class Record  
{
    public JPanel recordPanel()  
    {  
        JPanel record =  new JPanel();
        record.setLocation(0,100);
        record.setLayout(new BoxLayout(record, BoxLayout.Y_AXIS));
        record.add(getRecordPanel());
        record.add(getRecordAddPanel());
        return record;
    }
    JPanel getRecordPanel()
    {
        JPanel recordPanel = new JPanel();
        JButton addRecordButton = new JButton("Add new Record");
        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
        recordPanel.add(addRecordButton);
        String data[][]={ {"Amit","Bills","19/8/2024","670"},    
                          {"Jai","Food","18.8.2024","78"},    
                          {"Sachin","Heath","18/8/2024","700"}};    
        String column[]={"Name","Category","Date","Amount"};         
        JTable recordsTable = new JTable(data,column);
        recordPanel.add(recordsTable);
        return recordPanel;
    }
    JPanel getRecordAddPanel()
    {
        JPanel addRecordPanel = new JPanel();
        addRecordPanel.setLayout(null);
        JTextField recordName = new JTextField("Enter Record Name ");
        JRadioButton incomeRB = new JRadioButton("Income");
        JRadioButton expenceRB = new JRadioButton("Expence");
        ButtonGroup incExp = new ButtonGroup();
        incExp.add(incomeRB);
        incExp.add(expenceRB);
        String categoryList[] = {"Bills","Food","Health","Clothing"};
        JComboBox category = new JComboBox(categoryList);
        JTextField amount = new JTextField("Enter Amount");
        JButton save = new JButton("Save");
        JButton close = new JButton("Close");

        //Setting Locations
        recordName.setBounds(100,0,100,40);
        incomeRB.setBounds(100,50,100,50);
        expenceRB.setBounds(200,50,100,50);
        category.setBounds(100,100,100,40);
        amount.setBounds(100,150,100,40);
        save.setBounds(100,250,100,40);
        close.setBounds(200,250,100,40);

        addRecordPanel.add(recordName);
        addRecordPanel.add(incomeRB);
        addRecordPanel.add(expenceRB);
        addRecordPanel.add(category);
        addRecordPanel.add(amount);
        addRecordPanel.add(save);
        addRecordPanel.add(close);
        return addRecordPanel;
    }
}    