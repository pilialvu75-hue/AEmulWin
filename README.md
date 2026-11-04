# AEmulWin — applicazioni Windows su Android

**Repository ufficiale: pilialvu75-hue/AEmulWin.** Progetto autonomo destinato inizialmente al Samsung Galaxy S24 FE. NON modifica Ai-orchestrator-riserva e NON dipende da WEmulDroid.

**Stato reale:** codice Android **0.4.0 P0 — diagnostica, interfaccia e menu Aggiornamenti**; non è ancora un emulatore Windows eseguibile. L'integrazione Wine/Box64 avverrà soltanto dopo i test del telefono, con verifiche hardware e licenze.

## Aggiornamenti dall'interno dell'app — versione 0.4

La nuova interfaccia prevede **Impostazioni → Aggiornamenti**, con canale **Stabile** o **Beta**, controllo su richiesta, download ufficiali GitHub via HTTPS, verifica SHA-256 e installazione tramite il sistema Android con conferma dell'utente.

**Manca ancora un passaggio obbligatorio:** configurare una chiave di firma APK **permanente** nei segreti GitHub, per poter sovrapporre le future versioni senza perdita dati. La pipeline di release firmate è predisposta ma non può pubblicare finché non sono presenti i segreti. L'aggiornamento completo sul Galaxy NON è ancora collaudato.

La vecchia APK 0.3 debug può avere una firma differente: **se non l'hai ancora installata e vuoi evitare una successiva reinstallazione di AEmulWin, conviene attendere la prima APK con firma permanente**. Non occorre toccare Ai-orchestrator-riserva.

[Guida aggiornamenti e requisito firma](docs/UPDATES.md)

## APK diretta per il Galaxy S24 FE (senza ZIP Actions)

**Vecchia release diagnostica 0.3.0-test** (NON ancora aggiornabile in modo affidabile, perché usa la firma debug effimera):

https://github.com/pilialvu75-hue/AEmulWin/releases/download/v0.3.0-test/AEmulWin-0.3.0-test.apk

- [Pagina della release e checksum SHA-256](https://github.com/pilialvu75-hue/AEmulWin/releases/tag/v0.3.0-test)
- [Build CI Android PASS](https://github.com/pilialvu75-hue/AEmulWin/actions/runs/41754374993)

Il file è un'APK debug di prova. Può richiedere l'autorizzazione del browser per installare applicazioni. La firma debug creata su GitHub potrebbe differire da quella delle vecchie build e quindi impedire l'aggiornamento di **AEmulWin**; non toccare né disinstallare Ai-orchestrator-riserva.

## Prima prova con la vecchia APK diagnostica 0.3 — nessun benchmark pesante

1. Installa e apri l'APK 0.3.0 sul S24 FE.
2. Seleziona **English, Italiano, Français** e prova che la scelta rimanga dopo la chiusura.
3. Premi **Analizza questo telefono** (o l'equivalente nella lingua scelta), poi **Copia diagnosi**.
4. Apri **Rete e connessione remota**: la sessione remota deve indicare **SCOLLEGATO**, attivazione non disponibile e interruzione presente.
5. Attiva/disattiva **Consenti Internet ai programmi Windows (futuro)**, chiudi e riapri l'app: la preferenza deve conservarsi.
6. Non vengono avviati programmi Windows: il loro motore non è ancora installato.

## Permessi Internet: due livelli separati

Android concede il permesso **INTERNET** all'installazione: non appare come richiesta manuale revocabile allo stesso modo di fotocamera e microfono. AEmulWin 0.3.0 dichiara **INTERNET** e **ACCESS_NETWORK_STATE** per preparare applicazioni Windows che richiedano rete.

Il menu contiene una preferenza dedicata ai **futuri programmi Windows**, disattivata inizialmente. Oggi **non cambia i permessi Android e non può bloccare una rete Windows inesistente**. Quando integreremo il motore, esso dovrà applicare davvero questa regola e dimostrare che OFF impedisce i collegamenti di rete dei programmi ospitati.

Il **controllo remoto** è separato dal traffico dei programmi Windows: non è implementato, non apre porte e non effettua chiamate remote in questa versione.

## Lingue

- English
- Italiano
- Français

Menu e report diagnostici disponibili nelle tre lingue, con scelta salvata localmente.

## Sicurezza, licenze, roadmap

- [Sistema di aggiornamento interno e firma permanente](docs/UPDATES.md)

- [Roadmap indipendente](docs/ROADMAP.md)
- [Controllo remoto opt-in — non ancora operativo](docs/REMOTE_CONTROL.md)
- [Idea futura dell'hub Android multi-app](docs/ANDROID_HUB_FUTURE.md)
- [Valutazione Wine/Box64/Winlator e licenze](docs/ENGINE_RESEARCH.md)

La prima APK non distribuisce Wine, Box64, DLL di Windows o altro codice di terzi. Il launcher originale è MIT; i componenti futuri manterranno le rispettive licenze.

Le prime compilazioni sono PASS; **la nuova versione 0.4 e la funzione di aggiornamento devono ancora essere collaudate sul Galaxy fisico**. Non confondere la vecchia APK pubblica 0.3 con la nuova versione in sviluppo.
