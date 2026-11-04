# Progettazione — ponte di controllo temporaneo AEmulWin

**Stato: soltanto progettazione. Nessuna API remota implementata, nessuna porta aperta, nessun token distribuito.** La prima APK di prova non richiede il permesso INTERNET.

## Perché non una porta sempre aperta
Un server HTTP accessibile da Internet sul telefono sarebbe fragile (NAT, cambio IP, limitazioni Android in background) e molto rischioso. ChatGPT non possiede automaticamente l'accesso di rete o un client per usarlo.

Proposta: l'app, dopo un consenso manuale, apre una sessione **in uscita** verso un relay HTTPS autenticato. Un agente autorizzato usa un connettore separato per comunicare col relay; il relay inoltra soltanto operazioni limitate all'ambiente Windows virtualizzato.

## Processo di autorizzazione
1. L'utente preme **Abilita collaudo remoto** dentro AEmulWin, quando la funzione esisterà.
2. L'app mostra durata breve, elenco dei permessi richiesti e un codice di pairing a uso singolo.
3. L'utente conferma la connessione e può interromperla con un pulsante sempre visibile.
4. Screenshot e log potenzialmente personali richiedono consenso specifico, con revisione locale.
5. La sessione scade ed elimina le credenziali effimere; nessuna attivazione automatica al riavvio.
6. I comandi e le risposte vengono auditati localmente e con limiti di frequenza/volume.

## Operazioni consentite (soggette a capability di sessione)
- status: versione dell'app, runtime Windows e memoria disponibile
- list_programs: elenco programmi importati **nell'ambiente Windows emulato**, non altre app Android
- open_program: avvio di un programma già autorizzato
- screenshot: cattura solo della finestra dell'emulatore, con permesso utente
- tap/swipe/mouse/key: interazione soltanto con il canvas del runtime Windows
- diagnostics: metriche e crash del solo motore, con censura dei dati sensibili
- stop: arresto di un programma o della sessione

## Non consentito
- Shell Android arbitraria, root, installazioni silenziose o comandi su altre app
- Leggere contatti, password, file privati o schermate di altre applicazioni
- Credenziali salvate in repository pubblici, telemetria automatica o log pubblicati su GitHub
- Accettare connessioni remote senza pairing e conferma utente
- Trattare il pairing come prova che ChatGPT disponga realmente di un connettore

## Tecnologia candidata
- WebSocket in uscita verso relay sicuro (da progettare), TLS e credenziali effimere
- Capability token limitati, identificativo di sessione non indovinabile e revoca immediata
- Un'interfaccia API con schema e versione, documentata separatamente
- Preferire inviare eventi in modo esplicito e limitato, senza streaming continuo finché non richiesto

## Risultato desiderato
Una volta sviluppato anche un connettore accessibile dall'assistente, si potrà chiedere di eseguire test nell'ambiente Windows sul Galaxy senza aprire una porta WAN. Nessuna di queste capacità è disponibile nella prima APK P0.
