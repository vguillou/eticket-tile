package me.vguillou.etickettile;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import me.vguillou.etickettile.helper.ETicketBroadcastTileHelper;

/**
 * Main activity, containing the steps to "install" the tile, plus a few settings
 */
public final class MainActivity extends BaseMainActivity {

    /**
     * Marshmallow's helper to deal with the custom Quick Setting Tile
     */
    @Inject
    private ETicketBroadcastTileHelper mETicketBroadcastTileHelper;

    /**
     * Update prompt if >= Android N
     */
    private AlertDialog mUpdateAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Views
        findViewById(R.id.btn_copy_clipboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Copy to clipboard the Broadcast identifier to use
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("eTicket tile identifier", ETicketBroadcastTileHelper.BROADCAST_TILE_IDENTIFIER);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(view.getContext(), getString(R.string.activity_main_step_1_validated), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.btn_open_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettings();
            }
        });

        // Prompt to update if Android N or beyond
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mUpdateAlertDialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.dialog_update_necessary_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.dialog_update_necessary_btn_playstore, null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    })
                    .create();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Toggle the tile every time, as there is no way to know for sure when the user
        // actually did set up the broadcast tile
        showTile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Always prompt, ALWAYS !
        if (mUpdateAlertDialog != null && !mUpdateAlertDialog.isShowing())
            mUpdateAlertDialog.show();
    }

    @Override
    protected void beforeUninstall() {
        super.beforeUninstall();
        // Hide the tile, as it's not done automatically by the system (marshmallow) upon uninstallation
        hideTile();
    }

    @Override
    protected void cancelUninstall() {
        super.cancelUninstall();
        // Restore the tile anyway
        showTile();
    }

    /**
     * Show the Quick Setting Tile
     */
    private void showTile() {
        mETicketBroadcastTileHelper.showTile(false);
    }

    /**
     * Hide the Quick Setting Tile
     */
    private void hideTile() {
        mETicketBroadcastTileHelper.hideTile();
    }

    /**
     * Open the System settings
     */
    private void openSettings() {
        startActivityForResult(mSettingsHelper.getOpenSettingsIntent(), 0);
    }
}
