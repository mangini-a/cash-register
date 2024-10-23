package application;

import javax.swing.JOptionPane;

import view.HomeView;

public class DesktopApp {

	public static void main(String[] args) {
		try {
			// Start the application by displaying the HomeView
			new HomeView().display();
		} catch (Exception e) {
			// Log the exception and show an error message
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while starting the application: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
