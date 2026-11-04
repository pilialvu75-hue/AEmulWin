package org.aemulwin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Future Wine network policy: deny by default, separate from remote access.
 *
 * P0.3 contains NO Wine engine and NO network bridge. The stored preference
 * is only a user choice; it is NOT an Android OS firewall or a runtime
 * revocation of android.permission.INTERNET. A future bridge MUST consult
 * isGuestNetworkAllowed() before creating guest sockets and prove enforcement.
 */
final class GuestNetworkPolicy {
    private static final String PREFS = "aemulwin_settings";
    private static final String GUEST_NETWORK = "guest_network_allowed";

    static boolean isGuestNetworkAllowed(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getBoolean(GUEST_NETWORK, false);
    }

    static void setGuestNetworkAllowed(Context context, boolean allowed) {
        SharedPreferences.Editor editor = context
            .getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit();
        editor.putBoolean(GUEST_NETWORK, allowed).apply();
    }
}
