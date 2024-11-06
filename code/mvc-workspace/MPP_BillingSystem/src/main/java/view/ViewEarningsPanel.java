package view;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.InvoiceController;

@SuppressWarnings("serial")
public class ViewEarningsPanel extends JPanel {

	private ChartPanel chartPanel; // Container for the histogram created using JFreeChart
	private InvoiceController invoiceController;

	public ViewEarningsPanel(InvoiceController invoiceController) {
		this.invoiceController = invoiceController;
		displayProfitGraph();
		setLayout(new BorderLayout());
		layoutComponents();
	}

	private void displayProfitGraph() {
		Map<LocalDate, Double> dailyProfits = calculateDailyProfits();
		createProfitGraph(dailyProfits);
	}

	private Map<LocalDate, Double> calculateDailyProfits() {
		// Fetch data from the database
		List<Integer> invoiceIds = invoiceController.getAllInvoiceIds();
	    Map<LocalDate, Double> dailyProfits = new HashMap<>();

	    for (Integer invoiceId : invoiceIds) {
	        Instant issueInstant = invoiceController.getInvoiceIssueInstantById(invoiceId);
	        double totalPrice = invoiceController.getInvoiceTotalPriceById(invoiceId);
	        
	        // Convert Instant to LocalDate
	        LocalDate issueDate = LocalDate.ofInstant(issueInstant, ZoneId.systemDefault());

	        // Accumulate the total price for the corresponding date
	        dailyProfits.put(issueDate, dailyProfits.getOrDefault(issueDate, 0.0) + totalPrice);
	    }

	    return dailyProfits;
	}
	
	private void createProfitGraph(Map<LocalDate, Double> dailyProfits) {
		// Create dataset
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    
	    for (Map.Entry<LocalDate, Double> entry : dailyProfits.entrySet()) {
	        String formattedDate = entry.getKey().format(dateFormatter);
	        dataset.addValue(entry.getValue(), "Profit", formattedDate);
	    }

	    // Create chart
	    JFreeChart chart = ChartFactory.createBarChart(
	            null, // No title
	            "Date", // X-Axis Label
	            "Profit (€)", // Y-Axis Label
	            dataset // Dataset
	    );
	    
	    // Customize the chart
        customizeChart(chart);

	    // Create a ChartPanel that accommodates the graph
	    chartPanel = new ChartPanel(chart);
	    chartPanel.setBorder(BorderFactory.createEtchedBorder());
	}

	private void customizeChart(JFreeChart chart) {
		// Remove the legend
		chart.removeLegend();
		
		// Customize the plot
	    CategoryPlot plot = chart.getCategoryPlot();
	    plot.setBackgroundPaint(new Color(240, 240, 240)); // Light gray background
	    plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
	    plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
	    
	    // Customize the axis labels and the tick labels
	    Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
	    plot.getDomainAxis().setLabelFont(labelFont);
	    plot.getRangeAxis().setLabelFont(labelFont);
	    Font tickLabelFont = new Font("Segoe UI", Font.PLAIN, 12);
	    plot.getDomainAxis().setTickLabelFont(tickLabelFont);
	    plot.getRangeAxis().setTickLabelFont(tickLabelFont);
	    
	    // Customize the renderer
	    BarRenderer renderer = (BarRenderer) plot.getRenderer();
	    renderer.setSeriesPaint(0, new Color(0, 102, 204)); // Set bar color
	    renderer.setDrawBarOutline(true);
	    renderer.setSeriesOutlinePaint(0, Color.BLACK);
	    renderer.setSeriesOutlineStroke(0, new BasicStroke(1.0f));
	}
	
	private void layoutComponents() {
		add(chartPanel, BorderLayout.CENTER); // Add the chart panel to the center of the ViewEarningsPanel
		revalidate(); // Refresh the panel to show the new chart
		repaint(); // Repaint the panel
	}
}
