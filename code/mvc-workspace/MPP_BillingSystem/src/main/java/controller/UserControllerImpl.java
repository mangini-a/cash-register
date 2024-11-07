package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import model.User;
import model.UserRole;
import utils.HibernateSessionFactory;

public class UserControllerImpl implements UserController {

	// Private constructor to prevent instantiation
	private UserControllerImpl() {}

	private static class SingletonHelper {
		private static final UserControllerImpl singleInstance = new UserControllerImpl();
	}

	public static UserControllerImpl getInstance() {
		return SingletonHelper.singleInstance;
	}

	@Override
	public int addUser(String firstName, String lastName, String password, UserRole role) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    int userId = -1; // Default value for user ID
		try {
			transaction = session.beginTransaction();
			User user = new User(firstName, lastName, password, role);
			session.persist(user); // Use persist() to make the user entity persistent
			transaction.commit(); // Commit the transaction
			
			// Retrieve the generated ID after the transaction is committed
	        userId = user.getId();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
		return userId; // Return the generated user ID
	}
	
	@Override
	public void updateUser(int id, String newPassword, UserRole newRole) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, id);
			if (user != null) {
				user.setPassword(newPassword);
				user.setRole(newRole);
				session.persist(user); // Use persist() to update the user entity
				transaction.commit(); // Commit the transaction
			} else {
				throw new IllegalArgumentException("User not found with ID: " + id);
			}
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}

	@Override
	public void removeUserById(int id) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, id);
			if (user != null) {
				session.remove(user);
			}
			transaction.commit(); // Commit the transaction
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}

	@Override
	public List<Integer> getAllUserIds() {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    List<Integer> userIds = null; // Initialize the list to hold user IDs
		try {
			transaction = session.beginTransaction();
			userIds = session.createQuery("select u.id from User u", Integer.class).list();
			transaction.commit(); // Commit the transaction
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
		return userIds; // Return the list of user IDs
	}

	@Override
	public boolean isUserManager(int userId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    boolean isManager = false; // Default value
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			if (user != null) {
				isManager = user.getRole().equals(UserRole.MANAGER);
			}
			transaction.commit(); // Commit the transaction
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
		return isManager; // Return whether the user is a manager
	}

	@Override
	public String getUserFirstNameById(Integer userId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			transaction.commit(); // Commit the transaction
			return user != null ? user.getFirstName() : null; // Handle null case
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}

	@Override
	public String getUserLastNameById(Integer userId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			transaction.commit(); // Commit the transaction
			return user != null ? user.getLastName() : null; // Handle null case
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}
	
	@Override
	public String getUserPasswordById(Integer userId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			transaction.commit(); // Commit the transaction
			return user != null ? user.getPassword() : null; // Handle null case
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}

	@Override
	public UserRole getUserRoleById(Integer userId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			transaction.commit(); // Commit the transaction
			return user != null ? user.getRole() : null; // Handle null case
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
	}
}
