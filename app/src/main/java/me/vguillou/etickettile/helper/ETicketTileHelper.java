package me.vguillou.etickettile.helper;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Base helper to handle the eTicket Quick Settings tile
 */
public class ETicketTileHelper {

    /**
     * Keeps track of the last known state of the Quick Settings custom tile. There doesn't seem to
     * be a way to query the state of the tile.
     */
    private static final String PREF_ETICKET_TILE_SHOWN = "me.vguillou.ETICKETTILE_SHOWN";

    private final PreferenceHelper mPreferenceHelper;

    public ETicketTileHelper(@NonNull Context context) {
        mPreferenceHelper = new PreferenceHelper(context);
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
