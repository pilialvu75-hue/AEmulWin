package org.aemulwin;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.FeatureInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * AEmulWin P0 diagnostic shell.
 * This APK has no INTERNET permission, no listening server, and no Wine runtime.
 */
public final class MainActivity extends Activity {
    private static final int BG = Color.rgb(14, 19, 30);
    private static final int TEXT = Color.rgb(239, 241, 246);
    private static final int MUTED = Color.rgb(164, 179, 198);
    private static final int ACCENT = Color.rgb(130, 158, 255);
    private static final String PREFS = "aemulwin_ui";
    private static final String PREF_LANGUAGE = "language";
    private static final String[] LANGUAGE_CODES = {"en", "it", "fr"};
    private static final String[] LANGUAGE_NAMES = {"English", "Italiano", "Français"};
    private static final Map<String, String[]> WORDS = new HashMap<>();

    static {
        put("subtitle", "Windows on Android — independent lab", "Windows su Android — laboratorio indipendente", "Windows sur Android — laboratoire indépendant");
        put("language", "Language", "Lingua", "Langue");
        put("engineState", "P0 PROTOTYPE — Windows runtime NOT installed.", "PROTOTIPO P0 — motore Windows NON installato.", "PROTOTYPE P0 — moteur Windows NON installé.");
        put("remoteState", "Remote access OFF. No port is open.", "Accesso remoto DISATTIVATO. Nessuna porta aperta.", "Accès à distance DÉSACTIVÉ. Aucun port ouvert.");
        put("intro", "First test: inspect Android version, RAM, ABI, display, battery and graphics. No Windows software or heavy benchmarks are started.", "Prima prova: verifica Android, RAM, architettura, schermo, batteria e grafica. Nessun programma Windows o benchmark pesante viene avviato.", "Premier test : vérifier Android, RAM, architecture, écran, batterie et graphisme. Aucun programme Windows ni test intensif n'est lancé.");
        put("diagnose", "Inspect this phone", "Analizza questo telefono", "Analyser ce téléphone");
        put("copy", "Copy diagnostic report", "Copia diagnosi", "Copier le diagnostic");
        put("remoteMenu", "Remote connection menu", "Menu connessione remota", "Menu de connexion à distance");
        put("placeholder", "Tap «Inspect this phone» to start.", "Tocca «Analizza questo telefono» per iniziare.", "Appuyez sur «Analyser ce téléphone» pour commencer.");
        put("privacy", "Privacy: no Internet permission, no account and no automatic report upload. Share a report only if you choose to copy it.", "Privacy: nessun permesso Internet, nessun account e nessun invio automatico. Condividi il report soltanto se scegli di copiarlo.", "Confidentialité : aucun accès Internet, aucun compte et aucun envoi automatique. Partagez le rapport uniquement si vous décidez de le copier.");
        put("done", "Local diagnostic completed", "Diagnostica locale completata", "Diagnostic local terminé");
        put("copyFirst", "Run the diagnostic first", "Esegui prima la diagnostica", "Lancez d'abord le diagnostic");
        put("copied", "Report copied", "Diagnosi copiata", "Rapport copié");
        put("error", "Diagnostic failed", "Diagnostica non completata", "Échec du diagnostic");
        put("notShared", "No information was sent.", "Non è stato inviato alcun dato.", "Aucune information n'a été envoyée.");
        put("diagnosticTitle", "AEmulWin — P0 Android device report", "AEmulWin — Diagnosi P0 dispositivo Android", "AEmulWin — Diagnostic P0 de l'appareil Android");
        put("model", "Model", "Modello", "Modèle");
        put("abis", "Supported ABIs", "Architetture", "Architectures");
        put("totalRam", "Total RAM", "RAM totale", "RAM totale");
        put("freeRam", "Available system RAM", "RAM disponibile (sistema)", "RAM disponible (système)");
        put("lowMemory", "System low-memory warning", "Memoria bassa segnalata", "Alerte mémoire insuffisante");
        put("display", "Logical app display", "Schermo logico app", "Écran logique de l'appli");
        put("density", "Density", "Densità", "Densité");
        put("battery", "Battery", "Batteria", "Batterie");
        put("vulkan", "Vulkan support reported", "Supporto Vulkan dichiarato", "Support Vulkan déclaré");
        put("yes", "Yes", "Sì", "Oui");
        put("no", "No", "No", "Non");
        put("unknown", "Not available", "Non disponibile", "Indisponible");
        put("engineNotInstalled", "Wine/Box64 engine: NOT INSTALLED", "Motore Wine/Box64: NON INSTALLATO", "Moteur Wine/Box64 : NON INSTALLÉ");
        put("bridgeOff", "Remote control bridge: OFF", "Ponte di controllo remoto: DISATTIVATO", "Passerelle de contrôle à distance : DÉSACTIVÉE");
        put("noWindowsTests", "No Windows performance tests have been run.", "Nessun test di prestazioni Windows eseguito.", "Aucun test de performance Windows réalisé.");
        put("remoteTitle", "Remote connection", "Connessione remota", "Connexion à distance");
        put("connectionStatus", "Current status", "Stato attuale", "État actuel");
        put("disconnected", "DISCONNECTED", "SCOLLEGATO", "DÉCONNECTÉ");
        put("whyDisabled", "The secure relay and connector do not exist yet. Remote access cannot be activated in this build. This phone has no listening port and no INTERNET permission.", "Il relay sicuro e il connettore non sono ancora disponibili. Questa build non può attivare l'accesso remoto. Il telefono non apre porte e l'app non ha permesso INTERNET.", "Le relais sécurisé et le connecteur ne sont pas encore disponibles. Cette version ne peut pas activer l'accès à distance. Aucun port n'est ouvert et l'application n'a pas l'autorisation INTERNET.");
        put("futureAccess", "Future version: explicit pairing, time-limited permission, visible session status, immediate disconnect and local audit. Never an always-open public port.", "Versione futura: associazione esplicita, permesso temporaneo, stato visibile, disconnessione immediata e registro locale. Mai una porta pubblica sempre aperta.", "Version future : appairage explicite, autorisation temporaire, état visible, déconnexion immédiate et journal local. Jamais de port public ouvert en permanence.");
        put("connect", "Enable remote session (not available)", "Attiva sessione remota (non disponibile)", "Activer la session à distance (indisponible)");
        put("disconnect", "Stop / revoke connection", "Interrompi / revoca connessione", "Arrêter / révoquer la connexion");
        put("stopped", "No remote session is running.", "Nessuna sessione remota attiva.", "Aucune session à distance n'est active.");
        put("back", "Back to diagnostics", "Torna alla diagnostica", "Retour au diagnostic");
    }

