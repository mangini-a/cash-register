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
	public void addUser(User user) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				session.persist(user);
				session.getTransaction().commit();
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
				return user; // Returns the user if found, or null if not found
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public void updateUser(User user) {
		// TODO Auto-generated method stub

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
	public String getPassword(User user) {
		return user.getPassword();
	}
}
