package me.vguillou.etickettile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import me.vguillou.etickettile.di.Injector;
import me.vguillou.etickettile.helper.ETicketBroadcastTileHelper;
import me.vguillou.etickettile.helper.ETicketModeHelper;

/**
 * BroadcastReceiver for the BOOT_COMPLETED event which handles re-registering the custom tile after
 * a reboot if it was previously shown.
 */
public final class BootCompletedReceiver extends BroadcastReceiver {

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
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            inject();
            if (mETicketBroadcastTileHelper.isTileShown()) {
                mETicketBroadcastTileHelper.showTile(mETicketModeHelper.isETicketModeActivated());
            }
        }
    }
}
