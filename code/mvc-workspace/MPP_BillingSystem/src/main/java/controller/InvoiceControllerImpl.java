package controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;

import model.Invoice;
import model.Item;
import utils.HibernateSessionFactory;

public class InvoiceControllerImpl implements InvoiceController {
	
	// Private constructor to prevent instantiation
	private InvoiceControllerImpl() {}
	
	private static class SingletonHelper {
		private static final InvoiceControllerImpl singleInstance = new InvoiceControllerImpl();
	}

	public static InvoiceControllerImpl getInstance() {
		return SingletonHelper.singleInstance;
	}

	// Get the only instance of ItemController to perform item-related operations on the DB
	private ItemController itemController = ItemControllerImpl.getInstance();

	// Create a Map to store item IDs and their selected quantity
	private Map<Integer, Integer> cartLines = new HashMap<>();

	private double cartPrice = 0.0;

	@Override
	public void addCartLine(Integer itemId, Integer itemQty) throws StockExceededException {
		
		Item item = itemController.getItemById(itemId);
		Integer localQty = 0;
		
		// Check if the key passed as a parameter already exists
		if (cartLines.containsKey(itemId)) {
			Integer oldQty = cartLines.get(itemId);
			localQty = oldQty + itemQty;
			if (localQty > item.getQuantity()) {
				throw new StockExceededException(item.getQuantity());
			} else {
				cartLines.put(itemId, localQty);
			}
		} else {
			localQty = itemQty;
			cartLines.put(itemId, localQty);
		}
	}
	
	@Override
	public String getItemNameById(Integer itemId) {
		Item item = itemController.getItemById(itemId);
		return item.getName();
	}
	
	@Override
	public double getItemUnitPriceById(Integer itemId) {
		Item item = itemController.getItemById(itemId);
		return item.getUnitPrice();
	}

	@Override
	public double calculatePartial(Integer itemId, Integer itemQty) {
		Item item = itemController.getItemById(itemId);
		cartPrice += item.getUnitPrice() * itemQty;
		return cartPrice;
	}

	@Override
	public void emptyCartLines() {
		cartLines.clear();
		cartPrice = 0.0;
	}

	@Override
	public void addInvoice(int userId, Double totalPrice) {
		
		Invoice invoice = new Invoice(userId, Instant.now(), totalPrice);
		
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				session.persist(invoice);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
		
	}

	@Override
	public void updateInventory() {
		for (Integer itemId : cartLines.keySet()) {
			Item item = itemController.getItemById(itemId);
			itemController.updateItemQuantityById(item.getId(), item.getQuantity() - cartLines.get(itemId));
		}
	}

	public Map<Integer, Integer> getCartLines() {
		return cartLines;
	}
}
