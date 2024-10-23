package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import controller.InvoiceController;
import controller.InvoiceControllerImpl;
import controller.ItemController;
import controller.ItemControllerImpl;
import controller.StockExceededException;
import model.User;

@SuppressWarnings("serial")
public class InvoiceView extends JFrame {

	private JPanel contentPane;
	
	private JTextArea cartArea;
	private String cartAreaHeader = "ID" + "\t" + "Name" + "\t" + "Quantity" + "\t" + "Unit price" + "\n";
	private JScrollPane scrollPane;
	
	// Components used in the "Item Selection" section
	private JLabel lblItemId;
	private JComboBox<Integer> comboBoxItemId;
	private JLabel lblItemQty;
	private JComboBox<Integer> comboBoxItemQty;
	private JButton btnAddToCart;
	
	// Components used in the "Checkout" section
	private JLabel lblTotalPrice;
	private JTextField textFieldTotalPrice;
	private JButton btnCheckout;
	
	// Components used in the "Other Actions" section
 	private JButton btnClearCart;
	private JButton btnPrintCart;
	
	private JButton btnBackToHome;
	
	private ItemController itemController;
	private InvoiceController invoiceController;
	
	private Set<Integer> quantityModel;

	public InvoiceView(User user) {
		// Setup the frame
		setTitle("Cash Register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		// Initialize controllers
		itemController = ItemControllerImpl.getInstance();
		invoiceController = InvoiceControllerImpl.getInstance();
		
		cartArea = new JTextArea();
		cartArea.setEditable(false);
		cartArea.setText(cartAreaHeader);
		scrollPane = new JScrollPane(cartArea);
		contentPane.add(scrollPane);
		
		// Instantiate and populate the Item Selection section's components
		lblItemId = new JLabel("Item ID:");
		comboBoxItemId = new JComboBox<>();
		lblItemQty = new JLabel("Quantity:");
		comboBoxItemQty = new JComboBox<>();
		populateComboBoxItemId(comboBoxItemId);
		updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty);
		comboBoxItemId.addActionListener(e -> updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty));
		btnAddToCart = new JButton("Add to Cart", new ImageIcon("../img/add-to-cart.png"));
		btnAddToCart.addActionListener(e -> addToCart());
		
		// Create a panel for the Item Selection section
		JPanel panelItemSelection = new JPanel();
		panelItemSelection.setBorder(new TitledBorder("Item Selection"));
		panelItemSelection.setLayout(new FlowLayout());
		panelItemSelection.add(lblItemId);
		panelItemSelection.add(comboBoxItemId);
		panelItemSelection.add(lblItemQty);
		panelItemSelection.add(comboBoxItemQty);
		panelItemSelection.add(btnAddToCart);
		contentPane.add(panelItemSelection);
		
		// Instantiate the Checkout section's components
		lblTotalPrice = new JLabel("Total price:");
		textFieldTotalPrice = new JTextField();
		textFieldTotalPrice.setEditable(false);
		btnCheckout = new JButton("Checkout");
		btnCheckout.addActionListener(e -> checkout(user));
		
		// Create a panel for the Checkout section
		JPanel panelCheckout = new JPanel();
		panelCheckout.setBorder(new TitledBorder("Checkout"));
		panelCheckout.setLayout(new FlowLayout());
		panelCheckout.add(lblTotalPrice);
		panelCheckout.add(textFieldTotalPrice);
		panelCheckout.add(btnCheckout);
		contentPane.add(panelCheckout);
		
		// Instantiate the Other Actions section's components
		btnClearCart = new JButton("Clear Cart");
		btnClearCart.addActionListener(e -> clearCart());
		btnPrintCart = new JButton("Print Cart");
		btnPrintCart.addActionListener(e -> printCart());
		
		// Create a panel for the Other Actions section
		JPanel panelOtherButtons = new JPanel();
		panelOtherButtons.setBorder(new TitledBorder("Other Actions"));
		panelOtherButtons.setLayout(new FlowLayout());
		panelOtherButtons.add(btnClearCart);
		panelOtherButtons.add(btnPrintCart);
		contentPane.add(panelOtherButtons);
		
		// Add the "Back to Home" button separately
		btnBackToHome = new JButton("Back to Home");
		btnBackToHome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				HomeView homeView = new HomeView(user);
				homeView.display();
			}
		});
		contentPane.add(btnBackToHome);
	}
	
	private void printCart() {
		try {
			cartArea.print();
		} catch (PrinterException ex) {
			JOptionPane.showMessageDialog(null, "An error in the print system caused the job to be aborted.", "Printout failed", ABORT);
		}
	}

	private void clearCart() {
		invoiceController.emptyCartLines();
		comboBoxItemId.setSelectedIndex(0);
		comboBoxItemQty.setSelectedIndex(0);
		textFieldTotalPrice.setText("");
		cartArea.setText(cartAreaHeader);
	}

	private void addToCart() {
		Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
		Integer selectedItemQty = (Integer) comboBoxItemQty.getSelectedItem();

		try {
			String cartLine = invoiceController.addCartLine(selectedItemId, selectedItemQty);
			cartArea.append(cartLine);
			
			String partialPrice = String.valueOf(invoiceController.calculatePartial(selectedItemId, selectedItemQty));
			textFieldTotalPrice.setText(partialPrice);
		} catch (StockExceededException ex) {
			JOptionPane.showMessageDialog(null, "Item's current stock stops at " + ex.getItemQty() + "!", "Required quantity exceeds quantity in stock", ERROR);
		}
	}
	
	private void checkout(User user) {
		String strTot = textFieldTotalPrice.getText();
		if (!strTot.isBlank()) {
			Double totalPrice = Double.parseDouble(strTot);
			
			// Add a new invoice to the database
			invoiceController.addInvoice(user, totalPrice);
			
			// Update the stock by decreasing the sold quantities
            invoiceController.updateInventory();
			
			// Clear the cart and reset UI
			invoiceController.emptyCartLines();
			comboBoxItemId.setSelectedIndex(0);
			comboBoxItemQty.setSelectedIndex(0);
			textFieldTotalPrice.setText("");
			cartArea.setText(cartAreaHeader);
		} else {
			JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!");
		}
	}

	private void populateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		
		// Populate the model with all the item IDs
	    for (Integer id : itemController.getAllItemIds()) {
	            model.addElement(id);
	    }
	    
	    // Set the updated model to the combo box
	    comboBoxItemId.setModel(model);
	}
	
	private void updateComboBoxItemQty(JComboBox<Integer> comboBoxItemId, JComboBox<Integer> comboBoxItemQty) {
		try {
	        Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
	        if (selectedItemId != null) {
	            quantityModel = itemController.showOneToQuantity(itemController.getItemById(selectedItemId));
	            comboBoxItemQty.setModel(new DefaultComboBoxModel<>(quantityModel.toArray(new Integer[0])));
	        } else {
	        	comboBoxItemQty.setModel(new DefaultComboBoxModel<>(new Integer[0])); // Clear if no selection
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        comboBoxItemQty.setModel(new DefaultComboBoxModel<>(new Integer[0])); // Clear on error
	    }	
	}

	public void display() {
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(500, 500));
	}
}
