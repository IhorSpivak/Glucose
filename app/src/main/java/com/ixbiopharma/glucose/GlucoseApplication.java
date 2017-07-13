package com.ixbiopharma.glucose;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * GlucoseApplication
 *
 * Created by jwgibanez on 2/3/17.
 */

public class GlucoseApplication extends Application {

    private static GlucoseApplication app;
    public static boolean showPopup = true;

    public static GlucoseApplication getInstance() {
        return app;
    }

    public static String APP_VERSION = "1.0.0";
    public static final String OS_VERSION = String.valueOf(Build.VERSION.SDK_INT);

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            APP_VERSION = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Fabric.with(this, new Crashlytics());

        Realm.init(this);


        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setFontAttrId(R.attr.fontPath)
                .build());

        Fresco.initialize(this);

        SharedPreferences prefs = getSharedPreferences("myApp", MODE_PRIVATE);

        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
