package view;

import java.awt.*;
import javax.swing.*;

import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JTabbedPane mainTabbedPane;

	public ManagementView(User user) {
		// Configure the frame
        setTitle("Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainTabbedPane = new JTabbedPane();

        // Create and add the "Stock" tab
        JTabbedPane stockTabbedPane = createStockTabbedPane(user);
        mainTabbedPane.addTab("Stock", stockTabbedPane);
        
        // Create and add the "Staff" tab
        JTabbedPane staffTabbedPane = createStaffTabbedPane(user);
        mainTabbedPane.addTab("Staff", staffTabbedPane);

        // Create and add the "Accounting" tab
        JTabbedPane accountingTabbedPane = createAccountingTabbedPane(user);
        mainTabbedPane.addTab("Accounting", accountingTabbedPane);

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
        mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        add(mainPanel);
	}
	
	private JTabbedPane createStockTabbedPane(User user) {
		JTabbedPane stockTabbedPane = new JTabbedPane();
		
		// Create and add the "Item View" tab
		JPanel itemViewPanel = new JPanel();
		itemViewPanel.add(new JLabel("Item View Panel"));
		// Add more components as needed
		stockTabbedPane.addTab("Item View", itemViewPanel);
		
		// Create and add the "Product Details" tab
		JPanel productDetailsPanel = new JPanel();
		productDetailsPanel.add(new JLabel("Product Details Panel"));
		// Add more components as needed
		stockTabbedPane.addTab("Product Details", productDetailsPanel);
		
		return stockTabbedPane;
	}
	
	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane staffTabbedPane = new JTabbedPane();

		// Create and add the "User View" tab
        JPanel userViewPanel = new JPanel();
        userViewPanel.add(new JLabel("User  View Panel"));
        // Add more components as needed
        staffTabbedPane.addTab("User  View", userViewPanel);

        // Create and add the "Customer View" tab
        JPanel customerViewPanel = new JPanel();
        customerViewPanel.add(new JLabel("Customer View Panel"));
        // Add more components as needed
        staffTabbedPane.addTab("Customer View", customerViewPanel);

        return staffTabbedPane;
	}
	
	private JTabbedPane createAccountingTabbedPane(User user) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();

		// Create and add the "Accounting View" tab
        JPanel accountingViewPanel = new JPanel();
        accountingViewPanel.add(new JLabel("Accounting View Panel"));
        // Add more components as needed
        accountingTabbedPane.addTab("Accounting View", accountingViewPanel);

        // Create and add the "Reports View" tab
        JPanel reportsViewPanel = new JPanel();
        reportsViewPanel.add(new JLabel("Reports View Panel"));
        // Add more components as needed
        accountingTabbedPane.addTab("Reports View", reportsViewPanel);

        return accountingTabbedPane;
	}

	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
