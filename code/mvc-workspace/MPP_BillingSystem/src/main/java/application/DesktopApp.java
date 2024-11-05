package application;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.hibernate.Session;

import com.formdev.flatlaf.FlatIntelliJLaf;

import controller.UserController;
import controller.UserControllerImpl;
import jakarta.persistence.TypedQuery;
import utils.HibernateSessionFactory;
import view.HomeView;

public class DesktopApp {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatIntelliJLaf());
			warmUpDatabase();
			SwingUtilities.invokeLater(() -> {
				UserController userController = UserControllerImpl.getInstance(); // Get the singleton instance
				new HomeView(userController).display(); // Pass it to HomeView
			});
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred while starting the application: " + e.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
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
