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

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel cartTableModel;
	private JTable cartTable;

	private JComboBox<Integer> comboBoxItemId;
	private JComboBox<Integer> comboBoxItemQty;

	private JTextField textFieldTotalPrice;

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
		cartTableModel = new DefaultTableModel(new Object[] { "Item ID", "Name", "Quantity", "Unit Price" }, 0) {
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
		JScrollPane scrollPane = new JScrollPane(cartTable);

		// Create a title label for the table
		JLabel titleLabel = new JLabel("Shopping cart", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(titleLabel, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		
		// Create panels for each section
        JPanel panelItemSelection = createPanel("Item selection", 3);
        addItemSelectionComponents(panelItemSelection);

        JPanel panelCheckout = createPanel("Checkout", 2);
        addCheckoutComponents(panelCheckout, userId);

        JPanel panelOtherButtons = createPanel("Other actions", 2);
        addOtherActionComponents(panelOtherButtons);

        // Create a panel to hold the three previous sections vertically
        JPanel interactionPanel = new JPanel();
        interactionPanel.setLayout(new BoxLayout(interactionPanel, BoxLayout.Y_AXIS));
        interactionPanel.add(panelItemSelection);
        interactionPanel.add(Box.createVerticalStrut(10)); // Add a 10px vertical space
        interactionPanel.add(panelCheckout);
        interactionPanel.add(Box.createVerticalStrut(10)); // Add a 10px vertical space
        interactionPanel.add(panelOtherButtons);
        
        // Add the "Back to Home" button separately
        JPanel btnBackToHomePanel = new JPanel();
        JButton btnBackToHome = new JButton("Back to Home");
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
        contentPane.add(tablePanel, BorderLayout.WEST);
        contentPane.add(interactionPanel, BorderLayout.CENTER);
        contentPane.add(btnBackToHomePanel, BorderLayout.SOUTH);
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // 15px border on all sides
        
        setContentPane(contentPane);
        
		// Populate the Item Selection section's components
		populateComboBoxItemId();
		updateComboBoxItemQty();
	}

	private JPanel createPanel(String title, int rows) {
		JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
        		title, TitledBorder.LEADING, TitledBorder.TOP, 
        		new Font("Segoe UI", Font.ITALIC, 16)));
        panel.setLayout(new GridLayout(rows, 1, 0, 5)); // no horizontal gap, 5px vertical gap
        return panel;
	}
	
	private void addItemSelectionComponents(JPanel panel) {
		// Define the "Item ID" field to be selected and add it to the panel
		JPanel panelItemId = new JPanel(new GridLayout(1, 2));
		JLabel lblItemId = new JLabel("Item ID: ");
		lblItemId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelItemId.add(lblItemId);
		comboBoxItemId = new JComboBox<>();
		comboBoxItemId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboBoxItemId.addActionListener(e -> updateComboBoxItemQty());
		panelItemId.add(comboBoxItemId);
		panelItemId.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
		panel.add(panelItemId);  
		
		// Define the "Quantity" field to be selected and add it to the panel
		JPanel panelQuantity = new JPanel(new GridLayout(1, 2));
		JLabel lblQuantity = new JLabel("Quantity: ");
		lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelQuantity.add(lblQuantity);
		comboBoxItemQty = new JComboBox<>();
		comboBoxItemQty.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelQuantity.add(comboBoxItemQty);
		panelQuantity.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		panel.add(panelQuantity);
		
		// Define the "Add to Cart" button section and add it to the panel
		JPanel panelButton = new JPanel(new GridLayout(1, 1));
		JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setBackground(AppColors.ADD_BUTTON_COLOR);
        btnAddToCart.setToolTipText("Add the selected quantity of the chosen item to the shopping cart");
        btnAddToCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAddToCart.addActionListener(e -> addToCart());
		panelButton.add(btnAddToCart);
		panelButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        panel.add(panelButton);
	}
	
	private void addCheckoutComponents(JPanel panel, int userId) {
		// Define the "Total Price" field to be shown and add it to the panel
		JPanel panelTotalPrice = new JPanel(new GridLayout(1, 2));
		JLabel lblTotalPrice = new JLabel("Total Price: ");
		lblTotalPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelTotalPrice.add(lblTotalPrice);		
        textFieldTotalPrice = new JTextField(10);
        textFieldTotalPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textFieldTotalPrice.setEditable(false);
        panelTotalPrice.add(textFieldTotalPrice);
        panelTotalPrice.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
        panel.add(panelTotalPrice);
        
        // Define the "Checkout" button section and add it to the panel
        JPanel panelButton = new JPanel(new GridLayout(1, 1));
        JButton btnCheckout = new JButton("Checkout");
        btnCheckout.setBackground(AppColors.CHECKOUT_BUTTON_COLOR);
        btnCheckout.setToolTipText("Issue receipt and update purchased items' stock availability");
        btnCheckout.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnCheckout.addActionListener(e -> checkout(userId));
        panelButton.add(btnCheckout);
        panelButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        panel.add(panelButton);
	}
	
	private void addOtherActionComponents(JPanel panel) {
		// Define the "Clear Cart" button section and add it to the panel
		JPanel panelClear = new JPanel(new GridLayout(1, 1));
		JButton btnClearCart = new JButton("Clear Cart");
	    btnClearCart.setBackground(AppColors.ACTION_BUTTON_COLOR);
	    btnClearCart.setToolTipText("Reset the cart to restart the selection of a customer's desired items");
	    btnClearCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    btnClearCart.addActionListener(e -> clearCart());
	    panelClear.add(btnClearCart);
	    panelClear.setBorder(BorderFactory.createEmptyBorder(2, 5, 0, 5));
	    panel.add(panelClear);
	    
	    // Define the "Clear Cart" button section and add it to the panel
	 	JPanel panelPrint = new JPanel(new GridLayout(1, 1));
	    JButton btnPrintCart = new JButton("Print Cart");
	    btnPrintCart.setBackground(AppColors.ACTION_BUTTON_COLOR);
	    btnPrintCart.setToolTipText("Print a page (or a PDF document) representing the shopping cart");
	    btnPrintCart.setFont(new Font("Segoe UI", Font.BOLD, 16));
	    btnPrintCart.addActionListener(e -> printCart());
	    panelPrint.add(btnPrintCart);
	    panelPrint.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
	    panel.add(panelPrint);
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
			JOptionPane.showMessageDialog(null, "The item's current stock is limited to " + ex.getAvailableQty() + " pcs!",
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
		currencyFormat.setMinimumFractionDigits(2); // Ensure at least 2 decimal places
		currencyFormat.setMaximumFractionDigits(2); // Ensure at most 2 decimal places
		return currencyFormat.format(price);
	}

	/**
	 * Generates a receipt consisting of the products added to the cart. Their
	 * available stock quantities are updated accordingly.
	 * 
	 * @param userId the identifier of the user who issues the receipt
	 */
	private void checkout(int userId) {
		// Acquire the string representing the amount reached
		String stringTotalPrice = textFieldTotalPrice.getText();

		if (!stringTotalPrice.isBlank()) {
			try {
				// Remove the currency symbol and any whitespace
				String sanitizedPrice = stringTotalPrice.replaceAll("[^\\d,\\.]", "").trim();

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
				JOptionPane.showMessageDialog(null, "Invalid total price format!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!", "Empty cart",
					JOptionPane.WARNING_MESSAGE);
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
		// Acquire the string representing the amount reached
		String stringTotalPrice = textFieldTotalPrice.getText();
		
		if (!stringTotalPrice.isBlank()) {
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
		} else {
			JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!", "Empty cart",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Populates the JComboBox that allows you to select the ID of the product you
	 * want to choose.
	 */
	private void populateComboBoxItemId() {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer itemId : itemController.getAllItemIds()) {
			model.addElement(itemId);
		}
		comboBoxItemId.setModel(model);
	}

	/**
	 * Updates the selectable quantity of an item depending on which of them is
	 * selected.
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
