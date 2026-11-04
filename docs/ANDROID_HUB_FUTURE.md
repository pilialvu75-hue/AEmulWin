# Idea futura — Android Connector Hub (NON avviare ora)

## Intuizione
Un ponte Android riutilizzabile che possa offrire, **solo con autorizzazione dell'utente**, strumenti di collaudo tra ChatGPT / un agente compatibile e più applicazioni Android che scelgono esplicitamente di integrarlo.

Possibili client futuri:
- AEmulWin: controllo limitato al suo ambiente Windows compatibile.
- Ai-orchestrator-riserva: **soltanto in una fase futura e con un progetto separato autorizzato**. Non modificarlo per questo lavoro.
- Altre applicazioni Android: integrazione tramite SDK/API opt-in, senza accesso arbitrario a ciò che non espongono.

## Architettura ipotizzata
- **Hub** separato, con versionamento API e plugin/client per singola app.
- Una connessione uscente cifrata dal dispositivo verso un relay affidabile (senza server pubblico in ascolto).
- Pairing approvato sul telefono, ruoli e capacità **per app e per sessione**.
- Comandi strettamente tipizzati; i privilegi non transitano automaticamente fra app.
- Scadenza breve, disconnessione sempre visibile, revoca immediata, audit locale, limiti di frequenza.
- Screenshot/log personali soltanto su autorizzazione separata e con possibilità di revisione.
- Chiave/token **mai** pubblicati su GitHub.

## Vincolo di fattibilità
Avere un hub non dà accesso automatico a ChatGPT: serve anche un connettore o strumento compatibile, installato e autorizzato dall'utente. Android non permette a una semplice app di controllare liberamente applicazioni altrui.

## Stato
**Solo proposta aggiunta alla roadmap.** Nessun codice del connettore, nessun relay, nessuna rete, nessun permesso di controllo remoto nella APK P0.2. Riprenderemo il progetto dopo il primo vero test di AEmulWin e dopo l'integrazione del runtime Windows.
