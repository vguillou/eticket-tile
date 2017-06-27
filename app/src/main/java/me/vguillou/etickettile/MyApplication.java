package me.vguillou.etickettile;

import android.app.Application;

import me.vguillou.etickettile.helper.ETicketTileHelper;
import me.vguillou.etickettile.helper.PreferenceHelper;

public class MyApplication extends Application {

    /**
     * Keeps track of the last known installed flavor.
     */
    private static final String PREF_LAST_INSTALLED_FLAVOR = "me.vguillou.LAST_INSTALLED_FLAVOR";

    @Override
    public void onCreate() {
        super.onCreate();

        // Retrieve the previously installed flavor
        final PreferenceHelper preferenceHelper = new PreferenceHelper(this);
        final String previouslyInstalledFlavor = preferenceHelper.getString(PREF_LAST_INSTALLED_FLAVOR);

        // If we have the Android N and up flavor, but we are coming from the Marshmallow flavor,
        // a bit of clean up is necessary
        if (!Flavors.MIN_API_23.equals(BuildConfig.FLAVOR)
                && previouslyInstalledFlavor != null
                && Flavors.MIN_API_23.equals(previouslyInstalledFlavor)) {
            final ETicketTileHelper eTicketTileHelper = new ETicketTileHelper(this);
            eTicketTileHelper.setTileShown(false);
        }

        // Save the installed flavor
        preferenceHelper.setString(PREF_LAST_INSTALLED_FLAVOR, BuildConfig.FLAVOR);
    }
}
