package jooq.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jooq.DataService;
import jooq.InvoiceService;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

public class InvoiceServiceImpl implements InvoiceService {

    private static final Logger logger = Logger.getLogger(InvoiceServiceImpl.class.getName());

    private final DataService dataService;

    public InvoiceServiceImpl(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void generaScontrino(List<VocescontrinoRecord> vociScontrino, float prezzoTotale) {
        try {
            // Inserisce lo scontrino di cui viene passato il totale complessivo nella tabella Scontrino
            ScontrinoRecord scontrino = dataService.inserisciScontrino(prezzoTotale);
            logger.info("Scontrino inserito con ID: " + scontrino.getIdscontrino());

            // Inserisce le voci dello scontrino nella tabella VoceScontrino
            for (VocescontrinoRecord voceScontrino : vociScontrino) {
                voceScontrino.setIdscontrino(scontrino.getIdscontrino());
                dataService.inserisciVoceScontrino(voceScontrino);
                logger.info("Voce scontrino inserita: " + voceScontrino);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Errore durante la generazione dello scontrino", e);
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
