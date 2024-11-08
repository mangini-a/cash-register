package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Item;
import model.ItemCategory;

public class ItemControllerTest {

	private ItemControllerImpl itemController;
    private SessionFactory testSessionFactory;
    
    @BeforeEach
    public void setUp() {
    	// Set up the in-memory database
    	testSessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
    	
    	// Initialize the singleton with the in-memory SessionFactory
    	ItemControllerImpl.setTestSessionFactory(testSessionFactory);
    	itemController = ItemControllerImpl.getInstance();
    	
    	// Create a session and add a test item
    	try (Session session = testSessionFactory.openSession()) {
    		Transaction transaction = session.beginTransaction();
    		Item item = new Item("Test Item", 10, 2.90, ItemCategory.DAIRY);
    		session.persist(item);
    		transaction.commit();
    	}
    }
    
    @SuppressWarnings("deprecation")
	@AfterEach
    public void tearDown() {
    	// Clean up the in-memory database
    	try (Session session = testSessionFactory.openSession()) {
    		Transaction transaction = session.beginTransaction();
    		session.createQuery("delete from Item").executeUpdate();
    		transaction.commit();
    	}
    	testSessionFactory.close();
    }
    
    @Test
    public void testAddItem() {
    	// Act
        itemController.addItem("New Item", 25, 3.99, ItemCategory.BIO);
        
        // Assert
        List<Integer> itemIds = itemController.getAllItemIds();
        assertEquals(2, itemIds.size()); // We should have 2 items now
    }

    @Test
    public void testUpdateItem() {
        // Arrange
    	Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item
    	
    	// Act
    	itemController.updateItem(itemId, "Updated Item", 20, 15.99, ItemCategory.FISH);
    	
    	// Assert
    	String updatedName = itemController.getItemNameById(itemId);
    	assertEquals("Updated Item", updatedName);
    	int updatedQuantity = itemController.getItemQuantityById(itemId);
    	assertEquals(20, updatedQuantity);
    	double updatedUnitPrice = itemController.getItemUnitPriceById(itemId);
    	assertEquals(15.99, updatedUnitPrice, 0.01);
    	ItemCategory updatedCategory = itemController.getItemCategoryById(itemId);
    	assertEquals(ItemCategory.FISH, updatedCategory);
    }
    
    @Test
    public void testUpdateItemQuantityById() {
        // Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act
        itemController.updateItemQuantityById(itemId, 15);

        // Assert
        int updatedQuantity = itemController.getItemQuantityById(itemId);
        assertEquals(15, updatedQuantity); // The quantity should be updated to 15
    }

    @Test
    public void testRemoveItemById() {
    	// Arrange
    	Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item
    	
    	// Act
        itemController.removeItemById(itemId);
        
        // Assert
        List<Integer> itemIds = itemController.getAllItemIds();
        assertEquals(0, itemIds.size()); // The item should be removed
    }

    @Test
    public void testGetAllItemIds() {
    	// Act
        itemController.addItem("Chocolate bars", 25, 1.20, ItemCategory.SNACKS);
        itemController.addItem("Medium-sized plum", 75, 0.60, ItemCategory.FRUIT);

        // Assert
        List<Integer> itemIds = itemController.getAllItemIds();
        assertTrue(itemIds.size() >= 3); // Ensure at least three items are present
    }

    @Test
    public void testShowOneToQuantity() {
        // Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act
        Set<Integer> quantityScale = itemController.showOneToQuantity(itemId);
        
        // Assert
        assertEquals(10, quantityScale.size()); // The size should match the initial quantity
        for (int i = 1; i <= 10; i++) {
        	assertTrue(quantityScale.contains(i)); // Each number from 1 to 10 should be present
        }
    }

    @Test
    public void testGetItemNameById() {
    	// Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act
        String name = itemController.getItemNameById(itemId);
        
        // Assert
        assertEquals("Test Item", name); // The initial name should be "Test Item"
    }
    
    @Test
    public void testGetItemQuantityById() {
        // Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act 
        int quantity = itemController.getItemQuantityById(itemId);
        
        // Assert
        assertEquals(10, quantity); // The initial quantity should be 10
    }

    @Test
    public void testGetItemUnitPriceById() {
    	// Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act
        double unitPrice = itemController.getItemUnitPriceById(itemId);
        
        // Assert
        assertEquals(2.90, unitPrice, 0.01); // The initial unit price should be 2.90
    }

    @Test
    public void testGetItemCategoryById() {
    	// Arrange
        Integer itemId = itemController.getAllItemIds().get(0); // Get the ID of the first item

        // Act
        ItemCategory category = itemController.getItemCategoryById(itemId);
        
        // Assert
        assertEquals(ItemCategory.DAIRY, category); // The initial category should be ItemCategory.DAIRY
    }
}
