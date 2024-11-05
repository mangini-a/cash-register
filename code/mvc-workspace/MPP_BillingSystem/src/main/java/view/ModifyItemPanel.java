package view;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemControllerImpl;
import model.ItemCategory;

@SuppressWarnings("serial")
public class ModifyItemPanel extends JPanel {

	private static final Color REMOVE_BUTTON_COLOR = new Color(255, 105, 97); // Light red
	private static final Color UPDATE_BUTTON_COLOR = new Color(144, 238, 144); // Light green

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel itemTableModel;

	private JPanel formPanel; // Container for the form

	private JComboBox<Integer> comboBoxItemId;
	private JTextField fieldName;
	private JFormattedTextField fieldQuantity;
	private JFormattedTextField fieldUnitPrice;
	private JComboBox<ItemCategory> comboBoxCategory;

	private ItemController itemController;

	private PanelChangeListener listener;

	public ModifyItemPanel(PanelChangeListener listener) {
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
		itemTableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Quantity", "Unit Price", "Category" }, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // Make all cells non-editable
			}
		};
		JTable itemTable = new JTable(itemTableModel);
		itemTable.setFillsViewportHeight(true);

		// Center the content of all columns
		for (int i = 0; i < itemTable.getColumnCount(); i++) {
			itemTable.getColumnModel().getColumn(i).setCellRenderer(new StockTableCellRenderer());
		}

		itemTable.setRowHeight(20); // Set row height for vertical "centering"
		JScrollPane scrollPane = new JScrollPane(itemTable);

		// Create a title label for the table
		JLabel titleLabel = new JLabel("Products currently in stock", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(titleLabel, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Define the "Item ID" field to be selected
		JPanel panelItemId = new JPanel(new GridLayout(2, 1));
		JLabel lblItemId = new JLabel("Item ID *");
		lblItemId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelItemId.add(lblItemId);
		comboBoxItemId = new JComboBox<>();
		comboBoxItemId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboBoxItemId.addActionListener(
				e -> fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory));
		panelItemId.add(comboBoxItemId);

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

		// Define the "Remove" and the "Update" buttons section
		JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBackground(REMOVE_BUTTON_COLOR);
		btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnRemove.addActionListener(e -> {
			itemController.removeItemById((Integer) comboBoxItemId.getSelectedItem());
			listener.onItemChanged(); // Notify the listener
			clearFields();
			fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
			JOptionPane.showMessageDialog(null, "Item removed successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		});
		panelButtons.add(btnRemove);
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBackground(UPDATE_BUTTON_COLOR);
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnUpdate.addActionListener(e -> {
			updateItem(fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
			clearFields();
			fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
		});
		panelButtons.add(btnUpdate);

		// Create a panel to hold the six previous sections vertically
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(6, 1, 0, 10)); // 6 rows, 1 column, 10px vertical gap
		formPanel.add(panelItemId);
		formPanel.add(panelName);
		formPanel.add(panelQuantity);
		formPanel.add(panelUnitPrice);
		formPanel.add(panelCategory);
		formPanel.add(panelButtons);

		// Add all the items in the database to the corresponding table
		populateItemTable();

		// Add all the ids in the database to the corresponding JComboBox
		populateComboBoxItemId(comboBoxItemId);

		// Populate the fields for the initially selected item
		fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
	}

	private void layoutComponents() {
		add(tablePanel, BorderLayout.WEST);
		add(formPanel, BorderLayout.CENTER);
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

	private void updateItem(JTextField textFieldName, JFormattedTextField textFieldQuantity,
			JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
		try {
			String name = textFieldName.getText();
			Number quantityNumber = (Number) textFieldQuantity.getValue();
			Number unitPriceNumber = (Number) textFieldUnitPrice.getValue();
			ItemCategory category = (ItemCategory) comboBoxCategory.getSelectedItem();

			if (!name.isBlank() && quantityNumber != null && unitPriceNumber != null) {
				int quantity = quantityNumber.intValue(); // Convert to int
				double unitPrice = unitPriceNumber.doubleValue(); // Convert to double
				itemController.updateItem((Integer) comboBoxItemId.getSelectedItem(), name, quantity, unitPrice,
						category);
				listener.onItemChanged(); // Notify the listener
				JOptionPane.showMessageDialog(null, "Item updated successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
						"Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The item could not be updated!", "Something went wrong",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void populateItemTable() {
		// Fetch data from the database using Hibernate
		List<Integer> itemIds = itemController.getAllItemIds();

		// Clear any existing data in the table model
		itemTableModel.setRowCount(0);

		// Populate the table model with fetched items
		for (Integer itemId : itemIds) {
			Object[] rowData = { itemId, itemController.getItemNameById(itemId),
					itemController.getItemQuantityById(itemId), itemController.getItemUnitPriceById(itemId),
					itemController.getItemCategoryById(itemId) };
			itemTableModel.addRow(rowData);
		}
	}

	private void clearFields() {
		comboBoxItemId.setSelectedIndex(0);
		fieldName.setText("");
		fieldQuantity.setValue(1); // Reset to default value
		fieldUnitPrice.setValue(0.0); // Reset to default value
		comboBoxCategory.setSelectedIndex(0);
	}

	void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer itemId : itemController.getAllItemIds()) {
			model.addElement(itemId);
		}
		comboBoxItemId.setModel(model);
	}

	private void fillItemFields(JComboBox<Integer> comboBoxItemId, JTextField textFieldName,
			JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice,
			JComboBox<ItemCategory> comboBoxCategory) {
		try {
			Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
			
			// Fill in the user fields
			textFieldName.setText(itemController.getItemNameById(selectedItemId));
			textFieldQuantity.setValue(itemController.getItemQuantityById(selectedItemId));
			textFieldUnitPrice.setValue(itemController.getItemUnitPriceById(selectedItemId));
			ItemCategory selectedCategory = itemController.getItemCategoryById(selectedItemId);
			comboBoxCategory.setSelectedItem(selectedCategory);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "The item details could not be loaded!", "Something went wrong",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public JComboBox<Integer> getComboBoxItemId() {
		return comboBoxItemId;
	}
}
