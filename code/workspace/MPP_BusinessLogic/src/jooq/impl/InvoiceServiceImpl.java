package jooq.impl;

import java.util.List;

import jooq.DataService;
import jooq.InvoiceService;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

public class InvoiceServiceImpl implements InvoiceService {
	
	private DataService dataService;

	// Ogni istanza di InvoiceServiceImpl dev'essere inizializzata con un'istanza di DataService valida
	public InvoiceServiceImpl(DataService dataService) {
		this.dataService = dataService;
	}

	/*
	 * Metodo utilizzato nella classe RegistraScontrinoPanel.java (contenuta nel package ui.rspanel).
	 */
	@Override
	public void generaScontrino(List<VocescontrinoRecord> vociScontrino, float prezzoTotale) {
		try {
			// Inserisce lo scontrino di cui viene passato il totale complessivo nella tabella Scontrino
			ScontrinoRecord scontrino = dataService.inserisciScontrino(prezzoTotale);
			
			// Inserisci le voci dello scontrino (passate come parametro) nella tabella VoceScontrino
			for (VocescontrinoRecord voceScontrino : vociScontrino) {
				voceScontrino.setIdscontrino(scontrino.getIdscontrino());
				dataService.inserisciVoceScontrino(voceScontrino);
			}
		} catch (Exception e) {
			// Gestisce qualunque eccezione si sollevi durante la generazione dello scontrino
	        throw new RuntimeException("Errore durante la generazione dello scontrino: " + e.getMessage(), e);
	    }
	}

	@Override
	public List<ScontrinoRecord> getScontrini() {
		return dataService.getScontrini();
	}

	@Override
	public List<VocescontrinoRecord> getDettagliScontrino(int idScontrino) {
		return dataService.getDettagliScontrino(idScontrino);
	}
}
