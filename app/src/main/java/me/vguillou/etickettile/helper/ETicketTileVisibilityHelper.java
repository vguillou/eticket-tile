package me.vguillou.etickettile.helper;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Base helper to handle the eTicket Quick Settings tile
 */
@Singleton
public class ETicketTileVisibilityHelper {

    /**
     * Keeps track of the last known state of the Quick Settings custom tile. There doesn't seem to
     * be a way to query the state of the tile.
     */
    private static final String PREF_ETICKET_TILE_SHOWN = "me.vguillou.ETICKETTILE_SHOWN";

    protected final Context mContext;

    protected final PreferenceHelper mPreferenceHelper;

    @Inject
    public ETicketTileVisibilityHelper(@NonNull Context context,
                                       @NonNull PreferenceHelper preferenceHelper) {
        mContext = context.getApplicationContext();
        mPreferenceHelper = preferenceHelper;
    }

    /**
     * Check if the tile is shown (was added to the quick settings)
     *
     * @return {@code true} if shown, {@code false} otherwise
     */
    public boolean isTileShown() {
        return mPreferenceHelper.getBoolean(PREF_ETICKET_TILE_SHOWN);
    }

    /**
     * Set the "shown" flag, indicating that the tile was added to the quick settings
     *
     * @param shown the value to set to the flag
     */
    public void setTileShown(final boolean shown) {
        mPreferenceHelper.setBoolean(PREF_ETICKET_TILE_SHOWN, shown);
    }
}
