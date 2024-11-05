package controller;

import java.time.Instant;
import java.util.List;

public interface InvoiceController {

	/**
	 * Adds a cart line to the associated HashMap.
	 *
	 * @param itemId the item id selected by the user
	 * @param itemQty the item quantity selected by the user
	 * @throws StockExceededException 
	 */
	void addCartLine(Integer itemId, Integer itemQty) throws StockExceededException;
	
	/**
	 * Calculates the cart price step by step.
	 *
	 * @param itemId the item id selected by the user
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
	void addInvoice(int userId, double totalPrice);

	/**
	 * Updates the available quantity of each sold item.
	 */
	void updateInventory();

	/**
	 * Gets all the invoice identifiers from the database.
	 *
	 * @return a list with all the invoice identifiers
	 */
	List<Integer> getAllInvoiceIds();

	Instant getInvoiceIssueInstantById(Integer invoiceId);

	double getInvoiceTotalPriceById(Integer invoiceId);
	
	int getInvoiceOperatorById(Integer invoiceId);
}
