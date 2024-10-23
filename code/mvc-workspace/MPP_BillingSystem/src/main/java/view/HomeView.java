package view;

import java.awt.*;

import javax.swing.*;

import controller.UserController;
import controller.UserControllerImpl;
import model.User;

@SuppressWarnings("serial")
public class HomeView extends JFrame {

	private JPanel contentPane;

	private UserController userController;

	/**
	 * Instantiates a new home view when the application is first launched.
	 */
	public HomeView() {
		initialize();
	}

	/**
	 * Instantiates a new home view after a user has logged in.
	 *
	 * @param user the user who logged in
	 */
	public HomeView(User user) {
		initialize();
		userController = UserControllerImpl.getInstance();
		setupUserButtons(user);
	}

	private void initialize() {
		// Configure the frame
		setTitle("Main Menu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Configure the "Login" button
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(e -> {
			setVisible(false);
			new LoginView().display();
		});
		gbc.gridx = 0; // Column 0
		gbc.gridy = 0; // Row 0
		contentPane.add(btnLogin, gbc);

		// Configure the "Exit" button
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(e -> System.exit(0));
		gbc.gridx = 1; // Column 1
		gbc.gridy = 0; // Row 0
		contentPane.add(btnExit, gbc);

		// Configure the "Cash Register" button
		JButton btnCashRegister = new JButton("Cash Register");
		btnCashRegister.setEnabled(false);
		btnCashRegister.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"You must sign in to access this screen!", "Access denied", JOptionPane.WARNING_MESSAGE));
		gbc.gridx = 0; // Column 0
		gbc.gridy = 1; // Row 1
		contentPane.add(btnCashRegister, gbc);

		// Configure the "Management" button
		JButton btnManagement = new JButton("Management");
		btnManagement.setEnabled(false);
		btnManagement.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"You must sign in to access this screen!", "Access denied", JOptionPane.WARNING_MESSAGE));
		gbc.gridx = 1; // Column 1
		gbc.gridy = 1; // Row 1
		contentPane.add(btnManagement, gbc);
	}

	private void setupUserButtons(User user) {
		JButton btnCashier = (JButton) contentPane.getComponent(2);
		btnCashier.setEnabled(true);
		btnCashier.addActionListener(e -> {
			setVisible(false);
			new InvoiceView(user).display();
		});

		JButton btnManagement = (JButton) contentPane.getComponent(3);
		boolean isManager = userController.isUserManager(user);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(e -> {
			if (isManager) {
				setVisible(false);
				new ManagementView(user).display();
			} else {
				JOptionPane.showMessageDialog(null, "You must be a manager to access this screen!", "Access denied",
						JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	public void display() {
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(800, 600));
	}
}
