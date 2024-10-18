package controller;

import java.util.HashMap;
import java.util.Map;

import model.Item;

public class InvoiceControllerImpl implements InvoiceController {

	private static InvoiceControllerImpl singleInstance = null;

	public static InvoiceControllerImpl getInstance() {
		if (singleInstance == null) {
			singleInstance = new InvoiceControllerImpl();
		}
		return singleInstance;
	}
	
	private ItemController itemController = ItemControllerImpl.getInstance();
	
	// Create a HashMap to store item ids and their selected quantity
    private Map<Integer, Integer> invoiceLines = new HashMap<>();
    
    private double cartPrice = 0.0;
	
	@Override
	public String addInvoiceLine(int itemId, int itemQty) {
		
		int localQty = 0;
		
		// Check if the key passed as a parameter already exists
        if (invoiceLines.containsKey(itemId)) {
            int oldQty = invoiceLines.get(itemId);
            localQty = oldQty + itemQty;
            invoiceLines.put(itemId, localQty);
        } else {
        	localQty = itemQty;
        	invoiceLines.put(itemId, localQty);
        }
        
        // Show the added invoice line in the corresponding text area
        Item item = itemController.getItemById(itemId);
        return itemId + "\t" + item.getName() + "\t" + localQty + "\t" + item.getUnitPrice() + "\n";
	}

	@Override
	public double calculatePartial(int itemId, int itemQty) {
		Item item = itemController.getItemById(itemId);
		cartPrice += item.getUnitPrice() * itemQty;
		return cartPrice;
	}
	
	@Override
	public void emptyInvoiceLines() {
		invoiceLines.clear();
		cartPrice = 0.0;
	}
}
