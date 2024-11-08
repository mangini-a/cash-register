package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.User;
import model.UserRole;
import utils.HibernateSessionFactory;

public class UserControllerImpl implements UserController {
	
	private static UserControllerImpl instance;
	private SessionFactory sessionFactory;

	// Private constructor to prevent instantiation
	private UserControllerImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	// Static method to get the singleton instance
	public static UserControllerImpl getInstance() {
		if (instance == null) {
			synchronized (UserControllerImpl.class) {
				if (instance == null) {
					// Initialize with the SessionFactory from HibernateSessionFactory
					instance = new UserControllerImpl(HibernateSessionFactory.getSessionFactory());
				}
			}
		}
		return instance;
	}
	
	// Static method to allow for a different SessionFactory (pointing to an in-memory database) in tests
    public static void setTestSessionFactory(SessionFactory testSessionFactory) {
    	synchronized (UserControllerImpl.class) {
    		instance = new UserControllerImpl(testSessionFactory);
    	}
    }

	@Override
	public int addUser(String firstName, String lastName, String password, UserRole role) {
		Session session = sessionFactory.openSession();
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
	public void updateUser(Integer userId, String newPassword, UserRole newRole) {
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
			if (user != null) {
				user.setPassword(newPassword);
				user.setRole(newRole);
				session.persist(user); // Use persist() to update the user entity
				transaction.commit(); // Commit the transaction
			} else {
				throw new IllegalArgumentException("User not found with ID: " + userId);
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
	public void removeUserById(Integer userId) {
		Session session = sessionFactory.openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			User user = session.get(User.class, userId);
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
		Session session = sessionFactory.openSession();
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
	public boolean isUserManager(Integer userId) {
		Session session = sessionFactory.openSession();
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
		Session session = sessionFactory.openSession();
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
		Session session = sessionFactory.openSession();
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
		Session session = sessionFactory.openSession();
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
		Session session = sessionFactory.openSession();
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
