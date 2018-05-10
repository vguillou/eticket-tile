package me.vguillou.etickettile.helper;

import android.content.Context;
import android.service.quicksettings.Tile;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Helper for managing a Quick Settings tile that switches between on / off states.
 * Supports using an alternative mode,
 * as sing the native {@code getQsTile().setState()} is broken on many AOSP based ROMs.
 */
public class QsTileActivationHelper {

    /**
     * If an alternative display mode is to be used for the tile
     */
    private static final String PREF_ALT_TILE_DISPLAY_MODE = "me.vguillou.ALT_TILE_DISPLAY_MODE";
    private static final String PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED = "me.vguillou.ALT_TILE_DISPLAY_MODE_HAS_CHANGED";

    private final Context mContext;

    /**
     * The preference helper
     */
    private final PreferenceHelper mPreferenceHelper;

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
    @Inject
    public QsTileActivationHelper(@NonNull final Context context,
                                  @NonNull final PreferenceHelper preferenceHelper,
                                  @Named("drawable_tile_activated") final int drawableOn,
                                  @Named("string_cd_tile_activated") final int contentDescOn,
                                  @Named("drawable_tile_deactivated") final int drawableOff,
                                  @Named("string_cd_tile_deactivated") final int contentDescOff) {
        mContext = context;
        mPreferenceHelper = preferenceHelper;
        mDrawableOn = drawableOn;
        mContentDescOn = contentDescOn;
        mDrawableOff = drawableOff;
        mContentDescOff = contentDescOff;
    }

    /**
     * @return The method to use to activate the tile
     */
    @NonNull
    private TileActivator getTileActivator() {
        return isAlternativeMode()
                ? new TileActivator.AlphaDrawableActivator(mDrawableOn, mDrawableOff)
                : new TileActivator.StandardActivator(mDrawableOn);
    }

    /**
     * Initialize the tile to manage.
     * Must be called in {@code onTileAdded} in the {@code TileService}
     *
     * @param tile The tile to manage
     */
    public void tileAdded(@Nullable final Tile tile) {
        if (tile != null) {
            getTileActivator().tileAdded(mContext, tile);
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
    public void changeState(@Nullable final Tile tile,
                            final boolean activated) {
        if (tile != null) {
            final TileActivator tileActivator = getTileActivator();

            // If the activation method changed, some work may be required
            if (mPreferenceHelper.getBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED))
                tileActivator.activatorChanged(mContext, tile);

            // Change the state
            tileActivator.changeState(mContext, tile, activated);

            // Accessibility
            tile.setContentDescription(mContext.getString(
                    activated ? mContentDescOn : mContentDescOff));

            // Update the tile obviously
            tile.updateTile();
        }

        // Reset the "ModeHasChanged" flag
        mPreferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED, false);
    }

    /**
     * @return If the helper uses an alternative toggle method
     */
    public boolean isAlternativeMode() {
        return mPreferenceHelper.getBoolean(PREF_ALT_TILE_DISPLAY_MODE);
    }

    /**
     * @param alternativeMode Makes the helper use an alternative toggle methode, or not
     */
    public void setAlternativeMode(boolean alternativeMode) {
        mPreferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE, alternativeMode);
        mPreferenceHelper.setBoolean(PREF_ALT_TILE_DISPLAY_MODE_HAS_CHANGED, true);
    }
}
