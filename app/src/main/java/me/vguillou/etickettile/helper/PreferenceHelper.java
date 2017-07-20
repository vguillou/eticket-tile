package me.vguillou.etickettile.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper class for tracking preference values to keep track of the state of the custom tile
 */
@Singleton
public final class PreferenceHelper {

    private static final String PREFS_NAME = "eticket_tile_prefs";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public PreferenceHelper(@NonNull Context context) {
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Set a boolean value to the shared preferences
     *
     * @param key   Preference key
     * @param value Preference value
     */
    public void setBoolean(@NonNull String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Get a boolean value from the shared preferences
     *
     * @param key Preference key
     * @return the value if found, or {@code false}
     */
    public boolean getBoolean(@NonNull String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    /**
     * Set an int value to the shared preferences
     *
     * @param key   Preference key
     * @param value Preference value
     */
    public void setInt(@NonNull String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    /**
     * Get an int value from the shared preferences
     *
     * @param key Preference key
     * @return the value if found, or {@code 0}
     */
    public int getInt(@NonNull String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    /**
     * Set a String value to the shared preferences
     *
     * @param key   Preference key
     * @param value Preference value
     */
    public void setString(@NonNull String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    /**
     * Get a String value from the shared preferences
     *
     * @param key Preference key
     * @return the value if found, or {@code null}
     */
    public String getString(@NonNull String key) {
        return mSharedPreferences.getString(key, null);
    }
}
