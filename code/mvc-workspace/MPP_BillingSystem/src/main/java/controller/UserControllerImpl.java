package controller;

import java.util.List;

import org.hibernate.Session;

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
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = new User(firstName, lastName, password, role);
				session.persist(user);
				session.getTransaction().commit();
				return user.getId(); // Return the new user's generated ID
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
	
	@Override
	public void updateUser(int id, String newPassword, UserRole newRole) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, id);
				if (user != null) {
					user.setPassword(newPassword);
					user.setRole(newRole);
					session.persist(user);
					session.getTransaction().commit();
				} else {
					throw new IllegalArgumentException("User not found with ID: " + id);
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
	
	@Override
	public List<User> getAllUsers() {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
            try {
                List<User> users = session.createQuery("FROM User", User.class).list();
                session.getTransaction().commit();
                return users;
            } catch (Exception e) {
            	session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
            }
        }
	}

	@Override
	public User getUserById(int id) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, id);
				session.getTransaction().commit();
				return user; // Return the user if found, or null if not found
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public void removeUserById(int id) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, id);
				if (user != null) {
					session.remove(user);
				}
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public List<Integer> getAllUserIds() {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				List<Integer> userIds = session.createQuery("select u.id from User u", Integer.class).list();
				session.getTransaction().commit();
				return userIds;
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public boolean isUserManager(User user) {
		return user.getRole().equals(UserRole.MANAGER);
	}

	@Override
	public String getFirstName(User user) {
		return user.getFirstName();
	}

	@Override
	public String getLastName(User user) {
		return user.getLastName();
	}
	@Override
	public String getPassword(User user) {
		return user.getPassword();
	}

	@Override
	public UserRole getRole(User user) {
		return user.getRole();
	}
}
