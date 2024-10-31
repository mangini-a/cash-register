package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemControllerImpl;
import model.ItemCategory;

@SuppressWarnings("serial")
public class AddItemPanel extends JPanel {
	
	// Define color constants
    private static final Color BUTTON_COLOR = new Color(144, 238, 144); // Light green
    
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
	
	// Component used for the "Add" button
	private JButton btnAdd;

	private ItemController itemController;

	public AddItemPanel() {
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
		setLayout(new GridLayout(5, 1, 0, 10)); // 5 rows, 1 column, 10px vertical gap
        layoutComponents();
	}

	private void initializeComponents() {
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

		// Define the "Add" button
		btnAdd = new JButton("Add");
		btnAdd.setBackground(BUTTON_COLOR);
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnAdd.addActionListener(e -> addItem(fieldName, fieldQuantity, fieldUnitPrice, comboBoxCategory));
	}

	private JFormattedTextField createQuantityFormattedTextField() {
		NumberFormatter intFormatter = new NumberFormatter();
        intFormatter.setValueClass(Integer.class);
        intFormatter.setMinimum(0);
        intFormatter.setMaximum(Integer.MAX_VALUE);
        intFormatter.setAllowsInvalid(false);
        return new JFormattedTextField(intFormatter);
	}
	
	private JFormattedTextField createUnitPriceFormattedTextField() {
		NumberFormatter decFormatter = new NumberFormatter();
		decFormatter.setValueClass(Double.class);
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
                itemController.addItem(name, quantity, unitPrice, category);
                clearFields();
            } else {
            	JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!", "Missing information", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void clearFields() {
		fieldName.setText("");
		fieldQuantity.setText("0");
		fieldUnitPrice.setText("0.00");
		comboBoxCategory.setSelectedIndex(0);
	}
	
	private void layoutComponents() {
        add(panelName);
        add(panelQuantity);
        add(panelUnitPrice);
        add(panelCategory);
        add(btnAdd);
	}
}
