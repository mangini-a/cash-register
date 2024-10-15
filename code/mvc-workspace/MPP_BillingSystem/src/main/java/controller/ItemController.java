package controller;

import java.util.List;

import model.Item;

public interface ItemController {

	/**
	 * Adds a new item to the database.
	 *
	 * @param item the item to be added to the database
	 */
	void addItem(Item item);
	
	/**
	 * Gets an item from the database by its id.
	 *
	 * @param id the item's id
	 * @return the item
	 */
	Item getItemById(int id);
	
	void updateItem(Item item);
	
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
	
	void recalculateItemQuantity(int id, int soldQuantity);
}
