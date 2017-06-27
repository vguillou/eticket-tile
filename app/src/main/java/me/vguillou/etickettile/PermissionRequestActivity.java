package me.vguillou.etickettile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import me.vguillou.etickettile.helper.SettingsHelper;

/**
 * First activity to be shown to the user.
 * Asks nicely to grant to permission to modify the System settings.
 */
public final class PermissionRequestActivity extends Activity {

    private SettingsHelper mSettingsHelper;

    /**
     * Intent to launch the PermissionRequestActivity
     *
     * @param context Calling context
     * @return The intent
     */
    public static Intent getLaunchIntent(@NonNull Context context) {
        final Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the settings helper
        mSettingsHelper = new SettingsHelper(this);

        // If permissions are fine, let's launch the main activity
        if (mSettingsHelper.hasPermission()) {
            startActivity(MainActivity.getLaunchIntent(this));
            finishAffinity();
            return;
        }

        // Views
        setContentView(R.layout.activity_permission_request);
        findViewById(R.id.btnGoToPermissions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSettingsHelper.requestPermissionIfNeeded();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // If permissions are fine, let's launch the main activity
        if (mSettingsHelper.hasPermission()) {
            startActivity(MainActivity.getLaunchIntent(this));
            finishAffinity();
        }
    }
}
