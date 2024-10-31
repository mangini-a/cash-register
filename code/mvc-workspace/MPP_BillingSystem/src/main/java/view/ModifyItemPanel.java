package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemControllerImpl;
import model.Item;
import model.ItemCategory;

@SuppressWarnings("serial")
public class ModifyItemPanel extends JPanel {

	// Define color constants
	private static final Color REMOVE_BUTTON_COLOR = new Color(255, 105, 97); // Light red
	private static final Color UPDATE_BUTTON_COLOR = new Color(144, 238, 144); // Light green

	// Components used for the "Item ID" field
	private JPanel panelItemId;
	private JLabel lblItemId;
	private JComboBox<Integer> comboBoxItemId;

	// Components used for the "Name" field
	private JPanel panelName;
	private JLabel lblName;
	private JTextField fieldName;

	// Components used for the "Quantity" field
	private JPanel panelQuantity;
	private JLabel lblQuantity;
	private JFormattedTextField fieldQuantity;

	// Components used for the "Unit Price" field
	private JPanel panelUnitPrice;
	private JLabel lblUnitPrice;
	private JFormattedTextField fieldUnitPrice;

	// Components used for the "Category" field
	private JPanel panelCategory;
	private JLabel lblCategory;
	private JComboBox<ItemCategory> comboBoxCategory;

	// Components used for the "Remove" and "Update" buttons
	private JPanel panelButtons;
	private JButton btnRemove, btnUpdate;

	private ItemController itemController;

	public ModifyItemPanel() {
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
		setLayout(new GridLayout(6, 1, 0, 10)); // 6 rows, 1 column, 10px vertical gap
		layoutComponents();
	}

	private void initializeComponents() {
		// Define the "Item ID" field to be selected
		panelItemId = new JPanel(new GridLayout(2, 1));
		lblItemId = new JLabel("Item ID *");
		lblItemId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelItemId.add(lblItemId);
		comboBoxItemId = new JComboBox<>();
		comboBoxItemId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboBoxItemId.addActionListener(
				e -> fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory));
		panelItemId.add(comboBoxItemId);

		// Define the "Name" field to be filled in
		panelName = new JPanel(new GridLayout(2, 1));
		lblName = new JLabel("Name *");
		lblName.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelName.add(lblName);
		fieldName = new JTextField();
		fieldName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelName.add(fieldName);

		// Define the "Quantity" field to be filled in
		panelQuantity = new JPanel(new GridLayout(2, 1));
		lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelQuantity.add(lblQuantity);
		fieldQuantity = createQuantityFormattedTextField();
		fieldQuantity.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelQuantity.add(fieldQuantity);

		// Define the "Unit Price" field to be filled in
		panelUnitPrice = new JPanel(new GridLayout(2, 1));
		lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelUnitPrice.add(lblUnitPrice);
		fieldUnitPrice = createUnitPriceFormattedTextField();
		fieldUnitPrice.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelUnitPrice.add(fieldUnitPrice);

		// Define the "Category" field to be selected
		panelCategory = new JPanel(new GridLayout(2, 1));
		lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelCategory.add(lblCategory);
		comboBoxCategory = new JComboBox<>(ItemCategory.values());
		comboBoxCategory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelCategory.add(comboBoxCategory);

		// Define the "Remove" and the "Update" buttons
		panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
		btnRemove = new JButton("Remove");
		btnRemove.setBackground(REMOVE_BUTTON_COLOR);
		btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnRemove.addActionListener(e -> {
			itemController.removeItemById((Integer) comboBoxItemId.getSelectedItem());
			populateComboBoxItemId(comboBoxItemId);
			fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
			JOptionPane.showMessageDialog(null, "Item removed successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		});
		panelButtons.add(btnRemove);
		btnUpdate = new JButton("Update");
		btnUpdate.setBackground(UPDATE_BUTTON_COLOR);
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnUpdate.addActionListener(e -> updateItem(fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory));
		panelButtons.add(btnUpdate);

		// Add all the ids in the database to the corresponding JComboBox
		populateComboBoxItemId(comboBoxItemId);

		// Populate the fields for the initially selected item
		fillItemFields(comboBoxItemId, fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory);
	}

	private JFormattedTextField createQuantityFormattedTextField() {
		NumberFormatter intFormatter = new NumberFormatter(NumberFormat.getInstance());
		intFormatter.setValueClass(Integer.class);
		intFormatter.setMinimum(0);
		intFormatter.setMaximum(Integer.MAX_VALUE);
		intFormatter.setAllowsInvalid(false);
		return new JFormattedTextField(intFormatter);
	}

	private JFormattedTextField createUnitPriceFormattedTextField() {
		NumberFormat format = DecimalFormat.getInstance();
		format.setMinimumFractionDigits(2);
		format.setMaximumFractionDigits(2);
		format.setRoundingMode(RoundingMode.HALF_UP);
		NumberFormatter decFormatter = new NumberFormatter(format);
		decFormatter.setMinimum(0.0);
		decFormatter.setMaximum(Double.MAX_VALUE);
		decFormatter.setAllowsInvalid(false);
		return new JFormattedTextField(decFormatter);
	}

	private void updateItem(JTextField textFieldName, JFormattedTextField textFieldQuantity,
			JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
		try {
			String name = textFieldName.getText();
			String quantityString = textFieldQuantity.getText();
			String unitPriceString = textFieldUnitPrice.getText();
			ItemCategory category = (ItemCategory) comboBoxCategory.getSelectedItem();

			if (!name.isBlank() && !quantityString.isBlank() && !unitPriceString.isBlank()) {
				int quantity = Integer.parseInt(quantityString);
				double unitPrice = Double.parseDouble(unitPriceString);
				itemController.updateItem((Integer) comboBoxItemId.getSelectedItem(), name, quantity, unitPrice,
						category);
				clearFields();
				JOptionPane.showMessageDialog(null, "Item updated successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
						"Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		comboBoxItemId.setSelectedIndex(0);
		fieldName.setText("");
		fieldQuantity.setText("0");
		fieldUnitPrice.setText("0.00");
		comboBoxCategory.setSelectedIndex(0);
	}

	private void layoutComponents() {
		add(panelItemId);
		add(panelName);
        add(panelQuantity);
        add(panelUnitPrice);
        add(panelCategory);
        add(panelButtons);
	}

	private void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer id : itemController.getAllItemIds()) {
			model.addElement(id);
		}
		comboBoxItemId.setModel(model);
	}

	private void fillItemFields(JComboBox<Integer> comboBoxItemId, JTextField textFieldName,
			JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice,
			JComboBox<ItemCategory> comboBoxCategory) {
		try {
			Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
			Item selectedItem = itemController.getItemById(selectedItemId);
			textFieldName.setText(itemController.getName(selectedItem));
			textFieldQuantity.setText(String.valueOf(itemController.getQuantity(selectedItem)));
			textFieldUnitPrice.setText(String.valueOf(itemController.getUnitPrice(selectedItem)));
			ItemCategory selectedCategory = itemController.getCategory(selectedItem);
			comboBoxCategory.setSelectedItem(selectedCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
