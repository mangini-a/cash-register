package view;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.UserController;
import controller.UserControllerImpl;
import model.User;
import model.UserRole;

@SuppressWarnings("serial")
public class ModifyUserPanel extends JPanel {

	private static final Color REMOVE_BUTTON_COLOR = new Color(255, 105, 97); // Light red
	private static final Color UPDATE_BUTTON_COLOR = new Color(144, 238, 144); // Light green

	private JPanel tablePanel; // Container for the title label and the table itself
	private DefaultTableModel userTableModel;
	private JTable userTable;
	private StaffTableCellRenderer renderer;

	private JPanel formPanel; // Container for the form

	private JComboBox<Integer> comboBoxUserId;
	private JTextField fieldFirstName;
	private JTextField fieldLastName;
	private JTextField fieldPassword;
	private JComboBox<UserRole> comboBoxRole;

	private UserController userController;

	private PanelChangeListener listener;

	private int loggedManagerId;

	public ModifyUserPanel(PanelChangeListener listener, User user) {
		this.listener = listener;
		userController = UserControllerImpl.getInstance();
		initializeComponents();
		loggedManagerId = user.getId();
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
		JLabel titleLabel = new JLabel("Staff members you can edit", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
		titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add some padding

		// Create a panel to hold the title label and the table itself
		tablePanel = new JPanel();
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(titleLabel, BorderLayout.NORTH);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		// Define the "User ID" field to be selected
		JPanel panelUserId = new JPanel(new GridLayout(2, 1));
		JLabel lblUserId = new JLabel("User ID *");
		lblUserId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelUserId.add(lblUserId);
		comboBoxUserId = new JComboBox<>();
		comboBoxUserId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboBoxUserId.addActionListener(
				e -> fillUserFields(comboBoxUserId, fieldFirstName, fieldLastName, fieldPassword, comboBoxRole));
		panelUserId.add(comboBoxUserId);

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

		// Define the "Remove" and the "Update" buttons section
		JPanel panelButtons = new JPanel(new GridLayout(1, 2, 10, 0));
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBackground(REMOVE_BUTTON_COLOR);
		btnRemove.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnRemove.addActionListener(e -> {
			userController.removeUserById((Integer) comboBoxUserId.getSelectedItem());
			listener.onItemChanged(); // Notify the listener
			clearFields();
			fillUserFields(comboBoxUserId, fieldFirstName, fieldLastName, fieldPassword, comboBoxRole);
			JOptionPane.showMessageDialog(null, "Item removed successfully!", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		});
		panelButtons.add(btnRemove);
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBackground(UPDATE_BUTTON_COLOR);
		btnUpdate.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnUpdate.addActionListener(e -> {
			updateUser(fieldFirstName, fieldLastName, fieldPassword, comboBoxRole);
			clearFields();
			fillUserFields(comboBoxUserId, fieldFirstName, fieldLastName, fieldPassword, comboBoxRole);
		});
		panelButtons.add(btnUpdate);

		// Create a panel to hold the six previous sections vertically
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(6, 1, 0, 10)); // 6 rows, 1 column, 10px vertical gap
		formPanel.add(panelUserId);
		formPanel.add(panelFirstName);
		formPanel.add(panelLastName);
		formPanel.add(panelPassword);
		formPanel.add(panelRole);
		formPanel.add(panelButtons);

		// Add all the users in the database to the corresponding table
		populateUserTable();

		// Add all the ids in the database to the corresponding JComboBox
		populateComboBoxUserId(comboBoxUserId);

		// Populate the fields for the initially selected item
		fillUserFields(comboBoxUserId, fieldFirstName, fieldLastName, fieldPassword, comboBoxRole);
	}

	private void layoutComponents() {
		add(tablePanel, BorderLayout.WEST);
		add(formPanel, BorderLayout.CENTER);
	}

	private void updateUser(JTextField fieldFirstName, JTextField fieldLastName, JTextField fieldPassword,
			JComboBox<UserRole> comboBoxRole) {
		try {
			String firstName = fieldFirstName.getText();
			String lastName = fieldLastName.getText();
			String password = fieldPassword.getText();
			UserRole role = (UserRole) comboBoxRole.getSelectedItem();

			if (!firstName.isBlank() && !lastName.isBlank() && !password.isBlank()) {
				userController.updateUser((Integer) comboBoxUserId.getSelectedItem(), password, role);
				listener.onUserChanged(); // Notify the listener
				JOptionPane.showMessageDialog(null, "User updated successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Fields with * must be filled to complete the operation!",
						"Missing information", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "The user could not be updated!", "Something went wrong",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void populateUserTable() {
		// Fetch data from the database using Hibernate
		List<User> users = userController.getAllUsers();

		// Clear any existing data in the table model
		userTableModel.setRowCount(0);

		// Populate the table model with fetched items
		for (User user : users) {
			Object[] rowData = { user.getId(), user.getFirstName(), user.getLastName(), user.getRole() };
			userTableModel.addRow(rowData);
		}
	}

	private void clearFields() {
		comboBoxUserId.setSelectedIndex(0);
		fieldFirstName.setText("");
		fieldLastName.setText("");
		fieldPassword.setText("");
		comboBoxRole.setSelectedIndex(0);
	}

	void populateComboBoxUserId(JComboBox<Integer> comboBoxUserId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		for (Integer id : userController.getAllUserIds()) {
			model.addElement(id);
		}
		comboBoxUserId.setModel(model);
	}

	private void fillUserFields(JComboBox<Integer> comboBoxUserId, JTextField fieldFirstName, JTextField fieldLastName,
			JTextField fieldPassword, JComboBox<UserRole> comboBoxRole) {
		try {
			Integer selectedUserId = (Integer) comboBoxUserId.getSelectedItem();
			User selectedUser = userController.getUserById(selectedUserId);
			fieldFirstName.setText(userController.getFirstName(selectedUser));
			fieldLastName.setText(userController.getLastName(selectedUser));
			fieldPassword.setText(userController.getPassword(selectedUser));
			UserRole selectedRole = userController.getRole(selectedUser);
			comboBoxRole.setSelectedItem(selectedRole);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "The user details could not be loaded!", "Something went wrong",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public JComboBox<Integer> getComboBoxUserId() {
		return comboBoxUserId;
	}
}
