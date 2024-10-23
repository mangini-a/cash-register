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
		contentPane.setLayout(new GridLayout(3, 1, 10, 10));

		// Make the "Login" screen accessible
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new LoginView().display();
			});
		});
		contentPane.add(btnLogin);

		// Make the "Cash Register" screen not accessible
		JButton btnCashRegister = new JButton("Cash Register");
		btnCashRegister.setEnabled(false);
		btnCashRegister.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"You must sign in to access this screen!", "Access denied", JOptionPane.WARNING_MESSAGE));
		contentPane.add(btnCashRegister);

		// Make the "Management" screen not accessible
		JButton btnManagement = new JButton("Management");
		btnManagement.setEnabled(false);
		btnManagement.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"You must sign in to access this screen!", "Access denied", JOptionPane.WARNING_MESSAGE));
		contentPane.add(btnManagement);
	}

	private void setupUserButtons(User user) {
		// Make the "Login" screen not accessible
		JButton btnLogin = (JButton) contentPane.getComponent(0);
		btnLogin.setEnabled(false);
		btnLogin.addActionListener(e -> JOptionPane.showMessageDialog(null,
				"You have already gone through the authentication process!", "Access denied", JOptionPane.WARNING_MESSAGE));
		
		// Make the "Cash Register" screen accessible
		JButton btnCashRegister = (JButton) contentPane.getComponent(1);
		btnCashRegister.setEnabled(true);
		btnCashRegister.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new InvoiceView(user).display();
			});
		});

		// Make the "Management" screen accessible only to managers
		JButton btnManagement = (JButton) contentPane.getComponent(2);
		boolean isManager = userController.isUserManager(user);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(e -> {
			if (isManager) {
				dispose();
				SwingUtilities.invokeLater(() -> {
					new ManagementView(user).display();
				});
			} else {
				JOptionPane.showMessageDialog(null, "You must be a manager to access this screen!", "Access denied",
						JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	public void display() {
		setMinimumSize(new Dimension(500, 500));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
