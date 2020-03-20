package com.kolon.sign2.network;

import com.kolon.sign2.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by sunho_kim on 2019-12-02.
 */

public class NetworkConstants {
    public static final String networkSuccess = "success";

    public static final int REQUEST_CODE_NOTICE_POPUP = 1000;
    public static final String TARGET_URL_IF_001 = "appstore_APP_IF_001";
    public static final String TARGET_URL_IF_002 = "appstore_APP_IF_002";
    public static final String TARGET_URL_IF_003 = "appstore_APP_IF_003";
    public static final String TARGET_URL_IF_004 = "appstore_APP_IF_004";
    public static final String TARGET_URL_IF_005 = "appstore_APP_IF_005";
    public static final String TARGET_URL_IF_006 = "appstore_APP_IF_006";
    public static final String TARGET_URL_IF_007 = "appstore_APP_IF_007";
    public static final String TARGET_URL_IF_008 = "appstore_APP_IF_008";
    public static final String TARGET_URL_IF_009 = "appstore_APP_IF_009";
    public static final String TARGET_URL_IF_010 = "appstore_APP_IF_010";
    public static final String TARGET_URL_IF_111 = "Board-listBoard";
    public static final String TARGET_URL_IF_112 = "Board-detailBoard";

    public static OkHttpClient getRequestHeader() {
        OkHttpClient okHttpClient;
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

}
