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

	// Get the only instance of ItemController to perform item-related operations on the DB
	private ItemController itemController = ItemControllerImpl.getInstance();

	// Create a Map to store item IDs and their selected quantity
	private Map<Integer, Integer> cartLines = new HashMap<>();

	private double cartPrice = 0.0;

	@Override
	public String addCartLine(Integer itemId, Integer itemQty) {
		// Associate the specified value with the specified key in this map
		cartLines.put(itemId, itemQty);

		// Generate a string to be shown in the UI text area
		Item item = itemController.getItemById(itemId);
		return itemId + "\t" + item.getName() + "\t" + itemQty + "\t" + item.getUnitPrice() + "\n";
	}

	@Override
	public double calculatePartial(Integer itemId, Integer itemQty) {
		Item item = itemController.getItemById(itemId);
		cartPrice += item.getUnitPrice() * itemQty;
		return cartPrice;
	}

	@Override
	public void emptyCartLines() {
		cartLines.clear();
		cartPrice = 0.0;
	}
}
