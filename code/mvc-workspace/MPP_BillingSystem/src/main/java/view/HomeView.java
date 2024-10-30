package view;

import java.awt.*;
import java.awt.event.ActionListener;
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
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		// Create the "Login" button and make the corresponding screen accessible
		JButton btnLogin = createButton("Login", "Log into the system with your credentials", e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new LoginView().display();
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
			button.setBackground(new Color(0, 123, 255)); // bright blue
		} else if (text.equals("Management")) {
			button.setBackground(new Color(40, 167, 69)); // green
		} else if (text.equals("Login")) {
			button.setBackground(new Color(52, 58, 64)); // dark gray
		}

		button.setForeground(Color.WHITE);
		button.setToolTipText(toolTipText);
		button.setPreferredSize(new Dimension(200, 150));
		button.setFont(new Font("Tahoma", Font.BOLD, 16));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createRaisedBevelBorder());

		if (actionListener != null) {
			button.addActionListener(actionListener);
		}

		return button;
	}

	private void setupUserButtons(User user) {
		// Make the "Login" screen not accessible
		JButton btnLogin = (JButton) contentPane.getComponent(0);
		btnLogin.setEnabled(false);

		// Make the "Cash Register" screen accessible
		JButton btnCashRegister = (JButton) contentPane.getComponent(1);
		btnCashRegister.setEnabled(true);
		btnCashRegister.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new CashRegisterView(user).display();
			});
		});

		// Make the "Management" screen accessible only to managers
		JButton btnManagement = (JButton) contentPane.getComponent(2);
		boolean isManager = userController.isUserManager(user);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new ManagementView(user).display();
			});
		});
	}

	public void display() {
		setMinimumSize(new Dimension(680, 190));
		setResizable(false);
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
