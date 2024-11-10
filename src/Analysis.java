import java.awt.Color;

import javax.swing.JPanel;

public class Analysis {
    JPanel jPAnalysis;
    public Analysis()
    {
        jPAnalysis = new JPanel();
        jPAnalysis.setBackground(Color.decode("#607D8B"));
    }
    public JPanel getAnalysisPanel()
    {
        return jPAnalysis;
    }
}
