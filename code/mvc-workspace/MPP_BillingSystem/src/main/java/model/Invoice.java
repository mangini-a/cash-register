package model;

import java.time.LocalDate;

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
	
	@Column(name = "issue_date")
	private LocalDate issueDate;
	
	@Column(name = "total_price")
	private double totalPrice;
	
	@Enumerated(EnumType.STRING)
	private InvoiceStatus status;

	public Invoice() {
		super();
	}
}