    private static void put(String key, String en, String it, String fr) {
        WORDS.put(key, new String[]{en, it, fr});
    }

    private int languageIndex = 0;
    private boolean remotePage = false;
    private String lastReport;
    private TextView reportView;
    private TextView remoteStatusView;
    private RemoteAccessGate remoteAccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(BG);
        getWindow().setStatusBarColor(BG);
        remoteAccess = new RemoteAccessGate();
        SharedPreferences preferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        String code = preferences.getString(PREF_LANGUAGE, "");
        if (code.isEmpty()) {
            String system = Locale.getDefault().getLanguage();
            code = ("it".equals(system) || "fr".equals(system)) ? system : "en";
        }
        for (int i = 0; i < LANGUAGE_CODES.length; i++) {
            if (LANGUAGE_CODES[i].equals(code)) {
                languageIndex = i;
                break;
            }
        }
        renderScreen();
    }

    private String t(String key) {
        String[] phrases = WORDS.get(key);
        return phrases == null ? key : phrases[languageIndex];
    }

    private void renderScreen() {
        ScrollView scroller = new ScrollView(this);
        scroller.setFillViewport(true);
        scroller.setBackgroundColor(BG);
        LinearLayout content = new LinearLayout(this);
        content.setPadding(dp(22), dp(32), dp(22), dp(32));
        content.setOrientation(LinearLayout.VERTICAL);
        scroller.addView(content);
        setContentView(scroller);

        content.addView(label("AEmulWin", 30, TEXT, true));
        addWithGap(content, label(t("subtitle"), 15, ACCENT, false), 3);
        addWithGap(content, label(t("language"), 14, MUTED, false), 18);

        Spinner language = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_spinner_item, LANGUAGE_NAMES);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language.setAdapter(adapter);
        language.setSelection(languageIndex);
        addWithGap(content, language, 2);
        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != languageIndex) {
                    languageIndex = position;
                    getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                        .putString(PREF_LANGUAGE, LANGUAGE_CODES[position]).apply();
                    // The previous report remains accessible until language change.
                    // It is cleared now so that its text never mixes languages.
                    lastReport = null;
                    renderScreen();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        if (remotePage) {
            renderRemote(content);
        } else {
            renderDiagnostics(content);
        }
    }

    private void renderDiagnostics(LinearLayout content) {
        TextView state = label(
            t("engineState") + "\n" + t("remoteState"), 15, TEXT, false);
        state.setBackgroundColor(Color.rgb(34, 43, 63));
        state.setPadding(dp(15), dp(15), dp(15), dp(15));
        addWithGap(content, state, 22);

        addWithGap(content, label(t("intro"), 16, MUTED, false), 18);
        Button diagnose = button(t("diagnose"));
        addWithGap(content, diagnose, 24);
        diagnose.setOnClickListener(v -> runDiagnostics());

        Button copy = button(t("copy"));
        addWithGap(content, copy, 10);
        copy.setOnClickListener(v -> copyReport());

        Button remote = button(t("remoteMenu"));
        addWithGap(content, remote, 10);
        remote.setOnClickListener(v -> {
            remotePage = true;
            renderScreen();
        });

        reportView = label(lastReport == null ? t("placeholder") : lastReport, 15, TEXT, false);
        reportView.setPadding(dp(14), dp(14), dp(14), dp(14));
        reportView.setBackgroundColor(Color.rgb(25, 32, 46));
        addWithGap(content, reportView, 22);
        addWithGap(content, label(t("privacy"), 13, MUTED, false), 18);
    }

    private void renderRemote(LinearLayout content) {
        addWithGap(content, label(t("remoteTitle"), 24, TEXT, true), 24);
        addWithGap(content, label(t("connectionStatus"), 14, MUTED, false), 16);
        remoteStatusView = label(t("disconnected"), 20, ACCENT, true);
        addWithGap(content, remoteStatusView, 4);
        addWithGap(content, label(t("whyDisabled"), 15, TEXT, false), 18);
        addWithGap(content, label(t("futureAccess"), 15, MUTED, false), 18);

        Button connect = button(t("connect"));
        connect.setEnabled(false); // Never imply a working network connection.
        addWithGap(content, connect, 22);

        Button disconnect = button(t("disconnect"));
        addWithGap(content, disconnect, 10);
        disconnect.setOnClickListener(v -> {
            remoteAccess.stop();
            remoteStatusView.setText(t("disconnected"));
            Toast.makeText(this, t("stopped"), Toast.LENGTH_SHORT).show();
        });

        Button back = button(t("back"));
        addWithGap(content, back, 20);
        back.setOnClickListener(v -> {
            remotePage = false;
            renderScreen();
        });
    }

    @Override
    public void onBackPressed() {
        if (remotePage) {
            remotePage = false;
            renderScreen();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStop() {
        if (remoteAccess != null) {
            remoteAccess.stop();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (remoteAccess != null) {
            remoteAccess.stop();
        }
        super.onDestroy();
    }

    private void runDiagnostics() {
        try {
            ActivityManager manager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memory = new ActivityManager.MemoryInfo();
            manager.getMemoryInfo(memory);
            DisplayMetrics metrics = getResources().getDisplayMetrics();

            Intent battery = registerReceiver(
                null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            String batteryText = t("unknown");
            if (battery != null) {
                int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                if (level >= 0 && scale > 0) {
                    batteryText = String.format(Locale.ROOT, "%.0f%%", (100.0 * level / scale));
                }
            }

            boolean vulkan = false;
            FeatureInfo[] features = getPackageManager().getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo feature : features) {
                    if (feature.name != null &&
                        feature.name.startsWith("android.hardware.vulkan")) {
                        vulkan = true;
                    }
                }
            }

            lastReport =
                t("diagnosticTitle") + "\n" +
                "Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n" +
                t("model") + ": " + Build.MANUFACTURER + " " + Build.MODEL + "\n" +
                t("abis") + ": " + Arrays.toString(Build.SUPPORTED_ABIS) + "\n" +
                t("totalRam") + ": " + mib(memory.totalMem) + " MiB\n" +
                t("freeRam") + ": " + mib(memory.availMem) + " MiB\n" +
                t("lowMemory") + ": " + (memory.lowMemory ? t("yes") : t("no")) + "\n" +
                t("display") + ": " + metrics.widthPixels + " × " + metrics.heightPixels + " px\n" +
                t("density") + ": " + metrics.densityDpi + " dpi\n" +
                t("battery") + ": " + batteryText + "\n" +
                t("vulkan") + ": " + (vulkan ? t("yes") : t("unknown")) + "\n" +
                t("engineNotInstalled") + "\n" +
                t("bridgeOff") + "\n" +
                t("noWindowsTests");

            reportView.setText(lastReport);
            Toast.makeText(this, t("done"), Toast.LENGTH_SHORT).show();
        } catch (Exception error) {
            lastReport = null;
            reportView.setText(t("error") + ": " + error.getClass().getSimpleName() +
                ". " + t("notShared"));
        }
    }

    private void copyReport() {
        if (lastReport == null) {
            Toast.makeText(this, t("copyFirst"), Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard =
            (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("AEmulWin P0", lastReport));
            Toast.makeText(this, t("copied"), Toast.LENGTH_SHORT).show();
        }
    }

    private static String mib(long bytes) {
        return String.format(Locale.ROOT, "%.0f", bytes / 1048576.0);
    }

    private Button button(String title) {
        Button control = new Button(this);
        control.setAllCaps(false);
        control.setText(title);
        control.setTextSize(16);
        return control;
    }

    private TextView label(String value, int sp, int color, boolean bold) {
        TextView text = new TextView(this);
        text.setText(value);
        text.setTextSize(sp);
        text.setTextColor(color);
        text.setLineSpacing(dp(3), 1.0f);
        if (bold) {
            text.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        return text;
    }

    private void addWithGap(LinearLayout parent, View child, int topMarginDp) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        p.topMargin = dp(topMarginDp);
        parent.addView(child, p);
    }

    private int dp(int value) {
        return (int) (getResources().getDisplayMetrics().density * value + 0.5f);
    }
}
