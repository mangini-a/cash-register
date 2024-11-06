package view;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import controller.InvoiceController;
import controller.InvoiceControllerImpl;
import controller.ItemController;
import controller.ItemControllerImpl;
import controller.StockExceededException;
import controller.UserController;
import view.colors.AppColors;
import view.renderers.CartTableCellRenderer;

@SuppressWarnings("serial")
public class CashRegisterView extends JFrame {

	private JPanel contentPane;

	// Components used for the cart composition representation
	private DefaultTableModel cartTableModel;
	private JTable cartTable;
	private JScrollPane scrollPane;

	// Components used in the "Item Selection" section
	private JLabel lblItemId;
	private JComboBox<Integer> comboBoxItemId;
	private JLabel lblItemQty;
	private JComboBox<Integer> comboBoxItemQty;
	private JButton btnAddToCart;

	// Components used in the "Checkout" section
	private JLabel lblTotalPrice;
	private JTextField textFieldTotalPrice;
	private JButton btnCheckout;

	// Components used in the "Other Actions" section
	private JButton btnClearCart;
	private JButton btnPrintCart;

	// Components used in the bottom panel
	private JPanel btnBackToHomePanel;
	private JButton btnBackToHome;

	private ItemController itemController;
	private InvoiceController invoiceController;

	private Set<Integer> quantityModel;

