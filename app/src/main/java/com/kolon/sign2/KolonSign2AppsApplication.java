package com.kolon.sign2;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.kolon.sign2.utils.CommonUtils;

public class KolonSign2AppsApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String deviceId = CommonUtils.getDeviceID(getApplicationContext());
        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.setCrashlyticsCollectionEnabled(true);
        crashlytics.setCustomKey("device_id",deviceId);
        crashlytics.setCustomKey("build_type",BuildConfig.BUILD_TYPE);
//        throw new RuntimeException("Test Crash"); // test Only
    }


}
