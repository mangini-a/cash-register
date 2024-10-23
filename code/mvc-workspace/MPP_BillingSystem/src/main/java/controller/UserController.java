package controller;

import java.util.List;

import model.User;

public interface UserController {

	/**
	 * Adds a new user to the database.
	 *
	 * @param user the user to be added to the database
	 */
	void addUser(User user);
	
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
}
