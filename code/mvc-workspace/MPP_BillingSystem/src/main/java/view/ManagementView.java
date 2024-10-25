package view;

import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.NumberFormatter;

import controller.ItemController;
import controller.ItemControllerImpl;
import model.Item;
import model.ItemCategory;
import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JTabbedPane mainTabbedPane;

	// Initialize controllers
	private ItemController itemController = ItemControllerImpl.getInstance();

	public ManagementView(User user) {
		// Configure the frame
		setTitle("Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainTabbedPane = new JTabbedPane();

		// Create and add the "Stock" tab
		JTabbedPane stockTabbedPane = createStockTabbedPane(user);
		mainTabbedPane.addTab("Stock", stockTabbedPane);

		// Create and add the "Staff" tab
		JTabbedPane staffTabbedPane = createStaffTabbedPane(user);
		mainTabbedPane.addTab("Staff", staffTabbedPane);

		// Create and add the "Accounting" tab
		JTabbedPane accountingTabbedPane = createAccountingTabbedPane(user);
		mainTabbedPane.addTab("Accounting", accountingTabbedPane);

		// Create the "Back to Home" button
		JButton backButton = new JButton("Back to Home");
		backButton.setToolTipText("Go back to Home");
		backButton.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(user).display();
			});
		});

		// Layout setup
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
		mainPanel.add(backButton, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private JTabbedPane createStockTabbedPane(User user) {
		JTabbedPane stockTabbedPane = new JTabbedPane();

		// Create and add the "Add a new item" tab
		JPanel addItemPanel = createAddItemPanel(user);
		// Add more components as needed
		stockTabbedPane.addTab("Add a new item", addItemPanel);

		// Create and add the "Modify an existing item" tab
		JPanel modifyItemPanel = createModifyItemPanel(user);
		// Add more components as needed
		stockTabbedPane.addTab("Modify an existing item", modifyItemPanel);

		return stockTabbedPane;
	}

	private JPanel createAddItemPanel(User user) {
		JPanel addItemPanel = new JPanel();

		// Define the "Name" mandatory field to be filled in
		JLabel lblName = new JLabel("Name *");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblName.setBounds(12, 83, 100, 22);
		addItemPanel.add(lblName);

		JTextField textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldName.setColumns(10);
		textFieldName.setBounds(134, 83, 116, 22);
		addItemPanel.add(textFieldName);

		// Define the "Quantity" mandatory field to be filled in
		JLabel lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblQuantity.setBounds(12, 118, 100, 16);
		addItemPanel.add(lblQuantity);

		NumberFormatter intFormatter = new NumberFormatter(NumberFormat.getInstance());
		intFormatter.setValueClass(Integer.class);
		intFormatter.setMinimum(0);
		intFormatter.setMaximum(Integer.MAX_VALUE);
		intFormatter.setAllowsInvalid(false);
		JFormattedTextField textFieldQuantity = new JFormattedTextField(intFormatter);
		textFieldQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldQuantity.setColumns(10);
		textFieldQuantity.setBounds(134, 118, 116, 22);
		addItemPanel.add(textFieldQuantity);

		// Define the "Unit Price" mandatory field to be filled in
		JLabel lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUnitPrice.setBounds(12, 153, 100, 16);
		addItemPanel.add(lblUnitPrice);

		NumberFormat format = DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setRoundingMode(RoundingMode.HALF_UP);
		InternationalFormatter decFormatter = new InternationalFormatter(format);
		decFormatter.setMinimum(0.0);
		decFormatter.setMaximum(Double.MAX_VALUE);
		decFormatter.setAllowsInvalid(false);
		JFormattedTextField textFieldUnitPrice = new JFormattedTextField(decFormatter);
		textFieldUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldUnitPrice.setColumns(10);
		textFieldUnitPrice.setBounds(134, 153, 116, 22);
		addItemPanel.add(textFieldUnitPrice);

		// Define the "Category" mandatory field to be filled in
		JLabel lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(12, 54, 100, 16);
		addItemPanel.add(lblCategory);

		JComboBox<ItemCategory> comboBoxCategory = new JComboBox<>();
		comboBoxCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBoxCategory.setModel(new DefaultComboBoxModel<>(ItemCategory.values()));
		comboBoxCategory.setBounds(134, 48, 116, 25);
		addItemPanel.add(comboBoxCategory);

		// Define the "Add" button
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(e -> {
			try {
				String name = textFieldName.getText();
				ItemCategory category = ItemCategory
						.valueOf(String.valueOf(comboBoxCategory.getSelectedItem()).toUpperCase());
				String stringQuantity = textFieldQuantity.getText();
				String stringUnitPrice = textFieldUnitPrice.getText();

				if (!name.isBlank() && !stringQuantity.isBlank() && !stringUnitPrice.isBlank()) {
					int quantity = Integer.parseInt(stringQuantity);
					double unitPrice = Double.parseDouble(stringUnitPrice);
					Item item = new Item(name, quantity, unitPrice, null, null, category);
					itemController.addItem(item);
					textFieldName.setText("");
					textFieldQuantity.setText("0");
					textFieldUnitPrice.setText("0.00");
					comboBoxCategory.setSelectedIndex(0);
				} else {
					JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
							"Missing information", JOptionPane.WARNING_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(395, 48, 97, 25);
		addItemPanel.add(btnAdd);

		return addItemPanel;
	}

	private JPanel createModifyItemPanel(User user) {
		JPanel modifyItemPanel = new JPanel();

		// Define the "Item ID" mandatory field to be filled in
		JLabel lblItemId = new JLabel("Item ID *");
		lblItemId.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblItemId.setBounds(18, 48, 100, 22);
		modifyItemPanel.add(lblItemId);

		JComboBox<Integer> comboBoxItemId = new JComboBox<>();
		comboBoxItemId.setBounds(152, 48, 116, 22);
		modifyItemPanel.add(comboBoxItemId);

		// Define the "Name" mandatory field to be filled in
		JLabel lblName = new JLabel("Name *");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblName.setBounds(18, 118, 100, 22);
		modifyItemPanel.add(lblName);

		JTextField textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldName.setColumns(10);
		textFieldName.setBounds(152, 121, 116, 22);
		modifyItemPanel.add(textFieldName);

		// Define the "Quantity" mandatory field to be filled in
		JLabel lblQuantity = new JLabel("Quantity *");
		lblQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblQuantity.setBounds(18, 156, 100, 16);
		modifyItemPanel.add(lblQuantity);

		NumberFormatter intFormatter = new NumberFormatter(NumberFormat.getInstance());
		intFormatter.setValueClass(Integer.class);
		intFormatter.setMinimum(0);
		intFormatter.setMaximum(Integer.MAX_VALUE);
		intFormatter.setAllowsInvalid(false);
		JFormattedTextField textFieldQuantity = new JFormattedTextField(intFormatter);
		textFieldQuantity.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldQuantity.setColumns(10);
		textFieldQuantity.setBounds(152, 153, 116, 22);
		modifyItemPanel.add(textFieldQuantity);

		// Define the "Unit Price" mandatory field to be filled in
		JLabel lblUnitPrice = new JLabel("Unit Price *");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUnitPrice.setBounds(18, 189, 100, 23);
		modifyItemPanel.add(lblUnitPrice);

		NumberFormat format = DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		format.setRoundingMode(RoundingMode.HALF_UP);
		InternationalFormatter decFormatter = new InternationalFormatter(format);
		decFormatter.setMinimum(0.0);
		decFormatter.setMaximum(Double.MAX_VALUE);
		decFormatter.setAllowsInvalid(false);
		JFormattedTextField textFieldUnitPrice = new JFormattedTextField(decFormatter);
		textFieldUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldUnitPrice.setColumns(10);
		textFieldUnitPrice.setBounds(152, 189, 116, 25);
		modifyItemPanel.add(textFieldUnitPrice);

		// Define the "Category" mandatory field to be filled in
		JLabel lblCategory = new JLabel("Category *");
		lblCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblCategory.setBounds(18, 89, 100, 16);
		modifyItemPanel.add(lblCategory);

		JComboBox<ItemCategory> comboBoxCategory = new JComboBox<>();
		comboBoxCategory.setFont(new Font("Tahoma", Font.BOLD, 14));
		comboBoxCategory.setModel(new DefaultComboBoxModel<>(ItemCategory.values()));
		comboBoxCategory.setBounds(152, 83, 116, 25);
		modifyItemPanel.add(comboBoxCategory);

		populateComboBoxItemId(comboBoxItemId);
		fillItemFields(comboBoxItemId, textFieldName, textFieldQuantity, textFieldUnitPrice, comboBoxCategory);
		comboBoxItemId.addActionListener(e -> fillItemFields(comboBoxItemId, textFieldName, textFieldQuantity,
				textFieldUnitPrice, comboBoxCategory));

		// Define the "Update" button
		JButton btnAdd = new JButton("Update");
		btnAdd.addActionListener(e -> {
			try {
				String name = textFieldName.getText();
				String stringQuantity = textFieldQuantity.getText();
				String stringUnitPrice = textFieldUnitPrice.getText();
				ItemCategory category = ItemCategory.valueOf(String.valueOf(comboBoxCategory.getSelectedItem()).toUpperCase());

				if (!name.isBlank() && !stringQuantity.isBlank() && !stringUnitPrice.isBlank()) {
					int quantity = Integer.parseInt(stringQuantity);
					double unitPrice = Double.parseDouble(stringUnitPrice);
					
					// Item item = new Item(name, quantity, unitPrice, null, null, category);
					// itemController.addItem(item);
					
					comboBoxItemId.setSelectedIndex(0);
					textFieldName.setText("");
					textFieldQuantity.setText("0");
					textFieldUnitPrice.setText("0.00");
					comboBoxCategory.setSelectedIndex(0);
					JOptionPane.showMessageDialog(null, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
							"Missing information", JOptionPane.WARNING_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnAdd.setBounds(395, 48, 97, 25);
		addItemPanel.add(btnAdd);

		return modifyItemPanel;
	}

	private void fillItemFields(JComboBox<Integer> comboBoxItemId, JTextField textFieldName,
			JFormattedTextField textFieldQuantity, JFormattedTextField textFieldUnitPrice,
			JComboBox<ItemCategory> comboBoxCategory) {
		try {
			Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
			Item selectedItem = itemController.getItemById(selectedItemId);
			textFieldName.setText(itemController.getItemName(selectedItem));
			textFieldQuantity.setText(String.valueOf(itemController.getItemQuantity(selectedItem)));
			textFieldUnitPrice.setText(String.valueOf(itemController.getItemUnitPrice(selectedItem)));
			ItemCategory selectedCategory = itemController.getItemCategory(selectedItem);
			comboBoxCategory.setSelectedItem(selectedCategory);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane staffTabbedPane = new JTabbedPane();

		// Create and add the "User View" tab
		JPanel userViewPanel = new JPanel();
		userViewPanel.add(new JLabel("User  View Panel"));
		// Add more components as needed
		staffTabbedPane.addTab("User  View", userViewPanel);

		// Create and add the "Customer View" tab
		JPanel customerViewPanel = new JPanel();
		customerViewPanel.add(new JLabel("Customer View Panel"));
		// Add more components as needed
		staffTabbedPane.addTab("Customer View", customerViewPanel);

		return staffTabbedPane;
	}

	private JTabbedPane createAccountingTabbedPane(User user) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();

		// Create and add the "Accounting View" tab
		JPanel accountingViewPanel = new JPanel();
		accountingViewPanel.add(new JLabel("Accounting View Panel"));
		// Add more components as needed
		accountingTabbedPane.addTab("Accounting View", accountingViewPanel);

		// Create and add the "Reports View" tab
		JPanel reportsViewPanel = new JPanel();
		reportsViewPanel.add(new JLabel("Reports View Panel"));
		// Add more components as needed
		accountingTabbedPane.addTab("Reports View", reportsViewPanel);

		return accountingTabbedPane;
	}

	private void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer id : itemController.getAllItemIds()) {
			model.addElement(id);
		}
		comboBoxItemId.setModel(model);
	}

	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
