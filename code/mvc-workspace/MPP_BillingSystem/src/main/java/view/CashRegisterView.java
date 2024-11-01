package view;

import java.awt.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.InvoiceController;
import controller.InvoiceControllerImpl;
import controller.ItemController;
import controller.ItemControllerImpl;
import controller.StockExceededException;
import model.User;

@SuppressWarnings("serial")
public class CashRegisterView extends JFrame {

	private JPanel contentPane;

	// Define color constants
	private static final Color BUTTON_COLOR = new Color(173, 216, 230); // Light blue

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

	public CashRegisterView(User user) {
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
			cartTable.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedTableCellRenderer());
		}

		// Set row height for vertical "centering"
		cartTable.setRowHeight(20);

		scrollPane = new JScrollPane(cartTable);

		// Instantiate and populate the Item Selection section's components
		lblItemId = new JLabel("Item ID:");
		comboBoxItemId = new JComboBox<>();
		lblItemQty = new JLabel("Quantity:");
		comboBoxItemQty = new JComboBox<>();
		populateComboBoxItemId(comboBoxItemId);
		updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty);
		comboBoxItemId.addActionListener(e -> updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty));
		btnAddToCart = new JButton("Add to Cart");
		btnAddToCart.setBackground(BUTTON_COLOR);
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
		btnCheckout.setBackground(BUTTON_COLOR);
		btnCheckout.addActionListener(e -> checkout(user));

		// Create a panel for the Checkout section
		JPanel panelCheckout = new JPanel();
		panelCheckout.setBorder(new TitledBorder("Checkout"));
		panelCheckout.setLayout(new FlowLayout());
		panelCheckout.add(lblTotalPrice);
		panelCheckout.add(textFieldTotalPrice);
		panelCheckout.add(btnCheckout);

		// Instantiate the Other Actions section's components
		btnClearCart = new JButton("Clear Cart");
		btnClearCart.setBackground(BUTTON_COLOR);
		btnClearCart.addActionListener(e -> clearCart());
		btnPrintCart = new JButton("Print Cart");
		btnPrintCart.setBackground(BUTTON_COLOR);
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
				new HomeView(user).display();
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

	public class CenterAlignedTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			setHorizontalAlignment(SwingConstants.CENTER); // Center align the text
			return cell;
		}
	}

	private void addToCart() {
		Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
		Integer selectedItemQty = (Integer) comboBoxItemQty.getSelectedItem();
		try {
			invoiceController.addCartLine(selectedItemId, selectedItemQty);
			Object[] rowData = { selectedItemId, invoiceController.getItemNameById(selectedItemId), selectedItemQty,
					invoiceController.getItemUnitPriceById(selectedItemId) };
			cartTableModel.addRow(rowData);
			String partialPrice = String.valueOf(invoiceController.calculatePartial(selectedItemId, selectedItemQty));
			textFieldTotalPrice.setText(partialPrice);
		} catch (StockExceededException ex) {
			JOptionPane.showMessageDialog(null, "The item's current stock is limited to " + ex.getItemQty() + " pcs!",
					"Required quantity exceeds quantity in stock", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void checkout(User user) {
		String strTot = textFieldTotalPrice.getText();
		if (!strTot.isBlank()) {
			Double totalPrice = Double.parseDouble(strTot);

			// Add a new invoice to the database
			invoiceController.addInvoice(user, totalPrice);

			// Update the stock by decreasing the sold quantities
			invoiceController.updateInventory();

			// Clear the cart and reset UI
			invoiceController.emptyCartLines();
			comboBoxItemId.setSelectedIndex(0);
			comboBoxItemQty.setSelectedIndex(0);
			textFieldTotalPrice.setText("");
			cartTableModel.setRowCount(0); // Clear the table
		} else {
			JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!", "Empty cart",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private void clearCart() {
		invoiceController.emptyCartLines();
		comboBoxItemId.setSelectedIndex(0);
		comboBoxItemQty.setSelectedIndex(0);
		textFieldTotalPrice.setText("");
		cartTableModel.setRowCount(0); // Clear the table
	}

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

	private void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer id : itemController.getAllItemIds()) {
			model.addElement(id);
		}
		comboBoxItemId.setModel(model);
	}

	private void updateComboBoxItemQty(JComboBox<Integer> comboBoxItemId, JComboBox<Integer> comboBoxItemQty) {
		try {
			Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
			if (selectedItemId != null) {
				quantityModel = itemController.showOneToQuantity(itemController.getItemById(selectedItemId));
				comboBoxItemQty.setModel(new DefaultComboBoxModel<>(quantityModel.toArray(new Integer[0])));
			} else {
				comboBoxItemQty.setModel(new DefaultComboBoxModel<>(new Integer[0])); // Clear if no selection
			}
		} catch (Exception e) {
			e.printStackTrace();
			comboBoxItemQty.setModel(new DefaultComboBoxModel<>(new Integer[0])); // Clear on error
		}
	}

	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
