package model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@Column(name = "issue_instant")
	private Instant issueInstant;
	
	@Column(name = "total_price")
	private double totalPrice;
	
	public Invoice() {
		super();
	}

	public Invoice(int userId, Instant issueInstant, Double totalPrice) {
		super();
		this.user = new User(); // Create a proxy object
		this.user.setId(userId);
		this.issueInstant = issueInstant;
		this.totalPrice = totalPrice;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Instant getIssueInstant() {
		return issueInstant;
	}

	public void setIssueInstant(Instant issueInstant) {
		this.issueInstant = issueInstant;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
