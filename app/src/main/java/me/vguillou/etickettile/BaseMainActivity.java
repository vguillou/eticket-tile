package me.vguillou.etickettile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import me.vguillou.etickettile.helper.AppLauncherIconHelper;
import me.vguillou.etickettile.helper.SettingsHelper;

/**
 * Base implementation for the MainActivity. Here is the common code between Marshmallow and Nougat+
 */
public abstract class BaseMainActivity extends Activity {

    /**
     * "startActivityForResult" request code used when requesting the uninstallation
     */
    private static final int UNINSTALL_REQUEST_CODE = 1;

    /**
     * Helper to deal with the system settings
     */
    protected SettingsHelper mSettingsHelper;

    /**
     * Helper to show/hide the App's launcher icon in the user's app drawer
     */
    private AppLauncherIconHelper mAppLauncherIconHelper;

    /**
     * Button to show/hide the App's launcher icon in the user's app drawer
     */
    protected Button btnAppLauncherToggle;

    /**
     * Intent to launch to MainActivity
     *
     * @param context Calling context
     * @return The intent
     */
    public static Intent getLaunchIntent(@NonNull Context context) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the helpers
        mSettingsHelper = new SettingsHelper(this);
        mAppLauncherIconHelper = new AppLauncherIconHelper(this, "alias");

        // If no permission, redirect to the PermissionRequestActivity
        if (!mSettingsHelper.hasPermission()) {
            startActivity(PermissionRequestActivity.getLaunchIntent(this));
            finishAffinity();
        }

        // Views
        setContentView(R.layout.activity_main);

        btnAppLauncherToggle = (Button) findViewById(R.id.btn_app_launcher_toggle);
        updateBtnAppLauncherToggle();
        btnAppLauncherToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAppLauncherIconHelper.isAppLauncherIconEnabled())
                    toggleAppLauncherIcon();
                else {
                    new AlertDialog.Builder(view.getContext())
                            .setMessage(R.string.dialog_confirm_hide_launcher_icon)
                            .setCancelable(true)
                            .setPositiveButton(R.string.dialog_confirm_hide_launcher_icon_btn_positive, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    toggleAppLauncherIcon();
                                }
                            })
                            .setNegativeButton(R.string.dialog_confirm_hide_launcher_icon_btn_negative, null)
                            .create()
                            .show();
                }
            }
        });

        findViewById(R.id.btn_uninstall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beforeUninstall();
                requestUninstall();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // If no permission, redirect to the PermissionRequestActivity
        if (!mSettingsHelper.hasPermission()) {
            startActivity(PermissionRequestActivity.getLaunchIntent(this));
            finishAffinity();
        }
    }

    /**
     * Should do any processing necessary before the uninstallation of the app.
     */
    protected void beforeUninstall() {
        // Nothing to do by default
    }

    /**
     * Should do any processing necessary after the cancellation of the uninstallation of the app.
     * Basically, should undo whatever was done in {@code beforeUninstall()}
     */
    protected void cancelUninstall() {
        // Nothing to do by default
    }

    /**
     * Toggles the display of the app's launcher icon in the system's app drawer
     */
    private void toggleAppLauncherIcon() {
        mAppLauncherIconHelper.toggleAppLauncherIcon();
        updateBtnAppLauncherToggle();
    }

    /**
     * Updates the Button to show/hide the App's launcher icon in the user's app drawer
     */
    private void updateBtnAppLauncherToggle() {
        btnAppLauncherToggle.setText(mAppLauncherIconHelper.isAppLauncherIconEnabled() ? R.string.activity_main_button_hide_launcher_icon : R.string.activity_main_button_show_launcher_icon);
    }

    /**
     * Request the uninstallation of the app
     */
    private void requestUninstall() {
        final Uri packageURI = Uri.parse("package:" + getPackageName());
        final Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivityForResult(uninstallIntent, UNINSTALL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If we get back the uninstallation request, the it was not validated.
        if (requestCode == UNINSTALL_REQUEST_CODE)
            cancelUninstall();
    }
}
