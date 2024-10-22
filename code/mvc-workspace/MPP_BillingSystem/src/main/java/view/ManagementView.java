package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JTabbedPane tabbedPane;

	public ManagementView(User user) {
		setTitle("Management Screen");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(620, 280, 600, 400);

		tabbedPane = new JTabbedPane();

		// Create panels for each main area
		JPanel userPanel = createUserPanel(user);
		JPanel productPanel = createProductPanel(user);
		JPanel accountingPanel = createAccountingPanel(user);

		// Add panels to the tabbed pane
		tabbedPane.addTab("Users", userPanel);
		tabbedPane.addTab("Products", productPanel);
		tabbedPane.addTab("Accounting", accountingPanel);
		
		// Create Back Button
        JButton backButton = new JButton("Back");
        backButton.setIcon(new ImageIcon("path/to/back_icon.png")); // Set your icon path
        backButton.setToolTipText("Go back to Home");
        backButton.addActionListener(e -> {
            setVisible(false);
            new HomeView(user).display(); // Assuming HomeView has a display method
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
        btnUserView.setIcon(new ImageIcon("path/to/user_icon.png")); // Set your icon path
        btnUserView.setToolTipText("View user details");
        btnUserView.addActionListener(e -> {
            setVisible(false);
            new UserView(user).display();
        });
        
        JButton btnCustomerView = new JButton("Customer view");
        btnCustomerView.setIcon(new ImageIcon("path/to/customer_icon.png")); // Set your icon path
        btnCustomerView.setToolTipText("View customer details");
        btnCustomerView.addActionListener(e -> {
            setVisible(false);
            new CustomerView(user).display();
        });
        
        panel.add(btnUserView);
        panel.add(btnCustomerView);
        return panel;
    }

	private JPanel createProductPanel(User user) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JButton btnItemView = new JButton("Item view");
		btnItemView.setIcon(new ImageIcon("path/to/item_icon.png")); // Set your icon path
        btnItemView.setToolTipText("View item details");
		btnItemView.addActionListener(e -> {
			setVisible(false);
			new ItemView(user).display();
		});

		JButton btnProductDetails = new JButton("Product details");
		 btnProductDetails.setIcon(new ImageIcon("path/to/product_icon.png")); // Set your icon path
	        btnProductDetails.setToolTipText("View product details");
		btnProductDetails.addActionListener(e -> {
			setVisible(false);
			new ProductDetailsView(user).display();
		});

		panel.add(btnItemView);
		panel.add(btnProductDetails);
		return panel;
	}

	private JPanel createAccountingPanel(User user) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 1));

		JButton btnAccountingView = new JButton("Accounting view");
		btnAccountingView.setIcon(new ImageIcon("path/to/accounting_icon.png")); // Set your icon path
        btnAccountingView.setToolTipText("View accounting details");
		btnAccountingView.addActionListener(e -> {
			setVisible(false);
			new AccountingView(user).display();
		});

		JButton btnReportsView = new JButton("Reports view");
		 btnReportsView.setIcon(new ImageIcon("path/to/reports_icon.png")); // Set your icon path
	        btnReportsView.setToolTipText("View reports");
		btnReportsView.addActionListener(e -> {
			setVisible(false);
			new ReportsView(user).display();
		});

		panel.add(btnAccountingView);
		panel.add(btnReportsView);
		return panel;
	}

	public void display() {
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(600, 400));
	}
}
