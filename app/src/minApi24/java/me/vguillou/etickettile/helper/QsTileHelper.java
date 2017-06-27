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
    }

    /**
     * Initialize the tile to manage.
     * Must be called in {@code onTileAdded} in the {@code TileService}
     *
     * @param tile The tile to manage
     */
    public static void initialize(final Tile tile) {
        if (tile != null) {
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
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(mContext,
                    activated ? mDrawableOn : mDrawableOff));
            tile.setContentDescription(mContext.getString(
                    activated ? mContentDescOn : mContentDescOff));
            tile.updateTile();
        }
    }
}
