package me.vguillou.etickettile.helper;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.support.annotation.NonNull;

/**
 * Helper for managing a Quick Settings tile that switches between on / off states.
 * Using the native {@code getQsTile().setState()} is broken on many AOSP based ROMs.
 */
public class QsTileHelper {

    /**
     * If an alternative display mode is to be used for the tile
     */
    private static final String PREF_ALT_TILE_DISPLAY_MODE = "me.vguillou.ALT_TILE_DISPLAY_MODE";
    private static final String PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED = "me.vguillou.ALT_TILE_DISPLAY_MODE_HAS_CHANGED";

    /**
     * The preference helper
     */
    private final PreferenceHelper mPreferenceHelper;

    private final Context mContext;

    private final int mDrawableOn;

    private final int mDrawableOff;

    private final int mContentDescOn;

    private final int mContentDescOff;

    /**
     * Constructor
     *
     * @param context        The Calling context
     * @param drawableOn     The drawable to display in the activated state
     * @param contentDescOn  The content description for the activated state
     * @param drawableOff    The drawable to display in the deactivated state
     * @param contentDescOff The content description for the deactivated state
     */
    public QsTileHelper(@NonNull final Context context,
                        final int drawableOn,
                        final int contentDescOn,
                        final int drawableOff,
                        final int contentDescOff) {
        mContext = context;
        mDrawableOn = drawableOn;
        mContentDescOn = contentDescOn;
        mDrawableOff = drawableOff;
        mContentDescOff = contentDescOff;
        mPreferenceHelper = new PreferenceHelper(context);
    }

    /**
     * Initialize the tile to manage.
     * Must be called in {@code onTileAdded} in the {@code TileService}
     *
     * @param preferenceHelper The preference helper
     * @param tile The tile to manage
     */
    public static void initialize(@NonNull PreferenceHelper preferenceHelper, final Tile tile) {
        if (tile != null && isAlternativeMode(preferenceHelper)) {
            // Always activated, this way it is possible to handle alpha manually
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
    }

    /**
     * Change the state of the Quick Settings tile.
     * Typically called in {@code onClick} in the {@code TileService}
     *
     * @param tile      The tile to manage
     * @param activated {@code true} to activate, {@code false} to deactivate
     */
    public void changeState(final Tile tile,
                            final boolean activated) {
        if (tile != null) {
            // Alt mode, handling alpha manually
            if (isAlternativeMode(mPreferenceHelper))
                altChangeState(tile, activated);
            // Normal mode
            else {
                // If the mode has changed, we must restore the good drawable for normal mode
                if (mPreferenceHelper.getBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED))
                    tile.setIcon(Icon.createWithResource(mContext, mDrawableOn));
                tile.setState(activated ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
            }
            // Accessibility
            tile.setContentDescription(mContext.getString(
                    activated ? mContentDescOn : mContentDescOff));
            tile.updateTile();
            mPreferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED, false);
        }
    }

    private void altChangeState(final Tile tile,
                                final boolean activated) {
        tile.setState(Tile.STATE_ACTIVE);
        tile.setIcon(Icon.createWithResource(mContext,
                activated ? mDrawableOn : mDrawableOff));
    }

    /**
     * @param preferenceHelper The preference helper
     * @return If the helper uses an alternative toggle method
     */
    public static boolean isAlternativeMode(@NonNull PreferenceHelper preferenceHelper) {
        return preferenceHelper.getBoolean(PREF_ALT_TILE_DISPLAY_MODE);
    }

    /**
     * @param preferenceHelper The preference helper
     * @param alternativeMode Makes the helper use an alternative toggle methode, or not
     */
    public static void setAlternativeMode(@NonNull PreferenceHelper preferenceHelper, boolean alternativeMode) {
        preferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE, alternativeMode);
        preferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED, true);
    }
}
