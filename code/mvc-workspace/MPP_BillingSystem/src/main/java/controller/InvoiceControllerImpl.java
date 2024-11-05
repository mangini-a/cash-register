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

	private double cartPrice = 0.0;

	@Override
	public void addCartLine(Integer itemId, Integer itemQty) throws StockExceededException {
		
		Integer localQty = 0;
		
		// Check if the key passed as a parameter already exists
		if (cartLines.containsKey(itemId)) {
			Integer oldQty = cartLines.get(itemId);
			localQty = oldQty + itemQty;
			if (localQty > itemController.getItemQuantityById(itemId)) {
				throw new StockExceededException(itemController.getItemQuantityById(itemId));
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
	public void addInvoice(int userId, double totalPrice) {
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
		for (Integer itemId : cartLines.keySet()) {
			itemController.updateItemQuantityById(itemId, itemController.getItemQuantityById(itemId) - cartLines.get(itemId));
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
