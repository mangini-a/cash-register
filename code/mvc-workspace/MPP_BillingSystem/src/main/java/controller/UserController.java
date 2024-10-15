package controller;

import java.util.List;

import model.User;
import model.UserRole;

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
	 * Gets all the user ids from the database.
	 *
	 * @return a list with all the user ids
	 */
	List<Integer> getAllUserIds();
	
	int getLoggedUserId();
	
	boolean setLoggedUser(int loggedId);
	
	/**
	 * Checks if a user is authorized to access a certain functionality.
	 *
	 * @param userId the user's id
	 * @param requiredRole the required role to access the functionality
	 * @return true, if the user is actually authorized
	 */
	boolean isUserAuthorized(int userId, UserRole requiredRole);
}
