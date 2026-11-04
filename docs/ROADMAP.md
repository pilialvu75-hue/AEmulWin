# AEmulWin — roadmap separata

## Principi vincolanti

- Repository ufficiale e indipendente: `pilialvu75-hue/AEmulWin` (rinomina verificata).
- Non aprire PR, non toccare file e non avviare build in Ai-orchestrator-riserva, WEmulDroid o altri progetti.
- Primo target: Samsung Galaxy S24 FE, ARM64, 8 GB RAM: test brevi, controllare surriscaldamento e memoria disponibile.
- Non affermare che la shell Android sia già un emulatore Windows.
- Scaricare componenti esterni solo da sorgenti affidabili e rispettare ogni licenza.

## P0 — App Android funzionante
- [x] Separazione repo e primo launcher Android senza rete
- [x] Diagnostica risorse, ABI, display e batteria
- [x] Ponte remoto disabilitato per impostazione predefinita
- [x] Menu lingua English / Italiano / Français con scelta persistente (collaudo reale da fare)
- [x] Menu connessione con stato scollegato, attivazione bloccata e stop/revoca disponibile (nessuna rete/porta attiva)
- [ ] Verificare sul Galaxy tutte le 3 lingue, salvataggio scelta e menu connessione
- [x] Prima compilazione diagnostica Android P0 con CI PASS
- [x] Compilazione Android P0.3 (lingue, menu rete e connessione) con CI PASS
- [x] APK P0.3 pubblicata come release GitHub con download diretto per smartphone
- [x] Permessi Android INTERNET / ACCESS_NETWORK_STATE disponibili per i programmi Windows futuri
- [x] Preferenza Internet programmi Windows (OFF iniziale) distinta da remoto, oggi non operativa
- [ ] Dimostrare nel futuro motore che lo switch OFF blocchi davvero le connessioni dei programmi Windows
- [ ] Installazione e avvio sul Samsung reale
- [ ] Test persistenza preferenza rete Windows e sua indipendenza dal comando remoto
- [ ] Report locale e screenshot testati
- [ ] Monitorare RAM e temperatura durante i primi test

## P0.4 — Aggiornamenti dall'app (in corso)

- [x] Impostazioni → Aggiornamenti, lingua EN/IT/FR, canali stabile/beta
- [x] Download solo da release GitHub ufficiali via HTTPS e verifica SHA-256
- [x] Controlli packageName, numero versione e conferma del PackageInstaller Android
- [x] Workflow manuale di release firmate, che rifiuta pubblicazioni senza segreti
- [x] VersionCode progressivi, distinti tra beta e stabile
- [ ] Configurare una volta la chiave di firma privata permanente nei segreti GitHub
- [ ] Pubblicare prima beta firmata e verificare l'APK sul Galaxy S24 FE
- [ ] Pubblicare una seconda versione con la medesima firma e provarne aggiornamento e conservazione dati
- [ ] Verificare le lingue, il canale stabile/beta, gli errori di rete, lo spazio disco e le interruzioni download

**Limite:** la vecchia versione 0.3 debug usa una firma effimera e non può essere aggiornata in modo affidabile a quella con firma definitiva. I dati di Ai-orchestrator-riserva non sono coinvolti. Vedi [UPDATES.md](UPDATES.md).

## P1 — Primo vero programma Windows
- Valutare versione e licenza dei sorgenti originali Winlator, Wine, Box64 e Mesa
- Scoprire se l'hardware Exynos / Xclipse ha percorso grafico compatibile (possibile VirGL/Zink/Vortek; Turnip è per Adreno)
- Preparare un runtime separato e sandboxed, senza copiare indiscriminatamente binari
- Import file .exe locale, con consenso e isolando filesystem
- Primo test con programma Windows semplice e legalmente distribuibile (es. esempio originale o eseguibile libero)
- Verificare la finestra Windows reale, interazioni touch/mouse, audio e arresto pulito
- Log e diagnostica per fallimenti; non avviare stress test sul telefono

## P2 — Usabilità
- Profili schermo/risoluzione e fullscreen
- Tastiera virtuale Windows, mouse touch, pressione prolungata, scroll, gamepad
- Import da cartella, repository e URL HTTPS sotto conferma
- Gestione locale spazio disco, RAM e memoria/snapshot
- Notifiche diagnostiche e feedback in-app
- Supporto multiarchitettura e motori opzionali in seguito, in base a test reali

## P3 — Ponte di collaudo AI
- Sessione autorizzata manualmente dall'utente; disabilitata per impostazione predefinita
- Connessione TLS in uscita a relay autenticato, senza porta pubblica in ascolto
- Pairing, consenso per ciascuna sessione, scadenza, revoke e kill switch
- API con comandi consentiti: screenshot, tocco, input, elenco app emulatore, avvia programma, report
- Vietata esecuzione shell Android arbitraria, accesso ad altre app o ai dati privati
- Audit locale e revisione prima di inviare log o screenshot
- Ponte compatibile con un connettore effettivamente utilizzabile da ChatGPT; senza quel componente l'assistente non può entrare nel telefono

## P4 — Progetto HUB Android (parcheggiato, non da implementare ora)
- Connettore indipendente che collega un agente autorizzato ad AEmulWin e, in futuro, ad altre app Android che scelgono di integrarlo.
- Nessuna modifica ad Ai-orchestrator-riserva nella fase attuale; qualsiasi interfaccia verso quel progetto sarà valutata separatamente.
- Il connettore deve separare app, credenziali, ambiti e autorizzazioni; nessun accesso generalizzato al telefono.
- Requisiti e valutazioni preliminari documentati in [ANDROID_HUB_FUTURE.md](ANDROID_HUB_FUTURE.md).

## Criteri di accettazione
1. L'app diagnostica si apre sul telefono senza permessi extra.
2. L'ambiente Windows reale avvia un eseguibile semplice.
3. L'esecuzione Windows non interferisce con altre app Android.
4. I consumi e i problemi vengono registrati con limiti chiari.
5. La sessione remota non si attiva da sola ed è revocabile.
