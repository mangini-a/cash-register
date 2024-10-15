package model;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "item")
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	private int quantity;
	
	@Column(name = "unit_price")
	private double unitPrice;
	
	@Column(name = "received_date")
	private LocalDate receivedDate;
	
	@Column(name = "expiration_date")
	private LocalDate expirationDate;
	
	@Enumerated(EnumType.STRING)
	private ItemCategory category;

	public Item() {
		super();
	}

	public Item(String name, int quantity, double unitPrice, LocalDate receivedDate, LocalDate expirationDate,
			ItemCategory category) {
		super();
		this.name = name;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.receivedDate = receivedDate;
		this.expirationDate = expirationDate;
		this.category = category;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public LocalDate getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(LocalDate receivedDate) {
		this.receivedDate = receivedDate;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public ItemCategory getCategory() {
		return category;
	}

	public void setCategory(ItemCategory category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", quantity=" + quantity + ", unitPrice=" + unitPrice
				+ ", receivedDate=" + receivedDate + ", expirationDate=" + expirationDate + ", category=" + category
				+ "]";
	}
}
