package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import model.Item;
import model.ItemCategory;
import utils.HibernateSessionFactory;

public class ItemControllerImpl implements ItemController {

	// Private constructor to prevent instantiation
	private ItemControllerImpl() {}

	private static class SingletonHelper {
		private static final ItemControllerImpl singleInstance = new ItemControllerImpl();
	}

	public static ItemControllerImpl getInstance() {
		return SingletonHelper.singleInstance;
	}

	@Override
	public void addItem(Item item) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				session.persist(item);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public Item getItemById(int id) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, id);
				session.getTransaction().commit();
				return item; // Returns the item if found, or null if not found
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public void updateItemQuantityById(int id, int newQuantity) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, id);
				if (item != null) {
					item.setQuantity(newQuantity);
					session.persist(item);
					session.getTransaction().commit();
				} else {
					throw new IllegalArgumentException("Item not found with ID: " + id);
				}
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public void removeItemById(int id) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, id);
				if (item != null) {
					session.remove(item);
				}
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public List<Integer> getAllItemIds() {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				List<Integer> itemIds = session.createQuery("select i.id from Item i", Integer.class).list();
				session.getTransaction().commit();
				return itemIds;
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public Set<Integer> showOneToQuantity(Item item) {
		Set<Integer> qtys = new HashSet<>();
		int quantity = item.getQuantity();
		for (int i = 1; i <= quantity; i++) {
			qtys.add(i);
		}
		return qtys;
	}

	@Override
	public String getItemName(Item selectedItem) {
		return selectedItem.getName();
	}

	@Override
	public int getItemQuantity(Item selectedItem) {
		return selectedItem.getQuantity();
	}

	@Override
	public double getItemUnitPrice(Item selectedItem) {
		return selectedItem.getUnitPrice();
	}

	@Override
	public ItemCategory getItemCategory(Item selectedItem) {
		return selectedItem.getCategory();
	}
}
