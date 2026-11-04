package org.aemulwin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Read-only, non-exported content provider exposing ONLY the vetted staged APK
 * to Android's system package installer, via a temporary URI grant.
 */
public final class UpdateApkProvider extends ContentProvider {
    @Override
    public boolean onCreate() { return true; }

    private File resolve(Uri uri) throws FileNotFoundException {
        Context context = getContext();
        if (context == null || !"/apk".equals(uri.getPath())) {
            throw new FileNotFoundException("Update resource not found");
        }
        File apk = new File(new File(context.getCacheDir(), "updates"), "aemulwin.apk");
        if (!apk.isFile() || apk.length() < 1024) throw new FileNotFoundException("No verified update APK");
        return apk;
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        if (!"r".equals(mode)) throw new FileNotFoundException("Read-only resource");
        return ParcelFileDescriptor.open(resolve(uri), ParcelFileDescriptor.MODE_READ_ONLY);
    }

    @Override
    public String getType(Uri uri) {
        return "/apk".equals(uri.getPath()) ? "application/vnd.android.package-archive" : null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        try {
            File apk = resolve(uri);
            String[] cols = projection != null ? projection :
                new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE};
            MatrixCursor cursor = new MatrixCursor(cols);
            Object[] row = new Object[cols.length];
            for (int i = 0; i < cols.length; i++) {
                if (OpenableColumns.DISPLAY_NAME.equals(cols[i])) row[i] = "AEmulWin-update.apk";
                else if (OpenableColumns.SIZE.equals(cols[i])) row[i] = apk.length();
            }
            cursor.addRow(row);
            return cursor;
        } catch (FileNotFoundException ignored) {
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException("Read only");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] args) {
        throw new UnsupportedOperationException("Read only");
    }

    @Override
    public int delete(Uri uri, String selection, String[] args) {
        throw new UnsupportedOperationException("Read only");
    }
}
