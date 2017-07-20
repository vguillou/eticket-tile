package me.vguillou.etickettile.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.kcoppock.broadcasttilesupport.BroadcastTileIntentBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import me.vguillou.etickettile.TileBroadcastReceiver;

@Singleton
public final class ETicketBroadcastTileHelper extends ETicketTileVisibilityHelper {

    /**
     * This is the identifier of the custom Broadcast Tile. Whatever action you configured the tile
     * for must be used when configuring the tile. For Broadcast tiles, only alphanumeric characters
     * (and periods) are allowed. Keep in mind that this excludes underscores.
     */
    public static final String BROADCAST_TILE_IDENTIFIER = "me.vguillou.ETICKETTILE";

    private final int mTileLabel;

    private final int mDrawableOn;

    private final int mDrawableOff;

    private final int mContentDescOn;

    private final int mContentDescOff;

    @Inject
    public ETicketBroadcastTileHelper(@NonNull Context context,
                                      @NonNull PreferenceHelper preferenceHelper,
                                      @Named("string_tile_label") final int tileLabel,
                                      @Named("drawable_tile_activated") final int drawableOn,
                                      @Named("string_cd_tile_activated") final int contentDescOn,
                                      @Named("drawable_tile_deactivated") final int drawableOff,
                                      @Named("string_cd_tile_deactivated") final int contentDescOff) {
        super(context, preferenceHelper);
        mTileLabel = tileLabel;
        mDrawableOn = drawableOn;
        mContentDescOn = contentDescOn;
        mDrawableOff = drawableOff;
        mContentDescOff = contentDescOff;
    }

    /**
     * Show the Quick Settings Tile
     *
     * @param activated if it should be shown with or without the activated state
     */
    public void showTile(final boolean activated) {
        setTileShown(true);

        // Set up an Intent that will be broadcast by the system, and received by the exported
        // TileBroadcastReceiver.
        final Intent eticketModeClickIntent = new Intent(TileBroadcastReceiver.ACTION_ETICKET_TILE_CLICK);

        // Set up a PendingIntent that will be delivered back to the application on a long-click
        // of the custom Broadcast Tile.
        final Intent eticketModeLongClickIntent = new Intent(TileBroadcastReceiver.ACTION_ETICKET_TILE_LONG_CLICK);

        // Send the update event to the Broadcast Tile. Custom tiles are hidden by default until
        // enabled with this broadcast Intent.
        mContext.sendBroadcast(new BroadcastTileIntentBuilder(mContext.getApplicationContext(), BROADCAST_TILE_IDENTIFIER)
                .setVisible(true)
                .setLabel(mContext.getString(mTileLabel))
                .setIconResource(activated
                        ? mDrawableOn
                        : mDrawableOff)
                .setOnClickBroadcast(eticketModeClickIntent)
                .setOnLongClickBroadcast(eticketModeLongClickIntent)
                .setContentDescription(mContext.getString(activated
                        ? mContentDescOn
                        : mContentDescOff))
                .build());
    }

    /**
     * Hide the Quick Settings Tile
     */
    public void hideTile() {
        setTileShown(false);

        mContext.sendBroadcast(new BroadcastTileIntentBuilder(mContext, BROADCAST_TILE_IDENTIFIER)
                .setVisible(false)
                .build());
    }
}
