package view;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import controller.UserController;
import controller.UserControllerImpl;
import model.User;

@SuppressWarnings("serial")
public class HomeView extends JFrame {

	private JPanel contentPane;
	
	// Define color constants
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
	
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

		// Make the "Login" screen accessible
		JButton btnLogin = createButton("Login", "", e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new LoginView().display();
			});
		});
		contentPane.add(btnLogin);

		// Make the "Cash Register" screen not accessible
		JButton btnCashRegister = createButton("Cash Register", "", null);
		btnCashRegister.setEnabled(false);
		contentPane.add(btnCashRegister);

		// Make the "Management" screen not accessible
		JButton btnManagement = createButton("Management", "", null);
		btnManagement.setEnabled(false);
		contentPane.add(btnManagement);
	}

	private JButton createButton(String text, String iconPath, ActionListener actionListener) {
		JButton button = new JButton(text);
		button.setBackground(BUTTON_COLOR);
		button.setForeground(Color.WHITE);
        if (iconPath != null && !iconPath.isEmpty()) {
            try {
                BufferedImage originalImage = ImageIO.read(new File(iconPath));
                Image scaledImage = originalImage.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                button.setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Icons cannot be loaded!", "Input error", JOptionPane.ERROR_MESSAGE);
            }
        }
        button.setHorizontalTextPosition(SwingConstants.CENTER); // Center the text horizontally
        button.setVerticalTextPosition(SwingConstants.BOTTOM); // Position the text below the icon
        button.setPreferredSize(new Dimension(200, 150));
        button.setFont(new Font("Tahoma", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setToolTipText(text);
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
