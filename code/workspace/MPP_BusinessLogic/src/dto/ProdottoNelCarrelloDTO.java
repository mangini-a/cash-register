package dto;

/*
 * Data Transfer Object (DTO) che il layer di presentazione può usare per trasferire dati al sottostante.
 * Rappresenta i prodotti inseriti nel carrello.
 */
public class ProdottoNelCarrelloDTO {

	private int id;
	private String nome;
	private float prezzo;
	private int qta;

	public ProdottoNelCarrelloDTO(int id, String nome, float prezzo, int qta) {
		this.id = id;
		this.nome = nome;
		this.prezzo = prezzo;
		this.qta = qta;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public float getPrezzo() {
		return prezzo;
	}

	public int getQta() {
		return qta;
	}
}
