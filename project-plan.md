# 1. Introduzione

Il progetto nasce dalla volontà di proporre una soluzione intuitiva ed efficace per la gestione dell'inventario e la registrazione degli scontrini nel contesto di piccoli esercizi commerciali (botteghe, minimarket, ecc.), ancora diffusi nelle aree meno densamente popolate.
Nello specifico, l'operatore di cassa potrà usufruire del sistema interagendo con un'interfaccia grafica, la quale faciliterà l'emissione degli scontrini e la consultazione dell'elenco dei prodotti, dei relativi prezzi e delle quantità rimanenti.
Ad occuparsi del lavoro saranno Alessandro Mangini, Alessandro Pesenti e Luca Pesenti.

# 2. Modello di processo

Si seguirà l'approccio allo sviluppo software noto come RAD (Rapid Application Development), il quale risulta particolarmente adatto allo sviluppo di applicativi guidati dai requisiti dell'interfaccia utente.
Tale modello di sviluppo permette di delineare sin da subito le funzioni chiave necessarie a soddisfare le esigenze dell'utenza, rifinendole sulla base delle evidenze raccolte sul campo nelle fasi iniziali, deputate alla prototipazione.
Intraprendendo un approccio incrementale vengono minimizzate le chance di incappare in intricate situazioni di errore che contraddistinguono, ad esempio, i grandi progetti fondati sul modello a cascata.

# 3. Organizzazione del progetto

La squadra farà riferimento ad un'organizzazione a matrice:

| **Member** | **Requirements Planning** | **User Design** | **Construction** | **Cutover** |
|:----------:|:-------------------------:|:---------------:|:----------------:|:-----------:|
| A. Mangini |           **x**           |      **x**      |                  |    **x**    |
| A. Pesenti |                           |      **x**      |       **x**      |    **x**    |
| L. Pesenti |           **x**           |                 |       **x**      |    **x**    | 

Oltre a ciò, si prevede il coinvolgimento attivo degli utenti attraverso sessioni di feedback periodiche per garantire un prodotto che rispecchi a pieno le necessità del cliente.

# 4. Standard, linee guida, procedure

Il progetto si atterrà alle convenzioni per la programmazione Java come definite da Oracle.
Le procedure per la documentazione saranno stabilite e rispettate per garantire una traccia chiara delle attività svolte durante l'intero ciclo di sviluppo.

# 5. Attività di gestione

Sarà essenziale assicurarsi che gli obiettivi del progetto risultino allineati con le aspettative degli utenti finali, pertanto si prevede di ricorrere ad una dashboard di progetto.
Tra le operazioni da non trascurare rientreranno anche il monitoraggio costante dello stato di avanzamento delle attività, il quale consente di individuare eventuali deviazioni rispetto al piano ed adottare misure correttive ove necessario, e la gestione accurata di requisiti, tempi e costi, in grado di garantire che le risorse vengano allocate efficacemente. 

# 6. Rischi

Le potenziali situazioni critiche, come la scarsa adozione dell'applicazione o i ritardi nella consegna, sono stati identificati.
Al fine di mitigare tali rischi si prevedono il coinvolgimento attivo degli utenti ed una pianificazione flessibile, con lo scopo di introdurre risorse aggiuntive in caso di necessità. 

# 7. Personale

La quantità di personale richiesta rimarrà invariata lungo l'intero processo di sviluppo.
Come dettagliato nel punto 3, i componenti del team si alterneranno nelle varie fasi del progetto, sulla base delle competenze pregresse e delle inclinazioni individuali.

# 8. Metodi e tecniche

Per l'elicitazione dei requisiti verranno impiegate le tecniche di intervista, analisi delle attività ed analisi degli scenari.
Successivamente a tali requisiti verrà assegnata una priorità sulla base dello schema MoSCoW.
Per le fasi di modellazione, strutturazione dell'architettura e design del software verrà utilizzato il tool StarUML, con cui si realizzeranno i diagrammi UML.
Le attività di testing verranno condotte tramite simulazioni di utilizzo dell'applicativo, oltre che prevedendo test di unità con JUnit.

# 9. Garanzia di qualità

Tenendo a mente il fatto che un buon software debba risultare il meno complesso possibile, dal punto di vista operativo i fattori di qualità a cui prestare maggiore attenzione saranno la non-suscettibilità ad input erronei (affidabilità) e l'intuitività nell'utilizzo (usabilità).
La portabilità verrà garantita dall'utilizzo del linguaggio Java, i cui programmi possono essere eseguiti su diverse piattaforme senza dover essere riscritti, grazie all'astrazione fornita dalla JVM.

# 10. Workpackages

Il progetto si articolerà in diverse attività, riportate a seguire:

| **Slot** |      **Activity**     |
|:--------:|:---------------------:|
|     1    |        Overview       |
|     2    |      Project Plan     |
|     3    | Requirements Planning |
|     4    |      UML Diagrams     |
|     5    |      User Design      |
|     6    |      Construction     |
|     7    |        Cutover        |

# 11. Risorse

Ogni componente della squadra si servirà del proprio laptop, oltre che dei comuni servizi di messaggistica per la comunicazione interna.
Si consulterà anche del materiale online al fine di determinare le complicazioni più comunemente riscontrate dagli utilizzatori di applicativi analoghi, ma anche le inefficienze caratterizzanti i registratori di cassa tradizionali.

# 12. Budget e programma

Ipotizzando, come da specifica, che ciascun membro dedichi 35 ore alla realizzazione del progetto, la pianificazione condurrà all'allocazione del lavoro nella seguente modalità:

| **Hours**|      **Activity**     |
|:--------:|:---------------------:|
|     5    |        Overview       |
|     10    |      Project Plan     |
|     25    | Requirements Planning |
|     20    |      UML Diagrams     |
|     20    |      User Design      |
|     20    |      Construction     |
|     5    |        Cutover        |

# 13. Cambiamenti

Trovandosi nel contesto di un processo agile, le modifiche introdotte ad ogni iterazione verranno gestite in modo leggero (senza essere, nel concreto, viste come cambiamenti) ed incorporate rapidamente.
I cambiamenti in questione saranno registrati, valutati in termini di costo ed implementati soltanto dopo essere stati approvati, oltre che giudicati congeniali. 

# 14. Consegna

Le procedure di consegna dell'applicativo al cliente saranno precedute da una revisione finale del software e dalla redazione di una documentazione completa.
Ultimato ciò, non dovrà passare in secondo piano l'organizzazione di sessioni formative per gli operatori che si interfacceranno con il prodotto.
