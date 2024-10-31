package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JTabbedPane mainTabbedPane;
	private JPanel mainPanel;
	
	private JPanel btnBackToHomePanel;
	private JButton btnBackToHome;

	public ManagementView(User user) {
		// Configure the frame
		setTitle("Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the main tabbed pane, to which three tabs are added
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Stock", createStockTabbedPane(user));
		mainTabbedPane.addTab("Staff", createStaffTabbedPane(user));
		mainTabbedPane.addTab("Accounting", createAccountingTabbedPane(user)); 

		// Add the "Back to Home" button
		btnBackToHomePanel = new JPanel();
		btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setToolTipText("Go back to the home page");
		btnBackToHome.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(user).display();
			});
		});
		btnBackToHomePanel.add(btnBackToHome);
		btnBackToHomePanel.setBorder(new EmptyBorder(0, 15, 15, 15));

		// Set up the content pane with the BorderLayout
		BorderLayout layout = new BorderLayout();
		layout.setVgap(10); // Add a 10px vertical gap between components
				
		mainPanel = new JPanel(layout);
		mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
		mainPanel.add(btnBackToHomePanel, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private JTabbedPane createStockTabbedPane(User user) {
		JTabbedPane stockTabbedPane = new JTabbedPane();
		stockTabbedPane.addTab("Add a new item", createAddItemPanel(user));
		stockTabbedPane.addTab("Modify an existing item's details", createModifyItemPanel(user));
		return stockTabbedPane;
	}
	
	private JPanel createAddItemPanel(User user) {
		JPanel addItemPanel = new AddItemPanel();
		addItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		return addItemPanel;
	}
	
	private JPanel createModifyItemPanel(User user) {
		JPanel modifyItemPanel = new ModifyItemPanel(); 
        modifyItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
        return modifyItemPanel;
	}

	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane staffTabbedPane = new JTabbedPane();
		staffTabbedPane.addTab("Add a new user", createAddUserPanel());
		staffTabbedPane.addTab("Modify an existing user's credentials", createModifyUserPanel());
		return staffTabbedPane;
	}

	private JPanel createAddUserPanel() {
		return new AddUserPanel();
	}
	
	private JPanel createModifyUserPanel() {
		return new ModifyUserPanel();
	}

	private JTabbedPane createAccountingTabbedPane(User user) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();
		// ...
		return accountingTabbedPane;
	}

	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
