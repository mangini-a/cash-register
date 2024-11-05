package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

import controller.UserController;

@SuppressWarnings("serial")
public class HomeView extends JFrame {

	private JPanel contentPane;
	private UserController userController;

	/**
	 * Instantiates a new home page when the application is first launched.
	 * @param userController the only instance of UserControllerImpl
	 */
	public HomeView(UserController userController) {
		this.userController = userController;
		initialize();
	}

	/**
	 * Instantiates a new home page after a user has logged in.
	 *
	 * @param userController the the only instance of UserControllerImpl
	 * @param userId the user who logged in
	 */
	public HomeView(UserController userController, int userId) {
		this.userController = userController;
		initialize();
		setupUserButtons(userId);
	}

	private void initialize() {
		// Configure the frame
		setTitle("Home Page");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

		// Create the "Login" button and make the corresponding screen accessible
		JButton btnLogin = createButton("Login", "Log into the system with your credentials", e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new LoginView(userController).display();
			});
		});
		contentPane.add(btnLogin);

		// Create the "Cash Register" button and make the corresponding screen not accessible
		JButton btnCashRegister = createButton("Cash Register", "Record a new receipt", null);
		btnCashRegister.setEnabled(false);
		contentPane.add(btnCashRegister);

		// Create the "Management" button and make the corresponding screen not accessible
		JButton btnManagement = createButton("Management", "Manage stock, staff and accounting", null);
		btnManagement.setEnabled(false);
		contentPane.add(btnManagement);
	}

	private JButton createButton(String text, String toolTipText, ActionListener actionListener) {
		JButton button = new JButton(text);

		// Set the background color based on the button text
		if (text.equals("Cash Register")) {
			button.setBackground(new Color(173, 216, 230)); // Light blue
		} else if (text.equals("Management")) {
			button.setBackground(new Color(144, 238, 144)); // Light green
		} else if (text.equals("Login")) {
			button.setBackground(new Color(211, 211, 211)); // Light gray
		}

		button.setToolTipText(toolTipText);
		button.setPreferredSize(new Dimension(200, 150));
		button.setFont(new Font("Segoe UI", Font.BOLD, 18));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createRaisedBevelBorder());

		if (actionListener != null) {
			button.addActionListener(actionListener);
		}

		return button;
	}

	private void setupUserButtons(int userId) {
		// Make the "Login" screen not accessible
		JButton btnLogin = (JButton) contentPane.getComponent(0);
		btnLogin.setEnabled(false);

		// Make the "Cash Register" screen accessible
		JButton btnCashRegister = (JButton) contentPane.getComponent(1);
		btnCashRegister.setEnabled(true);
		btnCashRegister.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new CashRegisterView(userController, userId).display();
			});
		});

		// Make the "Management" screen accessible only to managers
		JButton btnManagement = (JButton) contentPane.getComponent(2);
		boolean isManager = userController.isUserManager(userId);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new ManagementView(userController, userId).display();
			});
		});
	}

	public void display() {
		setMinimumSize(new Dimension(660, 180));
		setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
