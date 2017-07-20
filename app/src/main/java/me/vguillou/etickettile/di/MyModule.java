package me.vguillou.etickettile.di;

import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.accessibility.AccessibilityManager;

import org.codejargon.feather.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

import me.vguillou.etickettile.R;

/**
 * DI module
 */
public class MyModule {

    private final Application mApp;

    public MyModule(Application app) {
        mApp = app;
    }

    @Provides
    Context context() {
        return mApp;
    }

    @Provides
    @Singleton
    ContentResolver contentResolver() {
        return mApp.getContentResolver();
    }

    @Provides
    AccessibilityManager accessibilityManager() {
        return (AccessibilityManager) mApp.getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
    }

    @Provides
    PackageManager packageManager() {
        return mApp.getPackageManager();
    }


    //////////////////////////////////////////
    //      Injectable Configuration        //
    //////////////////////////////////////////

    @Provides
    @Singleton
    @Named("main_activity_component_name")
    ComponentName mainActivityName() {
        return new ComponentName(mApp, mApp.getPackageName() + ".alias");
    }

    @Provides
    @Named("string_tile_label")
    int stringTileLabel() {
        return R.string.tile_label;
    }

    @Provides
    @Named("drawable_tile_activated")
    int drawableTileActivated() {
        return R.drawable.ic_eticket_24dp;
    }

    @Provides
    @Named("drawable_tile_deactivated")
    int drawableTileDeactivated() {
        return R.drawable.ic_eticket_disabled_24dp;
    }

    @Provides
    @Named("string_cd_tile_activated")
    int stringCdTileActivated() {
        return R.string.tile_content_desc_on;
    }

    @Provides
    @Named("string_cd_tile_deactivated")
    int stringCdTileDeactivated() {
        return R.string.tile_content_desc_off;
    }

    @Provides
    @Named("string_cd_tile_clicked_activated")
    int stringCdTileClickedActivated() {
        return R.string.tile_content_desc_click_on;
    }

    @Provides
    @Named("string_cd_tile_clicked_deactivated")
    int stringCdTileClickedDeactivated() {
        return R.string.tile_content_desc_click_off;
    }
}
