package controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Item item = new Item(name, quantity, unitPrice, category);
			session.persist(item); // Persist the new item
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
	public void updateItem(Integer itemId, String newName, int newQuantity, double newUnitPrice, ItemCategory newCategory) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				item.setName(newName);
				item.setQuantity(newQuantity);
				item.setUnitPrice(newUnitPrice);
				item.setCategory(newCategory);
				session.persist(item); // Persist the existing item
				transaction.commit(); // Commit the transaction
			} else {
				throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	public void updateItemQuantityById(Integer itemId, int newQuantity) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				item.setQuantity(newQuantity);
				session.persist(item); // Persist the existing item
				transaction.commit(); // Commit the transaction
			} else {
				throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	public void removeItemById(Integer itemId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				session.remove(item); // Remove the item if it exists
				transaction.commit(); // Commit the transaction
			} else {
	            throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	public List<Integer> getAllItemIds() {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    List<Integer> itemIds = null; // Initialize the list to hold item IDs
		try {
			transaction = session.beginTransaction();
			itemIds = session.createQuery("select i.id from Item i", Integer.class).list();
			transaction.commit(); // Commit the transaction
		} catch (Exception e) {
			if (transaction != null) {
	            transaction.rollback(); // Roll back only if the transaction is active
	        }
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
	        session.close(); // Ensure the session is closed
	    }
	    return itemIds; // Return the list of item IDs
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
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    String itemName = null; // Initialize the variable to hold the item name
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				itemName = item.getName(); // Get the item name
			} else {
	            throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	    return itemName; // Return the item name
	}

	@Override
	public int getItemQuantityById(Integer itemId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    int itemQuantity = 0; // Initialize the variable to hold the item quantity
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				itemQuantity = item.getQuantity(); // Get the item quantity
			} else {
				throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
	    return itemQuantity; // Return the item quantity
	}

	@Override
	public double getItemUnitPriceById(Integer itemId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    double itemUnitPrice = 0.0; // Initialize the variable to hold the item unit price
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				itemUnitPrice = item.getUnitPrice(); // Get the item unit price
			} else {
	            throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
		return itemUnitPrice; // Return the item unit price
	}

	@Override
	public ItemCategory getItemCategoryById(Integer itemId) {
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Transaction transaction = null;
	    ItemCategory itemCategory = null; // Initialize the variable to hold the item category
		try {
			transaction = session.beginTransaction();
			Item item = session.get(Item.class, itemId);
			if (item != null) {
				itemCategory = item.getCategory(); // Get the item category
			} else {
				throw new IllegalArgumentException("Item not found with ID: " + itemId);
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
		return itemCategory; // Return the item category
	}
}
