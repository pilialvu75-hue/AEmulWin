# Scelta del motore Windows — ricerca tecnica

L'obiettivo iniziale è eseguire un programma Windows x86/x64 su Android ARM64, NON virtualizzare un intero Windows con relativa licenza.

## Candidati verificati
- **Winlator (brunodev85)**: codice Android, motore basato su Wine e Box86/Box64; sorgente app sotto licenza LGPL-2.1 nel repository winlator-app. Il suo runtime include ulteriori componenti sotto licenze diverse.
- **Wine**: strato di compatibilità Windows (Wine is not an emulator). Licenza Wine da verificare nella versione precisa adottata.
- **Box64**: traduzione eseguibili x86_64 su ARM64; verificare toolchain e requisiti hardware.
- **Mesa / VirGL / Zink e alternative grafiche**: va scelta una configurazione adeguata al chipset Samsung Exynos/Xclipse, evitando di dare per scontato supporto Turnip/Adreno.

## Approccio
1. Prima APK leggera di diagnostica Android e collaudo del telefono.
2. Inventario del motore Winlator e delle dipendenze con licenze, versioni, provenienza, file binari e hash.
3. Test di compatibilità CPU/GPU e raccolta errori sul dispositivo reale.
4. Adattamento con collegamenti chiari ai sorgenti e obblighi di redistribuzione, senza incollare codice indiscriminatamente.
5. Primo .exe semplice e piccolo; non testare giochi pesanti prima del funzionamento elementare.

## Fonti
- https://github.com/brunodev85/winlator
- https://github.com/brunodev85/winlator-app
- https://www.winehq.org/
- https://github.com/ptitSeb/box64

## Nota
La prima APK non include Wine, Box64, DLL Windows, immagini Microsoft, giochi o componenti di terze parti.
