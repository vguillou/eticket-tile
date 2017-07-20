package me.vguillou.etickettile;

import android.service.quicksettings.TileService;

import javax.inject.Inject;

import me.vguillou.etickettile.di.Injector;
import me.vguillou.etickettile.helper.ETicketModeHelper;
import me.vguillou.etickettile.helper.ETicketTileVisibilityHelper;
import me.vguillou.etickettile.helper.QsTileActivationHelper;

/**
 * Nougat+ (api 24+) service to handle the tile
 */
public class ETicketTileService extends TileService {

    @Inject
    private QsTileActivationHelper mQsTileActivationHelper;

    @Inject
    private ETicketTileVisibilityHelper mETicketTileVisibilityHelper;

    @Inject
    private ETicketModeHelper mETicketModeHelper;

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
    public void onTileAdded() {
        super.onTileAdded();
        inject();
        mETicketTileVisibilityHelper.setTileShown(true);

        // Initializing the tile
        mQsTileActivationHelper.tileAdded(getQsTile());
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        inject();
        mETicketTileVisibilityHelper.setTileShown(false);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        inject();
        mETicketTileVisibilityHelper.setTileShown(true);

        // Restore the tile state whenever it becomes visible
        mQsTileActivationHelper.changeState(getQsTile(), mETicketModeHelper.isETicketModeActivated());
    }

    @Override
    public void onClick() {
        super.onClick();
        inject();

        // If lacking permissions, launching the config activity
        if (!mETicketModeHelper.hasPermission()) {
            startActivityAndCollapse(PermissionRequestActivity.getLaunchIntent(getApplicationContext()));
            return;
        }

        // Otherwise we can toggle the eTicket mode and update the tile
        final boolean activated = mETicketModeHelper.toggle();
        mQsTileActivationHelper.changeState(getQsTile(), activated);
    }
}
