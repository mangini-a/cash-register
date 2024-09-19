package jooq;

import java.sql.*;
import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import dbinit.CreateDatabase;
import jooq.generated.tables.Prodotto;
import jooq.generated.tables.Scontrino;
import jooq.generated.tables.Vocescontrino;
import jooq.generated.tables.records.ProdottoRecord;
import jooq.generated.tables.records.ScontrinoRecord;
import jooq.generated.tables.records.VocescontrinoRecord;

public class DataService implements AutoCloseable {

    private Connection conn;
    private DSLContext context;

    public DataService() {
    	// Connection is the only JDBC resource that we need
        try {
            conn = DriverManager.getConnection(CreateDatabase.DB_URL);
            context = DSL.using(conn, SQLDialect.SQLITE);
        } catch (SQLException e) {
            throw new DataServiceException("Failed to connect to database", e);
        }
    }
    
    @Override
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DataServiceException("Failed to close connection", e);
			}
		}
	}

    // Metodo per garantire che la connessione al database sia sempre attiva
    private void ensureConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Connessione assente o chiusa. Riconnessione al database...");
                conn = DriverManager.getConnection(CreateDatabase.DB_URL);
                context = DSL.using(conn, SQLDialect.SQLITE);
                System.out.println("Connessione ristabilita.");
            }
        } catch (SQLException e) {
            throw new DataServiceException("Failed to reconnect to database", e);
        }
    }
    
    // Inserisce un nuovo prodotto nella tabella Prodotto
    public void aggiungiProdotto(String nome, float prezzo, int qtaDisponibile, String descrizione) {
        ensureConnection();
        ProdottoRecord prodottoRecord = context.newRecord(Prodotto.PRODOTTO);
        prodottoRecord.setNome(nome);
        prodottoRecord.setPrezzo(prezzo);
        prodottoRecord.setQtadisponibile(qtaDisponibile);
        prodottoRecord.setDescrizione(descrizione);

        prodottoRecord.insert();

        int idGenerato = prodottoRecord.getIdprodotto();
        System.out.println("IdProdotto generato: " + idGenerato);
    }

    // Elimina un determinato prodotto sulla base del proprio nome
    public int eliminaProdotto(String nome) {
        ensureConnection();
        return context.deleteFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.NOME.eq(nome)).execute();
    }

    // Recupera un determinato prodotto sulla base della propria descrizione
    public ProdottoRecord getProdottoByDescrizione(String descrizione) {
        ensureConnection();
        return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.DESCRIZIONE.eq(descrizione)).fetchOne();
    }

    // Recupera un determinato prodotto sulla base del proprio ID
    public ProdottoRecord getProdottoById(int idProdotto) {
        ensureConnection();
        return context.selectFrom(Prodotto.PRODOTTO).where(Prodotto.PRODOTTO.IDPRODOTTO.eq(idProdotto)).fetchOne();
    }

    // Recupera tutti i prodotti contenuti nella tabella Prodotto
    public List<ProdottoRecord> getProdotti() {
        ensureConnection();
        return context.selectFrom(Prodotto.PRODOTTO).fetch();
    }

    // Aggiorna la quantità disponibile di un prodotto in seguito al suo inserimento nel carrello
    public void aggiornaQtaProdotto(Integer idProdotto, int nuovaQta) {
        ensureConnection();
        context.update(Prodotto.PRODOTTO)
               .set(Prodotto.PRODOTTO.QTADISPONIBILE, nuovaQta)
               .where(Prodotto.PRODOTTO.IDPRODOTTO.eq(idProdotto)).execute();
    }

    // Inserisce un nuovo scontrino nella tabella Scontrino
    public ScontrinoRecord inserisciScontrino(float prezzoTotale) {
        ensureConnection();
        ScontrinoRecord scontrino = context.newRecord(Scontrino.SCONTRINO);
        scontrino.setPrezzotot(prezzoTotale);
        scontrino.insert();
        return scontrino;
    }

    // Inserisce una nuova linea di scontrino nella tabella VoceScontrino
    public void inserisciVoceScontrino(VocescontrinoRecord voceScontrino) {
        ensureConnection();
        voceScontrino.insert();
    }

    // Recupera tutti gli scontrini contenuti nella tabella Scontrino
    public List<ScontrinoRecord> getScontrini() {
        ensureConnection();
        return context.selectFrom(Scontrino.SCONTRINO).fetch();
    }

    // Recupera i dettagli di un determinato scontrino sulla base del suo ID
    public List<VocescontrinoRecord> getDettagliScontrino(int idScontrino) {
        ensureConnection();
        return context.selectFrom(Vocescontrino.VOCESCONTRINO)
                      .where(Vocescontrino.VOCESCONTRINO.IDSCONTRINO.eq(idScontrino)).fetch();
    }
    
    // Genera uno scontrino
    public void generaScontrino(List<VocescontrinoRecord> vociScontrino, float prezzoTotale) {
        ensureConnection();

        context.transaction(configuration -> {
            DSLContext transactionalContext = DSL.using(configuration);

            // Inserisci lo scontrino
            ScontrinoRecord scontrino = transactionalContext.newRecord(Scontrino.SCONTRINO);
            scontrino.setPrezzotot(prezzoTotale);
            scontrino.insert();

            // Inserisci ogni voce scontrino
            for (VocescontrinoRecord voceScontrino : vociScontrino) {
                voceScontrino.setIdscontrino(scontrino.getIdscontrino()); // Associa la voce allo scontrino
                transactionalContext.newRecord(Vocescontrino.VOCESCONTRINO, voceScontrino).insert();
            }
        });
    }


    // Modifica un determinato prodotto sulla base del proprio nome
    public int modificaProdotto(String nome, String nuovoNome, float nuovoPrezzo, int nuovaQta, String nuovaDescrizione) {
        ensureConnection();
        return context.update(Prodotto.PRODOTTO)
                      .set(Prodotto.PRODOTTO.NOME, nuovoNome)
                      .set(Prodotto.PRODOTTO.PREZZO, nuovoPrezzo)
                      .set(Prodotto.PRODOTTO.QTADISPONIBILE, nuovaQta)
                      .set(Prodotto.PRODOTTO.DESCRIZIONE, nuovaDescrizione)
                      .where(Prodotto.PRODOTTO.NOME.eq(nome)).execute();
    }

    // Esempio di metodo per la generazione di uno scontrino completo con transazione
    public void generaScontrinoCompleto(List<VocescontrinoRecord> vociScontrino, float prezzoTotale) {
        ensureConnection();

        context.transaction(configuration -> {
            DSLContext transactionalContext = DSL.using(configuration);

            // Inserisci lo scontrino
            ScontrinoRecord scontrino = transactionalContext.newRecord(Scontrino.SCONTRINO);
            scontrino.setPrezzotot(prezzoTotale);
            scontrino.insert();

            // Inserisci ogni voce scontrino
            for (VocescontrinoRecord voce : vociScontrino) {
                voce.setIdscontrino(scontrino.getIdscontrino()); // Associa la voce allo scontrino
                transactionalContext.newRecord(Vocescontrino.VOCESCONTRINO, voce).insert();
            }
        });
    }
    
    public boolean isProductAlreadyExisting(String nomeProdotto) {
    	// Fetch all ProdottoRecord objects from the database
        Result<ProdottoRecord> result = context.selectFrom(Prodotto.PRODOTTO).fetch();

        // Iterate over the records and check if any of them have the same name
        for (ProdottoRecord record : result) {
            if (record.getNome().equals(nomeProdotto)) {
                return true; // Found a product with the same name
            }
        }

        // If no product with the same name was found, return false
        return false;
    }
}