	public CashRegisterView(UserController userController, int userId) {
		// Setup the frame
		setTitle("Cash Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initialize controllers
		itemController = ItemControllerImpl.getInstance();
		invoiceController = InvoiceControllerImpl.getInstance();

		// Define the cart composition table's model
		cartTableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Quantity", "Unit Price" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		cartTable = new JTable(cartTableModel);
		cartTable.setFillsViewportHeight(true);
		
		// Center the content of all columns
		for (int i = 0; i < cartTable.getColumnCount(); i++) {
			cartTable.getColumnModel().getColumn(i).setCellRenderer(new CartTableCellRenderer());
		}
		
		// Set row height for vertical "centering"
		cartTable.setRowHeight(20);

		scrollPane = new JScrollPane(cartTable);

		// Instantiate and populate the Item Selection section's components
		lblItemId = new JLabel("Item ID:");
		comboBoxItemId = new JComboBox<>();
		lblItemQty = new JLabel("Quantity:");
		comboBoxItemQty = new JComboBox<>();
		populateComboBoxItemId();
		updateComboBoxItemQty();
		comboBoxItemId.addActionListener(e -> updateComboBoxItemQty());
		btnAddToCart = new JButton("Add to Cart");
		btnAddToCart.setBackground(AppColors.CASH_REGISTER_COLOR);
		btnAddToCart.setForeground(Color.WHITE);
		btnAddToCart.setToolTipText("Add the selected quantity of the chosen item to the shopping cart");
		btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnAddToCart.addActionListener(e -> addToCart());

		// Create a panel for the Item Selection section
		JPanel panelItemSelection = new JPanel();
		panelItemSelection.setBorder(new TitledBorder("Item Selection"));
		panelItemSelection.setLayout(new FlowLayout());
		panelItemSelection.add(lblItemId);
		panelItemSelection.add(comboBoxItemId);
		panelItemSelection.add(lblItemQty);
		panelItemSelection.add(comboBoxItemQty);
		panelItemSelection.add(btnAddToCart);

		// Instantiate the Checkout section's components
		lblTotalPrice = new JLabel("Total price:");
		textFieldTotalPrice = new JTextField();
		textFieldTotalPrice.setEditable(false);
		btnCheckout = new JButton("Checkout");
		btnCheckout.setBackground(AppColors.CASH_REGISTER_COLOR);
		btnCheckout.setForeground(Color.WHITE);
		btnCheckout.setToolTipText("Issue receipt and update purchased items' stock availability");
		btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnCheckout.addActionListener(e -> checkout(userId));

		// Create a panel for the Checkout section
		JPanel panelCheckout = new JPanel();
		panelCheckout.setBorder(new TitledBorder("Checkout"));
		panelCheckout.setLayout(new FlowLayout());
		panelCheckout.add(lblTotalPrice);
		panelCheckout.add(textFieldTotalPrice);
		panelCheckout.add(btnCheckout);

		// Instantiate the Other Actions section's components
		btnClearCart = new JButton("Clear Cart");
		btnClearCart.setBackground(AppColors.CASH_REGISTER_COLOR);
		btnClearCart.setForeground(Color.WHITE);
		btnClearCart.setToolTipText("Reset the cart to restart the selection of a customer's desired items");
		btnClearCart.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnClearCart.addActionListener(e -> clearCart());
		btnPrintCart = new JButton("Print Cart");
		btnPrintCart.setBackground(AppColors.CASH_REGISTER_COLOR);
		btnPrintCart.setForeground(Color.WHITE);
		btnPrintCart.setToolTipText("Print a page (or a PDF document) representing the shopping cart");
		btnPrintCart.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnPrintCart.addActionListener(e -> printCart());

		// Create a panel for the Other Actions section
		JPanel panelOtherButtons = new JPanel();
		panelOtherButtons.setBorder(new TitledBorder("Other Actions"));
		panelOtherButtons.setLayout(new FlowLayout());
		panelOtherButtons.add(btnClearCart);
		panelOtherButtons.add(btnPrintCart);

		// Create a panel to hold the three previous sections vertically
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.add(panelItemSelection);
		centerPanel.add(Box.createVerticalStrut(10)); // Add a 10px vertical space
		centerPanel.add(panelCheckout);
		centerPanel.add(Box.createVerticalStrut(10)); // Add a 10px vertical space
		centerPanel.add(panelOtherButtons);

		// Add the "Back to Home" button separately
		btnBackToHomePanel = new JPanel();
		btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setToolTipText("Go back to the home page");
		btnBackToHome.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(userController, userId).display();
			});
		});
		btnBackToHomePanel.add(btnBackToHome);

		// Set up the content pane with the BorderLayout
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10); // Add a 10px horizontal gap between components
		layout.setVgap(10); // Add a 10px vertical gap between components

		contentPane = new JPanel(layout);
		contentPane.add(scrollPane, BorderLayout.WEST);
		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(btnBackToHomePanel, BorderLayout.SOUTH);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // 15px border on all sides

		setContentPane(contentPane);
	}

	/**
	 * Adds a product to the shopping cart, in the quantities selected by the user.
	 * Also keeps track of the total price due.
	 */
	private void addToCart() {
		// Acquire data selected by the user
		Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
		Integer selectedItemQty = (Integer) comboBoxItemQty.getSelectedItem();

		try {
			// Add a product line to the cart, both back-end and front-end
			invoiceController.addCartLine(selectedItemId, selectedItemQty);
			Object[] rowData = { selectedItemId, itemController.getItemNameById(selectedItemId), selectedItemQty,
					itemController.getItemUnitPriceById(selectedItemId) };
			cartTableModel.addRow(rowData);

			// Calculate the updated partial price
			double partialPrice = invoiceController.calculatePartial(selectedItemId, selectedItemQty);
			
			// Format the price in €
	        String formattedPrice = formatPrice(partialPrice);
			textFieldTotalPrice.setText(formattedPrice);
		} catch (StockExceededException ex) {
			JOptionPane.showMessageDialog(null, "The item's current stock is limited to " + ex.getItemQty() + " pcs!",
					"Required quantity exceeds quantity in stock", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Formats a price as a currency in €.
	 *
	 * @param price the price to be formatted
	 * @return a string representing the formatted price
	 */
    private String formatPrice(double price) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
        return currencyFormat.format(price);
    }

	/**
	 * Generates a receipt consisting of the products added to the cart.
	 * Their available stock quantities are updated accordingly.
	 * 
	 * @param userId the identifier of the user who issues the receipt
	 */
	private void checkout(int userId) {
		// Acquire the string representing the amount reached
		String stringTotalPrice = textFieldTotalPrice.getText();
		
		// Debug: Print the original string
	    System.out.println("Original total price string: " + stringTotalPrice);

		if (!stringTotalPrice.isBlank()) {
			try {
				// Remove the currency symbol and any whitespace
				String sanitizedPrice = stringTotalPrice.replaceAll("[^\\d,\\.]", "").trim();
				
				// Debug: Print the sanitized string
	            System.out.println("Sanitized total price string: " + sanitizedPrice);

	            // Replace comma with dot for decimal point
	            sanitizedPrice = sanitizedPrice.replace(",", ".");

	            // Parse the sanitized string to double
	            double totalPrice = Double.parseDouble(sanitizedPrice);
	            
	            invoiceController.addInvoice(userId, totalPrice); // Add a new invoice to the database
	            invoiceController.updateInventory(); // Update the stock by decreasing the sold quantities
	            clearCart(); // Clear the cart and reset the UI
	            JOptionPane.showMessageDialog(null, "Receipt generated and stock availability updated!", 
	            		"Checkout done", JOptionPane.INFORMATION_MESSAGE);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Invalid total price format!", 
	                    "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!", 
					"Empty cart", JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Clears the shopping cart, both logically and graphically.
	 */
	private void clearCart() {
		invoiceController.emptyCartLines();
		comboBoxItemId.setSelectedIndex(0);
		comboBoxItemQty.setSelectedIndex(0);
		textFieldTotalPrice.setText("");
		cartTableModel.setRowCount(0); // Clear the table
	}

	/**
	 * Prints a page (or a set of pages) representing the shopping cart table.
	 */
	private void printCart() {
		PrinterJob printerJob = PrinterJob.getPrinterJob();
		printerJob.setPrintable(cartTable.getPrintable(JTable.PrintMode.FIT_WIDTH, null, null));

		boolean doPrint = printerJob.printDialog();
		if (doPrint) {
			try {
				printerJob.print();
			} catch (PrinterException ex) {
				JOptionPane.showMessageDialog(null, "An error in the print system caused the job to be aborted.",
						"Printout failed", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Populates the JComboBox that allows you to select the ID of the product you want to choose.
	 */
	private void populateComboBoxItemId() {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer itemId : itemController.getAllItemIds()) {
			model.addElement(itemId);
		}
		comboBoxItemId.setModel(model);
	}

	/**
	 * Updates the selectable quantity of an item depending on which of them is selected.
	 */
	private void updateComboBoxItemQty() {
		try {
			Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
			quantityModel = itemController.showOneToQuantity(selectedItemId);
			comboBoxItemQty.setModel(new DefaultComboBoxModel<>(quantityModel.toArray(new Integer[0])));
		} catch (Exception e) {
			e.printStackTrace();
			comboBoxItemQty.setModel(new DefaultComboBoxModel<>(new Integer[0])); // Clear on error
		}
	}

	/**
	 * Displays the window in the center of the screen.
	 */
	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
