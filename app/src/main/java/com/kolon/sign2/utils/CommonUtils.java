package com.kolon.sign2.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

/**
 * Created by sunho_kim on 2019-12-02.
 */

public class CommonUtils {

    public static String getCurrentDate() {
        Calendar startCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startCalendar.getTime());
    }
    public static String getCurrentDateforSeq(String senderId) {
        Calendar startCalendar = Calendar.getInstance();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startCalendar.getTime()) + "_" + senderId;
    }



    public static String getTimeStamp() {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        Calendar days = Calendar.getInstance(timeZone);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(timeZone);
        String strDate = df.format(days.getTime());
        return strDate;
    }

    public static String getAppVersion(Context context) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return  pi.versionName;
    }
    public static String getAppPackageNm(Context context) {
//        int index = context.getPackageName().lastIndexOf(".");
//        String packageNm = context.getPackageName().substring(0, index) + context.getPackageName().substring(index+1);
        String packageNm = context.getPackageName();
        return  packageNm;
    }

    public static String getAppVersionName(Context context) {
        String version = null;

        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return version;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceID(Context context) {
        String result  = "";
        SharedPreferenceManager prefs = SharedPreferenceManager.getInstance(context);
        String androidId = prefs.getStringPreference(Constants.PREF_DEVICE_ID);
        if (null != androidId && androidId.length() > 0) {
            result = androidId;
        } else {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                result = tm.getDeviceId();
            } catch (SecurityException e) { /**Android Q(OS 10)에서 IMEI정보를 가져올 수 없음. */
                result = "";
            }
            if (null != result && result.length() > 0) {
            } else {
                try {
                    androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    if (!"9774d56d682e549c".equals(androidId)) {
                        result = UUID.nameUUIDFromBytes(androidId.getBytes(Charset.forName("utf-8"))).toString();
                    } else {
                        androidId = UUID.randomUUID().toString();
                        result = androidId;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            prefs.setStringPreference(Constants.PREF_DEVICE_ID, result);
        }
        return result;
    }

    public static String getTokenKey() {
        return "";
    }


    public static void hideSoftKeyboard(EditText editText, Context context) {
        InputMethodManager imm= (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(EditText editText, Context context) {
        InputMethodManager imm= (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }


    public static String ConvertDateString(String beforeString) {
        String  result = "";
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(beforeString);
            result = new SimpleDateFormat("yyyy.MM.dd").format(date);
        }catch (ParseException e) {
            try {
                Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(beforeString);
                result = new SimpleDateFormat("yyyy.MM.dd").format(date);
            }catch (ParseException e2) {
                result = beforeString;
            }
        }
        return result;
    }

    public static String EncryptionPassWord(String passwd) {
        boolean isTest = false;
        String passWord = "";
        String passWord2 = "";
        if (!isTest) {
            try {
                passwd = CipherUtils.encrypt(Constants.ENCRYPTION_KEY, passwd );
                passWord = URLEncoder.encode(passwd, "UTF-8");
            } catch (Exception e) {
                passWord = "exception";
            }
        } else {
            passWord = passwd;
        }
        return passWord;
    }

    public static String getCurrentTimeZone() {
        String timeZoneId =  Calendar.getInstance().getTimeZone().getID();
        if (null != timeZoneId && !TextUtils.isEmpty(timeZoneId)) {
            // do nothing
        } else {
            timeZoneId = "Asia/Seoul";
        }
        return timeZoneId;
    }


    public static String phoneNumberFormat(String number) {
        if (number == null) {
            return "";
        }
        if (number.length() == 8) {
            return number.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (number.length() == 12) {
            return number.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }
        return number.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

    public static String getPlatformCd(Context context) {
        String platformCd = "";
        if (isTabletDevice(context)) {
            platformCd = Constants.PLATFORMCD_TABLET;
        } else {
            platformCd = Constants.PLATFORMCD;
        }
        return platformCd;
    }

    public static boolean isTabletDevice(Context context) {
        if (Build.VERSION.SDK_INT >= 19){
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() &&
                    checkTabletDeviceWithUserAgent(context);
        }else{
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() ;
        }
    }

    private static boolean checkTabletDeviceWithScreenSize(Context context) {
        boolean device_large = ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkTabletDeviceWithProperties() {
        try {
            InputStream ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream();
            byte[] bts = new byte[1024];
            ism.read(bts);
            ism.close();

            boolean isTablet = new String(bts).toLowerCase().contains("tablet");
            return isTablet;
        }
        catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    private static boolean checkTabletDeviceWithUserAgent(Context context) {
        WebView webView = new WebView(context);
        String ua=webView.getSettings().getUserAgentString();
        webView = null;
        if(ua.contains("Mobile Safari")){
            return false;
        } else{
            return true;
        }
    }

    public static float getUserTextSize(Context context,Object textsize) {
        float value = Float.parseFloat(textsize.toString());
        SharedPreferenceManager pref = SharedPreferenceManager.getInstance(context);
        float userSetValue = pref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0);
        float size = DpiUtil.convertPixelsToDp(context, (Float.parseFloat(textsize.toString())  + DpiUtil.convertDpToPixel(context, userSetValue)));
        value = size;
        return value;
    }

    public static void changeTextSize(Context context, TextView changeTextView){
        changeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getUserTextSize(context, changeTextView.getTag()));
    }

    //textview size변경
    public static void textSizeSetting(Context context, TextView changeTextView){
        changeTextView.setTag(changeTextView.getTextSize());
        changeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, CommonUtils.getUserTextSize(context, changeTextView.getTag()));
    }

    //주로 dialog용 fragmentmanger 반환
    public static FragmentManager getFragmentManager(Context context) {
        try {
            final AppCompatActivity activity = (AppCompatActivity) context;
            //  return activity.getFragmentManager();
            return activity.getSupportFragmentManager();

        } catch (ClassCastException e) {
            Log.d("CommonUtils", "#### Can't get the fragment manager with this");
            return null;
        }
    }

    public static void callKolonTalk(Context context, String userId, String companyCode) {
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(companyCode)){
            Toast.makeText(context, context.getResources().getString(R.string.txt_kolontalk_no_user), Toast.LENGTH_SHORT).show();
            return;
        }

        // 앱 열기
        try {
            SharedPreferenceManager pref = SharedPreferenceManager.getInstance(context);
//                SSOUtils.putKolonProviderValue(context, "APPLINK", "Y");
//                String param = pref.getStringPreference(Constants.PREF_USER_ID) + "^" + pref.getStringPreference(Constants.PREF_USER_PW);
//                SSOUtils.putKolonProviderValue(context, "PARAM", param);
//                context.startActivity(launchIntent);
            /**스키마 작업이 완료되었을 때*/
            SSOUtils.putKolonProviderValue(context, "APPLINK", "Y");
            String param = pref.getStringPreference(Constants.PREF_USER_ID) + "^" + pref.getStringPreference(Constants.PREF_USER_PW);
            SSOUtils.putKolonProviderValue(context, "PARAM", param);
            String url = BuildConfig.kolontalk + "?id=" + userId + "&companyCode=" + companyCode; // 스키마 정보 "// + ?파라미터
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Log.d("CommonUtils", "#### ActivityNotFoundException err:" + e.getMessage());
            alertAppNotInstallDialog(context);
        }
    }

    private static void alertAppNotInstallDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.txt_kolontalk_not_installed));
        builder.setPositiveButton(context.getString(R.string.txt_login_alert_close),
                (dialog, which) -> {
                    callKolonApps(context);
                    dialog.dismiss();
                });
        builder.setNegativeButton(context.getString(R.string.txt_cancel),
                (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.show();
    }

    public static void callKolonApps(Context context) {
        try {
            String url = BuildConfig.kolonapps;  //"kolonapps://";
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            String url = context.getString(R.string.txt_kolonapps_web_url);
            try {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                String packageName = "com.android.chrome";
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                browserIntent.setPackage(packageName);
                browserIntent.setData(Uri.parse(url));
                context.startActivity(browserIntent);
            } catch ( ActivityNotFoundException e2) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                browserIntent.setData(Uri.parse(url));
                context.startActivity(browserIntent);
            }
        }
    }

}
