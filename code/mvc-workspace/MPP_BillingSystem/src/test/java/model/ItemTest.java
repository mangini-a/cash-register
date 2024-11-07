package model;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ItemTest {

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
	public void testItemCRUD() {
		Transaction transaction = null;
		try {
			// Start a transaction
			transaction = session.beginTransaction();

			// Create a new Item
			Item item = new Item("Apple", 100, 0.5, ItemCategory.FRUIT);
			session.persist(item);
			transaction.commit();

			// Verify that the item was saved
			assertNotEquals(0, item.getId()); // ID should be generated

			// Retrieve the Item
			Item retrievedItem = session.get(Item.class, item.getId());
			assertNotNull(retrievedItem);
			assertEquals("Apple", retrievedItem.getName());
			assertEquals(100, retrievedItem.getQuantity());
			assertEquals(0.5, retrievedItem.getUnitPrice(), 0.01);
			assertEquals(ItemCategory.FRUIT, retrievedItem.getCategory());

			// Update the Item
			transaction = session.beginTransaction();
			retrievedItem.setQuantity(150);
			session.persist(retrievedItem);
			transaction.commit();

			// Verify the update
			Item updatedItem = session.get(Item.class, retrievedItem.getId());
			assertEquals(150, updatedItem.getQuantity());

			// Delete the Item
			transaction = session.beginTransaction();
			session.remove(updatedItem);
			transaction.commit();

			// Verify deletion
			Item deletedItem = session.get(Item.class, updatedItem.getId());
			assertNull(deletedItem);

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
			fail("Exception occurred: " + e.getMessage());
		}
	}
}
