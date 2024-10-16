package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.UserRole;

@SuppressWarnings("serial")
public class HomeView extends JFrame {

	private static final double PERCENT = 0.6;
	private JPanel contentPane;
	// private UserController userController;

	public HomeView(UserRole userRole) {
		// userController = UserControllerImpl.getInstance();
		// userController.setLoggedUser(userRole);
		// Setup the frame
		setFont(new Font("Dialog", Font.PLAIN, 6));
		setTitle("Main Menu");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(620, 280, 653, 374);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// User user = userController.getUserById(userRole);
		
		JButton btnCashier = new JButton("Cash Register");
		// boolean isNotCustomer = (user != null) ? !user.getRole().equals(UserRole.CUSTOMER) : false;
		boolean isNotCustomer = !userRole.equals(UserRole.CUSTOMER) && !userRole.equals(UserRole.TEMP);
		btnCashier.setEnabled(isNotCustomer);
		btnCashier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNotCustomer) {
					CashierView cashierView = new CashierView(userRole);
					cashierView.display();
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as MANAGER");
				}
			}
		});
		btnCashier.setFont(new Font("Serif", Font.BOLD, 15));
		btnCashier.setBounds(194, 272, 129, 54);
		contentPane.add(btnCashier);

		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				LoginView loginView = new LoginView();
				loginView.display();
			}
		});
		btnLogin.setFont(new Font("Serif", Font.BOLD, 15));
		btnLogin.setBounds(12, 13, 79, 29);
		contentPane.add(btnLogin);

		JButton btnManagement = new JButton("Management");
		// boolean isManager = (user != null) ? user.getRole().equals(UserRole.MANAGER) : false;
		boolean isManager = userRole.equals(UserRole.MANAGER);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isManager) {
					ManagementView managementView = new ManagementView();
					setVisible(false);
					managementView.display();
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as MANAGER");
				}
			}
		});
		btnManagement.setFont(new Font("Serif", Font.BOLD, 15));
		btnManagement.setBounds(387, 272, 129, 54);
		contentPane.add(btnManagement);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		btnExit.setFont(new Font("Serif", Font.BOLD, 15));
		btnExit.setBounds(12, 55, 79, 29);
		contentPane.add(btnExit);
	}

	public void display() {
		setVisible(true);
		setResizable(true);
		setMinimumSize(new Dimension(500, 500));
		setLocationRelativeTo(null);
		final Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		setSize((int) (dimension.getWidth() * (HomeView.PERCENT)), (int) (dimension.getHeight() * HomeView.PERCENT));
	}
}
