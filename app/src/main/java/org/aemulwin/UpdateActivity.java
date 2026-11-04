package org.aemulwin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Settings -> Updates. Network and download are user-initiated only. */
public final class UpdateActivity extends Activity {
    private static final int BG = Color.rgb(14, 19, 30);
    private static final int FG = Color.rgb(239, 241, 246);
    private static final int MUTED = Color.rgb(170, 188, 207);
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    private int lang = 1;
    private TextView status;
    private Button check, download, install;
    private CheckBox allowBeta;
    private UpdateClient.Candidate candidate;

    // English, Italiano, Français
    private static final String[][] TEXT = {
        {"Settings → Updates", "Current version", "Include beta releases",
         "Check for updates", "Download verified APK", "Install downloaded update",
         "Only official AEmulWin GitHub releases. Android must approve installation. "
             + "The installed APK and the update MUST have the same signing certificate.",
         "Ready. No network request has been made.", "Checking official releases...",
         "No newer release found on the selected channel.",
         "Available release", "Download this update?", "Downloading and verifying SHA-256...",
         "APK downloaded and verified. Tap Install for the Android system confirmation.",
         "Installation will be approved in Android. If you are asked to permit this app as an installer, return here and tap Install again.",
         "Update failed: ", "Back"},
        {"Impostazioni → Aggiornamenti", "Versione installata", "Includi versioni beta",
         "Controlla aggiornamenti", "Scarica APK verificato", "Installa aggiornamento scaricato",
         "Solo release ufficiali GitHub di AEmulWin. L'installazione richiede conferma Android. "
             + "L'APK installata e quella nuova DEVONO avere la stessa firma.",
         "Pronto. Nessun controllo Internet effettuato.", "Controllo delle release ufficiali...",
         "Nessuna versione più recente trovata nel canale selezionato.",
         "Release disponibile", "Scaricare questo aggiornamento?", "Download e verifica SHA-256...",
         "APK scaricata e verificata. Premi Installa per la conferma di Android.",
         "Android chiederà di autorizzare l'installazione. Se devi abilitare l'origine, torna qui e premi nuovamente Installa.",
         "Aggiornamento non riuscito: ", "Indietro"},
        {"Paramètres → Mises à jour", "Version installée", "Inclure les versions bêta",
         "Rechercher des mises à jour", "Télécharger l'APK vérifié", "Installer la mise à jour",
         "Versions officielles GitHub d'AEmulWin uniquement. Android doit confirmer l'installation. "
             + "Les APK doivent avoir exactement la même signature.",
         "Prêt. Aucune connexion réseau effectuée.", "Recherche des versions officielles...",
         "Aucune version plus récente dans le canal choisi.",
         "Version disponible", "Télécharger cette mise à jour ?", "Téléchargement et contrôle SHA-256...",
         "APK téléchargé et vérifié. Touchez Installer pour la confirmation Android.",
         "Android demandera une autorisation d'installation. Si nécessaire, revenez et touchez Installer à nouveau.",
         "Échec de mise à jour : ", "Retour"}
    };

    private String t(int key) { return TEXT[lang][key]; }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String l = getSharedPreferences("aemulwin_ui", MODE_PRIVATE)
                .getString("language", "it");
        lang = "en".equals(l) ? 0 : "fr".equals(l) ? 2 : 1;
        createScreen();
    }

    private TextView text(String value, int size, boolean bold) {
        TextView label = new TextView(this);
        label.setText(value);
        label.setTextColor(FG);
        label.setTextSize(size);
        if (bold) label.setTypeface(null, Typeface.BOLD);
        return label;
    }

    private Button button(String label) {
        Button result = new Button(this);
        result.setText(label);
        result.setAllCaps(false);
        return result;
    }

    private void add(LinearLayout parent, View view, int marginTop) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = marginTop;
        parent.addView(view, params);
    }

    private void createScreen() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (22 * getResources().getDisplayMetrics().density);
        content.setPadding(padding, padding, padding, padding);
        scroll.addView(content);
        setContentView(scroll);

        add(content, text(t(0), 25, true), 15);
        String current = installedVersion();
        add(content, text(t(1) + ": " + current, 15, false), 14);
        allowBeta = new CheckBox(this);
        allowBeta.setText(t(2));
        allowBeta.setTextColor(FG);
        allowBeta.setChecked(getSharedPreferences("aemulwin_ui", MODE_PRIVATE)
                .getBoolean("updates_beta", false));
        allowBeta.setOnCheckedChangeListener((view, checked) -> {
            getSharedPreferences("aemulwin_ui", MODE_PRIVATE).edit()
                .putBoolean("updates_beta", checked).apply();
            candidate = null;
            download.setEnabled(false);
            status.setText(t(7));
        });
        add(content, allowBeta, 20);
        check = button(t(3));
        add(content, check, 12);
        download = button(t(4));
        download.setEnabled(false);
        add(content, download, 10);
        install = button(t(5));
        install.setEnabled(UpdateClient.stagedFile(this).isFile());
        add(content, install, 10);

        status = text(t(7), 15, false);
        status.setTextColor(MUTED);
        add(content, status, 24);
        add(content, text(t(6), 14, false), 24);
        Button back = button(t(16));
        add(content, back, 24);
        back.setOnClickListener(v -> finish());

        check.setOnClickListener(v -> checkUpdates(current));
        download.setOnClickListener(v -> askDownload());
        install.setOnClickListener(v -> {
            try {
                UpdateClient.promptInstall(this);
            } catch (Exception error) {
                status.setText(t(15) + error.getMessage());
            }
        });
    }

    private String installedVersion() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) { return "0.0.0"; }
    }

    private void checkUpdates(String current) {
        check.setEnabled(false);
        download.setEnabled(false);
        candidate = null;
        status.setText(t(8));
        boolean includeBeta = allowBeta.isChecked();
        worker.execute(() -> {
            try {
                UpdateClient.Candidate result = UpdateClient.findUpdate(includeBeta, current);
                runOnUiThread(() -> {
                    candidate = result;
                    status.setText(result == null ? t(9) :
                            t(10) + ": " + result.tag +
                            (result.beta ? " (beta)" : ""));
                    download.setEnabled(result != null);
                    check.setEnabled(true);
                });
            } catch (Exception error) {
                runOnUiThread(() -> {
                    status.setText(t(15) + error.getMessage());
                    check.setEnabled(true);
                });
            }
        });
    }

    private void askDownload() {
        final UpdateClient.Candidate selected = candidate;
        if (selected == null) return;
        new AlertDialog.Builder(this).setTitle(t(11))
                .setMessage(selected.tag + "\n" + UpdateClient.REPO)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {})
                .setPositiveButton(android.R.string.ok, (dialog, which) -> download(selected))
                .show();
    }

    private void download(UpdateClient.Candidate selected) {
        check.setEnabled(false);
        download.setEnabled(false);
        install.setEnabled(false);
        status.setText(t(12));
        worker.execute(() -> {
            try {
                File file = UpdateClient.download(this, selected);
                runOnUiThread(() -> {
                    install.setEnabled(file.isFile());
                    check.setEnabled(true);
                    status.setText(t(13));
                });
            } catch (Exception error) {
                runOnUiThread(() -> {
                    status.setText(t(15) + error.getMessage());
                    check.setEnabled(true);
                    download.setEnabled(true);
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        worker.shutdownNow();
        super.onDestroy();
    }
}
