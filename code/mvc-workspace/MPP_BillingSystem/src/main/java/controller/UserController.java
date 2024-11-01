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
	 */
	void addUser(String firstName, String lastName, String password, UserRole role);
	
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
	
	void updateUser(User user);
	
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
	 * Gets the specified user's password.
	 *
	 * @param user the user
	 * @return the user's password
	 */
	String getPassword(User user);
}
