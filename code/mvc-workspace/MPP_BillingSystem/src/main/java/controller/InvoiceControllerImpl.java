package controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Invoice;
import utils.HibernateSessionFactory;

public class InvoiceControllerImpl implements InvoiceController {

	private static InvoiceControllerImpl instance;
	private SessionFactory sessionFactory;

	// Get the only instance of ItemController to perform item-related operations on the DB
	private ItemController itemController;

	// Private constructor to prevent instantiation
	private InvoiceControllerImpl(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		this.itemController = ItemControllerImpl.getInstance();
	}

	// Static method to get the singleton instance
	public static InvoiceControllerImpl getInstance() {
		if (instance == null) {
			synchronized (InvoiceControllerImpl.class) {
				if (instance == null) {
					// Initialize with the SessionFactory from HibernateSessionFactory
					instance = new InvoiceControllerImpl(HibernateSessionFactory.getSessionFactory());
				}
			}
		}
		return instance;
	}

	// Static method to allow for a different SessionFactory (pointing to an in-memory database) in tests
	public static void setTestSessionFactory(SessionFactory testSessionFactory) {
		synchronized (InvoiceControllerImpl.class) {
			instance = new InvoiceControllerImpl(testSessionFactory);
		}
	}

	// Create a Map to store the selected items' identifiers and desired quantity
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
	public void addInvoice(Integer userId, double totalPrice) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Invoice invoice = new Invoice(userId, Instant.now(), totalPrice);
			session.persist(invoice); // Use persist() to make the invoice entity persistent
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
	public List<Integer> getAllInvoiceIds() {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		List<Integer> invoiceIds = null; // Initialize the list to hold invoice IDs
		try {
			transaction = session.beginTransaction();
			invoiceIds = session.createQuery("select i.id from Invoice i", Integer.class).list();
			transaction.commit(); // Commit the transaction
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback(); // Roll back only if the transaction is active
			}
			throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
			session.close(); // Ensure the session is closed
		}
		return invoiceIds; // Return the list of invoice IDs
	}

	@Override
	public Instant getInvoiceIssueInstantById(Integer invoiceId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		Instant issueInstant = null; // Initialize the variable to hold the issue instant
		try {
			transaction = session.beginTransaction();
			Invoice invoice = session.get(Invoice.class, invoiceId);
			if (invoice != null) {
				issueInstant = invoice.getIssueInstant(); // Get the issue instant if the invoice exists
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
		return issueInstant; // Return the issue instant (may be null if invoice not found)
	}

	@Override
	public double getInvoiceTotalPriceById(Integer invoiceId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		double totalPrice = 0.0; // Initialize the variable to hold the total price
		try {
			transaction = session.beginTransaction();
			Invoice invoice = session.get(Invoice.class, invoiceId);
			if (invoice != null) {
				totalPrice = invoice.getTotalPrice(); // Get the total price if the invoice exists
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
		return totalPrice; // Return the total price (may be 0.0 if invoice not found)
	}

	@Override
	public int getInvoiceOperatorById(Integer invoiceId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		int operator = -1; // Initialize the variable to hold the operator (assuming -1 indicates not found)
		try {
			transaction = session.beginTransaction();
			Invoice invoice = session.get(Invoice.class, invoiceId);
			if (invoice != null) {
				operator = invoice.getOperator(); // Get the operator if the invoice exists
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
		return operator; // Return the operator (may be -1 if invoice not found)
	}

	@Override
	public void removeUserInvoicesById(Integer selectedUserId) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
		    transaction = session.beginTransaction();
		    
		    // Get all invoice IDs
		    List<Integer> invoiceIds = getAllInvoiceIds();
		    
		    for (Integer invoiceId : invoiceIds) {
		    	// Check if the invoice belongs to the selected user
		    	if (getInvoiceOperatorById(invoiceId) == selectedUserId) {
		    		Invoice invoice = session.get(Invoice.class, invoiceId);
		    		if (invoice != null) {
						session.remove(invoice); // Remove the invoice if it exists
						System.out.println("Removing invoice with ID: " + invoiceId);
					} else {
			            System.out.println("Invoice not found with ID: " + invoiceId);
			        }
		    	}
		    }
		    
		    transaction.commit(); // Commit the transaction after processing all invoices
		} catch (Exception e) {
		    if (transaction != null) {
		        transaction.rollback(); // Roll back only if the transaction is active
		    }
		    throw e; // Re-throw the exception to propagate it up the call stack
		} finally {
		    session.close(); // Ensure the session is closed
		}
	}
}
