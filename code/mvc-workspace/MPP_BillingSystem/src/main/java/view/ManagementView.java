package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.InvoiceController;
import controller.InvoiceControllerImpl;
import controller.ItemController;
import controller.ItemControllerImpl;
import controller.UserController;
import view.listeners.PanelChangeListener;

@SuppressWarnings("serial")
class ManagementView extends JFrame implements PanelChangeListener {

	private JTabbedPane mainTabbedPane;
	private JPanel mainPanel;
	
	// Components of the first tab (Stock)
	private AddItemPanel addItemPanel;
    private ModifyItemPanel modifyItemPanel;
    
    // Components of the second tab (Staff)
    private AddUserPanel addUserPanel;
    private ModifyUserPanel modifyUserPanel;
    
    // Components of the third tab (Accounting)
    private ViewTransactionsPanel viewTransactionsPanel;
    private ViewEarningsPanel viewEarningsPanel;
	
    // Components of the bottom section
	private JPanel btnBackToHomePanel;
	private JButton btnBackToHome;
	
	ManagementView(UserController userController, int userId) {
		// Configure the frame
		setTitle("Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Get the singleton instances of the controllers used in the first and last tab
		ItemController itemController = ItemControllerImpl.getInstance();
		InvoiceController invoiceController = InvoiceControllerImpl.getInstance();
		
		// Create the main tabbed pane, to which three tabs are added
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Stock", createStockTabbedPane(itemController));
		mainTabbedPane.addTab("Staff", createStaffTabbedPane(userController, invoiceController, userId));
		mainTabbedPane.addTab("Accounting", createAccountingTabbedPane(userController, invoiceController, userId)); 

		// Add the "Back to Home" button
		btnBackToHomePanel = new JPanel();
		btnBackToHome = new JButton("Back to Home");
		btnBackToHome.setToolTipText("Go back to the home page");
		btnBackToHome.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(userController, userId).display();
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

	private JTabbedPane createStockTabbedPane(ItemController itemController) {
		JTabbedPane stockTabbedPane = new JTabbedPane();
		addItemPanel = new AddItemPanel(this, itemController);
		addItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		stockTabbedPane.addTab("Add a new item", addItemPanel);
		modifyItemPanel = new ModifyItemPanel(this, itemController); 
		modifyItemPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		stockTabbedPane.addTab("Modify an existing item's details", modifyItemPanel);
		return stockTabbedPane;
	}

	private JTabbedPane createStaffTabbedPane(UserController userController, InvoiceController invoiceController, int userId) {
		JTabbedPane staffTabbedPane = new JTabbedPane();
		addUserPanel = new AddUserPanel(this, userId, userController);
		addUserPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		staffTabbedPane.addTab("Add a new user", addUserPanel);
		modifyUserPanel = new ModifyUserPanel(this, userId, userController, invoiceController);
		modifyUserPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		staffTabbedPane.addTab("Modify an existing user's credentials", modifyUserPanel);
		return staffTabbedPane;
	}

	private JTabbedPane createAccountingTabbedPane(UserController userController, InvoiceController invoiceController, int userId) {
		JTabbedPane accountingTabbedPane = new JTabbedPane();
		viewTransactionsPanel = new ViewTransactionsPanel(userId, userController, invoiceController);
		viewTransactionsPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		accountingTabbedPane.addTab("View the past transactions list", viewTransactionsPanel);
		viewEarningsPanel = new ViewEarningsPanel(invoiceController);
		viewEarningsPanel.setBorder(new EmptyBorder(15, 15, 0, 15));
		accountingTabbedPane.addTab("Quantify the day-to-day profit", viewEarningsPanel);
		return accountingTabbedPane;
	}

	@Override
	public void onItemChanged() {
		// Refresh both panels when an item is added, updated, or removed
        addItemPanel.populateItemTable();
        modifyItemPanel.populateItemTable();
        modifyItemPanel.populateComboBoxItemId(modifyItemPanel.getComboBoxItemId());
	}
	
	@Override
	public void onUserChanged() {
		// Refresh both panels when a user is added, updated, or removed
        addUserPanel.populateUserTable();
        modifyUserPanel.populateUserTable();
        modifyUserPanel.populateComboBoxUserId(modifyUserPanel.getComboBoxUserId());
        
        // Refresh the Accounting tab's panels when a user is removed (it could have issued receipts)
        viewTransactionsPanel.populateInvoiceTable();
        viewEarningsPanel.displayProfitGraph(); // Update the graph with new data
	}
	
	public void display() {
		setMinimumSize(new Dimension(800, 600));
		setResizable(true);
		setVisible(true);
		setLocationRelativeTo(null);
	}
}
