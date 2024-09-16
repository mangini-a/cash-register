package jooq.impl;

import java.util.List;

import jooq.DataService;
import jooq.InvoiceService;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

public class InvoiceServiceImpl implements InvoiceService {
	
	private DataService dataService;

	public InvoiceServiceImpl(DataService dataService) {
		this.dataService = dataService;
	}

	@Override
	public void generaScontrino(List<VocescontrinoRecord> vociScontrino, float prezzoTotale) {
		// Inserisci lo scontrino nella tabella Scontrino
        ScontrinoRecord scontrino = dataService.inserisciScontrino(prezzoTotale);

        // Inserisci le voci dello scontrino nella tabella VoceScontrino
        for (VocescontrinoRecord voceScontrino : vociScontrino) {
            voceScontrino.setIdscontrino(scontrino.getIdscontrino());
            dataService.inserisciVoceScontrino(voceScontrino);
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
