package jooq;

import java.util.List;

import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

/**
 * Definisce metodi utili a gestire le operazioni sulle tabelle Scontrino e VoceScontrino.
 */
public interface InvoiceService {
	void generaScontrino(List<VocescontrinoRecord> vociScontrino, float prezzoTotale);
    List<ScontrinoRecord> getScontrini();
    List<VocescontrinoRecord> getDettagliScontrino(int idScontrino);
}
