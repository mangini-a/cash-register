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

import model.User;
import model.UserRole;

@SuppressWarnings("serial")
public class HomeView extends JFrame {

	private static final double PERCENT = 0.6;
	private JPanel contentPane;

	public HomeView(UserRole userRole) {
		// Setup the frame
		setFont(new Font("Dialog", Font.PLAIN, 6));
		setTitle("Main menu");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(620, 280, 653, 374);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnCashier = new JButton("Cash register");
		boolean isNotCustomer = !userRole.equals(UserRole.CUSTOMER) && !userRole.equals(UserRole.TEMP);
		btnCashier.setEnabled(isNotCustomer);
		btnCashier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNotCustomer) {
					InvoiceView invoiceView = new InvoiceView(userRole);
					invoiceView.display();
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as CASHIER or MANAGER!",
							"Access denied", JOptionPane.WARNING_MESSAGE);
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
		boolean isManager = userRole.equals(UserRole.MANAGER);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isManager) {
					ManagementView managementView = new ManagementView();
					setVisible(false);
					managementView.display();
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as MANAGER!", "Access denied",
							JOptionPane.WARNING_MESSAGE);
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

	public HomeView(User user) {
		// Setup the frame
		setFont(new Font("Dialog", Font.PLAIN, 6));
		setTitle("Main menu");
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(620, 280, 653, 374);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setForeground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnCashier = new JButton("Cash register");
		boolean isNotCustomer = !user.getRole().equals(UserRole.CUSTOMER) && !user.getRole().equals(UserRole.TEMP);
		btnCashier.setEnabled(isNotCustomer);
		btnCashier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNotCustomer) {
					InvoiceView invoiceView = new InvoiceView(user);
					invoiceView.display();
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as CASHIER or MANAGER!",
							"Access denied", JOptionPane.WARNING_MESSAGE);
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
		boolean isManager = user.getRole().equals(UserRole.MANAGER);
		btnManagement.setEnabled(isManager);
		btnManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isManager) {
					ManagementView managementView = new ManagementView();
					setVisible(false);
					managementView.display();
				} else {
					JOptionPane.showMessageDialog(null, "You must be authenticated as MANAGER!", "Access denied",
							JOptionPane.WARNING_MESSAGE);
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
