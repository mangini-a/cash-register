package model;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;

public class InvoiceTest {

	private SessionFactory sessionFactory;
	private Session session;

	@Before
	public void setUp() {
		// Load the test configuration
		sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
		session = sessionFactory.openSession();
	}

	@After
	public void tearDown() {
		if (session != null) {
			session.close();
		}
		if (sessionFactory != null) {
			sessionFactory.close();
		}
	}

	@Test
	public void testInvoiceCRUD() {
		Transaction transaction = null;
		try {
			// Start a transaction
			transaction = session.beginTransaction();

			// Create a new User for the Invoice
			User user = new User("Matteo", "Sardella", "1234 POSSE", UserRole.MANAGER);
			session.persist(user);
			transaction.commit();

			// Start a new transaction for Invoice
			transaction = session.beginTransaction();

			// Create a new Invoice
			Invoice invoice = new Invoice(user.getId(), Instant.now(), 150.75);
			session.persist(invoice);
			transaction.commit();

			// Verify that the invoice was saved
			assertNotEquals(0, invoice.getId()); // ID should be generated

			// Retrieve the Invoice
			Invoice retrievedInvoice = session.get(Invoice.class, invoice.getId());
			assertNotNull(retrievedInvoice);
			assertEquals(user.getId(), retrievedInvoice.getOperator());
			assertEquals(invoice.getTotalPrice(), retrievedInvoice.getTotalPrice(), 0.01);

			// Update the Invoice
			transaction = session.beginTransaction();
			retrievedInvoice.setTotalPrice(200.00);
			session.persist(retrievedInvoice);
			transaction.commit();

			// Verify the update
			Invoice updatedInvoice = session.get(Invoice.class, retrievedInvoice.getId());
			assertEquals(200.00, updatedInvoice.getTotalPrice(), 0.01);

			// Delete the Invoice
			transaction = session.beginTransaction();
			session.remove(updatedInvoice);
			transaction.commit();

			// Verify deletion
			Invoice deletedInvoice = session.get(Invoice.class, updatedInvoice.getId());
			assertNull(deletedInvoice);

			// Clean up the User
			transaction = session.beginTransaction();
			session.remove(user);
			transaction.commit();

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			fail("Exception occurred: " + e.getMessage());
		}
	}
}
