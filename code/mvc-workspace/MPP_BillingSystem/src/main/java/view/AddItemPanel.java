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
public class AddItemPanel extends JPanel {
	
	private JLabel lblName;
	private JTextField textFieldName;
	private JLabel lblQuantity;
	private JFormattedTextField textFieldQuantity;
	private JLabel lblUnitPrice;
	private JFormattedTextField textFieldUnitPrice;
	private JLabel lblCategory;
	private JComboBox<ItemCategory> comboBoxCategory;
	private JButton btnAdd;

	private ItemController itemController;

	public AddItemPanel() {
		setLayout(null);
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
	}

	private void initializeComponents() {
		// Create the "Name" mandatory field
		lblName = new JLabel("Name *");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblName.setBounds(12, 83, 100, 22);
		add(lblName);
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldName.setColumns(10);
		textFieldName.setBounds(134, 83, 116, 22);
		add(textFieldName);

		// Create the "Quantity" mandatory field
		lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblQuantity.setBounds(12, 118, 100, 16);
		add(lblQuantity);
		textFieldQuantity = createQuantityFormattedTextField();
		textFieldQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldQuantity.setColumns(10);
        textFieldQuantity.setBounds(134, 118, 116, 22);
        add(textFieldQuantity);

		// Create the "Unit Price" mandatory field
		lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUnitPrice.setBounds(12, 153, 100, 16);
		add(lblUnitPrice);
		textFieldUnitPrice = createUnitPriceFormattedTextField();
		textFieldUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldUnitPrice.setColumns(10);
		textFieldUnitPrice.setBounds(134, 153, 116, 22);
		add(textFieldUnitPrice);

		// Create the "Category" mandatory field
		lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(12, 54, 100, 16);
		add(lblCategory);
		comboBoxCategory = new JComboBox<>(ItemCategory.values());
		comboBoxCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBoxCategory.setBounds(134, 48, 116, 25);
		add(comboBoxCategory);

		// Create the "Add" button
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> addItem(textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory));
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(395, 48, 97, 25);
		add(btnAdd);
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

	private void addItem(JTextField textFieldName, JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice, JComboBox<ItemCategory> comboBoxCategory) {
        try {
            String name = textFieldName.getText();
            String quantityString = textFieldQuantity.getText();
            String unitPriceString = textFieldUnitPrice.getText();
            ItemCategory category = (ItemCategory) comboBoxCategory.getSelectedItem();

            if (!name.isBlank() && (!quantityString.isBlank() && (!unitPriceString.isBlank()))) {
            	int quantity = Integer.parseInt(quantityString);
            	double unitPrice = Double.parseDouble(unitPriceString);
                Item item = new Item(name, quantity, unitPrice, null, null, category);
                itemController.addItem(item);
                clearFields();
            } else {
            	JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!", "Missing information", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		textFieldName.setText("");
		textFieldQuantity.setText("0");
		textFieldUnitPrice.setText("0.00");
		comboBoxCategory.setSelectedIndex(0);
	}
}
