package me.vguillou.etickettile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.vguillou.etickettile.helper.ETicketBroadcastTileHelper;
import me.vguillou.etickettile.helper.ETicketModeHelper;

/**
 * Exported receiver for the custom event on the custom Quick Settings tile
 */
public final class TileBroadcastReceiver extends BroadcastReceiver {

    /**
     * The action broadcast from the Quick Settings tile when clicked
     */
    public static final String ACTION_ETICKET_TILE_CLICK = "me.vguillou.ACTION_ETICKET_TILE_CLICK";

    /**
     * The action broadcast from the Quick Settings tile when long clicked
     */
    public static final String ACTION_ETICKET_TILE_LONG_CLICK = "me.vguillou.ACTION_ETICKET_TILE_LONG_CLICK";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Click
        if (ACTION_ETICKET_TILE_CLICK.equals(intent.getAction())) {
            final ETicketModeHelper eTicketModeHelper = new ETicketModeHelper(context);

            // If lacking permissions, launching the permission request activity
            if (!eTicketModeHelper.hasPermission()) {
                context.startActivity(PermissionRequestActivity.getLaunchIntent(context));
                return;
            }

            // Otherwise we can toggle the eTicket mode and update the tile
            final boolean activated = eTicketModeHelper.toggle();
            new ETicketBroadcastTileHelper(context).showTile(activated);
        }
        // Long click
        else if (ACTION_ETICKET_TILE_LONG_CLICK.equals(intent.getAction())) {
            context.startActivity(MainActivity.getLaunchIntent(context));
        }
    }
}
