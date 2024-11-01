package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame implements PanelChangeListener {

	private JTabbedPane mainTabbedPane;
	private JPanel mainPanel;
	
	// Components of the first tab (Stock)
	private AddItemPanel addItemPanel;
    private ModifyItemPanel modifyItemPanel;
    
    // Components of the second tab (Staff)
    private AddUserPanel addUserPanel;
    private ModifyUserPanel modifyUserPanel;
	
	private JPanel btnBackToHomePanel;
	private JButton btnBackToHome;

	public ManagementView(User user) {
		// Configure the frame
		setTitle("Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the main tabbed pane, to which three tabs are added
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Stock", createStockTabbedPane());
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

	private JTabbedPane createStockTabbedPane() {
		JTabbedPane stockTabbedPane = new JTabbedPane();
		addItemPanel = new AddItemPanel(this);
		addItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		stockTabbedPane.addTab("Add a new item", addItemPanel);
		modifyItemPanel = new ModifyItemPanel(this); 
		modifyItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		stockTabbedPane.addTab("Modify an existing item's details", modifyItemPanel);
		return stockTabbedPane;
	}

	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane staffTabbedPane = new JTabbedPane();
		addUserPanel = new AddUserPanel(this);
		addUserPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		staffTabbedPane.addTab("Add a new user", addUserPanel);
		modifyUserPanel = new ModifyUserPanel(this);
		modifyUserPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		staffTabbedPane.addTab("Modify an existing user's credentials", modifyUserPanel);
		return staffTabbedPane;
	}

	private JTabbedPane createAccountingTabbedPane(User user) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();
		// ...
		return accountingTabbedPane;
	}

	@Override
	public void onItemChanged() {
		// Refresh both panels when an item is added, updated, or removed
        addItemPanel.populateItemTable();
        modifyItemPanel.populateItemTable();
        modifyItemPanel.populateComboBoxItemId(modifyItemPanel.getComboBoxItemId());
	}
	
	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
