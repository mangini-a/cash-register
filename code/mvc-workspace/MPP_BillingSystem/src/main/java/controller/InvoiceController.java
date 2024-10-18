package controller;

public interface InvoiceController {

	/**
	 * Adds an invoice line to the associated HashMap.
	 *
	 * @param itemId the item id chosen by the user
	 * @param itemQty the item quantity required by the user
	 * @return a string representing the invoice line
	 */
	String addInvoiceLine(int itemId, int itemQty);
	
	/**
	 * Calculates the partial cart price step by step.
	 *
	 * @param itemId the item id chosen by the user
	 * @param itemQty the item quantity required by the user
	 * @return a double representing the cumulative sum of the cart items' price
	 */
	double calculatePartial(int itemId, int itemQty);
	
	/**
	 * Empties the invoice lines' HashMap and resets the cart price.
	 */
	void emptyInvoiceLines();
}
