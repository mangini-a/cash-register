package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
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
		
		JTextArea invoiceArea = new JTextArea();
		invoiceArea.setBackground(new Color(211, 211, 211));
		invoiceArea.setEditable(false);
		String row = "Id" + "\t" + "Product" + "\t" + "Quantity" + "\t" + "Unit price" + "\n";
		invoiceArea.setText(row);

		JScrollPane scrollPane = new JScrollPane(invoiceArea);
		scrollPane.setBounds(15, 15, 345, 420);
		contentPane.add(scrollPane);

		JLabel lblComment = new JLabel("Search for an Item ID, choose quantity and click Add to cart");
		lblComment.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblComment.setBounds(370, 84, 442, 16);
		contentPane.add(lblComment);

		JComboBox<Integer> comboBoxItemId = new JComboBox<>();
		JComboBox<Integer> comboBoxItemQty = new JComboBox<>();
		comboBoxItemId.setBounds(370, 122, 120, 23);
		comboBoxItemId.setModel(new DefaultComboBoxModel(itemController.getAllItemIds().toArray()));
		// Show quantities from 1 to the available quantity of the pre-selected item ID
		// in the related drop-down list
		quantityModel = itemController.fromOneToQuantity(
				itemController.getItemById(Integer.parseInt(comboBoxItemId.getSelectedItem().toString())));
		comboBoxItemId.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Show quantities from 1 to the available quantity of the selected item ID in
					// the related drop-down list
					quantityModel = itemController.fromOneToQuantity(
							itemController.getItemById(Integer.parseInt(comboBoxItemId.getSelectedItem().toString())));
					comboBoxItemQty.setModel(new DefaultComboBoxModel(quantityModel.toArray()));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(comboBoxItemId);

		comboBoxItemQty.setModel(new DefaultComboBoxModel(quantityModel.toArray()));
		comboBoxItemQty.setBounds(505, 122, 100, 23);
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
		
		JButton btnAdd = new JButton("Add to cart");
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int itemId = Integer.parseInt(comboBoxItemId.getSelectedItem().toString());
					int itemQty = Integer.parseInt(comboBoxItemQty.getSelectedItem().toString());

					String invoiceLine = invoiceController.addInvoiceLine(itemId, itemQty);
					invoiceArea.append(invoiceLine);

					String totalPrice = String.valueOf(invoiceController.calculatePartial(itemId, itemQty));
					textFieldTotalPrice.setText(totalPrice);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		btnAdd.setBounds(620, 122, 120, 23);
		contentPane.add(btnAdd);

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Empty the invoice lines' HashMap and reset the cart price
				invoiceController.emptyInvoiceLines();
				
				// Graphically clear the screen
				comboBoxItemId.setSelectedIndex(0);
				comboBoxItemQty.setSelectedIndex(0);
				textFieldTotalPrice.setText("");
				invoiceArea.setText(row);
			}

		});
		btnClear.setBounds(370, 193, 120, 23);
		contentPane.add(btnClear);
		
		JButton btnPrint = new JButton("Print");
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					invoiceArea.print();
				} catch (PrinterException e1) {
					JOptionPane.showMessageDialog(null, "No Printer Found");
				}
			}
		});
		btnPrint.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPrint.setBounds(505, 193, 100, 23);
		contentPane.add(btnPrint);

		JButton btnPay = new JButton("Pay");
		btnPay.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String strTot = textFieldTotalPrice.getText();
				if (!strTot.isBlank()) {
					Double tot = Double.parseDouble(strTot);
					int userId = user.getId();
					boolean payCheck = shopController.addPayment(userId, tot);
					
					// Empty the invoice lines' HashMap and reset the cart price
					invoiceController.emptyInvoiceLines();
					
					// Graphically clear the screen
					comboBoxItemId.setSelectedIndex(0);
					comboBoxItemQty.setSelectedIndex(0);
					textFieldTotalPrice.setText("");
					invoiceArea.setText(row);
					
					if (payCheck && opCheck) {
						JOptionPane.showMessageDialog(null, "Operation ended successfully!");
						comboBox_CustomerId.setSelectedIndex(0);
						comboBoxItemQty.setSelectedIndex(0);
						comboBoxItemId.setSelectedIndex(0);
						textFieldTotalPrice.setText("");
						invoiceArea.setText(row);
					} else {
						JOptionPane.showMessageDialog(null, "Something went wrong! Try again");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Start by adding some items to the cart!");
				}
			}
		});
		btnPay.setBounds(820, 270, 90, 25);
		contentPane.add(btnPay);

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

	public void display() {
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(500, 500));
	}
}
