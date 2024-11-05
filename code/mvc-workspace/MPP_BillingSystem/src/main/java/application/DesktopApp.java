package application;

import javax.swing.*;

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
				UserController userController = UserControllerImpl.getInstance();
				new HomeView(userController).display();
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
