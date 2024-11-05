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
	public void addItem(String name, int quantity, double unitPrice, ItemCategory category) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = new Item(name, quantity, unitPrice, category);
				session.persist(item);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
	
	@Override
	public void updateItem(int id, String newName, int newQuantity, double newUnitPrice, ItemCategory newCategory) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, id);
				if (item != null) {
					item.setName(newName);
					item.setQuantity(newQuantity);
					item.setUnitPrice(newUnitPrice);
					item.setCategory(newCategory);
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
	public void updateItemQuantityById(Integer itemId, int newQuantity) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, itemId);
				if (item != null) {
					item.setQuantity(newQuantity);
					session.persist(item);
					session.getTransaction().commit();
				} else {
					throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	public Set<Integer> showOneToQuantity(Integer itemId) {
		Set<Integer> quantityScale = new HashSet<>();
		int quantity = getItemQuantityById(itemId);
		for (int i = 1; i <= quantity; i++) {
			quantityScale.add(i);
		}
		return quantityScale;
	}
	
	@Override
	public String getItemNameById(Integer itemId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, itemId);
				session.getTransaction().commit();
				return item.getName();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public int getItemQuantityById(Integer itemId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, itemId);
				session.getTransaction().commit();
				return item.getQuantity();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public double getItemUnitPriceById(Integer itemId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, itemId);
				session.getTransaction().commit();
				return item.getUnitPrice();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public ItemCategory getItemCategoryById(Integer itemId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Item item = session.get(Item.class, itemId);
				session.getTransaction().commit();
				return item.getCategory();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
}
