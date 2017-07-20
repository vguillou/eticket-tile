package me.vguillou.etickettile.di;

import android.app.Application;

import org.codejargon.feather.Feather;

import me.vguillou.etickettile.MyApplication;

/**
 * Simple abstraction of the dependency injector
 */
public class Injector {

    private Feather feather;

    public Injector() {
    }

    public Injector startApp(final Application app, Object module) {
        feather = Feather.with(module);
        feather.injectFields(app);
        return this;
    }

    public void inject(Object target) {
        MyApplication.getInstance()
                .getInjector()
                .feather
                .injectFields(target);
    }
}
