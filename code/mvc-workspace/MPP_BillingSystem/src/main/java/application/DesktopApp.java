package application;

import javax.swing.JOptionPane;

import org.hibernate.Session;

import jakarta.persistence.TypedQuery;
import utils.HibernateSessionFactory;
import view.HomeView;

public class DesktopApp {

	public static void main(String[] args) {
		try {
			warmUpDatabase();
			// Start the application by displaying the HomeView
			new HomeView().display();
		} catch (Exception e) {
			// Log the exception and show an error message
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while starting the application: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private static void warmUpDatabase() {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			TypedQuery<Integer> query = session.createQuery("SELECT 1", Integer.class);
			query.getResultList();
		}
	}
}
