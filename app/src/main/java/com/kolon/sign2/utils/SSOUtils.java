package com.kolon.sign2.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.miaps.external.contentprovider.KolonProvider;
import com.miaps.external.contentprovider.KolonProviderReal;
import com.kolon.sign2.BuildConfig;
/**
 * Created by sunho_kim on 2019-12-02.
 */

public class SSOUtils {
    public static boolean is_App_Link;
    public static String getKolonProviderValue(Context context, String key, String defaultValue) {
        String ret = "";
        try {
            String packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
            if("com.kolon.sign2".equals(packageName)) {
                // REAL
                ret = KolonProviderReal.getEx(context, key);
            } else {
                // DEV
                ret = KolonProvider.getEx(context, key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static  void putKolonProviderValue(Context context, String key, String value) {
        try {
            String packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
            if("com.kolon.sign2".equals(packageName)) {
                // REAL
                KolonProviderReal.putEx(context, key, value);
            } else {
                // DEV
                KolonProvider.putEx(context, key, value);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean findIKENApp(Context context){
        PackageManager pm = context.getPackageManager();
        String ikenPackageNm = "";
        if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            ikenPackageNm = "com.kolon.ikenapp2dev";
        } else {
            ikenPackageNm = "com.kolon.ikenapp2";
        }
        try {
            pm.getApplicationInfo(ikenPackageNm, 0);
            return true;
        }
        catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
