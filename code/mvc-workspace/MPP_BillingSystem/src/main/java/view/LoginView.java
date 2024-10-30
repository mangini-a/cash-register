package view;

import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.UserController;
import controller.UserControllerImpl;
import model.User;

@SuppressWarnings("serial")
public class LoginView extends JFrame {

	private JPanel contentPane;
	
	// Define color constants
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
	
	// Components used for the "User ID" field
	private JPanel panelUserId;
	private JLabel lblUserId;
	private JTextField fieldUserId;
	
	// Components used for the "Password" field
	private JPanel panelUserPassword;
	private JLabel lblUserPassword;
	private JPasswordField fieldUserPassword;
	
	// Component used for the "Show Password" option
	private JCheckBox chckbxShowPassword;
	
	// Component used for the "Sign In" button
	private JButton btnSignIn;
	
	private UserController userController;

	public LoginView() {
		// Configure the frame
		setTitle("Login Procedure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel(new GridLayout(4, 1, 0, 10)); // 4 rows, 1 column, 10px vertical gap
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20)); // 20px border on all sides
		setContentPane(contentPane);
        
		// Get the only instance of UserController to perform user-related operations on the DB
		userController = UserControllerImpl.getInstance();

		// Define the "User ID" field to be filled in
		panelUserId = new JPanel(new GridLayout(2, 1));
		lblUserId = new JLabel("User ID *");
		lblUserId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelUserId.add(lblUserId);
		fieldUserId = new JTextField();
		panelUserId.add(fieldUserId);
		contentPane.add(panelUserId);

		// Define the "Password" field to be filled in
		panelUserPassword = new JPanel(new GridLayout(2, 1));
		lblUserPassword = new JLabel("Password *");
		lblUserPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panelUserPassword.add(lblUserPassword);
		fieldUserPassword = new JPasswordField();
		panelUserPassword.add(fieldUserPassword);
		contentPane.add(panelUserPassword);
		
		// Define the "Show Password" option
		chckbxShowPassword = new JCheckBox("Show Password");
		chckbxShowPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxShowPassword.isSelected()) {
					fieldUserPassword.setEchoChar((char) 0);
				} else {
					fieldUserPassword.setEchoChar('*');
				}
			}
		});
		contentPane.add(chckbxShowPassword);
        
        // Define the "Sign In" button
		btnSignIn = new JButton("Sign In");
		btnSignIn.setBackground(BUTTON_COLOR);
		btnSignIn.setForeground(Color.WHITE);
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
							User user = userController.getUserById(userId);
							String userPassword = userController.getPassword(user);
							if (userPassword.equals(inputUserPassword)) {
								dispose();
								SwingUtilities.invokeLater(() -> {
									new HomeView(user).display();
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
		btnSignIn.setFont(new Font("Tahoma", Font.BOLD, 14));
		contentPane.add(btnSignIn);
	}

	public void display() {
		setSize(500, 500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
