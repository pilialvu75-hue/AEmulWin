package org.aemulwin;

/**
 * Explicitly inert remote-access gate for the P0 Android diagnostic APK.
 *
 * There is deliberately no networking implementation, no listener, no
 * hard-coded URL/token and no automatic startup. A future, separately
 * reviewed connector may implement Session and be injected only after
 * an on-device opt-in flow.
 */
final class RemoteAccessGate {
    interface Session {
        void close();
        boolean isConnected();
    }

    private Session session;

    synchronized boolean isAvailable() {
        return false; // No authenticated relay/connector exists in P0.
    }

    synchronized boolean isConnected() {
        return session != null && session.isConnected();
    }

    synchronized void stop() {
        Session current = session;
        session = null;
        if (current != null) {
            current.close();
        }
    }
}
