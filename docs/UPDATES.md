# AEmulWin — Aggiornamenti dall'app

## Percorso

Dalla versione 0.4 del codice: **Impostazioni → Aggiornamenti**.

- Il canale predefinito è **Stabile**. È possibile includere le **Beta** tramite un interruttore.
- Il controllo delle nuove versioni viene effettuato **solo premendo Controlla aggiornamenti**.
- La ricerca utilizza le release GitHub ufficiali del repository pilialvu75-hue/AEmulWin.
- Prima del download l'utente conferma la versione.
- L'app scarica su HTTPS da GitHub, confronta SHA-256, controlla formato APK, package `org.aemulwin` e versionCode crescente.
- **Installa** apre il PackageInstaller ufficiale Android, che richiede l'approvazione dell'utente. L'app non può installarsi silenziosamente.
- Android rifiuta APK non firmati con la **stessa chiave della versione installata**. Nessun bypass.

## IMPORTANTE — requisito di firma non ancora completato

La vecchia APK **0.3.0-test** era compilata con una chiave di debug temporanea su un runner GitHub. Il runner è effimero e la chiave privata non è stata conservata: non è possibile garantire un aggiornamento sovrapponibile da questa APK.

Per ottenere **aggiornamenti continui senza reinstallazioni successive** occorre creare UNA SOLA VOLTA una chiave di firma permanente, conservarla privatamente ed esporla ai workflow GitHub tramite segreti cifrati. Il repository pubblico non deve mai contenerla.

### Segreti necessari (Settings → Secrets and variables → Actions)

- `AEMULWIN_KEYSTORE_B64`: keystore privato codificato base64
- `AEMULWIN_STORE_PASSWORD`: password del keystore
- `AEMULWIN_KEY_ALIAS`: alias della chiave
- `AEMULWIN_KEY_PASSWORD`: password della chiave

Il workflow manuale [Publish signed AEmulWin update](../.github/workflows/publish-signed.yml) si rifiuta di pubblicare una release se manca anche un solo segreto. Non ristampa i valori dei segreti e genera un APK firmato con la stessa chiave a ogni release, salvo rotazioni decise esplicitamente.

**Non pubblicare segreti, keystore o password in issue, commit o chat pubbliche. Non reinstallare Ai-orchestrator-riserva; è un'applicazione diversa.**

Se AEmulWin 0.3 debug è già installata, **potrebbe essere necessario un unico passaggio manuale di sostituzione** per iniziare a utilizzare una futura APK con firma permanente; non tentare forzature che aggirino i controlli Android. Questa limitazione riguarda SOLO AEmulWin.

## Stato delle prove

- [x] Implementazione UI EN/IT/FR, canali stabile/beta, rete HTTPS, SHA-256, verifica APK e handoff PackageInstaller.
- [x] CI Android di debug del codice di aggiornamento.
- [x] Workflow per release firmate, che richiede una chiave privata persistente.
- [ ] Configurare la chiave di firma e la sua custodia sicura.
- [ ] Pubblicare la prima APK 0.4.0 beta firmata in modo permanente.
- [ ] Collaudo reale sul Galaxy S24 FE: controllo, download, installazione approvata, riavvio e dati conservati.
- [ ] Collaudo di una **seconda versione** firmata con la stessa chiave per dimostrare l'upgrade senza perdita dati.

Non dichiarare già riuscito l'aggiornamento end-to-end sul telefono.

## Privacy e sicurezza

La rete usata per cercare versioni è indipendente dalla preferenza **Internet dei programmi Windows emulati** e dalla funzione **Controllo remoto** (quest'ultima resta disabilitata). Nessun APK viene scaricato o installato automaticamente in background.
