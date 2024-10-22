package controller;

import model.User;

public interface InvoiceController {

	/**
	 * Adds a cart line to the associated HashMap.
	 *
	 * @param itemId the item ID chosen by the user
	 * @param itemQty the item quantity desired by the user
	 * @return a string representing the cart line
	 * @throws StockExceededException 
	 */
	String addCartLine(Integer itemId, Integer itemQty) throws StockExceededException;
	
	/**
	 * Calculates the partial cart price step by step.
	 *
	 * @param itemId the item ID chosen by the user
	 * @param itemQty the item quantity desired by the user
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
	 * @param user the user which generated it
	 * @param totalPrice the total cart amount
	 */
	void addInvoice(User user, Double totalPrice);

	void updateInventory();
}
