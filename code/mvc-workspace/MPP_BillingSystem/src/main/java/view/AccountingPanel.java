package view;

import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.*;
import org.jfree.data.category.DefaultCategoryDataset;

import controller.InvoiceController;
import controller.UserController;
import view.renderers.AccountingTableCellRenderer;

@SuppressWarnings("serial")
public class AccountingPanel extends JPanel {

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel invoiceTableModel;
	private JTable invoiceTable;
	private AccountingTableCellRenderer renderer;
	
	private ChartPanel chartPanel; // Container for the histogram created using JFreeChart

	private UserController userController;
	private InvoiceController invoiceController;

	public AccountingPanel(int userId, UserController userController, InvoiceController invoiceController) {
		this.userController = userController;
		this.invoiceController = invoiceController;
		initializeComponents();
		renderer = new AccountingTableCellRenderer(userId);
		invoiceTable.setDefaultRenderer(Object.class, renderer); // Apply the renderer to all columns
		setLayout(new BorderLayout());
		layoutComponents();
	}

	private void initializeComponents() {
		// Define the invoices table's model
		invoiceTableModel = new DefaultTableModel(new Object[] { "Date", "Operator ID", "Name", "Surname", "Role", "Amount" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		invoiceTable = new JTable(invoiceTableModel);
		invoiceTable.setFillsViewportHeight(true);
		invoiceTable.setRowHeight(20); // Set row height for vertical "centering"
		
		// Create a JScrollPane and add the table to it
		JScrollPane scrollPane = new JScrollPane(invoiceTable);

		// Create a title label for the table
		JLabel titleLabel = new JLabel("Transaction history", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(titleLabel, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Add all the invoices in the database to the corresponding table
		populateInvoiceTable();
	}

	private void populateInvoiceTable() {
		// Fetch data from the database using Hibernate
		List<Integer> invoiceIds = invoiceController.getAllInvoiceIds();

		// Clear any existing data in the table model
		invoiceTableModel.setRowCount(0);
		
		// Create a DateTimeFormatter for formatting the Instant
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneId.systemDefault());

		// Populate the table model with fetched items
		for (Integer invoiceId : invoiceIds) {
			Instant issueInstant = invoiceController.getInvoiceIssueInstantById(invoiceId);
	        String formattedDate = formatter.format(issueInstant); // Format the Instant
	        
			Object[] rowData = { 
					formattedDate, 
					invoiceController.getInvoiceOperatorById(invoiceId),
					userController.getUserFirstNameById(invoiceController.getInvoiceOperatorById(invoiceId)),
					userController.getUserLastNameById(invoiceController.getInvoiceOperatorById(invoiceId)),
					userController.getUserRoleById(invoiceController.getInvoiceOperatorById(invoiceId)),
					invoiceController.getInvoiceTotalPriceById(invoiceId)
			};
			invoiceTableModel.addRow(rowData);
		}
		
		// Create the histogram after populating the table
	    createHistogram();
	}
	
	private void createHistogram() {
	    // Create a dataset for the histogram
	    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	    // Fetch data from the database
	    for (Integer invoiceId : invoiceController.getAllInvoiceIds()) {
	        Instant issueInstant = invoiceController.getInvoiceIssueInstantById(invoiceId);
	        String date = DateTimeFormatter.ofPattern("dd-MM-yyyy").withZone(ZoneId.systemDefault()).format(issueInstant);
	        double amount = invoiceController.getInvoiceTotalPriceById(invoiceId);

	        // Add the amount to the dataset
	        dataset.addValue(amount, "Amount", date);
	    }

	    // Create the chart
	    JFreeChart chart = ChartFactory.createBarChart(
	            "Cumulative Profit per Day", // Chart title
	            "Date", // X-axis label
	            "Profit (€)", // Y-axis label
	            dataset // Dataset
	    );

	    // Create a ChartPanel and add it to the AccountingPanel
	    chartPanel = new ChartPanel(chart);
	}
	
	private void layoutComponents() {
		add(tablePanel, BorderLayout.CENTER);
		chartPanel.setPreferredSize(new Dimension(800, 400)); // Set preferred size
		add(chartPanel, BorderLayout.SOUTH); // Add the chart panel to the bottom of the AccountingPanel
		revalidate(); // Refresh the panel to show the new chart
		repaint(); // Repaint the panel
	}
}
