package view;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.UserController;
import view.colors.AppColors;

@SuppressWarnings("serial")
class LoginView extends JFrame {

	private JPanel contentPane;

	// Components used for the "User ID" field
	private JPanel panelUserId;
	private JLabel lblUserId;
	private JTextField fieldUserId;

	// Components used for the "Password" field
	private JPanel panelUserPassword;
	private JLabel lblUserPassword;
	private JPasswordField fieldUserPassword;

	// Component used for the "Show Password" option
	private JPanel checkBoxPanel;
	private JCheckBox checkBoxShowPassword;

	// Component used for the "Sign In" button
	private JButton btnSignIn;

	private UserController userController;

	/**
	 * Instantiates a new login page in order to let the user access the system.
	 *
	 * @param userController the only instance of UserControllerImpl
	 */
	LoginView(UserController userController) {
		// Configure the frame
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel(new GridLayout(4, 1, 0, 10)); // 4 rows, 1 column, 10px vertical gap
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // 15px border on all sides
		setContentPane(contentPane);

		// Get the only instance of UserController to perform user-related operations on the DB
		this.userController = userController;

		// Define the "User ID" field to be filled in
		panelUserId = new JPanel(new GridLayout(2, 1));
		lblUserId = new JLabel("User ID *");
		lblUserId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelUserId.add(lblUserId);
		fieldUserId = new JTextField();
		fieldUserId.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelUserId.add(fieldUserId);
		contentPane.add(panelUserId);

		// Define the "Password" field to be filled in
		panelUserPassword = new JPanel(new GridLayout(2, 1));
		lblUserPassword = new JLabel("Password *");
		lblUserPassword.setFont(new Font("Segoe UI", Font.BOLD, 14));
		panelUserPassword.add(lblUserPassword);
		fieldUserPassword = new JPasswordField();
		fieldUserPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panelUserPassword.add(fieldUserPassword);
		contentPane.add(panelUserPassword);

		// Define the "Show Password" option
		checkBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		checkBoxShowPassword = new JCheckBox("Show Password");
		checkBoxShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		checkBoxShowPassword.addActionListener(e -> {
			if (checkBoxShowPassword.isSelected()) {
				fieldUserPassword.setEchoChar((char) 0);
			} else {
				fieldUserPassword.setEchoChar('*');
			}
		});
		checkBoxPanel.add(checkBoxShowPassword);
		contentPane.add(checkBoxPanel);

		// Define the "Sign In" button
		btnSignIn = new JButton("Sign In");
		btnSignIn.setBackground(AppColors.LOGIN_COLOR);
		btnSignIn.setForeground(Color.WHITE);
		btnSignIn.setToolTipText("Access the system using your credentials");
		btnSignIn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnSignIn.addActionListener(e -> signIn());
		contentPane.add(btnSignIn);
	}

	private void signIn() {
		try {
			// Acquire data entered by the user
			int inputUserId = Integer.parseInt(fieldUserId.getText());
			char[] passwordChars = fieldUserPassword.getPassword();
			String inputUserPassword = String.valueOf(passwordChars);

			Arrays.fill(passwordChars, '0'); // Clear the returned character array after use

			boolean isIdPresent = false;

			for (Integer userId : userController.getAllUserIds()) {
				if (userId == inputUserId) {
					isIdPresent = true;
					String userPassword = userController.getUserPasswordById(userId);
					if (userPassword.equals(inputUserPassword)) {
						dispose();
						SwingUtilities.invokeLater(() -> {
							new HomeView(userController, userId).display();
						});
					} else {
						JOptionPane.showMessageDialog(null, "You entered a wrong password!", "Access denied",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
			if (!isIdPresent) {
				JOptionPane.showMessageDialog(null, "Your ID could not be found!", "Access denied",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (NullPointerException npe) {
			JOptionPane.showMessageDialog(null, "User ID and/or Password have not been specified!",
					"Missing information", JOptionPane.WARNING_MESSAGE);
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Your ID must be an integer numeric value!", "Unprocessable input",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void display() {
		setSize(315, 420);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
