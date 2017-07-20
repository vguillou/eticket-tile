package me.vguillou.etickettile.helper;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.support.annotation.NonNull;

/**
 * Different tile activation methods. Never calls "updateTile()".
 */
public abstract class TileActivator {

    protected void tileAdded(@NonNull final Context context,
                             @NonNull final Tile tile) {
        // Do nothing by default
    }

    /**
     * Called when the ITileActivator used by the app changed
     */
    protected void activatorChanged(@NonNull final Context context,
                                    @NonNull final Tile tile) {
        // Do nothing by default
    }

    /**
     * Called when the state of the tile must change
     */
    protected abstract void changeState(@NonNull final Context context,
                                        @NonNull final Tile tile,
                                        final boolean activated);

    /**
     * Standard way of toggling the tile activation's state
     */
    protected static final class StandardActivator extends TileActivator {

        private int mDrawableTileOnResId;

        protected StandardActivator(int drawableTileOnResId) {
            mDrawableTileOnResId = drawableTileOnResId;
        }

        @Override
        protected void tileAdded(@NonNull final Context context,
                                 @NonNull final Tile tile) {
            tile.setState(Tile.STATE_INACTIVE);
        }

        @Override
        protected void activatorChanged(@NonNull final Context context,
                                        @NonNull final Tile tile) {
            tile.setIcon(Icon.createWithResource(context, mDrawableTileOnResId));
        }

        @Override
        protected void changeState(@NonNull final Context context,
                                   @NonNull final Tile tile,
                                   final boolean activated) {
            tile.setState(activated ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
        }
    }

    /**
     * Changing the tile's drawable manually, typically with a semi-transparent for when deactivated
     */
    protected static final class AlphaDrawableActivator extends TileActivator {

        private int mDrawableTileOnResId;
        private int mDrawableTileOffResId;

        protected AlphaDrawableActivator(int drawableTileOnResId,
                                         int drawableTileOffResId) {
            mDrawableTileOnResId = drawableTileOnResId;
            mDrawableTileOffResId = drawableTileOffResId;
        }

        @Override
        protected void tileAdded(@NonNull final Context context,
                                 @NonNull final Tile tile) {
            tile.setState(Tile.STATE_ACTIVE);
        }

        @Override
        public void changeState(@NonNull final Context context,
                                @NonNull final Tile tile,
                                final boolean activated) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(context,
                    activated ? mDrawableTileOnResId : mDrawableTileOffResId));
        }
    }
}

