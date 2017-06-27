package me.vguillou.etickettile.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

/**
 * Helper to handle accessibility.
 */
public final class AccessibilityHelper {

    private final AccessibilityManager mAccessibilityManager;

    public AccessibilityHelper(@NonNull Context context) {
        mAccessibilityManager = (AccessibilityManager) context.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
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
