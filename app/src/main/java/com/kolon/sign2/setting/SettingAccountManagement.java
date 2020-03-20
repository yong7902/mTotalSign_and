package com.kolon.sign2.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.view.AccountManageView;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;

import static com.kolon.sign2.utils.Constants.PREF_CHANGED_ACCOUNT_DATA;

/**
 * 계정관리
 * */
public class SettingAccountManagement extends AppCompatActivity implements View.OnClickListener{//}, ListEditInterface {

    private LinearLayout mTitleBack;
    private AccountManageView accView;
    private SharedPreferenceManager mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_account_management);
        mPref = SharedPreferenceManager.getInstance(this);
        initView();
    }

    private void initView() {
        mTitleBack = findViewById(R.id.ll_setting_title_back);
        mTitleBack.setOnClickListener(this);
        accView = findViewById(R.id.setting_account_view);
        setData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_setting_title_back:
                finish();
                break;
        }
    }

    private void setData() {

        accView.setInterface(new AccountManageView.accountInterface() {
            @Override
            public void clickAdd() {
            }

            @Override
            public void selectItem(String originId, Res_AP_IF_004_VO.result.multiuserList item) {
                String value = new Gson().toJson(item);
                mPref.setStringPreference(PREF_CHANGED_ACCOUNT_DATA, value);
            }
        });
        accView.setType(0);
    //    accView.setData();
    }

}
