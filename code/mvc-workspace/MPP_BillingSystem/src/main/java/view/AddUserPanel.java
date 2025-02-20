package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.UserController;
import model.UserRole;
import view.colors.AppColors;
import view.listeners.PanelChangeListener;
import view.renderers.StaffTableCellRenderer;

@SuppressWarnings("serial")
class AddUserPanel extends JPanel {

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel userTableModel;
	private JTable userTable;
	private StaffTableCellRenderer renderer;

	private JPanel formPanel; // Container for the form

	private JTextField fieldFirstName;
	private JTextField fieldLastName;
	private JTextField fieldPassword;
	private JComboBox<UserRole> comboBoxRole;

	private UserController userController;

	private PanelChangeListener listener;
	
	AddUserPanel(PanelChangeListener listener, int loggedManagerId, UserController userController) {
		this.listener = listener;
		this.userController = userController;
		initializeComponents();
	    renderer = new StaffTableCellRenderer(loggedManagerId);
	    userTable.setDefaultRenderer(Object.class, renderer); // Apply the renderer to all columns
		BorderLayout layout = new BorderLayout();
		layout.setHgap(10); // Add a 10px horizontal gap between components
		setLayout(layout);
		layoutComponents();
	}

	private void initializeComponents() {
		// Define the existing users table's model
		userTableModel = new DefaultTableModel(new Object[] { "ID", "Name", "Surname", "Role" }, 0) {
			@Override
	        public boolean isCellEditable(int row, int column) {
	            return false; // Make all cells non-editable
	        }
		};
		userTable = new JTable(userTableModel);
		userTable.setFillsViewportHeight(true);
		userTable.setRowHeight(20); // Set row height for vertical "centering"
		JScrollPane scrollPane = new JScrollPane(userTable);

		// Create a title label for the table
		JLabel titleLabel = new JLabel("Current staff members", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(titleLabel, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Define the "First Name" field to be filled in
		JPanel panelFirstName = new JPanel(new GridLayout(2, 1));
		JLabel lblFirstName = new JLabel("First Name *");
		lblFirstName.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelFirstName.add(lblFirstName);
		fieldFirstName = new JTextField();
		fieldFirstName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelFirstName.add(fieldFirstName);

		// Define the "Last Name" field to be filled in
		JPanel panelLastName = new JPanel(new GridLayout(2, 1));
		JLabel lblLastName = new JLabel("Last Name *");
		lblLastName.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelLastName.add(lblLastName);
		fieldLastName = new JTextField();
		fieldLastName.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelLastName.add(fieldLastName);

		// Define the "Password" field to be filled in
		JPanel panelPassword = new JPanel(new GridLayout(2, 1));
		JLabel lblPassword = new JLabel("Password *");
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelPassword.add(lblPassword);
		fieldPassword = new JTextField();
		fieldPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelPassword.add(fieldPassword);

		// Define the "Role" field to be selected
		JPanel panelRole = new JPanel(new GridLayout(2, 1));
		JLabel lblRole = new JLabel("Role *");
		lblRole.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelRole.add(lblRole);
		comboBoxRole = new JComboBox<>(UserRole.values());
		comboBoxRole.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelRole.add(comboBoxRole);

		// Define the "Add" button section
		JPanel panelButton = new JPanel(new GridLayout(1, 1));
		JButton btnAdd = new JButton("Add");
		btnAdd.setBackground(AppColors.ADD_BUTTON_COLOR);
		btnAdd.setToolTipText("Make the user part of the staff");
		btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnAdd.addActionListener(e -> addUser(fieldFirstName, fieldLastName, fieldPassword, comboBoxRole));
		panelButton.add(btnAdd);

		// Create a panel to hold the five previous sections vertically
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(5, 1, 0, 10)); // 5 rows, 1 column, 10px vertical gap
		formPanel.add(panelFirstName);
		formPanel.add(panelLastName);
		formPanel.add(panelPassword);
		formPanel.add(panelRole);
		formPanel.add(panelButton);

		// Add all the users in the database to the corresponding table
		populateUserTable();
	}

	private void layoutComponents() {
		add(tablePanel, BorderLayout.WEST);
		add(formPanel, BorderLayout.CENTER);
	}

	private void addUser(JTextField fieldFirstName, JTextField fieldLastName, JTextField fieldPassword,
			JComboBox<UserRole> comboBoxRole) {
		try {
			String firstName = fieldFirstName.getText();
			String lastName = fieldLastName.getText();
			String password = fieldPassword.getText();
			UserRole role = (UserRole) comboBoxRole.getSelectedItem();

			if (!firstName.isBlank() && !lastName.isBlank() && !password.isBlank()) {
				int generatedId = userController.addUser(firstName, lastName, password, role);
				listener.onUserChanged(); // Notify the listener
				clearFields();
				JOptionPane.showMessageDialog(null, "User added successfully with generated ID: " + generatedId + "!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
						"Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The new user could not be added!", "Something went wrong",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void populateUserTable() {
		// Fetch data from the database using Hibernate
		List<Integer> userIds = userController.getAllUserIds();

		// Clear any existing data in the table model
		userTableModel.setRowCount(0);

		// Populate the table model with fetched items
		for (Integer userId : userIds) {
			Object[] rowData = { userId, userController.getUserFirstNameById(userId), 
					userController.getUserLastNameById(userId), userController.getUserRoleById(userId) };
			userTableModel.addRow(rowData);
		}
	}
	
	private void clearFields() {
		fieldFirstName.setText("");
		fieldLastName.setText("");
		fieldPassword.setText("");
		comboBoxRole.setSelectedIndex(0);
	}
}
