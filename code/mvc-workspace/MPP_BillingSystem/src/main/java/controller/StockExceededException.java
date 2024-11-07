package controller;

@SuppressWarnings("serial")
public class StockExceededException extends Exception {
	
	private Integer availableQty;
	
	public StockExceededException(Integer availableQty) {
		this.availableQty = availableQty;
	}

	public Integer getAvailableQty() {
		return availableQty;
	}
}
