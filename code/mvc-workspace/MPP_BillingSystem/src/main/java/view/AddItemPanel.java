package view;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemControllerImpl;
import model.Item;
import model.ItemCategory;

@SuppressWarnings("serial")
public class AddItemPanel extends JPanel {

	private static final Color ADD_BUTTON_COLOR = new Color(144, 238, 144); // Light green

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel itemTableModel;

	private JPanel formPanel; // Container for the form

	private JTextField fieldName;
	private JFormattedTextField fieldQuantity;
	private JFormattedTextField fieldUnitPrice;
	private JComboBox<ItemCategory> comboBoxCategory;

	private ItemController itemController;
	
	private PanelChangeListener listener;

	public AddItemPanel(PanelChangeListener listener) {
		this.listener = listener;
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10); // Add a 10px horizontal gap between components
		setLayout(layout);
		layoutComponents();
	}

	private void initializeComponents() {
		// Define the existing items table's model
		itemTableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Quantity", "Unit Price", "Category" }, 0);
		JTable itemTable = new JTable(itemTableModel);
		itemTable.setFillsViewportHeight(true);

		// Center the content of all columns
		for (int i = 0; i < itemTable.getColumnCount(); i++) {
			itemTable.getColumnModel().getColumn(i).setCellRenderer(new CenterAlignedTableCellRenderer());
		}

		itemTable.setRowHeight(20); // Set row height for vertical "centering"
		JScrollPane scrollPane = new JScrollPane(itemTable);
		
		// Create a title label for the table
        JLabel titleLabel = new JLabel("Current Stock", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding
		
		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Define the "Name" field to be filled in
		JPanel panelName = new JPanel(new GridLayout(2, 1));
		JLabel lblName = new JLabel("Name *");
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelName.add(lblName);
		fieldName = new JTextField();
		fieldName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelName.add(fieldName);

		// Define the "Quantity" field to be filled in
		JPanel panelQuantity = new JPanel(new GridLayout(2, 1));
		JLabel lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelQuantity.add(lblQuantity);
		fieldQuantity = createQuantityFormattedTextField();
		fieldQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelQuantity.add(fieldQuantity);

		// Define the "Unit Price" field to be filled in
		JPanel panelUnitPrice = new JPanel(new GridLayout(2, 1));
		JLabel lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelUnitPrice.add(lblUnitPrice);
		fieldUnitPrice = createUnitPriceFormattedTextField();
		fieldUnitPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelUnitPrice.add(fieldUnitPrice);

		// Define the "Category" field to be selected
		JPanel panelCategory = new JPanel(new GridLayout(2, 1));
		JLabel lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelCategory.add(lblCategory);
		comboBoxCategory = new JComboBox<>(ItemCategory.values());
		comboBoxCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelCategory.add(comboBoxCategory);

		// Define the "Add" button section
		JPanel panelButton = new JPanel(new GridLayout(1, 1));
		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(ADD_BUTTON_COLOR);
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnAdd.addActionListener(e -> {
			addItem(fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
			clearFields();
		});	
		panelButton.add(btnAdd);

		// Create a panel to hold the five previous sections vertically
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(5, 1, 0, 10)); // 5 rows, 1 column, 10px vertical gap
		formPanel.add(panelName);
		formPanel.add(panelQuantity);
		formPanel.add(panelUnitPrice);
		formPanel.add(panelCategory);
		formPanel.add(panelButton);
		
		// Add all the items in the database to the corresponding table
		populateItemTable();
	}
	
	private void layoutComponents() {
		add(tablePanel, BorderLayout.WEST);
		add(formPanel, BorderLayout.CENTER);
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
	
	private JFormattedTextField createQuantityFormattedTextField() {
		// Create a NumberFormatter for integers
        NumberFormat format = NumberFormat.getIntegerInstance();
		NumberFormatter intFormatter = new NumberFormatter(format);
		intFormatter.setValueClass(Integer.class);
		intFormatter.setMinimum(1); // Set minimum to 1 for positive integers
		intFormatter.setMaximum(Integer.MAX_VALUE);
		intFormatter.setAllowsInvalid(false); // Prevent invalid input
		intFormatter.setCommitsOnValidEdit(true); // Commit on valid edit
		
		// Create the JFormattedTextField with the formatter
		JFormattedTextField quantityField = new JFormattedTextField(intFormatter);
		quantityField.setValue(1); // Set a default value
		return quantityField;
	}

	private JFormattedTextField createUnitPriceFormattedTextField() {
		// Create a NumberFormatter for currency (€)
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.ITALY);
		NumberFormatter decFormatter = new NumberFormatter(format);
		decFormatter.setValueClass(Double.class);
		decFormatter.setMinimum(0.0);
		decFormatter.setMaximum(Double.MAX_VALUE);
		decFormatter.setAllowsInvalid(false); // Prevent invalid input
		decFormatter.setCommitsOnValidEdit(true); // Commit on valid edit
		
		// Create the JFormattedTextField with the formatter
        JFormattedTextField priceField = new JFormattedTextField(decFormatter);
        priceField.setValue(0.0); // Set a default value
		return priceField;
	}

	private void addItem(JTextField textFieldName, JFormattedTextField textFieldQuantity,
			JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
		try {
			String name = textFieldName.getText();
	        Number quantityNumber = (Number) textFieldQuantity.getValue();
	        Number unitPriceNumber = (Number) textFieldUnitPrice.getValue();
			ItemCategory category = (ItemCategory) comboBoxCategory.getSelectedItem();

			if (!name.isBlank() && quantityNumber != null && unitPriceNumber != null) {
				int quantity = quantityNumber.intValue(); // Convert to int
				double unitPrice = unitPriceNumber.doubleValue(); // Convert to double
				itemController.addItem(name, quantity, unitPrice, category);
				listener.onItemChanged(); // Notify the listener
				JOptionPane.showMessageDialog(null, "Item added successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
						"Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The new item could not be added!",
					"Something went wrong", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Orchestrates the data fetching and table population.
	 */
	void populateItemTable() {
		// Fetch data from the database using Hibernate
		List<Item> items = itemController.getAllItems(); 
		
		// Clear any existing data in the table model
		itemTableModel.setRowCount(0); 
		
		// Populate the table model with fetched items
		for (Item item : items) {
			Object[] rowData = {
					item.getId(),
					item.getName(),
					item.getQuantity(),
					item.getUnitPrice(),
					item.getCategory()
			};
			itemTableModel.addRow(rowData);
		}
	}
	
	private void clearFields() {
		fieldName.setText("");
		fieldQuantity.setValue(1); // Reset to default value
		fieldUnitPrice.setValue(0.0); // Reset to default value
		comboBoxCategory.setSelectedIndex(0);
	}
}
