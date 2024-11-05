package view;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.UserController;
import controller.UserControllerImpl;

@SuppressWarnings("serial")
public class LoginView extends JFrame {

	private JPanel contentPane;
	
	// Define color constants
    private static final Color BUTTON_COLOR = new Color(211, 211, 211); // Light gray
	
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

	public LoginView() {
		// Configure the frame
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel(new GridLayout(4, 1, 0, 10)); // 4 rows, 1 column, 10px vertical gap
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15)); // 15px border on all sides
		setContentPane(contentPane);
        
		// Get the only instance of UserController to perform user-related operations on the DB
		userController = UserControllerImpl.getInstance();

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
		checkBoxShowPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkBoxShowPassword.isSelected()) {
					fieldUserPassword.setEchoChar((char) 0);
				} else {
					fieldUserPassword.setEchoChar('*');
				}
			}
		});
		checkBoxPanel.add(checkBoxShowPassword);
		contentPane.add(checkBoxPanel);
        
        // Define the "Sign In" button
		btnSignIn = new JButton("Sign In");
		btnSignIn.setBackground(BUTTON_COLOR);
		btnSignIn.setFont(new Font("Segoe UI", Font.BOLD, 16));
		btnSignIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int inputUserId = Integer.parseInt(fieldUserId.getText());
					char[] passwordChars = fieldUserPassword.getPassword();
					String inputUserPassword = String.valueOf(passwordChars);
					
					// Clear the returned character array after use for stronger security
					Arrays.fill(passwordChars, '0');
					
					boolean isIdPresent = false;
					
					List<Integer> userIds = userController.getAllUserIds();
					for (int userId : userIds) {
						if (userId == inputUserId) {
							isIdPresent = true;
							// User user = userController.getUserById(userId);
							String userPassword = userController.getUserPasswordById(userId);
							if (userPassword.equals(inputUserPassword)) {
								dispose();
								SwingUtilities.invokeLater(() -> {
									new HomeView(userId).display();
								});
							} else {
								JOptionPane.showMessageDialog(null, "You entered a wrong password!", "Access denied", JOptionPane.WARNING_MESSAGE);
							}
						}
					}
					if (!isIdPresent) {
						JOptionPane.showMessageDialog(null, "Your ID could not be found!", "Access denied", JOptionPane.WARNING_MESSAGE);
					}
				} catch (NullPointerException npe) {
					JOptionPane.showMessageDialog(null, "User ID and/or Password have not been specified!", "Missing information", JOptionPane.WARNING_MESSAGE);
				} catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Your ID must be an integer numeric value!", "Unprocessable input", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		contentPane.add(btnSignIn);
	}

	public void display() {
		setSize(315, 420);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
