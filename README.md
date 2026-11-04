# AEmulWin — Windows applications on Android

**Progetto indipendente — repository ufficiale `pilialvu75-hue/AEmulWin`:** AEmulWin nasce per eseguire software Windows su Android, inizialmente su Galaxy S24 FE. **Questo repository NON è Ai-orchestrator-riserva e NON deve importare/modificare codice dal progetto principale.** Non è collegato a WEmulDroid.

> Stato iniziale: prototipo Android **P0 hardware/diagnostica**, NON un emulatore Windows già funzionante. Per eseguire applicazioni .exe servirà in una fase distinta un motore Wine + Box64 / alternativa equivalente verificata sul dispositivo, con licenze e dipendenze gestite correttamente.

## Perché iniziare con un prototipo leggero

Il Galaxy S24 FE ha 8 GB RAM: è più sensato raccogliere caratteristiche reali, controllare ABI, Android, memoria disponibile e schermo, e solo dopo installare un sistema Windows-compatibile più esigente. Il target non è un'installazione completa di Microsoft Windows: il primo obiettivo pratico è l'esecuzione di una semplice applicazione Windows tramite strato di compatibilità.

## Funzioni della prima APK

- Menu lingua **English / Italiano / Français**: cambia tutti i testi della UI e della diagnosi e conserva la scelta ai successivi avvii
- Diagnostica RAM/ABI/schermo/batteria e versione Android, eseguita **solo dopo un tocco**
- Copia diagnostica negli appunti per condividerla volontariamente
- Stato del motore Windows: **non integrato**, senza simulare esecuzioni .exe
- **Menu Connessione remota** con stato visibile, futuro pulsante di attivazione disabilitato finché non esiste un relay autenticato, e pulsante **Interrompi / revoca** già presente
- Ponte di controllo remoto **non implementato in questa APK**: nessuna porta aperta, nessuna connessione di rete e nessuna esecuzione comandi

L'applicazione in questa fase non richiede permessi di rete né accesso a file privati.

## Roadmap e sicurezza

- [Roadmap verificabile](docs/ROADMAP.md)
- [Ponte remoto opt-in: progettazione](docs/REMOTE_CONTROL.md)
- [Proposta futura: hub Android condiviso](docs/ANDROID_HUB_FUTURE.md)
- [Scelta del motore e condizioni di licenza](docs/ENGINE_RESEARCH.md)

## Prima APK diagnostica (build verificata)

Le APK debug sono pubblicate come artefatto **AEmulWin-P0-debug-APK** (ZIP contenente `app-debug.apk`) nella [pagina Actions](https://github.com/pilialvu75-hue/AEmulWin/actions/workflows/android-debug.yml). Seleziona la **build verde più recente**: l'artefatto è temporaneo e ha conservazione impostata a 7 giorni.

Per provarla su Galaxy S24 FE:
1. Apri il workflow GitHub da un browser con accesso all'account.
2. Scarica l'artefatto AEmulWin-P0-debug-APK.
3. Estrai l'APK dallo ZIP e installa l'app AEmulWin, autorizzando l'installazione esterna solo se richiesta.
4. Apri AEmulWin e prova il menu **Language / Lingua / Langue**, scegliendo a turno **English**, **Italiano**, **Français**; verifica che la scelta rimanga anche dopo aver chiuso e riaperto l'app.
5. Premi **Inspect this phone / Analizza questo telefono / Analyser ce téléphone**, poi **Copy / Copia / Copier** se vuoi condividere il report.
6. Apri il **menu connessione remota**: lo stato deve indicare scollegato, **Attiva** deve risultare disabilitato e **Interrompi / revoca** deve essere utilizzabile senza creare connessioni.
7. Non eseguire giochi o test pesanti: questa fase serve soltanto a verificare l'interfaccia e le informazioni hardware.

**Il primo APK è stato compilato, ma l'app aggiornata e la sua nuova interfaccia devono ancora superare la CI e il test sul telefono fisico.** Non contiene un motore Windows, non consente l'accesso remoto e non include ancora aggiornamenti in-app.

## Indirizzo GitHub

Il repository originariamente vuoto è stato rinominato correttamente in **pilialvu75-hue/AEmulWin**. Rimane distinto dal progetto principale Ai-orchestrator-riserva.

## Licenze

Il codice originale del launcher è MIT. Wine/Winlator/Box64 e gli altri componenti esterni restano soggetti alle loro specifiche licenze: nessun componente terzo è stato copiato o redistribuito in questa fase.
