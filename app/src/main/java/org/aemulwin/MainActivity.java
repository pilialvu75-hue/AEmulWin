package org.aemulwin;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Locale;

/**
 * AEmulWin P0: device read-only diagnostics.
 * Not a Windows runtime yet. No network, no remote server, no shell execution.
 */
public final class MainActivity extends Activity {
    private static final int BG = Color.rgb(14, 19, 30);
    private static final int TEXT = Color.rgb(239, 241, 246);
    private static final int MUTED = Color.rgb(164, 179, 198);
    private static final int ACCENT = Color.rgb(130, 158, 255);

    private TextView result;
    private String lastReport;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(BG);
        getWindow().setStatusBarColor(BG);

        ScrollView scroller = new ScrollView(this);
        scroller.setFillViewport(true);
        scroller.setBackgroundColor(BG);

        LinearLayout container = new LinearLayout(this);
        container.setPadding(dp(22), dp(42), dp(22), dp(32));
        container.setOrientation(LinearLayout.VERTICAL);
        scroller.addView(container);
        setContentView(scroller);

        TextView heading = label("AEmulWin", 30, TEXT, true);
        container.addView(heading);
        TextView subtitle = label("Windows su Android — laboratorio indipendente", 15, ACCENT, false);
        addWithGap(container, subtitle, 2);

        TextView state = label(
            "PROTOTIPO P0 • Il motore Windows NON è ancora integrato.\n" +
            "Controllo remoto disabilitato: nessuna porta aperta.",
            15, TEXT, false);
        state.setBackgroundColor(Color.rgb(34, 43, 63));
        state.setPadding(dp(15), dp(15), dp(15), dp(15));
        addWithGap(container, state, 22);

        TextView description = label(
            "Prima prova sul Galaxy: verifichiamo Android, memoria disponibile, " +
            "architettura ARM64, schermo, batteria e API grafiche. " +
            "Non vengono avviati programmi Windows né test pesanti.",
            16, MUTED, false);
        addWithGap(container, description, 20);

        Button diagnose = button("Analizza questo telefono");
        addWithGap(container, diagnose, 24);
        diagnose.setOnClickListener(v -> runDiagnostics());

        Button copy = button("Copia diagnosi");
        addWithGap(container, copy, 10);
        copy.setOnClickListener(v -> copyReport());

        result = label("Tocca «Analizza questo telefono» per iniziare.", 15, TEXT, false);
        result.setPadding(dp(14), dp(14), dp(14), dp(14));
        result.setBackgroundColor(Color.rgb(25, 32, 46));
        addWithGap(container, result, 22);

        TextView foot = label(
            "Privacy: nessuna connessione Internet, nessun account richiesto e " +
            "nessuna diagnostica inviata automaticamente. Puoi condividere " +
            "volontariamente il report copiandolo negli appunti.",
            13, MUTED, false);
        addWithGap(container, foot, 18);
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
            String batteryText = "Non disponibile";
            if (battery != null) {
                int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                if (level >= 0 && scale > 0) {
                    batteryText = String.format(Locale.ITALIAN,
                        "%.0f%%", (100.0 * level / scale));
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
                "AEmulWin — Diagnosi P0 (solo dispositivo Android)\n" +
                "Android: " + Build.VERSION.RELEASE + " (API " + Build.VERSION.SDK_INT + ")\n" +
                "Modello: " + Build.MANUFACTURER + " " + Build.MODEL + "\n" +
                "Architetture: " + Arrays.toString(Build.SUPPORTED_ABIS) + "\n" +
                "RAM totale: " + mib(memory.totalMem) + " MiB\n" +
                "RAM disponibile (sistema): " + mib(memory.availMem) + " MiB\n" +
                "Memoria bassa segnalata: " + (memory.lowMemory ? "Sì" : "No") + "\n" +
                "Schermo logico app: " + metrics.widthPixels + " × " +
                metrics.heightPixels + " px\n" +
                "Densità: " + metrics.densityDpi + " dpi\n" +
                "Batteria: " + batteryText + "\n" +
                "Supporto Vulkan dichiarato: " + (vulkan ? "Sì" : "Non rilevato") + "\n" +
                "Motore Wine / Box64: NON INSTALLATO\n" +
                "Ponte di controllo remoto: DISABILITATO\n" +
                "Nessun test di prestazioni Windows eseguito.";

            result.setText(lastReport);
            Toast.makeText(this, "Diagnostica locale completata", Toast.LENGTH_SHORT).show();
        } catch (Exception error) {
            lastReport = null;
            result.setText("Diagnostica non completata: " + error.getClass().getSimpleName() +
                ". Non è stato inviato alcun dato.");
        }
    }

    private void copyReport() {
        if (lastReport == null) {
            Toast.makeText(this, "Esegui prima la diagnostica", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard =
            (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            clipboard.setPrimaryClip(ClipData.newPlainText("AEmulWin P0", lastReport));
            Toast.makeText(this, "Diagnosi copiata negli appunti", Toast.LENGTH_SHORT).show();
        }
    }

    private static String mib(long bytes) {
        return String.format(Locale.ITALIAN, "%.0f", bytes / 1048576.0);
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
