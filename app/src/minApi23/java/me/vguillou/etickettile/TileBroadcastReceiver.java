package me.vguillou.etickettile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import me.vguillou.etickettile.di.Injector;
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

    @Inject
    private ETicketModeHelper mETicketModeHelper;

    @Inject
    private ETicketBroadcastTileHelper mETicketBroadcastTileHelper;

    /**
     * Dependencies injector
     */
    private Injector injector;

    /**
     * Inject dependencies if needed
     */
    private void inject() {
        if (injector == null) {
            injector = new Injector();
            injector.inject(this);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Click
        if (ACTION_ETICKET_TILE_CLICK.equals(intent.getAction())) {
            inject();

            // If lacking permissions, launching the permission request activity
            if (!mETicketModeHelper.hasPermission()) {
                context.startActivity(PermissionRequestActivity.getLaunchIntent(context));
                return;
            }

            // Otherwise we can toggle the eTicket mode and update the tile
            final boolean activated = mETicketModeHelper.toggle();
            mETicketBroadcastTileHelper.showTile(activated);
        }
        // Long click
        else if (ACTION_ETICKET_TILE_LONG_CLICK.equals(intent.getAction())) {
            context.startActivity(MainActivity.getLaunchIntent(context));
        }
    }
}
