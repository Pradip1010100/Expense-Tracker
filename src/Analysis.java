import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class Analysis {
    private JPanel analysisPanel;
    private JPanel chartPanel;
    private JComboBox<String> chartTypeCombo;
    
    public Analysis() {
        analysisPanel = new JPanel(new BorderLayout());
        analysisPanel.setBackground(Color.decode("#28262f"));
        
        // Create top panel for controls
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.decode("#585f74"));
        
        // Create chart type selector
        String[] chartTypes = {"Expense by Category (Pie)", "Monthly Expenses (Line)", "Income vs Expense (Bar)"};
        chartTypeCombo = new JComboBox<>(chartTypes);
        chartTypeCombo.setPreferredSize(new Dimension(200, 30));
        chartTypeCombo.addActionListener(e -> updateChart());
        
        controlPanel.add(chartTypeCombo);
        
        // Create chart panel
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.decode("#28262f"));
        JPanel p = new JPanel();
        p.setLayout(null);
        chartPanel.setBounds(100,100,500,500);
        p.add(chartPanel);

        analysisPanel.add(controlPanel, BorderLayout.NORTH);
        analysisPanel.add(p, BorderLayout.CENTER);
        
        // Initial chart
        updateChart();
    }
    
    private void updateChart() {
        chartPanel.removeAll();
        String selectedChart = (String) chartTypeCombo.getSelectedItem();
        
        switch (selectedChart) {
            case "Expense by Category (Pie)":
                createPieChart();
                break;
            case "Monthly Expenses (Line)":
                createLineChart();
                break;
            case "Income vs Expense (Bar)":
                createBarChart();
                break;
        }
        
        chartPanel.revalidate();
        chartPanel.repaint();
    }
    
    private void createPieChart() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        // Get expense data from database
        String[][] records = SQLiteConnection.getRecords();
        Map<String, Double> categoryTotals = new HashMap<>();
        
        // Calculate totals for each category
        for (String[] record : records) {
            String category = record[1]; // category name
            double amount = Double.parseDouble(record[3]); // amount
            String type = record[4]; // type (expense/income)
            
            if (type.equalsIgnoreCase("Expense")) {
                categoryTotals.merge(category, amount, Double::sum);
            }
        }
        
        // Add data to dataset
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }
        
        JFreeChart chart = ChartFactory.createPieChart(
            "Expenses by Category",
            dataset,
            true, // legend
            true, // tooltips
            false // URLs
        );
        
        // Customize chart appearance
        chart.setBackgroundPaint(Color.decode("#28262f"));
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(Color.decode("#28262f"));
        chart.getLegend().setItemPaint(Color.WHITE);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.decode("#28262f"));
        this.chartPanel.add(chartPanel);
    }
    
    private void createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Get expense data from database
        String[][] records = SQLiteConnection.getRecords();
        Map<String, Double> monthlyTotals = new HashMap<>();
        
        // Calculate monthly totals
        for (String[] record : records) {
            String date = record[2]; // date
            double amount = Double.parseDouble(record[3]); // amount
            String type = record[4]; // type
            
            // Extract month and year from date
            String monthYear = date.split(" ")[1].substring(3); // Assuming date format "HH:mm:ss dd-MM-yyyy"
            
            if (type.equalsIgnoreCase("Expense")) {
                monthlyTotals.merge(monthYear, amount, Double::sum);
            }
        }
        
        // Add data to dataset
        for (Map.Entry<String, Double> entry : monthlyTotals.entrySet()) {
            dataset.addValue(entry.getValue(), "Expenses", entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createLineChart(
            "Monthly Expenses",
            "Month",
            "Amount",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Customize chart appearance
        customizeChartAppearance(chart);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.decode("#28262f"));
        this.chartPanel.add(chartPanel);
    }
    
    private void createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Get data from database
        String[][] records = SQLiteConnection.getRecords();
        Map<String, Double> monthlyIncome = new HashMap<>();
        Map<String, Double> monthlyExpense = new HashMap<>();
        
        // Calculate monthly totals
        for (String[] record : records) {
            String date = record[2]; // date
            double amount = Double.parseDouble(record[3]); // amount
            String type = record[4]; // type
            
            // Extract month and year from date
            String monthYear = date.split(" ")[1].substring(3);
            
            if (type.equalsIgnoreCase("Income")) {
                monthlyIncome.merge(monthYear, amount, Double::sum);
            } else {
                monthlyExpense.merge(monthYear, amount, Double::sum);
            }
        }
        
        // Add data to dataset
        for (Map.Entry<String, Double> entry : monthlyIncome.entrySet()) {
            dataset.addValue(entry.getValue(), "Income", entry.getKey());
            dataset.addValue(monthlyExpense.getOrDefault(entry.getKey(), 0.0), "Expense", entry.getKey());
        }
        
        JFreeChart chart = ChartFactory.createBarChart(
            "Income vs Expenses",
            "Month",
            "Amount",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // Customize chart appearance
        customizeChartAppearance(chart);
        
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.decode("#28262f"));
        this.chartPanel.add(chartPanel);
    }
    
    private void customizeChartAppearance(JFreeChart chart) {
        chart.setBackgroundPaint(Color.decode("#28262f"));
        chart.getTitle().setPaint(Color.WHITE);
        chart.getLegend().setBackgroundPaint(Color.decode("#28262f"));
        chart.getLegend().setItemPaint(Color.WHITE);
        
        chart.getCategoryPlot().setBackgroundPaint(Color.decode("#28262f"));
        chart.getCategoryPlot().setDomainGridlinePaint(Color.WHITE);
        chart.getCategoryPlot().setRangeGridlinePaint(Color.WHITE);
        chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
        chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
        chart.getCategoryPlot().getDomainAxis().setLabelPaint(Color.WHITE);
        chart.getCategoryPlot().getRangeAxis().setLabelPaint(Color.WHITE);
    }
    
    public JPanel getAnalysisPanel() {
        return analysisPanel;
    }
}
