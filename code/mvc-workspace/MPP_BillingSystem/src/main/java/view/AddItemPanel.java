package view;

import java.awt.*;
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
		itemController = ItemControllerImpl.getInstance();
		initializeComponents();
		setLayout(new GridLayout(5, 2, 10, 10));
        layoutComponents();
	}

	private void initializeComponents() {
		lblName = new JLabel("Name *");
		textFieldName = new JTextField(10);

		lblQuantity = new JLabel("Quantity *");
		textFieldQuantity = createQuantityFormattedTextField();

		lblUnitPrice = new JLabel("Unit Price *");
		textFieldUnitPrice = createUnitPriceFormattedTextField();

		lblCategory = new JLabel("Category *");
		comboBoxCategory = new JComboBox<>(ItemCategory.values());

		btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> addItem(textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory));
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
	
	private void layoutComponents() {
		// Add labels and fields to the panel
        add(lblName);
        add(textFieldName);
        
        add(lblQuantity);
        add(textFieldQuantity);
        
        add(lblUnitPrice);
        add(textFieldUnitPrice);
        
        add(lblCategory);
        add(comboBoxCategory);
        
        // Add an empty cell for spacing
        add(new JLabel()); // Empty label for spacing
        add(btnAdd); // Add button in the last row
	}
}
