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
			    System.err.println("Error adding user: " + e.getMessage());
			    e.printStackTrace();
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
	public boolean isUserManager(int userId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, userId);
				session.getTransaction().commit();
				return user.getRole().equals(UserRole.MANAGER);
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public String getUserFirstNameById(Integer userId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, userId);
				session.getTransaction().commit();
				return user.getFirstName();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public String getUserLastNameById(Integer userId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, userId);
				session.getTransaction().commit();
				return user.getLastName();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
	
	@Override
	public String getUserPasswordById(Integer userId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, userId);
				session.getTransaction().commit();
				return user.getPassword();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public UserRole getUserRoleById(Integer userId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				User user = session.get(User.class, userId);
				session.getTransaction().commit();
				return user.getRole();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
}
