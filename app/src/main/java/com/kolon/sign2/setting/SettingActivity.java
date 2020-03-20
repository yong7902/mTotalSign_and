package com.kolon.sign2.setting;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kolon.sign2.R;
import com.kolon.sign2.activity.LoginActivity;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;

/**
 * Created by sunho_kim on 2019-12-02.
 */
public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mAlarmSetting, mLogOut, mAccountSetting, mTextSizeSetting, mMenuSetting;
    private TextView mLoginId, mAppVersion;
    private LinearLayout mSettingTitleBack;
    private SharedPreferenceManager mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_setting);
        mPref = SharedPreferenceManager.getInstance(getBaseContext());
        initView();
        loadData();
    }


    private void initView() {
        mAlarmSetting = findViewById(R.id.ll_setting_alarm);
        mAccountSetting = findViewById(R.id.ll_setting_account_manager);
        mTextSizeSetting = findViewById(R.id.ll_setting_textsize_manager);
        mMenuSetting = findViewById(R.id.ll_setting_menu_manager);

        mLogOut = findViewById(R.id.ll_setting_logout);
        mAppVersion = findViewById(R.id.tv_app_version);
        mSettingTitleBack = findViewById(R.id.ll_setting_title_back);

        mAlarmSetting.setOnClickListener(this);
        mAccountSetting.setOnClickListener(this);
        mTextSizeSetting.setOnClickListener(this);
        mMenuSetting.setOnClickListener(this);
        mSettingTitleBack.setOnClickListener(this);
        mLogOut.setOnClickListener(this);
        mAppVersion.setText(getAppVersion());
    }

    private void loadData() {
//        mLoginId.setText(mPref.getStringPreference(Constants.PREF_USER_IF_ID));
//        mAppVersion.setText(getAppVersion());
    }

    private String getAppVersion() {
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
        return  pi.versionName;
    }



    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_setting_title_back: {
                finish();
                break;
            }
            /**알림설정*/
            case R.id.ll_setting_alarm:
                intent = new Intent(this, SettingPushAlarm.class);
                startActivity(intent);
                break;
            /**계정관리*/
            case R.id.ll_setting_account_manager:
                intent = new Intent(this, SettingAccountManagement.class);
                startActivity(intent);
                break;
            /**글자크기*/
            case R.id.ll_setting_textsize_manager:
                intent = new Intent(this, SettingTextSizeManager.class);
                startActivity(intent);
                break;
            /**메뉴순서*/
            case R.id.ll_setting_menu_manager:
                intent = new Intent(this, SettingMenuChange.class);
                startActivity(intent);
                break;
            /**로그아웃*/
            case R.id.ll_setting_logout: {
                alertLogoutDialog();
                break;
            }
        }
    }


    private void alertLogoutDialog() {
        TextDialog logOutDialog = TextDialog.newInstance("", getString(R.string.txt_logout_alert_content), getString(R.string.txt_alert_cancel), getString(R.string.txt_alert_confirm));

        logOutDialog.setCancelable(false);
        logOutDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_left: {
                        logOutDialog.dismiss();
                        break;
                    }
                    case R.id.btn_right: {
                        logOutDialog.dismiss();
                        setPreference();
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(Constants.APP_LOGOUT_EXTRA, true);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
            }
        });
        logOutDialog.show(getSupportFragmentManager());

    }

    private void setPreference() {
//            mPref.setStringPreference(Constants.PREF_USER_ID, "");
//            mPref.setStringPreference(Constants.PREF_USER_PW, "");
//            mPref.setBooleanPreference(Constants.PREF_AUTO_LOGIN, false);
//            mPref.setBooleanPreference(Constants.PREF_SAVE_ID, false);
    }

    public interface logoutDialogListener {
        void onPositiveClicked();
    }

}
