# 1. Introduzione

Il progetto nasce dalla volontà di proporre una soluzione intuitiva ed efficace per la gestione dell'inventario e la registrazione degli scontrini nel contesto di piccoli esercizi commerciali (botteghe, minimarket, ecc.), ancora diffusi nelle aree meno densamente popolate.<br>
Nello specifico, l'operatore di cassa potrà usufruire del sistema (fornito sotto forma di *applicazione desktop*) interagendo con un'interfaccia grafica, la quale faciliterà l'emissione degli scontrini e la consultazione dell'elenco dei prodotti, dei relativi prezzi e delle quantità rimanenti.<br>
Ad occuparsi del lavoro saranno Alessandro Mangini, Alessandro Pesenti e Luca Pesenti.

# 2. Modello di processo

Si seguirà l'approccio allo sviluppo software noto come **RAD** (***Rapid Application Development***), il quale risulta particolarmente adatto allo sviluppo di applicativi guidati dai requisiti dell'interfaccia utente.<br>
Tale modello di sviluppo permette di delineare sin da subito le funzioni chiave necessarie a soddisfare le esigenze dell'utenza, rifinendole poi sulla base delle prove effettuate nelle fasi iniziali, deputate alla prototipazione.<br>
Intraprendere un approccio evolutivo significa anche minimizzare le chance di incappare in intricate situazioni di errore che contraddistinguono, ad esempio, i grandi progetti basati sul processo di sviluppo a cascata.

# 3. Organizzazione del progetto

Il team di sviluppo farà riferimento ad un'organizzazione a matrice:

| **Member** | **Requirements Planning** | **User Design** | **Construction** | **Cutover** |
|:----------:|:-------------------------:|:---------------:|:----------------:|:-----------:|
| A. Mangini |           **x**           |      **x**      |                  |    **x**    |
| A. Pesenti |                           |      **x**      |       **x**      |    **x**    |
| L. Pesenti |           **x**           |                 |       **x**      |    **x**    | 

Oltre a ciò, si prevede il coinvolgimento attivo degli utenti finali attraverso sessioni di feedback periodiche, con la finalità di garantire un prodotto che ne rispecchi a pieno le necessità.

# 4. Standard, linee guida, procedure

Il progetto si atterrà alle convenzioni per la programmazione Java come definite da Oracle.<br>
Le procedure per la stesura della documentazione saranno stabilite e rispettate per definire una traccia chiara delle attività svolte durante l'intero ciclo di sviluppo.

# 5. Attività di gestione

Sarà essenziale assicurarsi che gli obiettivi del progetto risultino allineati con le aspettative degli utenti finali, pertanto si prevede di ricorrere ad una dashboard di progetto.<br>
Tra le operazioni da non trascurare rientreranno anche il monitoraggio costante dello stato di avanzamento delle attività, il quale consentirà di individuare eventuali deviazioni rispetto al piano originario ed adottare conseguenti misure correttive, e la gestione accurata di requisiti, tempi e costi, in grado di assicurare che le risorse vengano allocate efficacemente.

# 6. Rischi

Le potenziali situazioni critiche, quali la scarsa adozione dell'applicazione o i ritardi nella consegna della stessa, sono stati identificati.<br>
Al fine di prevenire tali circostanze si prevedono il coinvolgimento attivo degli utenti ed una pianificazione flessibile, la quale permette di introdurre risorse aggiuntive in caso di necessità.

# 7. Personale

La quantità di personale richiesta rimarrà invariata lungo l'intero processo di sviluppo.<br>
Come dettagliato nel [punto 3](#3-organizzazione-del-progetto), i componenti del team si alterneranno nelle varie fasi di lavoro, tenendo in considerazione le competenze pregresse e le propensioni individuali.

# 8. Metodi e tecniche

Per l'elicitazione dei requisiti verranno, oltre alle interviste sul campo, impiegate le tecniche più organiche di analisi delle attività ed analisi degli scenari.<br>
Successivamente, a tali requisiti verrà assegnata una priorità sulla base dello schema **MoSCoW**.<br>
Per le fasi di modellazione, strutturazione dell'architettura e design del software verrà utilizzato il tool ***StarUML***.<br>
Le operazioni di testing verranno condotte tramite simulazioni di utilizzo dell'applicativo, oltre che prevedendo test di unità con **JUnit** all'interno dell'ambiente di sviluppo (***Eclipse IDE***).

# 9. Garanzia di qualità

Tenendo a mente il fatto che un buon software debba risultare il meno complesso possibile, dal punto di vista operativo i fattori di qualità a cui prestare maggiore attenzione saranno la non-suscettibilità ad input erronei (*affidabilità*) e l'intuitività nell'utilizzo (*usabilità*).<br>
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

Ogni componente della squadra si servirà del proprio laptop, oltre che dei comuni servizi di messaggistica per la comunicazione interna.<br>
Si consulterà anche del materiale online al fine di determinare le complicazioni più comunemente sperimentate dagli utilizzatori di applicativi analoghi, nonché le inefficienze caratterizzanti i registratori di cassa tradizionali.

# 12. Budget e programma

Ipotizzando, come da specifica, che ciascun membro dedichi circa 35 ore alla realizzazione dell'applicativo, si procederà alla suddivisione del lavoro nella seguente modalità:

| **Hours** |      **Activity**     |
|:---------:|:---------------------:|
|     5     |        Overview       |
|     10    |      Project Plan     |
|     25    | Requirements Planning |
|     20    |      UML Diagrams     |
|     20    |      User Design      |
|     20    |      Construction     |
|     5     |        Cutover        |

# 13. Cambiamenti

Operando nel contesto di un modello di processo agile, le modifiche introdotte in fase di prototipazione verranno gestite in modo leggero (senza essere, nel concreto, viste come cambiamenti rilevanti) ed incorporate rapidamente.<br>
Ciononostante i cambiamenti in questione saranno registrati, valutati in termini di rapporto costi/benefici ed implementati soltanto dopo essere stati giudicati congeniali.

# 14. Consegna

La procedura di consegna dell'applicativo al cliente sarà preceduta da una revisione finale del software e dalla rifinitura della documentazione, con l'obiettivo di migliorarne la capacità illustrativa.<br>
Ultimato ciò, non dovrà passare in secondo piano la predisposizione di sessioni formative per gli operatori che andranno ad interfacciarsi con il prodotto.
