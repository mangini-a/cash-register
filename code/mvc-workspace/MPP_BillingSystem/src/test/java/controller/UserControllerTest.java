package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.User;
import model.UserRole;

public class UserControllerTest {

	private UserControllerImpl userController;
	private SessionFactory testSessionFactory;
	
	@BeforeEach
    public void setUp() {
    	// Set up the in-memory database
    	testSessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
    	
    	// Initialize the singleton with the in-memory SessionFactory
    	UserControllerImpl.setTestSessionFactory(testSessionFactory);
    	userController = UserControllerImpl.getInstance();
    	
    	// Create a session and add a test user
    	try (Session session = testSessionFactory.openSession()) {
    		Transaction transaction = session.beginTransaction();
    		User user = new User("Amanda", "Stockton", "xyz900", UserRole.CASHIER);
    		session.persist(user);
    		transaction.commit();
    	}
	}
	
	@SuppressWarnings("deprecation")
	@AfterEach
    public void tearDown() {
    	// Clean up the in-memory database
    	try (Session session = testSessionFactory.openSession()) {
    		Transaction transaction = session.beginTransaction();
    		session.createQuery("delete from User").executeUpdate();
    		transaction.commit();
    	}
    	testSessionFactory.close();
    }
	
	@Test
	public void testAddUser() {
		// Act
		userController.addUser("Jeff", "Landers", "787kmm", UserRole.MANAGER);
		
		// Assert 
		List<Integer> userIds = userController.getAllUserIds();
		assertEquals(2, userIds.size()); // We should have 2 users now
	}
	
	@Test
	public void testUpdateUser() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		userController.updateUser(userId, "676kmm", UserRole.MANAGER);
		
		// Assert
		String updatedPassword = userController.getUserPasswordById(userId);
		assertEquals("676kmm", updatedPassword);
		UserRole updatedRole = userController.getUserRoleById(userId);
		assertEquals(UserRole.MANAGER, updatedRole);
	}
	
	@Test
	public void testRemoveUserById() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		userController.removeUserById(userId);
		
		// Assert
		List<Integer> userIds = userController.getAllUserIds();
		assertEquals(0, userIds.size()); // The user should be removed
	}
	
	@Test
	public void testGetAllUserIds() {
		// Act
		userController.addUser("Lynn", "Hill", "643az", UserRole.CASHIER);
		userController.addUser("Alex", "Honnold", "tcp721n", UserRole.MANAGER);
		
		// Assert
		List<Integer> userIds = userController.getAllUserIds();
		assertTrue(userIds.size() >= 3); // Ensure at least three users are present
	}
	
	@Test
	public void testIsUserManager_UserIsNotManager() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user

        // Act
        boolean result = userController.isUserManager(userId);

        // Assert
        assertFalse(result, "User should be identified as a manager");
	}
	
	@Test
    public void testIsUserManager_UserDoesNotExist() {
        // Act
        boolean result = userController.isUserManager(999); // Assuming 999 is a non-existent user ID

        // Assert
        assertFalse(result, "Non-existent user should not be identified as a manager");
    }
	
	@Test
	public void testGetUserFirstNameById() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		String firstName = userController.getUserFirstNameById(userId);
		
		// Assert
		assertEquals("Amanda", firstName); // The initial first name should be "Amanda"
	}
	
	@Test
	public void testGetUserLastNameById() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		String lastName = userController.getUserLastNameById(userId);
		
		// Assert
		assertEquals("Stockton", lastName); // The initial last name should be "Stockton"
	}
	
	@Test
	public void testGetUserPasswordById() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		String password = userController.getUserPasswordById(userId);
		
		// Assert
		assertEquals("xyz900", password); // The initial password should be "xyz900"
	}
	
	@Test
	public void testGetUserRoleById() {
		// Arrange
		Integer userId = userController.getAllUserIds().get(0); // Get the ID of the first user
		
		// Act
		UserRole role = userController.getUserRoleById(userId);
		
		// Assert
		assertEquals(UserRole.CASHIER, role); // The initial role should be UserRole.CASHIER
	}
}
