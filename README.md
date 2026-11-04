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

## Prima APK diagnostica (build verificata)

La prima APK debug compilata è disponibile nel workflow [Android P0 smoke APK — PASS](https://github.com/pilialvu75-hue/AEmulWin/actions/runs/41749819008) come artefatto **AEmulWin-P0-debug-APK** (ZIP contenente app-debug.apk). La pubblicazione dell'artefatto è temporanea, secondo la politica GitHub Actions impostata a 7 giorni.

Per provarla su Galaxy S24 FE:
1. Apri il workflow GitHub da un browser con accesso all'account.
2. Scarica l'artefatto AEmulWin-P0-debug-APK.
3. Estrai l'APK dallo ZIP e installa l'app AEmulWin, autorizzando l'installazione esterna solo se richiesta.
4. Apri AEmulWin e premi **Analizza questo telefono**. Se desideri, premi **Copia diagnosi** e incolla il report nella chat.
5. Non eseguire giochi o test pesanti: questa fase serve soltanto a verificare l'app e le informazioni hardware.

**La CI Android ha completato la compilazione, ma l'installazione e l'apertura dell'app sul telefono fisico non sono state ancora verificate.** La prima APK non contiene un motore Windows e non ha funzioni di aggiornamento automatico; una modalità di aggiornamento in-app controllata sarà valutata successivamente.

## Indirizzo GitHub

Il repository originariamente vuoto è stato rinominato correttamente in **pilialvu75-hue/AEmulWin**. Rimane distinto dal progetto principale Ai-orchestrator-riserva.

## Licenze

Il codice originale del launcher è MIT. Wine/Winlator/Box64 e gli altri componenti esterni restano soggetti alle loro specifiche licenze: nessun componente terzo è stato copiato o redistribuito in questa fase.
