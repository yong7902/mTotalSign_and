package com.kolon.sign2.setting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.SwitchCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.SplashActivity;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.setting.adapter.PushReceiveListAdapter;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.AlarmListResultVO;
import com.kolon.sign2.vo.CommonResultVO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 알림설정
 * */
public class SettingPushAlarm extends Activity implements View.OnClickListener {
    private LinearLayout mTitleBack;
    private ListView mAppPushListView;
    private ArrayList<AlarmListResultVO.List> mPushReceiveArray;
    private PushReceiveListAdapter mPushReceiveListAdapter;
    private SharedPreferenceManager mPref;
    private Res_AP_IF_101_VO.result userAuthInfo;//mdm , icon url
    private RelativeLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_push);
        mPref = SharedPreferenceManager.getInstance(getBaseContext());

        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());

        //read server data
        initView();
    }

    private void initView() {
        mTitleBack = findViewById(R.id.ll_setting_title_back);
        mTitleBack.setOnClickListener(this);
        mAppPushListView = (ListView)findViewById(R.id.lv_setting_app_list);
        mAppPushListView.setEmptyView(findViewById(android.R.id.empty));
        progress_bar = (RelativeLayout) findViewById(R.id.progress_bar);
        getPushSettingList();
    }


    private void setAppListView() {
        if (null != mPushReceiveArray && mPushReceiveArray.size() > 0) {
            mPushReceiveListAdapter = new PushReceiveListAdapter(this, mPushReceiveArray, mPref);
            mAppPushListView.setAdapter(mPushReceiveListAdapter);
        }
    }

    private void getPushSettingList() {
        showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();

        String timeStamp = CommonUtils.getTimeStamp();
        try {
            String auth = CipherUtils.encrypt(timeStamp, BuildConfig.r_key);
            presenter.getPushSettingList(auth, timeStamp, mPref.getStringPreference(Constants.PREF_DEVICE_ID), new NetworkPresenter.getPushSettingListener() {
                @Override
                public void onResponse(AlarmListResultVO result) {
                    hideProgressBar();
                    String errMsg = "";
                    if (result != null) {
                        if ("200".equals(result.getStatus().getStatusCd())) {
                            if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                                //Todo List Setting해서 Adapter에 셋 해야 함. ICon정보는 따로 셋해줘야 함.
                                setAppListView();
                                return;
                            } else {
                                errMsg = result.getResult().getErrorMsg();
                            }
                        } else {
                            errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
                        }
                    } else {
                        errMsg = getResources().getString(R.string.txt_network_error);
                    }
                    alertValidationDialog(errMsg);
                }
            });
        } catch (Exception e) {
            hideProgressBar();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_setting_title_back:
                appFinish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        appFinish();
    }

    private void appFinish(){
        finish();
    }

    private void alertValidationDialog(String message) {
        if (!SettingPushAlarm.this.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton(getString(R.string.txt_confirm),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }
    public void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

}
