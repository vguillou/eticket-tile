package me.vguillou.etickettile;

import android.service.quicksettings.TileService;

import me.vguillou.etickettile.helper.ETicketModeHelper;
import me.vguillou.etickettile.helper.ETicketTileHelper;
import me.vguillou.etickettile.helper.PreferenceHelper;
import me.vguillou.etickettile.helper.QsTileHelper;

/**
 * Nougat+ (api 24+) service to handle the tile
 */
public class ETicketTileService extends TileService {

    private ETicketModeHelper mETicketModeHelper;

    /**
     * @return The Quick Settings Tile helper
     */
    private QsTileHelper getQsTileHelper() {
        return new QsTileHelper(getApplicationContext(),
                R.drawable.ic_eticket_24dp,
                R.string.tile_content_desc_on,
                R.drawable.ic_eticket_disabled_24dp,
                R.string.tile_content_desc_off);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        new ETicketTileHelper(getApplicationContext()).setTileShown(true);

        // Initializing the tile
        QsTileHelper.initialize(new PreferenceHelper(getApplicationContext()), getQsTile());
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        new ETicketTileHelper(getApplicationContext()).setTileShown(false);
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        mETicketModeHelper = new ETicketModeHelper(getApplicationContext());

        // Restore the tile state whenever it becomes visible
        getQsTileHelper().changeState(getQsTile(), mETicketModeHelper.isETicketModeActivated());
    }

    @Override
    public void onClick() {
        super.onClick();

        // If lacking permissions, launching the config activity
        if (!mETicketModeHelper.hasPermission()) {
            startActivityAndCollapse(PermissionRequestActivity.getLaunchIntent(getApplicationContext()));
            return;
        }

        // Otherwise we can toggle the eTicket mode and update the tile
        final boolean activated = mETicketModeHelper.toggle();
        getQsTileHelper().changeState(getQsTile(), activated);
    }
}
