package controller;

import java.util.List;

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
	 * @param userId the user's id
	 * @param newPassword the user's new password
	 * @param newRole the user's new role (only if it switches from being a CASHIER to a MANAGER)
	 */
	void updateUser(Integer userId, String newPassword, UserRole newRole);
	
	/**
	 * Removes a user from the database by its id.
	 *
	 * @param userId the user's id
	 */
	void removeUserById(Integer userId);
	
	/**
	 * Gets all the user identifiers from the database.
	 *
	 * @return a list with all the user identifiers
	 */
	List<Integer> getAllUserIds();

	/**
	 * Checks if a user is a manager by its id.
	 *
	 * @param userId the id of the user to be checked
	 * @return true, if the user is actually a manager
	 */
	boolean isUserManager(Integer userId);
	
	/**
	 * Gets a user's first name by its id.
	 *
	 * @param userId the user's id
	 * @return the user's first name
	 */
	String getUserFirstNameById(Integer userId);
	
	/**
	 * Gets a user's last name by its id.
	 *
	 * @param userId the user's id
	 * @return the user's last name
	 */
	String getUserLastNameById(Integer userId);
	
	/**
	 * Gets a user's password by its id.
	 *
	 * @param userId the user's id
	 * @return the user's password
	 */
	String getUserPasswordById(Integer userId);
	
	/**
	 * Gets a user's role by its id.
	 *
	 * @param userId the user's id
	 * @return the user's role
	 */
	UserRole getUserRoleById(Integer userId);
}
