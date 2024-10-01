import java.awt.*;    
import javax.swing.*;    
    
public class Record  
{
    JFrame RFrame;
    JPanel recordPanel;
    JButton addRecordButton;
    JTable recordsTable;
    
    Record()  
    {  
        RFrame =  new JFrame("Expence Tracker");
        RFrame.setDefaultCloseOperation(RFrame.EXIT_ON_CLOSE);
        RFrame.setLayout(new BoxLayout(RFrame.getContentPane(), BoxLayout.Y_AXIS)); 
        RFrame.setVisible(true);
        RFrame.pack();
        RFrame.add(getRecordPanel());
        RFrame.add(getRecordAddPanel());
    }
    JPanel getRecordPanel()
    {
        recordPanel = new JPanel();
        addRecordButton = new JButton("Add new Record");
        recordPanel.setLayout(new BoxLayout(recordPanel, BoxLayout.Y_AXIS));
        recordPanel.add(addRecordButton);
        String data[][]={ {"Amit","Bills","19/8/2024","670"},    
                          {"Jai","Food","18.8.2024","78"},    
                          {"Sachin","Heath","18/8/2024","700"}};    
        String column[]={"Name","Category","Date","Amount"};         
        recordsTable = new JTable(data,column);
        recordPanel.add(recordsTable);
        return recordPanel;
    }
    JPanel getRecordAddPanel()
    {
        JPanel addRecordPanel = new JPanel();
        addRecordPanel.setLayout(new BoxLayout(addRecordPanel, BoxLayout.Y_AXIS));
        JTextField recordName = new JTextField("Enter Record Name ");
        JRadioButton incomeRB = new JRadioButton("Income");
        JRadioButton expenceRB = new JRadioButton("Expence");
        ButtonGroup incExp = new ButtonGroup();
        incExp.add(incomeRB);
        incExp.add(expenceRB);
        String categoryList[] = {"Bills","Food","Health","Clothing"};
        JComboBox Category = new JComboBox(categoryList);
        JTextField amount = new JTextField("Enter Amount");
        JButton save = new JButton("Save");
        JButton close = new JButton("Close");
        addRecordPanel.add(recordName);
        addRecordPanel.add(incomeRB);
        addRecordPanel.add(expenceRB);
        addRecordPanel.add(Category);
        addRecordPanel.add(amount);
        addRecordPanel.add(save);
        addRecordPanel.add(close);
        return addRecordPanel;
    }
    
    // main method  
    public static void main(String argvs[])   
    {    
        Record RFrame = new Record();
    }
}    