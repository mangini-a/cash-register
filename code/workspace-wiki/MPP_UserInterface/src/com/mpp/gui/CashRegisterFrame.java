package com.mpp.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class CashRegisterFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldItem;
	private JTextField textFieldPrice;
	private JTextField textFieldTotal;

	/**
	 * Create the frame.
	 */
	public CashRegisterFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		// Da qui in poi, un esempio elementare
		contentPane.setLayout(null);

		JLabel lblItem = new JLabel("Item:");
		lblItem.setBounds(10, 11, 46, 14);
		contentPane.add(lblItem);

		textFieldItem = new JTextField();
		textFieldItem.setBounds(66, 8, 150, 20);
		contentPane.add(textFieldItem);
		textFieldItem.setColumns(10);

		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(10, 42, 46, 14);
		contentPane.add(lblPrice);

		textFieldPrice = new JTextField();
		textFieldPrice.setBounds(66, 39, 150, 20);
		contentPane.add(textFieldPrice);
		textFieldPrice.setColumns(10);

		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addItem();
			}
		});
		btnAddItem.setBounds(226, 7, 89, 23);
		contentPane.add(btnAddItem);

		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(10, 73, 46, 14);
		contentPane.add(lblTotal);

		textFieldTotal = new JTextField();
		textFieldTotal.setEditable(false);
		textFieldTotal.setBounds(66, 70, 150, 20);
		contentPane.add(textFieldTotal);
		textFieldTotal.setColumns(10);
	}

	private void addItem() {
		String item = textFieldItem.getText();
		double price = Double.parseDouble(textFieldPrice.getText());
		// Add logic to update total and display items
		textFieldTotal.setText(String.valueOf(price)); // Example: Update total with the price of the item
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CashRegisterFrame frame = new CashRegisterFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
