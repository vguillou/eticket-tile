package me.vguillou.etickettile.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import me.vguillou.etickettile.R;

/**
 * Helper to activate/deactivate the scan mode, and check it's state.
 */
public final class ETicketModeHelper {

    /**
     * Keeps track of the last known state of the eTicket mode
     */
    private static final String PREF_ETICKET_MODE_ACTIVATED = "me.vguillou.ETICKET_MODE_ACTIVATED";

    private final Context mContext;

    private final SettingsHelper mSettingsHelper;

    private final PreferenceHelper mPreferenceHelper;

    private final AccessibilityHelper mAccessibilityHelper;

    public ETicketModeHelper(@NonNull Context context) {
        mContext = context;
        mSettingsHelper = new SettingsHelper(context);
        mPreferenceHelper = new PreferenceHelper(context);
        mAccessibilityHelper = new AccessibilityHelper(context);
    }

    /**
     * Toggle the scan mode
     *
     * @return {@code true} if activated afterwards, {@code false} otherwise.
     */
    public boolean toggle() {
        // Check if the scan mode is activated
        final boolean activated = isETicketModeActivated();

        // Activate / Deactivate
        final boolean success = activated ? deactivate() : activate();

        // Revert the activation flag if the activation/deactivation was successful and update the tile
        if (success) {
            mPreferenceHelper.setBoolean(PREF_ETICKET_MODE_ACTIVATED, !activated);
            mAccessibilityHelper.say(!activated
                    ? mContext.getString(R.string.tile_content_desc_click_on)
                    : mContext.getString(R.string.tile_content_desc_click_off));
        }
        // Return the activation state
        return success != activated;
    }

    /**
     * Check if the eTicket mode is currently activated
     *
     * @return {@code true} if currently activated
     */
    public boolean isETicketModeActivated() {
        return mPreferenceHelper.getBoolean(PREF_ETICKET_MODE_ACTIVATED);
    }

    /**
     * Check if the app has the necessary permissions
     *
     * @return {@code true} if is has
     */
    public boolean hasPermission() {
        return mSettingsHelper.hasPermission();
    }

    private boolean activate() {
        return mSettingsHelper.setMaxBrightness()
                && mSettingsHelper.setLockAutoRotate();
    }

    private boolean deactivate() {
        return mSettingsHelper.restorePreviousBrightness()
                && mSettingsHelper.restoreAutoRotate();
    }
}
