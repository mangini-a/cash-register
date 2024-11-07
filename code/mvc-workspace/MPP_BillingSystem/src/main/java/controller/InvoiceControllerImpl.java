package controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import model.Invoice;
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

	// Set the initial cart price
	private double cartPrice = 0.0;

	@Override
	public void addCartLine(Integer itemId, Integer itemQty) throws StockExceededException {
		// Local variable used to process the selected quantity of an item
		Integer localQty = 0;
		
		// Check whether the selected item id was previously chosen
		if (cartLines.containsKey(itemId)) {
			Integer oldQty = cartLines.get(itemId); // Retrieve the quantity previously added to the cart
			localQty = oldQty + itemQty; // Sum the quantity selected at this time
			Integer availableQty = itemController.getItemQuantityById(itemId); // Assess the current stock
			if (localQty > availableQty) {
				throw new StockExceededException(availableQty);
			} else {
				cartLines.put(itemId, localQty);
			}
		} else {
			localQty = itemQty;
			cartLines.put(itemId, localQty);
		}
	}

	@Override
	public double calculatePartial(Integer itemId, Integer itemQty) {
		cartPrice += itemController.getItemUnitPriceById(itemId) * itemQty;
		return cartPrice;
	}

	@Override
	public void emptyCartLines() {
		cartLines.clear();
		cartPrice = 0.0;
	}

	@Override
	public void addInvoice(Integer userId, double totalPrice) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Invoice invoice = new Invoice(userId, Instant.now(), totalPrice);
				session.persist(invoice);
				session.getTransaction().commit();
			} catch (Exception e) {
				session.getTransaction().rollback();
				System.err.println("Error adding invoice: " + e.getMessage());
			    e.printStackTrace();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public void updateInventory() {
		// For each purchased item
		for (Integer itemId : cartLines.keySet()) {
			// Retrieve the previously available quantity
			Integer previousQty = itemController.getItemQuantityById(itemId);
			
			// Get the amount that was sold
			Integer soldQty = cartLines.get(itemId);
			
			// Update the stock with the quantity now on hand
			itemController.updateItemQuantityById(itemId, previousQty - soldQty);
		}
	}

	@Override
	public List<Integer> getAllInvoiceIds() {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				List<Integer> invoiceIds = session.createQuery("select i.id from Invoice i", Integer.class).list();
				session.getTransaction().commit();
				return invoiceIds;
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public Instant getInvoiceIssueInstantById(Integer invoiceId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Invoice invoice = session.get(Invoice.class, invoiceId);
				session.getTransaction().commit();
				return invoice.getIssueInstant();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public double getInvoiceTotalPriceById(Integer invoiceId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Invoice invoice = session.get(Invoice.class, invoiceId);
				session.getTransaction().commit();
				return invoice.getTotalPrice();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}

	@Override
	public int getInvoiceOperatorById(Integer invoiceId) {
		try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
			session.beginTransaction();
			try {
				Invoice invoice = session.get(Invoice.class, invoiceId);
				session.getTransaction().commit();
				return invoice.getOperator();
			} catch (Exception e) {
				session.getTransaction().rollback();
				throw e; // Re-throw the exception to propagate it up the call stack
			}
		}
	}
}
