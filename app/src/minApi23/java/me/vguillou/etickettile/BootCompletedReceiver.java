package me.vguillou.etickettile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.vguillou.etickettile.helper.ETicketBroadcastTileHelper;
import me.vguillou.etickettile.helper.ETicketModeHelper;

/**
 * BroadcastReceiver for the BOOT_COMPLETED event which handles re-registering the custom tile after
 * a reboot if it was previously shown.
 */
public final class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            final ETicketBroadcastTileHelper helper = new ETicketBroadcastTileHelper(context);

            if (helper.isTileShown()) {
                helper.showTile(new ETicketModeHelper(context).isETicketModeActivated());
            }
        }
    }
}
