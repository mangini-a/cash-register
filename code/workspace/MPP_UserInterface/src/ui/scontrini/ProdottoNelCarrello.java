package ui.scontrini;

import jooq.generated.tables.records.ProdottoRecord;

/*
 * Un'istanza di tale classe rappresenta un prodotto aggiunto al carrello della spesa 
 * da parte del cliente (nel contesto della registrazione di uno scontrino).
 */
public class ProdottoNelCarrello {
	private ProdottoRecord prodotto;
	private int qtaProdotto;

	public ProdottoNelCarrello(ProdottoRecord prodotto, int qtaProdotto) {
		this.prodotto = prodotto;
		this.qtaProdotto = qtaProdotto;
	}

	public ProdottoRecord getProdotto() {
		return prodotto;
	}

	public int getQtaProdotto() {
		return qtaProdotto;
	}
}
