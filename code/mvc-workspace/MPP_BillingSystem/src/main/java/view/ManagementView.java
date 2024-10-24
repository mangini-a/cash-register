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

        // Create and add the inventory tab
        JTabbedPane inventoryTabbedPane = createInventoryTabbedPane(user);
        mainTabbedPane.addTab("Inventory", inventoryTabbedPane);
        
        // Create and add the user tab
        JTabbedPane staffTabbedPane = createStaffTabbedPane(user);
        mainTabbedPane.addTab("Staff", staffTabbedPane);

        // Create and add the accounting tab
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
	
	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane userTabbedPane = new JTabbedPane();

        // User View Panel
        JPanel userViewPanel = new JPanel();
        userViewPanel.add(new JLabel("User  View Panel"));
        // Add more components as needed
        userTabbedPane.addTab("User  View", userViewPanel);

        // Customer View Panel
        JPanel customerViewPanel = new JPanel();
        customerViewPanel.add(new JLabel("Customer View Panel"));
        // Add more components as needed
        userTabbedPane.addTab("Customer View", customerViewPanel);

        return userTabbedPane;
	}
	
	private JTabbedPane createInventoryTabbedPane(User user) {
		JTabbedPane itemTabbedPane = new JTabbedPane();

        // Item View Panel
        JPanel itemViewPanel = new JPanel();
        itemViewPanel.add(new JLabel("Item View Panel"));
        // Add more components as needed
        itemTabbedPane.addTab("Item View", itemViewPanel);

        // Product Details Panel
        JPanel productDetailsPanel = new JPanel();
        productDetailsPanel.add(new JLabel("Product Details Panel"));
        // Add more components as needed
        itemTabbedPane.addTab("Product Details", productDetailsPanel);

        return itemTabbedPane;
	}

	private JTabbedPane createAccountingTabbedPane(User user) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();

        // Accounting View Panel
        JPanel accountingViewPanel = new JPanel();
        accountingViewPanel.add(new JLabel("Accounting View Panel"));
        // Add more components as needed
        accountingTabbedPane.addTab("Accounting View", accountingViewPanel);

        // Reports View Panel
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
