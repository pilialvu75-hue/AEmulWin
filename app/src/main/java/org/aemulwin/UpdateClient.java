package org.aemulwin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Official GitHub Releases updater. All network work must run off the UI thread.
 * The user must explicitly approve download and Android Package Installer.
 *
 * AEmulWin must be signed with the SAME persistent private key as installed
 * builds. Android rejects signature mismatches; this code never bypasses that.
 */
final class UpdateClient {
    static final String REPO = "pilialvu75-hue/AEmulWin";
    private static final String API = "https://api.github.com/repos/" + REPO + "/releases?per_page=20";
    private static final long MAX_APK = 1024L * 1024L * 1024L;
    private static final Pattern VERSION = Pattern.compile("^v?(\\d+)\\.(\\d+)\\.(\\d+)(?:[-+].*)?$");
    private static final String DOWNLOAD_PREFIX = "https://github.com/" + REPO + "/releases/download/";

    static final class Candidate {
        final String tag;
        final String apkUrl;
        final String shaUrl;
        final boolean beta;
        Candidate(String tag, String apkUrl, String shaUrl, boolean beta) {
            this.tag = tag;
            this.apkUrl = apkUrl;
            this.shaUrl = shaUrl;
            this.beta = beta;
        }
    }

    static int compareVersion(String left, String right) {
        Matcher a = VERSION.matcher(left), b = VERSION.matcher(right);
        if (!a.matches() || !b.matches()) {
            throw new IllegalArgumentException("Invalid semantic version");
        }
        for (int i = 1; i <= 3; i++) {
            long x = Long.parseLong(a.group(i));
            long y = Long.parseLong(b.group(i));
            if (x != y) return x > y ? 1 : -1;
        }
        boolean aPre = left.matches(".*-[^+]+.*");
        boolean bPre = right.matches(".*-[^+]+.*");
        if (aPre != bPre) return aPre ? -1 : 1;
        return 0;
    }

    static Candidate findUpdate(boolean allowBeta, String installedVersion) throws Exception {
        JSONArray releases = new JSONArray(readText(API, 2 * 1024 * 1024));
        Candidate best = null;
        for (int i = 0; i < releases.length(); i++) {
            JSONObject release = releases.getJSONObject(i);
            if (release.optBoolean("draft", false)) continue;
            boolean beta = release.optBoolean("prerelease", false);
            if (beta && !allowBeta) continue;
            String tag = release.optString("tag_name", "");
            if (!VERSION.matcher(tag).matches()) continue;
            if (compareVersion(tag, installedVersion) <= 0) continue;
            JSONArray assets = release.optJSONArray("assets");
            if (assets == null) continue;
            String apk = null, checksum = null;
            for (int a = 0; a < assets.length(); a++) {
                JSONObject asset = assets.getJSONObject(a);
                String file = asset.optString("name", "");
                String url = asset.optString("browser_download_url", "");
                if (!url.startsWith(DOWNLOAD_PREFIX)) continue;
                if (file.equals("AEmulWin-" + tag.replaceFirst("^v", "") + ".apk")
                        || (file.endsWith(".apk") && apk == null)) {
                    apk = url;
                }
                if (file.endsWith(".sha256")) checksum = url;
            }
            if (apk == null || checksum == null) continue;
            Candidate item = new Candidate(tag, apk, checksum, beta);
            if (best == null || compareVersion(item.tag, best.tag) > 0) best = item;
        }
        return best;
    }

    private static HttpURLConnection open(String source) throws Exception {
        URL target = new URL(source);
        for (int hop = 0; hop < 7; hop++) {
            if (!"https".equalsIgnoreCase(target.getProtocol())) {
                throw new SecurityException("HTTPS required");
            }
            String host = target.getHost().toLowerCase(Locale.ROOT);
            if (!host.equals("api.github.com") && !host.equals("github.com")
                    && !host.equals("release-assets.githubusercontent.com")
                    && !host.equals("objects.githubusercontent.com")) {
                throw new SecurityException("Unexpected download host: " + host);
            }
            HttpURLConnection connection = (HttpURLConnection) target.openConnection();
            connection.setRequestProperty("User-Agent", "AEmulWin-Updater/0.1");
            connection.setRequestProperty("Accept", "application/vnd.github+json, application/octet-stream");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(30000);
            connection.setInstanceFollowRedirects(false);
            int code = connection.getResponseCode();
            if (code >= 300 && code < 400) {
                String location = connection.getHeaderField("Location");
                connection.disconnect();
                if (location == null) throw new SecurityException("Redirect without Location");
                target = new URL(target, location);
                continue;
            }
            if (code != 200) {
                connection.disconnect();
                throw new IllegalStateException("GitHub responded HTTP " + code);
            }
            return connection;
        }
        throw new SecurityException("Too many redirects");
    }

