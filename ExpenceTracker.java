
import java.awt.*;    
import javax.swing.*;    
    
public class ExpenceTracker  
{
    JFrame ETFrame;
    ExpenceTracker()  
    {  
        ETFrame =  new JFrame("Expence Tracker");
        ETFrame.setDefaultCloseOperation(ETFrame.EXIT_ON_CLOSE);
        ETFrame.add(addMenuBar());
        ETFrame.setVisible(true);
        ETFrame.pack();
    }
    JTabbedPane addMenuBar()
    {
        JTabbedPane tp=new JTabbedPane();
        
        JPanel record = new JPanel();
        Record r = new Record();
        record.add(r.recordPanel());
        JPanel analysis = new JPanel();
        JPanel budget = new JPanel();
        JPanel category = new JPanel();
        tp.add("Record",record);
        tp.add("Analysis",analysis);
        tp.add("Budjet",budget);
        tp.add("Category",category);
        return tp;
    }
    
    // main method  
    public static void main(String argvs[])   
    {    
        ExpenceTracker ETFrame = new ExpenceTracker();
    }
}    