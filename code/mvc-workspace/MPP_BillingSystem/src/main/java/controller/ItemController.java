package controller;

import java.util.List;
import java.util.Set;

import model.Item;
import model.ItemCategory;

public interface ItemController {

	/**
	 * Adds a new item to the database.
	 *
	 * @param name the item's name
	 * @param quantity the item's quantity
	 * @param unitPrice the item's unit price
	 * @param category the item's category
	 */
	void addItem(String name, int quantity, double unitPrice, ItemCategory category);
	
	/**
	 * Updates an existing item in the database.
	 *
	 * @param id the item's id
	 * @param newName the item's new name
	 * @param newQuantity the item's new quantity
	 * @param newUnitPrice the item's new unit price
	 * @param newCategory the item's new category
	 */
	void updateItem(int id, String newName, int newQuantity, double newUnitPrice, ItemCategory newCategory);
	
	/**
	 * Gets all the items in the database.
	 *
	 * @return all the items currently in the database
	 */
	List<Item> getAllItems();
	
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
	
	/**
	 * Gets the specified item's id.
	 *
	 * @param item the item
	 * @return the item's id
	 */
	int getId(Item item);

	/**
	 * Gets the specified item's name.
	 *
	 * @param item the item
	 * @return the item's name
	 */
	String getName(Item item);

	/**
	 * Gets the specified item's quantity.
	 *
	 * @param item the item
	 * @return the item's quantity
	 */
	int getQuantity(Item item);

	/**
	 * Gets the specified item's unit price.
	 *
	 * @param item the item
	 * @return the item's unit price
	 */
	double getUnitPrice(Item item);

	/**
	 * Gets the specified item's category.
	 *
	 * @param item the item
	 * @return the item's category
	 */
	ItemCategory getCategory(Item item);
}
