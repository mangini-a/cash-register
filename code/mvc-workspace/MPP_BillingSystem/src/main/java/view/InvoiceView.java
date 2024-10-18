package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.InvoiceController;
import controller.InvoiceControllerImpl;
import controller.ItemController;
import controller.ItemControllerImpl;
import controller.UserController;
import controller.UserControllerImpl;
import model.User;
import model.UserRole;

@SuppressWarnings("serial")
public class InvoiceView extends JFrame {

	private JPanel contentPane;

	private ItemController itemController;
	private InvoiceController invoiceController;

	private Set<Integer> quantityModel;
	private Set<Integer> selectedItemIds = new HashSet<>();

	public InvoiceView(User user) {
		// Setup the frame
		setTitle("Cash register");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(620, 280, 1300, 520);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Get the only instance of ItemController to perform item-related operations on the DB
		itemController = ItemControllerImpl.getInstance();

		// Get the only instance of InvoiceController to perform invoice-related operations
		invoiceController = InvoiceControllerImpl.getInstance();
		
		JTextArea cartArea = new JTextArea();
		cartArea.setBackground(new Color(211, 211, 211));
		cartArea.setEditable(false);
		String row = "Id" + "\t" + "Product" + "\t" + "Quantity" + "\t" + "Unit price" + "\n";
		cartArea.setText(row);

		JScrollPane scrollPane = new JScrollPane(cartArea);
		scrollPane.setBounds(15, 15, 345, 420);
		contentPane.add(scrollPane);

		JLabel lblComment = new JLabel("Search for an Item ID, choose a quantity and click Add to cart");
		lblComment.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblComment.setBounds(370, 84, 442, 16);
		contentPane.add(lblComment);

		// Initialize JComboBoxes
		JComboBox<Integer> comboBoxItemId = new JComboBox<>();
		JComboBox<Integer> comboBoxItemQty = new JComboBox<>();
		comboBoxItemId.setBounds(370, 122, 120, 23);
		comboBoxItemQty.setBounds(505, 122, 100, 23);
		
		// Populate comboBoxItemId
		updateComboBoxItemId(comboBoxItemId);
		
		// Add action listener for item ID selection
		comboBoxItemId.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty);
			}
		});
		
		// Initialize comboBoxItemQty with available quantities for the first item
		updateComboBoxItemQty(comboBoxItemId, comboBoxItemQty);
		
		// Add JComboBoxes to the content pane
		contentPane.add(comboBoxItemId);
		contentPane.add(comboBoxItemQty);
		
		JLabel lblTotalPrice = new JLabel("Total price:");
		lblTotalPrice.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTotalPrice.setBounds(820, 200, 90, 25);
		contentPane.add(lblTotalPrice);

		JTextField textFieldTotalPrice = new JTextField();
		textFieldTotalPrice.setEditable(false);
		textFieldTotalPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
		textFieldTotalPrice.setColumns(10);
		textFieldTotalPrice.setBounds(820, 230, 90, 25);
		contentPane.add(textFieldTotalPrice);
		
		// Initialize the "Add to cart" button
		JButton btnAdd = new JButton("Add to cart", new ImageIcon("../img/add-to-cart.png"));
		btnAdd.setBounds(620, 122, 120, 23);
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		// Add action listener for the "Add to cart" button
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					Integer selectedItemId = (Integer) comboBoxItemId.getSelectedItem();
					Integer selectedItemQty = (Integer) comboBoxItemQty.getSelectedItem();

					String cartLine = invoiceController.addCartLine(selectedItemId, selectedItemQty);
					cartArea.append(cartLine);

					String partialPrice = String.valueOf(invoiceController.calculatePartial(selectedItemId, selectedItemQty));
					textFieldTotalPrice.setText(partialPrice);
					
					// Add the selected item ID to the set of selected IDs
		            selectedItemIds.add(selectedItemId);

		            // Update the ComboBox to disable the selected item ID
		            updateComboBoxItemId(comboBoxItemId);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		
		// Add the "Add to cart" button to the content pane
		contentPane.add(btnAdd);

		// Initialize the "Clear cart" button
		JButton btnClear = new JButton("Clear cart");
		btnClear.setBounds(370, 193, 120, 23);
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		// Add action listener for the "Clear cart" button
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Empty the cart lines' HashMap and reset the cart price
				invoiceController.emptyCartLines();
				
				// Clear the selected item IDs
		        selectedItemIds.clear();
				
				// Graphically clear the screen
				comboBoxItemId.setSelectedIndex(0);
				comboBoxItemQty.setSelectedIndex(0);
				textFieldTotalPrice.setText("");
				cartArea.setText(row);
				
				// Update the ComboBox to show all available item IDs
		        updateComboBoxItemId(comboBoxItemId);
			}
		});
		
		// Add the "Clear cart" button to the content pane
		contentPane.add(btnClear);
		
		// Initialize the "Print cart" button
		JButton btnPrint = new JButton("Print cart");
		btnPrint.setBounds(505, 193, 100, 23);
		btnPrint.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		// Add action listener for the "Print cart" button
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					cartArea.print();
				} catch (PrinterException e1) {
					JOptionPane.showMessageDialog(null, "No Printer Found");
				}
			}
		});
		
		// Add the "Print cart" button to the content pane
		contentPane.add(btnPrint);

		// Initialize the "Checkout" button
		JButton btnCheckout = new JButton("Checkout");
		btnCheckout.setBounds(820, 270, 90, 25);
		btnCheckout.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		// Add action listener for the "Checkout" button
		btnCheckout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String strTot = textFieldTotalPrice.getText();
				if (!strTot.isBlank()) {
					Double tot = Double.parseDouble(strTot);
					int userId = user.getId();
					//boolean payCheck = shopController.addPayment(userId, tot);
					
					// Update inventory here
		            //invoiceController.updateInventory(); // Implement this method to decrease quantities
					
					// Clear the cart and reset UI
					invoiceController.emptyCartLines();
					comboBoxItemId.setSelectedIndex(0);
					comboBoxItemQty.setSelectedIndex(0);
					textFieldTotalPrice.setText("");
					cartArea.setText(row);
					/*
					if (payCheck) {
						JOptionPane.showMessageDialog(null, "Operation ended successfully!");
					} else {
						JOptionPane.showMessageDialog(null, "Something went wrong! Try again");
					}*/
				} else {
					JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!");
				}
			}
		});
		
		// Add the "Checkout" button to the content pane
		contentPane.add(btnCheckout);

		JButton btnBack = new JButton("Back");
		btnBack.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				HomeView homeView = new HomeView(user);
				homeView.display();
			}
		});
		btnBack.setBounds(820, 400, 90, 25);
		contentPane.add(btnBack);
	}
	
	private void updateComboBoxItemId(JComboBox<Integer> comboBoxItemId) {
		DefaultComboBoxModel<Integer> model = new DefaultComboBoxModel<>();
		
		// Populate the model with item IDs that have not been selected
	    for (Integer id : itemController.getAllItemIds()) {
	        if (!selectedItemIds.contains(id)) {
	            model.addElement(id);
	        }
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
