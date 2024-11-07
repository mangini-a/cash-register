package controller;

import java.util.List;
import java.util.Set;

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
	 * @param itemId the item's id
	 * @param newName the item's new name
	 * @param newQuantity the item's new quantity
	 * @param newUnitPrice the item's new unit price
	 * @param newCategory the item's new category
	 */
	void updateItem(Integer itemId, String newName, int newQuantity, double newUnitPrice, ItemCategory newCategory);
	
	/**
	 * Updates an item's quantity by its id.
	 *
	 * @param itemId the item's id
	 * @param newQuantity the new quantity (equal to the difference between the original quantity and the sold one)
	 */
	void updateItemQuantityById(Integer itemId, int newQuantity);
	
	/**
	 * Removes an item from the database by its id.
	 *
	 * @param itemId the item's id
	 */
	void removeItemById(Integer itemId);
	
	/**
	 * Gets all the item identifiers from the database.
	 *
	 * @return a list with all the item identifiers
	 */
	List<Integer> getAllItemIds();
	
	/**
	 * Captures an item's available quantity and returns a set with values from one to the amount itself.
	 * 
	 * @param itemId the item's id
	 * @return a set with all the values till quantity
	 */
	Set<Integer> showOneToQuantity(Integer itemId);
	
	/**
	 * Gets an item's name by its id.
	 *
	 * @param itemId the item's id
	 * @return the item's name
	 */
	String getItemNameById(Integer itemId);

	/**
	 * Gets an item's quantity by its id.
	 *
	 * @param itemId the item's id
	 * @return the item's quantity
	 */
	int getItemQuantityById(Integer itemId);

	/**
	 * Gets an item's unit price by its id.
	 *
	 * @param itemId the item's id
	 * @return the item's unit price
	 */
	double getItemUnitPriceById(Integer itemId);

	/**
	 * Gets an item's category by its id.
	 *
	 * @param itemId the item's id
	 * @return the item's category
	 */
	ItemCategory getItemCategoryById(Integer itemId);
}
