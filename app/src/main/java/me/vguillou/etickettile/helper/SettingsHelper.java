package me.vguillou.etickettile.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper to change the System Settings
 */
@Singleton
public final class SettingsHelper {

    private static final String PREF_PREVIOUS_BRIGHTNESS_LEVEL = "me.vguillou.PREVIOUS_BRIGHTNESS_LEVEL";
    private static final String PREF_PREVIOUS_BRIGHTNESS_MODE = "me.vguillou.PREVIOUS_BRIGHTNESS_MODE";
    private static final String PREF_PREVIOUS_AUTO_ROTATE = "me.vguillou.PREVIOUS_AUTO_ROTATE";
    private static final int MAX_BRIGHTNESS = 255;
    private static final int AUTO_ROTATE_ENABLED = 1;
    private static final int AUTO_ROTATE_DISABLED = 0;

    private final Context mContext;

    private final PreferenceHelper mPreferenceHelper;

    private final ContentResolver mContentResolver;

    @Inject
    public SettingsHelper(@NonNull Context context,
                          @NonNull PreferenceHelper preferenceHelper,
                          @NonNull ContentResolver contentResolver) {
        mContext = context.getApplicationContext();
        mPreferenceHelper = preferenceHelper;
        mContentResolver = contentResolver;
    }

    /**
     * Check if the app has the permission to change the System settings
     *
     * @return {@code true} if is has
     */
    public boolean hasPermission() {
        return Settings.System.canWrite(mContext);
    }

    /**
     * Request the permission to modify the System settings, if not already granted.
     */
    public void requestPermissionIfNeeded() {
        if (!hasPermission())
            requestPermission();
    }

    /**
     * Set the screen brightness of the device to max level.
     * Save the last setting before, and it can be restored with
     * {@code restorePreviousBrightness()}
     *
     * @return {@code true} if it was modified successfully
     */
    public boolean setMaxBrightness() {
        // Save current brightness settings
        mPreferenceHelper.setInt(PREF_PREVIOUS_BRIGHTNESS_LEVEL, getBrightnessLevel());
        mPreferenceHelper.setInt(PREF_PREVIOUS_BRIGHTNESS_MODE, getBrightnessMode());

        // Set max brightness
        return setBrightnessMode(
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL,
                MAX_BRIGHTNESS);
    }

    /**
     * Disable the auto rotate feature of the device.
     * Save the last setting before, and it can be restored with
     * {@code restoreAutoRotate()}
     *
     * @return {@code true} if it was modified successfully
     */
    public boolean setLockAutoRotate() {
        // Save current auto rotate settings
        mPreferenceHelper.setInt(PREF_PREVIOUS_AUTO_ROTATE, getAutoRotate());

        // Lock auto rotate
        return setAutoRotate(AUTO_ROTATE_DISABLED);
    }

    /**
     * Restores the last brightness setting, as it was before a call to
     * {@code setMaxBrightness()}
     *
     * @return {@code true} if it was modified successfully
     */
    public boolean restorePreviousBrightness() {
        return setBrightnessMode(
                mPreferenceHelper.getInt(PREF_PREVIOUS_BRIGHTNESS_MODE),
                mPreferenceHelper.getInt(PREF_PREVIOUS_BRIGHTNESS_LEVEL));
    }

    /**
     * Restores the last auto rotate setting, as it was before a call to
     * {@code setLockAutoRotate()}
     *
     * @return {@code true} if it was modified successfully
     */
    public boolean restoreAutoRotate() {
        return setAutoRotate(mPreferenceHelper.getInt(PREF_PREVIOUS_AUTO_ROTATE));
    }

    /**
     * Get the Intent to open the System settings activity.
     *
     * @return The intent
     */
    public Intent getOpenSettingsIntent() {
        return new Intent(Settings.ACTION_SETTINGS);
    }

    private int getBrightnessMode() {
        try {
            return Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            return AUTO_ROTATE_ENABLED;
        }
    }

    private int getBrightnessLevel() {
        try {
            return Settings.System.getInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            return MAX_BRIGHTNESS / 2;
        }
    }

    private int getAutoRotate() {
        try {
            return Settings.System.getInt(mContentResolver, Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            return 1;
        }
    }

    private boolean setBrightnessMode(final int mode, final int level) {
        if (hasPermission()) {
            try {
                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, mode);
                Settings.System.putInt(mContentResolver, Settings.System.SCREEN_BRIGHTNESS, level);
                return true;
            } catch (SecurityException e) {
                requestPermission();
            }
        } else {
            requestPermission();
        }
        return false;
    }

    private boolean setAutoRotate(final int autoRotate) {
        if (hasPermission()) {
            try {
                Settings.System.putInt(mContentResolver, Settings.System.ACCELEROMETER_ROTATION, autoRotate);
                return true;
            } catch (SecurityException e) {
                requestPermission();
            }
        } else {
            requestPermission();
        }
        return false;
    }

    private void requestPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}
