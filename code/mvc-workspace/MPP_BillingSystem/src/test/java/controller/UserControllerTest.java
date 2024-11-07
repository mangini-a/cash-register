package controller;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.UserRole;

public class UserControllerTest {

	private UserControllerImpl userController;
    private SessionFactory sessionFactory;

    @Before
    public void setUp() {
        userController = UserControllerImpl.getInstance();
        sessionFactory = new Configuration().configure("hibernate-test.cfg.xml").buildSessionFactory();
    }

    @SuppressWarnings("deprecation")
	@After
    public void tearDown() {
        // Clean up the database after tests
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    public void testAddUser() {
        int userId = userController.addUser("John", "Doe", "password123", UserRole.CASHIER);
        assertNotEquals(0, userId); // Ensure the user ID is generated
    }

    @Test
    public void testUpdateUser() {
        int userId = userController.addUser("Jane", "Doe", "password456", UserRole.CASHIER);
        userController.updateUser(userId, "newPassword", UserRole.MANAGER);

        String updatedPassword = userController.getUserPasswordById(userId);
        UserRole updatedRole = userController.getUserRoleById(userId);

        assertEquals("newPassword", updatedPassword);
        assertEquals(UserRole.MANAGER, updatedRole);
    }

    @Test
    public void testRemoveUserById() {
        int userId = userController.addUser("Mark", "Smith", "password789", UserRole.CASHIER);
        userController.removeUserById(userId);

        String firstName = userController.getUserFirstNameById(userId);
        assertNull(firstName); // User should not be found
    }

    @Test
    public void testGetAllUserIds() {
        userController.addUser("Alice", "Johnson", "password111", UserRole.CASHIER);
        userController.addUser("Bob", "Brown", "password222", UserRole.CASHIER);

        List<Integer> userIds = userController.getAllUserIds();
        assertTrue(userIds.size() >= 2); // Ensure at least two users are present
    }

    @Test
    public void testIsUserManager() {
        int userId = userController.addUser("Charlie", "Green", "password333", UserRole.MANAGER);
        assertTrue(userController.isUserManager(userId));

        int anotherUserId = userController.addUser("Diana", "White", "password444", UserRole.CASHIER);
        assertFalse(userController.isUserManager(anotherUserId));
    }

    @Test
    public void testGetUserFirstNameById() {
        int userId = userController.addUser("Eve", "Black", "password555", UserRole.CASHIER);
        String firstName = userController.getUserFirstNameById(userId);
        assertEquals("Eve", firstName);
    }

    @Test
    public void testGetUserLastNameById() {
        int userId = userController.addUser("Frank", "Gray", "password666", UserRole.CASHIER);
        String lastName = userController.getUserLastNameById(userId);
        assertEquals("Gray", lastName);
    }

    @Test
    public void testGetUserPasswordById() {
        int userId = userController.addUser("Grace", "Blue", "password777", UserRole.CASHIER);
        String password = userController.getUserPasswordById(userId);
        assertEquals("password777", password);
    }

    @Test
    public void testGetUserRoleById() {
        int userId = userController.addUser("Hank", "Red", "password888", UserRole.MANAGER);
        UserRole role = userController.getUserRoleById(userId);
        assertEquals(UserRole.MANAGER, role);
    }
}
