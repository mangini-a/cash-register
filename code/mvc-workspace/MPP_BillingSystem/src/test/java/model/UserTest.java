package model;

import static org.junit.Assert.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
	
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
    public void testUserCRUD() {
        Transaction transaction = null;
        try {
            // Start a transaction
            transaction = session.beginTransaction();

            // Create a new User
            User user = new User("Lorenz", "Brunner", "tieIn", UserRole.CASHIER);
            session.persist(user);
            transaction.commit();

            // Verify that the user was persisted
            assertNotEquals(0, user.getId()); // ID should be generated

            // Retrieve the User
            User retrievedUser  = session.get(User.class, user.getId());
            assertNotNull(retrievedUser);
            assertEquals("Lorenz", retrievedUser.getFirstName());
            assertEquals("Brunner", retrievedUser.getLastName());

            // Update the User
            transaction = session.beginTransaction();
            retrievedUser.setLastName("Voorn");
            session.persist(retrievedUser);
            transaction.commit();

            // Verify the update
            User updatedUser  = session.get(User.class, retrievedUser.getId());
            assertEquals("Voorn", updatedUser.getLastName());

            // Delete the User
            transaction = session.beginTransaction();
            session.remove(updatedUser);
            transaction.commit();

            // Verify deletion
            User deletedUser  = session.get(User.class, updatedUser.getId());
            assertNull(deletedUser);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            fail("Exception occurred: " + e.getMessage());
        }
    }
}
