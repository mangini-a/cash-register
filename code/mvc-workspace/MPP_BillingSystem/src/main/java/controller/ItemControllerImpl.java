package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;

import model.Item;
import utils.HibernateSessionFactory;

public class ItemControllerImpl implements ItemController {

	private static ItemControllerImpl singleInstance = null;

	public static ItemControllerImpl getInstance() {
		if (singleInstance == null) {
			singleInstance = new ItemControllerImpl();
		}
		return singleInstance;
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
	public void updateItem(Item item) {
		// TODO Auto-generated method stub

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
	public void recalculateItemQuantity(int itemId, int soldQuantity) {
		Item item = getItemById(itemId);
		int oldQuantity = item.getQuantity();
		int newQuantity = oldQuantity - soldQuantity;
		item.setQuantity(newQuantity);
		// updateItem(item); vedi come viene gestito l'aggiornamento nel package view
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
}
