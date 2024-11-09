package view;

import java.awt.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.InvoiceController;
import controller.UserController;
import view.renderers.AccountingTableCellRenderer;

@SuppressWarnings("serial")
public class ViewTransactionsPanel extends JPanel {

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel invoiceTableModel;
	private JTable invoiceTable;
	private AccountingTableCellRenderer renderer;

	private UserController userController;
	private InvoiceController invoiceController;

	public ViewTransactionsPanel(int userId, UserController userController, InvoiceController invoiceController) {
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
		invoiceTableModel = new DefaultTableModel(
				new Object[] { "Date", "Operator ID", "Name", "Surname", "Role", "Amount" }, 0) {
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

	void populateInvoiceTable() {
		// Fetch data from the database using Hibernate
		List<Integer> invoiceIds = invoiceController.getAllInvoiceIds();

		// Clear any existing data in the table model
		invoiceTableModel.setRowCount(0);
		
		// Create a list to hold the rows
	    List<Object[]> rows = new ArrayList<>();

		// Populate the list with fetched items
		for (Integer invoiceId : invoiceIds) {
			Instant issueInstant = invoiceController.getInvoiceIssueInstantById(invoiceId);

			Object[] rowData = { issueInstant, // Store the Instant for sorting
					invoiceController.getInvoiceOperatorById(invoiceId),
					userController.getUserFirstNameById(invoiceController.getInvoiceOperatorById(invoiceId)),
					userController.getUserLastNameById(invoiceController.getInvoiceOperatorById(invoiceId)),
					userController.getUserRoleById(invoiceController.getInvoiceOperatorById(invoiceId)),
					invoiceController.getInvoiceTotalPriceById(invoiceId) };
			rows.add(rowData);
		}
		
		// Sort the rows based on the date (first element in each row)
	    rows.sort((a, b) -> ((Instant) a[0]).compareTo((Instant) b[0]));

	    // Add sorted rows to the table model
	    for (Object[] row : rows) {
	        invoiceTableModel.addRow(row);
	    }
	}

	private void layoutComponents() {
		add(tablePanel, BorderLayout.CENTER);
	}
}
