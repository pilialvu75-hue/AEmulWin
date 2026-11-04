# AEmulWin — Windows applications on Android

**Progetto indipendente (nome GitHub provvisorio):** AEmulWin nasce per eseguire software Windows su Android, inizialmente su Galaxy S24 FE. **Questo repository NON è Ai-orchestrator-riserva e NON deve importare/modificare codice dal progetto principale.** Non è collegato a WEmulDroid.

> Stato iniziale: prototipo Android **P0 hardware/diagnostica**, NON un emulatore Windows già funzionante. Per eseguire applicazioni .exe servirà in una fase distinta un motore Wine + Box64 / alternativa equivalente verificata sul dispositivo, con licenze e dipendenze gestite correttamente.

## Perché iniziare con un prototipo leggero

Il Galaxy S24 FE ha 8 GB RAM: è più sensato raccogliere caratteristiche reali, controllare ABI, Android, memoria disponibile e schermo, e solo dopo installare un sistema Windows-compatibile più esigente. Il target non è un'installazione completa di Microsoft Windows: il primo obiettivo pratico è l'esecuzione di una semplice applicazione Windows tramite strato di compatibilità.

## Funzioni della prima APK

- Schermata indipendente in italiano
- Diagnostica RAM/ABI/schermo/batteria e versione Android, eseguita **solo dopo un tocco**
- Copia diagnostica negli appunti per condividerla volontariamente
- Stato del motore Windows: **non integrato**, senza simulare esecuzioni .exe
- Stato del ponte di controllo remoto: **disabilitato**; nessuna porta aperta, nessuna connessione di rete e nessuna esecuzione comandi

L'applicazione in questa fase non richiede permessi di rete né accesso a file privati.

## Roadmap e sicurezza

- [Roadmap verificabile](docs/ROADMAP.md)
- [Ponte remoto opt-in: progettazione](docs/REMOTE_CONTROL.md)
- [Scelta del motore e condizioni di licenza](docs/ENGINE_RESEARCH.md)

## Build

Progetto Android Java/Gradle minimale. La CI compila una APK **debug** quando il repository contiene tutti i file e pubblica un artefatto temporaneo di prova; l'esito CI non dimostra che il runtime Wine/Box64 funzioni.

## Indirizzo GitHub

Questo progetto è stato inizializzato nel repository vuoto pilialvu75-hue/Ai-orchestrator-riservas per evitare di crearne uno nuovo. **Rinominarlo appena possibile in AEmulWin** usando GitHub Settings → General → Repository name, per evitare confusione col progetto principale.

## Licenze

Il codice originale del launcher è MIT. Wine/Winlator/Box64 e gli altri componenti esterni restano soggetti alle loro specifiche licenze: nessun componente terzo è stato copiato o redistribuito in questa fase.
