package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.UserController;
import controller.UserControllerImpl;
import model.User;
import model.UserRole;

@SuppressWarnings("serial")
public class LoginView extends JFrame {

	private JPanel contentPane;
	private UserController userController;

	public LoginView() {
		// Setup the frame
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(620, 280, 600, 400);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Get the only instance of UserControllerImpl in order to perform operations on the database
		userController = UserControllerImpl.getInstance();
		
		JLabel lblWelcome = new JLabel("Welcome to Billing System!");
		lblWelcome.setFont(new Font("Rockwell Nova Extra Bold", Font.BOLD, 20));
		lblWelcome.setBounds(200, 15, 300, 60);
		contentPane.add(lblWelcome);

		JLabel lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUserId.setBounds(175, 115, 100, 45);
		contentPane.add(lblUserId);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPassword.setBounds(175, 145, 100, 45);
		contentPane.add(lblPassword);

		JTextField fieldUserId = new JTextField();
		fieldUserId.setBounds(250, 120, 120, 25);
		contentPane.add(fieldUserId);
		fieldUserId.setColumns(10);

		JPasswordField fieldUserPassword = new JPasswordField();
		fieldUserPassword.setBounds(250, 150, 120, 25);
		contentPane.add(fieldUserPassword);

		JCheckBox chckbxShowPassword = new JCheckBox("Show password");
		chckbxShowPassword.setFont(new Font("Tahoma", Font.BOLD, 13));
		chckbxShowPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxShowPassword.isSelected()) {
					fieldUserPassword.setEchoChar((char) 0);
				} else {
					fieldUserPassword.setEchoChar('*');
				}
			}
		});
		chckbxShowPassword.setBackground(Color.WHITE);
		chckbxShowPassword.setBounds(200, 250, 140, 25);
		contentPane.add(chckbxShowPassword);

		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int inputUserId = Integer.parseInt(fieldUserId.getText());
				String inputUserPassword = String.valueOf(fieldUserPassword.getPassword());
				boolean checkIdDb = false;

				List<Integer> userIds = userController.getAllUserIds();
				for (int userId : userIds) {
					if (userId == inputUserId) {
						checkIdDb = true;
						User user = userController.getUserById(userId);
						String userPassword = user.getPassword();
						if (userPassword.equals(inputUserPassword) && (!user.getRole().equals(UserRole.CUSTOMER))) {
							HomeView homeView = new HomeView(user.getRole());
							setVisible(false);
							homeView.repaint();
							homeView.display();
						} else {
							JOptionPane.showMessageDialog(null, "No authorization!");
						}
					}
				}
				if (!checkIdDb) {
					JOptionPane.showMessageDialog(null, "User not found!");
				}
			}
		});
		btnLogin.setBounds(140, 320, 120, 40);
		contentPane.add(btnLogin);

		JButton btnCancel = new JButton("Exit");
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				;
			}
		});
		btnCancel.setBounds(280, 320, 120, 40);
		contentPane.add(btnCancel);
	}

	public void display() {
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		setMinimumSize(new Dimension(500, 500));
	}
}
