package me.vguillou.etickettile.helper;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Helper to handle the app's launcher icon
 */
@Singleton
public final class AppLauncherIconHelper {

    private final ComponentName mMainActivityComponentName;

    private final PackageManager mPackageManager;

    /**
     * Constructor
     *
     * @param packageManager            the package manager
     * @param mainActivityComponentName ComponentName for the main Activity
     */
    @Inject
    public AppLauncherIconHelper(@NonNull PackageManager packageManager,
                                 @Named("main_activity_component_name") @NonNull final ComponentName mainActivityComponentName) {
        mPackageManager = packageManager;
        mMainActivityComponentName = mainActivityComponentName;
    }

    /**
     * Check if the app's launcher icon is enabled
     *
     * @return {@code true} if enabled
     */
    public boolean isAppLauncherIconEnabled() {
        final int state = mPackageManager.getComponentEnabledSetting(mMainActivityComponentName);
        return state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT || state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
    }

    /**
     * Toggle the state of the app's launcher icon between enabled and disabled
     */
    public void toggleAppLauncherIcon() {
        final boolean isEnabled = isAppLauncherIconEnabled();
        mPackageManager.setComponentEnabledSetting(mMainActivityComponentName,
                isEnabled ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
