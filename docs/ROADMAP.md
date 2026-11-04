# AEmulWin — roadmap separata

## Principi vincolanti

- Repository indipendente, da rinominare AEmulWin.
- Non aprire PR, non toccare file e non avviare build in Ai-orchestrator-riserva, WEmulDroid o altri progetti.
- Primo target: Samsung Galaxy S24 FE, ARM64, 8 GB RAM: test brevi, controllare surriscaldamento e memoria disponibile.
- Non affermare che la shell Android sia già un emulatore Windows.
- Scaricare componenti esterni solo da sorgenti affidabili e rispettare ogni licenza.

## P0 — App Android funzionante
- [x] Separazione repo e primo launcher Android senza rete
- [x] Diagnostica risorse, ABI, display e batteria
- [x] Ponte remoto disabilitato per impostazione predefinita
- [ ] Compilazione Android debug con CI PASS
- [ ] Installazione e avvio sul Samsung reale
- [ ] Report locale e screenshot testati
- [ ] Monitorare RAM e temperatura durante i primi test

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

## Criteri di accettazione
1. L'app diagnostica si apre sul telefono senza permessi extra.
2. L'ambiente Windows reale avvia un eseguibile semplice.
3. L'esecuzione Windows non interferisce con altre app Android.
4. I consumi e i problemi vengono registrati con limiti chiari.
5. La sessione remota non si attiva da sola ed è revocabile.
