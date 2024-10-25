package controller;

import java.util.List;
import java.util.Set;

import model.Item;
import model.ItemCategory;

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

	String getItemName(Item selectedItem);

	int getItemQuantity(Item selectedItem);

	double getItemUnitPrice(Item selectedItem);

	ItemCategory getItemCategory(Item selectedItem);
}
