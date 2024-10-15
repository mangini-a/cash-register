package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;

import model.User;
import model.UserRole;
import utils.HibernateSessionFactory;

class UserControllerTest {
	
	private UserController userController = new UserControllerImpl();
	
	@Test
	void testAddUser_Success() {
		// Arrange
		User user = new User();
		user.setFirstName("Tommaso");
		user.setLastName("Vinciguerra");
		user.setPassword("btx424");
		user.setRole(UserRole.CUSTOMER);
		
		// Act
		userController.addUser(user);
		
		// Assert
		// Query the database to verify that the user is added
		Session session = HibernateSessionFactory.getSessionFactory().openSession();
	    Query<User> query = session.createQuery("from User where id = :id", User.class);
	    query.setParameter("id", user.getId());
	    User addedUser  = query.uniqueResult();
	    
	    assertNotNull(addedUser);
	    assertEquals(user.getId(), addedUser.getId());
	    assertEquals(user.getFirstName(), addedUser.getFirstName());
	    assertEquals(user.getLastName(), addedUser.getLastName());
	    assertEquals(user.getPassword(), addedUser.getPassword());
	    assertEquals(user.getRole(), addedUser.getRole());
	}
	
	@Test
	void testAddUser_RollbackOnException() {
		// Arrange
		User user = new User();
		
		// Act and Assert
		assertThrows(RuntimeException.class, () -> userController.addUser(user));
		// Verify that the transaction is rolled back
		// This can be done by checking the transaction status or using a test database
	}
	
	@Test
	void testAddUser_NullUser() {
		// Act and Assert
		assertThrows(NullPointerException.class, () -> userController.addUser(null));
	}

	@Test
	void testGetUserById_ExistingId() {
		// Arrange
		int existingId = 1; // assume this id exists in the database
		
		// Act
		User user = userController.getUserById(existingId);
		
		// Assert
		assertNotNull(user);
		assertEquals(existingId, user.getId()); // verify the user's id
	}
	
	@Test
	void testGetUserById_NonExistingId() {
		// Arrange
		int nonExistingId = 999; // assume this id does not exist in the database
		
		// Act
		User user = userController.getUserById(nonExistingId);
		
		// Assert
		assertNull(user);
	}
	
	@Test
	void testGetUserById_NullId() {
		// Arrange
		Integer nullId = null;
		
		// Act and Assert
		try {
			userController.getUserById(nullId);
			fail("Expected NullPointerException for null ID");
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
