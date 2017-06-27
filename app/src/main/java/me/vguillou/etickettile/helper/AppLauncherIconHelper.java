package me.vguillou.etickettile.helper;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

/**
 * Helper to handle the app's launcher icon
 */
public final class AppLauncherIconHelper {

    private final ComponentName mComponentName;

    private final PackageManager mPackageManager;

    /**
     * Constructor
     *
     * @param context              Calling context
     * @param launcherActivityName Name of your app's main activity class, started when the launcher icon is pressed
     */
    public AppLauncherIconHelper(@NonNull final Context context,
                                 @NonNull final String launcherActivityName) {
        mPackageManager = context.getPackageManager();
        mComponentName = new ComponentName(context, context.getPackageName() + "." + launcherActivityName);
    }

    /**
     * Check if the app's launcher icon is enabled
     *
     * @return {@code true} if enabled
     */
    public boolean isAppLauncherIconEnabled() {
        final int state = mPackageManager.getComponentEnabledSetting(mComponentName);
        return state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    /**
     * Toggle the state of the app's launcher icon between enabled and disabled
     */
    public void toggleAppLauncherIcon() {
        final boolean isEnabled = isAppLauncherIconEnabled();
        mPackageManager.setComponentEnabledSetting(mComponentName,
                isEnabled ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
