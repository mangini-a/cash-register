package model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "invoice")
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne(optional=false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", 
				foreignKey = @ForeignKey(name = "InvoicesToUserById"))
	private User user;
	
	@Column(name = "issue_instant")
	private Instant issueInstant;
	
	@Column(name = "total_price")
	private double totalPrice;
	
	@Enumerated(EnumType.STRING)
	private InvoiceStatus status;

	public Invoice() {
		super();
	}
}
