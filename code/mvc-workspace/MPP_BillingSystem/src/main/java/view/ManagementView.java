package view;

import java.awt.*;
import javax.swing.*;
import model.User;

@SuppressWarnings("serial")
public class ManagementView extends JFrame {

	private JPanel mainPanel;
	private JTabbedPane mainTabbedPane;
	private JButton backButton;

	public ManagementView(User user) {
		// Configure the frame
		setTitle("Management");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create the main tabbed pane
		mainTabbedPane = new JTabbedPane();
		mainTabbedPane.addTab("Stock", createStockTabbedPane(user));
		mainTabbedPane.addTab("Staff", createStaffTabbedPane(user));
		mainTabbedPane.addTab("Accounting", createAccountingTabbedPane(user));

		// Create the "Back to Home" button
		backButton = new JButton("Back to Home");
		backButton.setToolTipText("Go back to the home page");
		backButton.addActionListener(e -> {
			dispose();
			SwingUtilities.invokeLater(() -> {
				new HomeView(user).display();
			});
		});

		// Layout setup
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
		mainPanel.add(backButton, BorderLayout.SOUTH);

		add(mainPanel);
	}

	private JTabbedPane createStockTabbedPane(User user) {
		JTabbedPane stockTabbedPane = new JTabbedPane();
		stockTabbedPane.addTab("Add a new item", createAddItemPanel(user));
		stockTabbedPane.addTab("Modify an existing item", createModifyItemPanel(user));
		return stockTabbedPane;
	}
	
	private JPanel createAddItemPanel(User user) {
		JPanel addItemPanel = new AddItemPanel();
		return addItemPanel;
	}
	
	private JPanel createModifyItemPanel(User user) {
		JPanel modifyItemPanel = new ModifyItemPanel();
        return modifyItemPanel;
	}

	private JTabbedPane createStaffTabbedPane(User user) {
		JTabbedPane staffTabbedPane = new JTabbedPane();
		// ...
		return staffTabbedPane;
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
