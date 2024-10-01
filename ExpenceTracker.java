import java.awt.*;    
import javax.swing.*;    
    
public class ExpenceTracker  
{
    JFrame ETFrame;
    JMenuBar menuBar;
    JMenu Record,Analysis,Budjet,Category;
    JRootPane rootPane;
    ExpenceTracker()  
    {  
        ETFrame =  new JFrame("Expence Tracker");
        ETFrame.setDefaultCloseOperation(ETFrame.EXIT_ON_CLOSE);
        rootPane = ETFrame.getRootPane();
        menuBar = new JMenuBar();
        Record = new JMenu("Record");
        Analysis = new JMenu("Analysis");
        Budjet = new JMenu("Budjet");
        Category = new JMenu("Category");
        menuBar.setLayout(new GridLayout(1, 4));
        menuBar.add(Record);
        menuBar.add(Analysis);
        menuBar.add(Budjet);
        menuBar.add(Category);
        menuBar.setBorderPainted(true);
        ETFrame.add(menuBar);
        rootPane.setJMenuBar(menuBar);
        ETFrame.setJMenuBar(menuBar);
        ETFrame.setVisible(true);
        ETFrame.pack();
    }    
    
    // main method  
    public static void main(String argvs[])   
    {    
        ExpenceTracker ETFrame = new ExpenceTracker();
    }
}    