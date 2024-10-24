package controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import model.Invoice;
import model.Item;
import model.User;
import model.UserRole;
import utils.HibernateSessionFactory;

/**
 * We use Mockito to mock dependencies like ItemController and Item.
 * We use assertions to verify the expected outcomes of each method.
 * We test for expected exceptions to ensure robustness.
 * The session and transaction are mocked to simulate the behavior of a real Hibernate session.
 * The goal is to test the logic of our methods without relying on the actual database!
 */
public class InvoiceControllerTest {

	@InjectMocks
    private InvoiceControllerImpl invoiceController;

    @Mock
    private ItemController itemController;

    @Mock
    private Item item;
    
    @Mock
    private Session session;
    
    @Mock
    private Transaction transaction;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Mock the HibernateSessionFactory to return the mocked session
        when(HibernateSessionFactory.getSessionFactory().openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }
    
    @Test
    public void testAddCartLine_Success() throws StockExceededException {
        when(itemController.getItemById(1)).thenReturn(item);
        when(item.getQuantity()).thenReturn(10);
        when(item.getId()).thenReturn(1);
        
        invoiceController.addCartLine(1, 5);
        
        assertEquals(5, invoiceController.getCartLines().get(1));
    }

    @Test
    public void testAddCartLine_StockExceeded() {
        when(itemController.getItemById(1)).thenReturn(item);
        when(item.getQuantity()).thenReturn(5);
        
        assertThrows(StockExceededException.class, () -> {
            invoiceController.addCartLine(1, 10);
        });
    }
    
    @Test
    public void testGetItemNameById() {
        when(itemController.getItemById(1)).thenReturn(item);
        when(item.getName()).thenReturn("Test Item");
        
        String itemName = invoiceController.getItemNameById(1);
        
        assertEquals("Test Item", itemName);
    }
    
    @Test
    public void testCalculatePartial() {
        when(itemController.getItemById(1)).thenReturn(item);
        when(item.getUnitPrice()).thenReturn(20.0);
        
        double total = invoiceController.calculatePartial(1, 2);
        
        assertEquals(40.0, total);
    }
    
    @Test
    public void testEmptyCartLines() throws StockExceededException {
        invoiceController.addCartLine(1, 5);
        invoiceController.emptyCartLines();
        
        assertTrue(invoiceController.getCartLines().isEmpty());
    }
    
    @Test
    public void testAddInvoice() {
    	// Arrange
        User user = new User("Lorenzo", "Vinciguerra", "tacomaBridge1940", UserRole.CASHIER);
        double totalPrice = 100.0;
        
        // Act
        assertDoesNotThrow(() -> {
            invoiceController.addInvoice(user, totalPrice);
        });
        
        // Verify that the session methods were called
        verify(session).beginTransaction();
        verify(session).persist(any(Invoice.class));
        verify(transaction).commit();
        verify(session).close();
    }
    
    @Test
    public void testAddInvoice_ExceptionHandling() {
        // Arrange
        User user = new User("Matteo", "Di Falco", "noPitStop", UserRole.MANAGER);
        double totalPrice = 100.0;

        // Simulate an exception during persist
        doThrow(new RuntimeException("Database error")).when(session).persist(any(Invoice.class));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            invoiceController.addInvoice(user, totalPrice);
        });

        // Verify that the transaction was rolled back
        verify(transaction).rollback();
        verify(session).close();
    }
}
