package me.vguillou.etickettile.helper;

import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper to handle accessibility.
 */
@Singleton
public final class AccessibilityHelper {

    private final AccessibilityManager mAccessibilityManager;

    @Inject
    public AccessibilityHelper(@NonNull AccessibilityManager accessibilityManager) {
        mAccessibilityManager = accessibilityManager;
    }

    /**
     * Make the device say out loud a message, if accessibility mode is enabled
     *
     * @param message The message to say out loud
     */
    public void say(final String message) {
        if (mAccessibilityManager.isEnabled() && message != null) {
            final AccessibilityEvent event = AccessibilityEvent.obtain();
            event.setEventType(AccessibilityEvent.TYPE_ANNOUNCEMENT);
            event.getText().add(message);
            event.setEnabled(true);
            mAccessibilityManager.sendAccessibilityEvent(event);
        }
    }
}
