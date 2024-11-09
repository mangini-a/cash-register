package controller;

import java.time.Instant;
import java.util.List;

public interface InvoiceController {

	/**
	 * Adds a cart line to the associated HashMap.
	 *
	 * @param itemId the item id selected by the user
	 * @param itemQty the item quantity selected by the user
	 * @throws StockExceededException to prevent more than the available quantity from being selected
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
	 * Updates the available quantity of each sold item.
	 */
	void updateInventory();
	
	/**
	 * Adds a new invoice to the database.
	 *
	 * @param userId the id of the user who generated it
	 * @param totalPrice the total cart amount
	 */
	void addInvoice(Integer userId, double totalPrice);

	/**
	 * Gets all the invoice identifiers from the database.
	 *
	 * @return a list with all the invoice identifiers
	 */
	List<Integer> getAllInvoiceIds();

	/**
	 * Gets an invoice's issue instant by its id.
	 *
	 * @param invoiceId the invoice's id
	 * @return the invoice's issue instant
	 */
	Instant getInvoiceIssueInstantById(Integer invoiceId);

	/**
	 * Gets an invoice's total price by its id.
	 *
	 * @param invoiceId the invoice's id
	 * @return the invoice's total price
	 */
	double getInvoiceTotalPriceById(Integer invoiceId);
	
	/**
	 * Gets the id of the operator who issued an invoice from the latter's id.
	 *
	 * @param invoiceId the invoice's id
	 * @return the identifier of the operator who issued the invoice
	 */
	int getInvoiceOperatorById(Integer invoiceId);

	/**
	 * Removes the invoices issued by the selected user by its id (if any exist).
	 *
	 * @param selectedUserId the selected user id
	 */
	void removeUserInvoicesById(Integer selectedUserId);
}
