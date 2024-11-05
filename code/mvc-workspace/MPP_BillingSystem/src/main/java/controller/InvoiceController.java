package controller;

public interface InvoiceController {

	/**
	 * Adds a cart line to the associated HashMap.
	 *
	 * @param itemId the item ID selected by the user
	 * @param itemQty the item quantity selected by the user
	 * @throws StockExceededException 
	 */
	void addCartLine(Integer itemId, Integer itemQty) throws StockExceededException;
	
	/**
	 * Gets the item name by its id.
	 *
	 * @param itemId the item ID selected by the user
	 * @return the item name
	 */
	String getItemNameById(Integer itemId);
	
	/**
	 * Gets the item unit price by its id.
	 *
	 * @param itemId the item ID selected by the user
	 * @return the item unit price
	 */
	double getItemUnitPriceById(Integer itemId);
	
	/**
	 * Calculates the partial cart price step by step.
	 *
	 * @param itemId the item ID selected by the user
	 * @param itemQty the item quantity selected by the user
	 * @return a double representing the cumulative sum of the cart items' price
	 */
	double calculatePartial(Integer itemId, Integer itemQty);
	
	/**
	 * Empties the cart lines' HashMap and resets the cart price.
	 */
	void emptyCartLines();
	
	/**
	 * Adds a new invoice to the database.
	 *
	 * @param userId the id of the user who generated it
	 * @param totalPrice the total cart amount
	 */
	void addInvoice(int userId, Double totalPrice);

	/**
	 * Updates the available quantity of each sold item.
	 */
	void updateInventory();
}
