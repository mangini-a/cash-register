package view;

import java.awt.*;
import javax.swing.*;

import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JTabbedPane tabbedPane;

	public ManagementView(User user) {
		// Configure the frame
		setTitle("Management Screen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tabbedPane = new JTabbedPane();

		// Create panels for each main area
		JPanel userPanel = createUserPanel(user);
		JPanel itemPanel = createItemPanel(user);
		JPanel accountingPanel = createAccountingPanel(user);

		// Add panels to the tabbed pane
		tabbedPane.addTab("Users", userPanel);
		tabbedPane.addTab("Items", itemPanel);
		tabbedPane.addTab("Accounting", accountingPanel);

		// Create the "Back to Home" button
		JButton backButton = new JButton("Back to Home");
		backButton.setToolTipText("Go back to Home");
		backButton.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(user).display();
			});
		});

		// Layout setup
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(tabbedPane, BorderLayout.CENTER);
		mainPanel.add(backButton, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private JPanel createUserPanel(User user) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JButton btnUserView = new JButton("User view");
		btnUserView.setToolTipText("View user details");
		btnUserView.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				// new UserView(user).display();
			});
		});

		JButton btnCustomerView = new JButton("Customer view");
		btnCustomerView.setToolTipText("View customer details");
		btnCustomerView.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				// new CustomerView(user).display();
			});
		});

		panel.add(btnUserView);
		panel.add(btnCustomerView);
		return panel;
	}

	private JPanel createItemPanel(User user) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JButton btnItemView = new JButton("Item view");
		btnItemView.setToolTipText("View item details");
		btnItemView.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				// new ItemView(user).display();
			});
		});

		JButton btnProductDetails = new JButton("Product details");
		btnProductDetails.setToolTipText("View product details");
		btnProductDetails.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				// new ProuctDetailsView(user).display();
			});
		});

		panel.add(btnItemView);
		panel.add(btnProductDetails);
		return panel;
	}

	private JPanel createAccountingPanel(User user) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JButton btnAccountingView = new JButton("Accounting view");
		btnAccountingView.setToolTipText("View accounting details");
		btnAccountingView.addActionListener(e -> {
			dispose();
            SwingUtilities.invokeLater(() -> {
            	//new AccountingView(user).display();
            });
		});

		JButton btnReportsView = new JButton("Reports view");
		btnReportsView.setToolTipText("View reports");
		btnReportsView.addActionListener(e -> {
			dispose();
            SwingUtilities.invokeLater(() -> {
            	//new ReportsView(user).display();
            });
		});

		panel.add(btnAccountingView);
		panel.add(btnReportsView);
		return panel;
	}

	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
