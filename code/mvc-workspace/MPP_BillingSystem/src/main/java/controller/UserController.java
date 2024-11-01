package controller;

import java.util.List;

import model.User;
import model.UserRole;

public interface UserController {

	/**
	 * Adds a new user to the database.
	 *
	 * @param firstName the user's first name
	 * @param lastName the user's last name
	 * @param password the user's password
	 * @param role the user's role
	 * @return the new user's generated ID
	 */
	int addUser(String firstName, String lastName, String password, UserRole role);
	
	/**
	 * Updates an existing user in the database.
	 *
	 * @param id the user's id
	 * @param newPassword the user's new password
	 * @param newRole the user's new role (only if it switches from being a CASHIER to a MANAGER)
	 */
	void updateUser(int id, String newPassword, UserRole newRole);
	
	/**
	 * Gets all the users in the database.
	 *
	 * @return all the users currently in the database
	 */
	List<User> getAllUsers();
	
	/**
	 * Gets a user from the database by its id.
	 *
	 * @param id the user's id
	 * @return the user
	 */
	User getUserById(int id);
	
	/**
	 * Removes a user from the database by its id.
	 *
	 * @param id the user's id
	 */
	void removeUserById(int id);
	
	/**
	 * Gets all the user identifiers from the database.
	 *
	 * @return a list with all the user identifiers
	 */
	List<Integer> getAllUserIds();

	/**
	 * Checks if a user is a manager.
	 *
	 * @param user the user to be checked
	 * @return true, if the user is actually a manager
	 */
	boolean isUserManager(User user);
	
	/**
	 * Gets the specified user's first name.
	 *
	 * @param user the user
	 * @return the user's first name
	 */
	String getFirstName(User user);
	
	/**
	 * Gets the specified user's last name.
	 *
	 * @param user the user
	 * @return the user's last name
	 */
	String getLastName(User user);
	
	/**
	 * Gets the specified user's password.
	 *
	 * @param user the user
	 * @return the user's password
	 */
	String getPassword(User user);
	
	/**
	 * Gets the specified user's role.
	 *
	 * @param user the user
	 * @return the user's role
	 */
	UserRole getRole(User user);
}
