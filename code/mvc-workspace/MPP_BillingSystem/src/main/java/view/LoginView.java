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
	private UserController userController;

	public LoginView() {
		// Configure the frame
		setTitle("Login Procedure");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel(new GridLayout(6, 1));
		setContentPane(contentPane);
        
		// Get the only instance of UserController to perform user-related operations on the DB
		userController = UserControllerImpl.getInstance();

		// Define the "User ID" field to be filled in
		JPanel panelUserId = new JPanel(new GridLayout(2, 1));
		JLabel lblUserId = new JLabel("User ID *");
		lblUserId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelUserId.add(lblUserId);
		JTextField fieldUserId = new JTextField();
		fieldUserId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fieldUserId.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelUserId.add(fieldUserId);
		contentPane.add(panelUserId);

		// Define the "Password" field to be filled in
		JPanel panelUserPassword = new JPanel(new GridLayout(2, 1));
		JLabel lblUserPassword = new JLabel("Password *");
		lblUserPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		panelUserPassword.add(lblUserPassword);
		JPasswordField fieldUserPassword = new JPasswordField();
		fieldUserPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		fieldUserPassword.setBorder(new EmptyBorder(10, 10, 10, 10));
		panelUserPassword.add(fieldUserPassword);
		contentPane.add(panelUserPassword);
		
		// Define the "Show Password" option
		JCheckBox chckbxShowPassword = new JCheckBox("Show Password");
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
        
        // Define the "SIGN IN" button
		JButton btnSignIn = new JButton("SIGN IN");
		btnSignIn.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnSignIn.setBackground(Color.decode("#206C88"));
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
							String userPassword = userController.getUserPassword(user);
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
		contentPane.add(btnSignIn);
	}

	public void display() {
		setSize(500, 500);
		setResizable(false);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
