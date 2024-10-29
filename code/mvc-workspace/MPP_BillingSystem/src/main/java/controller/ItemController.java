package controller;

import java.util.List;
import java.util.Set;

import model.Item;
import model.ItemCategory;

public interface ItemController {

	/**
	 * Adds a new item or updates an existing item in the database.
	 *
	 * @param item the item to be added or updated
	 */
	void addItem(Item item);
	
	/**
	 * Checks for the existence of an item ID in the database.
	 *
	 * @param id the item's identifier
	 * @return true, if successful
	 */
	boolean itemIdExists(Integer id);
	
	/**
	 * Gets an item from the database by its id.
	 *
	 * @param id the item's id
	 * @return the item
	 */
	Item getItemById(int id);
	
	/**
	 * Removes an item from the database by its id.
	 *
	 * @param id the item's id
	 */
	void removeItemById(int id);
	
	/**
	 * Gets the all the item ids from the database.
	 *
	 * @return a list with all the item ids
	 */
	List<Integer> getAllItemIds();
	
	/**
	 * Get quantity and all the numbers from one to argument
	 * @param item
	 * @return an array of all the values till quantity
	 */
	Set<Integer> showOneToQuantity(Item item);
	
	void updateItemQuantityById(int id, int newQuantity);

	String getName(Item selectedItem);

	int getQuantity(Item selectedItem);

	double getUnitPrice(Item selectedItem);

	ItemCategory getCategory(Item selectedItem);
}
