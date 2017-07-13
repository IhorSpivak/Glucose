package com.ixbiopharma.glucose.di;

import com.ixbiopharma.glucose.GlucoseApplication;
import com.ixbiopharma.glucose.api.ApiImpl;
import com.ixbiopharma.glucose.repository.storage.AppPrefs;
import com.ixbiopharma.glucose.repository.storage.RealmHelper;

/**
 * ApplicationComponent
 * <p>
 * Created by ivan on 22.04.17.
 */

class ApplicationComponent {

    private static RealmHelper realmHelper;
    private static ApiImpl api;
    private static AppPrefs prefs;

    static ApiImpl provideApi() {
        if (api == null) {
            api = new ApiImpl();
        }

        return api;
    }

    static RealmHelper provideRealmHelper() {
        if (realmHelper == null) {
            realmHelper = new RealmHelper();
        }

        return realmHelper;
    }

    static AppPrefs providePrefs() {
        if (prefs == null) {
            prefs = AppPrefs.create(GlucoseApplication.getInstance());
        }

        return prefs;
    }
}
