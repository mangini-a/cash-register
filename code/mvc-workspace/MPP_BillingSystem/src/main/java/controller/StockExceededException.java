package controller;

@SuppressWarnings("serial")
public class StockExceededException extends Exception {
	
	private int itemQty;
	
	public StockExceededException(int itemQty) {
		super();
		this.itemQty = itemQty;
	}

	public int getItemQty() {
		return itemQty;
	}
}
