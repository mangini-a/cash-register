package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Invoice;
import model.User;
import model.UserRole;

public class InvoiceControllerTest {

	private InvoiceControllerImpl invoiceController;
	private SessionFactory testSessionFactory;
	
	@BeforeEach
    public void setUp() {
    	// Set up the in-memory database
    	testSessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
    	
    	// Initialize the singleton with the in-memory SessionFactory
    	InvoiceControllerImpl.setTestSessionFactory(testSessionFactory);
    	ItemControllerImpl.setTestSessionFactory(testSessionFactory);
    	invoiceController = InvoiceControllerImpl.getInstance();
    	
    	// Create a session and add a test user
        try (Session session = testSessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            
            // Create and persist a test user
            User testUser = new User("Camilla", "Magliano", "quatreVingts80", UserRole.MANAGER);
            session.persist(testUser);
            
            // Create and persist a test invoice for the test user
            Invoice invoice = new Invoice(testUser.getId(), Instant.now(), 100.0); // Example invoice
            session.persist(invoice);
            
            transaction.commit();
        }
	}
	
	@SuppressWarnings("deprecation")
	@AfterEach
    public void tearDown() {
        // Clean up the in-memory database
        try (Session session = testSessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Invoice").executeUpdate(); // Delete all invoices first
            session.createQuery("delete from User").executeUpdate(); // Now delete the test user
            transaction.commit();
        }
        testSessionFactory.close();
    }
	
	@Test
    public void testAddInvoice() {
		// Arrange
	    User testUser = new User("Simone", "Boffelli", "cyan180", UserRole.CASHIER);
	    try (Session session = testSessionFactory.openSession()) {
	        Transaction transaction = session.beginTransaction();
	        session.persist(testUser); // Persist the test user
	        transaction.commit();
	    }
		
        // Act
        invoiceController.addInvoice(testUser.getId(), 150.0); // Add a new invoice for the test user

        // Assert
        List<Integer> invoiceIds = invoiceController.getAllInvoiceIds();
        assertEquals(2, invoiceIds.size()); // We should have 2 invoices now
    }
	
	@Test
	public void testGetAllInvoiceIds() {
		// Arrange
		User testUser = new User("Letizia", "Viti", "468y12", UserRole.CASHIER);
	    try (Session session = testSessionFactory.openSession()) {
	        Transaction transaction = session.beginTransaction();
	        session.persist(testUser ); // Persist the test user
	        transaction.commit();
	    }
	    
	    // Add invoices for the test user
	    invoiceController.addInvoice(testUser.getId(), 100.0); // Add a test invoice
	    invoiceController.addInvoice(testUser.getId(), 150.0); // Add another test invoice

	    // Act
	    List<Integer> invoiceIds = invoiceController.getAllInvoiceIds();

	    // Assert
	    assertNotNull(invoiceIds); // Ensure the list is not null
	    assertEquals(3, invoiceIds.size()); // We should have 3 invoices
	}

    @Test
    public void testGetInvoiceIssueInstantById() {
        // Arrange
        Integer invoiceId = invoiceController.getAllInvoiceIds().get(0); // Get the ID of the first invoice

        // Act
        Instant issueInstant = invoiceController.getInvoiceIssueInstantById(invoiceId);

        // Assert
        assertNotNull(issueInstant); // The issue instant should not be null
    }
    
    @Test
    public void testGetInvoiceTotalPriceById() {
    	// Arrange
    	Integer invoiceId = invoiceController.getAllInvoiceIds().get(0); // Get the ID of the first invoice
    	
    	// Act
    	double totalPrice = invoiceController.getInvoiceTotalPriceById(invoiceId);
    	
    	// Assert
    	assertEquals(100.0, totalPrice, 0.01); // The total price should match the initial value
    }

    @Test
    public void testGetInvoiceOperatorById() {
        // Arrange
        Integer invoiceId = invoiceController.getAllInvoiceIds().get(0); // Get the ID of the first invoice

        // Act
        int operator = invoiceController.getInvoiceOperatorById(invoiceId);

        // Assert
        assertEquals(1, operator); // The operator's ID should match the test user's
    }
}