    private static String readText(String address, int limit) throws Exception {
        HttpURLConnection connection = open(address);
        try (InputStream stream = new BufferedInputStream(connection.getInputStream())) {
            byte[] buffer = new byte[4096];
            java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
            int n;
            while ((n = stream.read(buffer)) >= 0) {
                output.write(buffer, 0, n);
                if (output.size() > limit) throw new SecurityException("Response too large");
            }
            return output.toString("UTF-8");
        } finally {
            connection.disconnect();
        }
    }

    static File download(Activity activity, Candidate item) throws Exception {
        String checksum = readText(item.shaUrl, 8192).trim();
        String[] parts = checksum.split("\\s+");
        if (parts.length < 1 || !parts[0].matches("[0-9a-fA-F]{64}")) {
            throw new SecurityException("Missing valid SHA-256");
        }
        File directory = new File(activity.getCacheDir(), "updates");
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new IllegalStateException("Cannot create updates folder");
        }
        File partial = new File(directory, "aemulwin.apk.part");
        File result = new File(directory, "aemulwin.apk");
        if (partial.exists()) partial.delete();
        if (result.exists()) result.delete();

        HttpURLConnection connection = open(item.apkUrl);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        long expectedBytes = connection.getContentLengthLong();
        if (expectedBytes > MAX_APK) {
            connection.disconnect();
            throw new SecurityException("APK too large");
        }
        long total = 0;
        try {
            try (InputStream input = new BufferedInputStream(connection.getInputStream());
                 BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(partial))) {
                byte[] data = new byte[65536];
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    if (total > MAX_APK) throw new SecurityException("APK exceeds limit");
                    digest.update(data, 0, count);
                    output.write(data, 0, count);
                }
            }
            if (total < 1024 || (expectedBytes >= 0 && total != expectedBytes)) {
                throw new SecurityException("APK download incomplete");
            }
            StringBuilder actual = new StringBuilder(64);
            for (byte b : digest.digest()) actual.append(String.format(Locale.ROOT, "%02x", b & 255));
            if (!actual.toString().equalsIgnoreCase(parts[0])) {
                throw new SecurityException("APK SHA-256 mismatch");
            }
            try (ZipFile zip = new ZipFile(partial)) {
                if (zip.getEntry("AndroidManifest.xml") == null) {
                    throw new SecurityException("Not an APK");
                }
            }
            PackageInfo archive = activity.getPackageManager().getPackageArchiveInfo(
                    partial.getAbsolutePath(), 0);
            if (archive == null || !activity.getPackageName().equals(archive.packageName)) {
                throw new SecurityException("APK belongs to another application");
            }
            PackageInfo installed = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), 0);
            if (archive.getLongVersionCode() <= installed.getLongVersionCode()) {
                throw new SecurityException("APK versionCode is not newer");
            }
            if (!partial.renameTo(result)) throw new IllegalStateException("Failed to stage APK");
            return result;
        } finally {
            connection.disconnect();
            if (partial.exists()) partial.delete();
        }
    }

    static File stagedFile(Activity activity) {
        return new File(new File(activity.getCacheDir(), "updates"), "aemulwin.apk");
    }

    static void promptInstall(Activity activity) throws Exception {
        File apk = stagedFile(activity);
        if (!apk.isFile() || apk.length() < 1024) {
            throw new IllegalStateException("No staged APK");
        }
        if (Build.VERSION.SDK_INT >= 26 &&
                !activity.getPackageManager().canRequestPackageInstalls()) {
            Intent settings = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(settings);
            throw new IllegalStateException("Allow installation for AEmulWin, then tap Install again");
        }
        Uri uri = Uri.parse("content://" + activity.getPackageName() + ".updates/apk");
        Intent installer = new Intent(Intent.ACTION_VIEW);
        installer.setDataAndType(uri, "application/vnd.android.package-archive");
        installer.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivity(installer);
    }
}
