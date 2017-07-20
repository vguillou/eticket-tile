package me.vguillou.etickettile;

import android.app.Application;

import javax.inject.Inject;

import me.vguillou.etickettile.di.Injector;
import me.vguillou.etickettile.di.MyModule;
import me.vguillou.etickettile.helper.ETicketTileVisibilityHelper;
import me.vguillou.etickettile.helper.PreferenceHelper;

public class MyApplication extends Application {

    /**
     * Keeps track of the last known installed flavor.
     */
    private static final String PREF_LAST_INSTALLED_FLAVOR = "me.vguillou.LAST_INSTALLED_FLAVOR";
    private static MyApplication instance;
    /**
     * Dependency injector
     */
    private Injector injector;
    @Inject
    private PreferenceHelper mPreferenceHelper;
    @Inject
    private ETicketTileVisibilityHelper mETicketTileVisibilityHelper;

    /**
     * @return App singleton instance
     */
    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        injector = buildInjector();

        // Retrieve the previously installed flavor
        final String previouslyInstalledFlavor = mPreferenceHelper.getString(PREF_LAST_INSTALLED_FLAVOR);

        // If we have the Android N and up flavor, but we are coming from the Marshmallow flavor,
        // a bit of clean up is necessary
        if (!Flavors.MIN_API_23.equals(BuildConfig.FLAVOR)
                && previouslyInstalledFlavor != null
                && Flavors.MIN_API_23.equals(previouslyInstalledFlavor)) {
            mETicketTileVisibilityHelper.setTileShown(false);
        }

        // Save the installed flavor
        mPreferenceHelper.setString(PREF_LAST_INSTALLED_FLAVOR, BuildConfig.FLAVOR);
    }

    /**
     * Build the injector. Override for tests.
     *
     * @return The Injector
     */
    private Injector buildInjector() {
        return new Injector().startApp(this, new MyModule(this));
    }

    /**
     * @return The injector, previously build for the app
     */
    public Injector getInjector() {
        return injector;
    }
}
