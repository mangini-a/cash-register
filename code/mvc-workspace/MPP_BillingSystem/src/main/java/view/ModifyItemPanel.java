package view;

import java.awt.Font;
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

	private JLabel lblItemId;
	private JComboBox<Integer> comboBoxItemId;
	private JLabel lblName;
	private JTextField textFieldName;
	private JLabel lblQuantity;
	private JFormattedTextField textFieldQuantity;
	private JLabel lblUnitPrice;
	private JFormattedTextField textFieldUnitPrice;
	private JLabel lblCategory;
	private JComboBox<ItemCategory> comboBoxCategory;
	private JButton btnRemove;
	private JButton btnUpdate;

	private ItemController itemController;

	public ModifyItemPanel() {
		setLayout(null);
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
	}

	private void initializeComponents() {
		// Create the "Item ID" mandatory field
		lblItemId = new JLabel("Item ID *");
		lblItemId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblItemId.setBounds(18, 48, 100, 22);
		add(lblItemId);
		comboBoxItemId = new JComboBox<>();
		comboBoxItemId.addActionListener(e -> fillItemFields(comboBoxItemId, textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory));
		comboBoxItemId.setBounds(152, 48, 116, 22);
		add(comboBoxItemId);

		// Create the "Name" mandatory field
		lblName = new JLabel("Name *");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblName.setBounds(18, 118, 100, 22);
		add(lblName);
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldName.setColumns(10);
		textFieldName.setBounds(152, 121, 116, 22);
		add(textFieldName);

		// Create the "Quantity" mandatory field
		lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblQuantity.setBounds(18, 156, 100, 16);
		add(lblQuantity);
		textFieldQuantity = createQuantityFormattedTextField();
		textFieldQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldQuantity.setColumns(10);
		textFieldQuantity.setBounds(152, 153, 116, 22);
		add(textFieldQuantity);

		// Create the "Unit Price" mandatory field
		lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUnitPrice.setBounds(18, 189, 100, 23);
		add(lblUnitPrice);
		textFieldUnitPrice = createUnitPriceFormattedTextField();
		textFieldUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldUnitPrice.setColumns(10);
		textFieldUnitPrice.setBounds(152, 189, 116, 25);
		add(textFieldUnitPrice);

		// Create the "Category" mandatory field
		lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(18, 89, 100, 16);
		add(lblCategory);
		comboBoxCategory = new JComboBox<>(ItemCategory.values());
		comboBoxCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBoxCategory.setBounds(152, 83, 116, 25);
		add(comboBoxCategory);
		
		// Create the "Remove" button
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(e -> {
			itemController.removeItemById((Integer) comboBoxItemId.getSelectedItem());
			populateComboBoxItemId(comboBoxItemId);
			fillItemFields(comboBoxItemId, textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory);
			JOptionPane.showMessageDialog(null, "Item removed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRemove.setBounds(495, 148, 97, 25);
		add(btnRemove);

		// Create the "Update" button
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(e -> updateItem(textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory));
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnUpdate.setBounds(395, 48, 97, 25);
		add(btnUpdate);
		
		// Add all the ids in the database to the corresponding JComboBox
		populateComboBoxItemId(comboBoxItemId);
		
		// Populate the fields for the initially selected item
		fillItemFields(comboBoxItemId, textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory);
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

	private void updateItem(JTextField textFieldName, JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
		try {
			String name = textFieldName.getText();
            String quantityString = textFieldQuantity.getText();
            String unitPriceString = textFieldUnitPrice.getText();
            ItemCategory category = (ItemCategory) comboBoxCategory.getSelectedItem();

			if (!name.isBlank() && !quantityString.isBlank() && !unitPriceString.isBlank()) {
				int quantity = Integer.parseInt(quantityString);
            	double unitPrice = Double.parseDouble(unitPriceString);
                itemController.updateItem((Integer) comboBoxItemId.getSelectedItem(), name, quantity, unitPrice, category);
                clearFields();
				JOptionPane.showMessageDialog(null, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!", "Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		comboBoxItemId.setSelectedIndex(0);
		textFieldName.setText("");
		textFieldQuantity.setText("0");
		textFieldUnitPrice.setText("0.00");
		comboBoxCategory.setSelectedIndex(0);
	}
	
	private void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer id : itemController.getAllItemIds()) {
			model.addElement(id);
		}
		comboBoxItemId.setModel(model);
	}
	
	private void fillItemFields(JComboBox<Integer> comboBoxItemId, JTextField textFieldName, JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
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
