package me.vguillou.etickettile.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.kcoppock.broadcasttilesupport.BroadcastTileIntentBuilder;

import me.vguillou.etickettile.R;
import me.vguillou.etickettile.TileBroadcastReceiver;

public final class ETicketBroadcastTileHelper extends ETicketTileHelper {

    /**
     * This is the identifier of the custom Broadcast Tile. Whatever action you configured the tile
     * for must be used when configuring the tile. For Broadcast tiles, only alphanumeric characters
     * (and periods) are allowed. Keep in mind that this excludes underscores.
     */
    public static final String BROADCAST_TILE_IDENTIFIER = "me.vguillou.ETICKETTILE";

    private final Context mContext;

    public ETicketBroadcastTileHelper(@NonNull Context context) {
        super(context);
        mContext = context.getApplicationContext();
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
                .setLabel(mContext.getString(R.string.tile_label))
                .setIconResource(activated
                        ? R.drawable.ic_eticket_24dp
                        : R.drawable.ic_eticket_disabled_24dp)
                .setOnClickBroadcast(eticketModeClickIntent)
                .setOnLongClickBroadcast(eticketModeLongClickIntent)
                .setContentDescription(mContext.getString(activated
                        ? R.string.tile_content_desc_on
                        : R.string.tile_content_desc_off))
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
