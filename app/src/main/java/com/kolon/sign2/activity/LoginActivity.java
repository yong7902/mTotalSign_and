package com.kolon.sign2.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.network.NetworkConstants;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.network.ServerInterface;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SSOUtils;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.CommonResultVO;
import com.kolon.sign2.vo.LoginParamVO;
import com.kolon.sign2.vo.LoginResultVO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * Created by sunho_kim on 2019-12-02.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
//    private CustProgressDialog mPd = null;
    private EditText mIDEditText, mPassEditText;
    private LinearLayout mIdEditTextDelete, mPwEditTextDelete;
    private CheckBox mIDSaveCheckBox, mAutoLoginCheckBox;
    private Button mLoginButton;
    private SharedPreferenceManager mPref;
    private Retrofit mRetrofit;
    private boolean mIsLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_login);
        mPref = SharedPreferenceManager.getInstance(getBaseContext());
        if(null != getIntent() && null != getIntent().getExtras()) {
            mIsLogout = getIntent().getBooleanExtra(Constants.APP_LOGOUT_EXTRA, false);
        } else {
            mIsLogout = false;
        }

        initView();
    }

    private void initView() {
        mIDEditText = (EditText)findViewById(R.id.et_login_id);
        mPassEditText = (EditText)findViewById(R.id.et_login_pwd);
        mIDSaveCheckBox = (CheckBox)findViewById(R.id.chk_id_save);
        mAutoLoginCheckBox = (CheckBox)findViewById(R.id.chk_auto_login);
        mLoginButton = (Button)findViewById(R.id.btn_login);
        mIdEditTextDelete = findViewById(R.id.iv_id_delete);
        mPwEditTextDelete = findViewById(R.id.iv_password_delete);

        mLoginButton.setOnClickListener(this);
        mIDSaveCheckBox.setOnCheckedChangeListener(this);
        mAutoLoginCheckBox.setOnCheckedChangeListener(this);
        mIdEditTextDelete.setOnClickListener(this);
        mPwEditTextDelete.setOnClickListener(this);

        mIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mIdEditTextDelete.setVisibility(View.VISIBLE);
                    if (mPassEditText.getText().length() > 0) {
                        mLoginButton.setClickable(true);
                        mLoginButton.setEnabled(true);
                    } else {
                        mLoginButton.setClickable(false);
                        mLoginButton.setEnabled(false);
                    }
                } else {
                    mIdEditTextDelete.setVisibility(View.GONE);
                    mLoginButton.setClickable(false);
                    mLoginButton.setEnabled(false);
                }
            }
        });

        mPassEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mPwEditTextDelete.setVisibility(View.VISIBLE);
                    if (mIDEditText.getText().length() > 0) {
                        mLoginButton.setClickable(true);
                        mLoginButton.setEnabled(true);
                    } else {
                        mLoginButton.setClickable(false);
                        mLoginButton.setEnabled(false);
                    }
                } else {
                    mPwEditTextDelete.setVisibility(View.GONE);
                    mLoginButton.setClickable(false);
                    mLoginButton.setEnabled(false);
                }
            }
        });

        if (mIsLogout) {
            mIDSaveCheckBox.setChecked(false);
            mAutoLoginCheckBox.setChecked(false);
            mIDEditText.setText("");
            mPassEditText.setText("");
            setPreference();
        } else {
            initLoginData();
        }
    }

    private void alertValidationDialog(String message) {
        if (!LoginActivity.this.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton(getString(R.string.txt_login_alert_close),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }

    /**
     * 통신 초기화
     */
    private void initNetwork(String endPoint) {
        mRetrofit = new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void initLoginData() {
        String userId = "";
        String password = "";
        boolean isIkenAppInstall;
        boolean is_Auto_Login;
        boolean is_Save_Id;
        SSOUtils.is_App_Link = false;
        if (SSOUtils.findIKENApp(getBaseContext())) {
            isIkenAppInstall = true;
        } else {
            isIkenAppInstall = false;
        }

        is_Save_Id = mPref.getBooleanPreference(Constants.PREF_SAVE_ID);
        is_Auto_Login = mPref.getBooleanPreference(Constants.PREF_AUTO_LOGIN);
        if ("Y".equalsIgnoreCase(SSOUtils.getKolonProviderValue(getApplicationContext(), "AUTOLOGIN", "N"))) {
            // IKEN app 자동로그인을 TRUE로 설정한 경우
            userId = SSOUtils.getKolonProviderValue(getApplicationContext(), "ID", "").replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
            password = SSOUtils.getKolonProviderValue(getApplicationContext(), "PW", "").replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
            mIDEditText.setText(userId);
            mPassEditText.setText(password);
            mIDSaveCheckBox.setChecked(true);
            mAutoLoginCheckBox.setChecked(true);
        } else if ("Y".equalsIgnoreCase(SSOUtils.getKolonProviderValue(getApplicationContext(), "SAVEID", "N"))) {
            // IKEN app 아이디저장을 TRUE로 설정한 경우
            userId = SSOUtils.getKolonProviderValue(getApplicationContext(), "ID", "").replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
            password = "";
            mIDEditText.setText(userId);
            mPassEditText.setText(password);
            mIDSaveCheckBox.setChecked(true);
            mAutoLoginCheckBox.setChecked(false);
        } else if (is_Auto_Login && !isIkenAppInstall) {
            userId = mPref.getStringPreference(Constants.PREF_USER_ID);
            password = mPref.getStringPreference(Constants.PREF_USER_PW);
            mIDEditText.setText(userId);
            mPassEditText.setText(password);
            mIDSaveCheckBox.setChecked(true);
            mAutoLoginCheckBox.setChecked(true);
        }else if (is_Save_Id && !is_Auto_Login && !isIkenAppInstall) {
            userId = mPref.getStringPreference(Constants.PREF_USER_ID);
            password = "";
            mIDEditText.setText(userId);
            mPassEditText.setText(password);
            mIDSaveCheckBox.setChecked(true);
            mAutoLoginCheckBox.setChecked(false);
        } else {
            userId = "";
            password = "";
            mIDEditText.setText(userId);
            mPassEditText.setText(password);
            mIDSaveCheckBox.setChecked(false);
            mAutoLoginCheckBox.setChecked(false);
        }
    }

    private void doLogin(final String userId, final String passwd) {
        initNetwork(BuildConfig.LoginURL);
        if (TextUtils.isEmpty(userId) || userId.length() <= 0 || TextUtils.isEmpty(passwd) || passwd.length() <= 0) {
            alertValidationDialog(getString(R.string.txt_login_alert_validation_fail));
            return;
        }
        showProgressDialog();
        LoginParamVO paramVO = new LoginParamVO();
        paramVO.setAccount(userId);
        paramVO.setPassword(passwd);
        ServerInterface service = mRetrofit.create(ServerInterface.class);
        final Call<LoginResultVO> repos = service.userLogin(BuildConfig.r_key, /*CommonUtils.getTimeStamp(),*/ paramVO);
        repos.enqueue(new Callback<LoginResultVO>() {
            @Override
            public void onResponse(Call<LoginResultVO> call, Response<LoginResultVO> response) {
                closeDialog();
                try {
                    if (null != response.body().account && !TextUtils.isEmpty(response.body().account)) { // 로그인 성공
                        mPref.setStringPreference(Constants.PREF_USER_ID, mIDEditText.getText().toString().trim());
                        mPref.setStringPreference(Constants.PREF_USER_PW, mPassEditText.getText().toString().trim());

                        //로그인 정보 저장
                        String loginStr = new Gson().toJson(response.body());
                        mPref.setStringPreference(Constants.PREF_LOGIN_INFO, loginStr);

                        //로그인 정보 저장 - 계정 변경시 사용용도
                        Res_AP_IF_004_VO.result.multiuserList userdata = new Res_AP_IF_004_VO().new result().new multiuserList();
                        userdata.setUserId(userId);//	로그인아이디(주계정)
                        userdata.setSubUserId(userId);//	멀티계정아이디
                        userdata.setUpdateDt("");//	등록일자
                        userdata.setCompanyCd(response.body().getCompanyCode());//	회사코드
                        userdata.setUserName(response.body().getName());//사용자명
                        userdata.setDeptCode(response.body().getDepartmentCode());//부서코드
                        userdata.setDeptName(response.body().getDepartmentName());//	부서명
                        userdata.setTitleName(response.body().getJobTitle());//	직책
                        userdata.setCompanyName(response.body().getCompanyName());//회사
                        userdata.setRoleName(response.body().getRoleName());//직급
                        String loginIfStr = new Gson().toJson(userdata);
                        mPref.setStringPreference(Constants.PREF_LOGIN_IF_INFO, loginIfStr);

                        //회사코드, 부서코드, 부서아이디(회사코드_부서코드) 저장 (전자결재 조회용)
                        String companyCd = response.body().getCompanyCode();
                        String deptCd = response.body().getDepartmentCode();
                        String deptId = companyCd +"_"+deptCd;

                        Log.d("LoginActivity", "#### companyCd:"+companyCd+"  deptCd:"+deptCd+"  deptId:"+deptId);

                        mPref.setStringPreference(Constants.PREF_DEPT_ID, deptId);// company code _ depart code
                        mPref.setStringPreference(Constants.PREF_DEPT_CD, deptCd);
                        mPref.setStringPreference(Constants.PREF_COMPANY_CD, companyCd);

                        setPreference();
                        getUserAuthSearch(userId, passwd);//사용자권한을 가져옴(MDM, ICON)

                   //     registerDevice(userId, passwd);
                    } else {
                        alertValidationDialog(getString(R.string.txt_login_alert_fail));
                    }
                } catch (NullPointerException e) {
                    alertValidationDialog(getString(R.string.txt_login_alert_fail));
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResultVO> call, Throwable t) {
                closeDialog();
                alertValidationDialog(getString(R.string.txt_network_error));
            }
        });
    }

    private void getUserAuthSearch(String userId, String passwd){
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        presenter.getUserAuthSearch(hm, new NetworkPresenter.getUserAuthSearchResult() {
            @Override
            public void onResponse(Res_AP_IF_101_VO result) {
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            //로그인 정보 저장
                            String info = new Gson().toJson(result.getResult());
                            mPref.setStringPreference(Constants.PREF_USER_AUTH_INFO, info);

                            registerDevice(userId);
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
    }

    private void registerDevice(String userId){
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        hm.put("dvcId", mPref.getStringPreference(Constants.PREF_DEVICE_ID));
        hm.put("tokenKey", mPref.getStringPreference(Constants.PREF_TOKEN_KEY));
        hm.put("dvcOs", "1");
        hm.put("dvcVersion", CommonUtils.getOSVersion());
        hm.put("dvcModelName", CommonUtils.getModel());
        hm.put("appVersion", CommonUtils.getAppVersion(getBaseContext()));
        hm.put("userId", userId);
        String timeStamp = CommonUtils.getTimeStamp();
        try {
            String auth = CipherUtils.encrypt(timeStamp, BuildConfig.r_key);
            presenter.registerDeviceInfo(auth, timeStamp, hm, new NetworkPresenter.getRegisterDeviceInfoListener() {
                @Override
                public void onResponse(CommonResultVO result) {
                    String errMsg = "";
                    if (result != null) {
                        if ("200".equals(result.getStatus().getStatusCd())) {
                            if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                                startMainActivity();
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
                    startMainActivity();
                }
            });
        } catch (Exception e) {
            startMainActivity();
        }
    }

    private void setPreference() {
        if (mAutoLoginCheckBox.isChecked()) {
            mPref.setStringPreference(Constants.PREF_USER_ID, mIDEditText.getText().toString().trim());
            mPref.setStringPreference(Constants.PREF_USER_IF_ID, mIDEditText.getText().toString().trim());
            mPref.setStringPreference(Constants.PREF_USER_PW, mPassEditText.getText().toString().trim());
            mPref.setBooleanPreference(Constants.PREF_AUTO_LOGIN, true);
            mPref.setBooleanPreference(Constants.PREF_SAVE_ID, true);
            SSOUtils.putKolonProviderValue(getBaseContext(),"AUTOLOGIN", "Y");
            SSOUtils.putKolonProviderValue(getBaseContext(),"SAVEID", "Y");
            SSOUtils.putKolonProviderValue(getBaseContext(),"ID", mIDEditText.getText().toString().trim().replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%"));
            SSOUtils.putKolonProviderValue(getBaseContext(),"PW", mPassEditText.getText().toString().trim().replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%"));
        } else {
            mPref.setBooleanPreference(Constants.PREF_AUTO_LOGIN, false);
            mPref.setStringPreference(Constants.PREF_USER_ID, mIDEditText.getText().toString().trim());
            mPref.setStringPreference(Constants.PREF_USER_IF_ID, mIDEditText.getText().toString().trim());
            if (mIDSaveCheckBox.isChecked()) {
                mPref.setBooleanPreference(Constants.PREF_SAVE_ID, true);
                SSOUtils.putKolonProviderValue(getBaseContext(),"SAVEID", "Y");
                SSOUtils.putKolonProviderValue(getBaseContext(),"AUTOLOGIN", "N");
                SSOUtils.putKolonProviderValue(getBaseContext(),"ID", mIDEditText.getText().toString().trim().replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%"));
                SSOUtils.putKolonProviderValue(getBaseContext(),"PW", "");
            } else {
                mPref.setBooleanPreference(Constants.PREF_SAVE_ID, false);
                SSOUtils.putKolonProviderValue(getBaseContext(),"SAVEID", "N");
                SSOUtils.putKolonProviderValue(getBaseContext(),"AUTOLOGIN", "N");
                SSOUtils.putKolonProviderValue(getBaseContext(),"ID", "");
                SSOUtils.putKolonProviderValue(getBaseContext(),"PW", "");
            }
        }

    }

    private void startMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chk_id_save : {
                if (!isChecked) {
                    if (mAutoLoginCheckBox.isChecked()) {
                        mAutoLoginCheckBox.setChecked(false);
                    }
                }
                break;
            }
            case R.id.chk_auto_login : {
                if (isChecked) {
                    mIDSaveCheckBox.setChecked(true);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                if (null != mIDEditText && null != mPassEditText) {
                    doLogin(mIDEditText.getText().toString().trim(), mPassEditText.getText().toString().trim());
                }
                break;
            }
            case R.id.iv_id_delete: {
                if (null != mIDEditText) {
                    mIDEditText.setText("");
                }
                break;
            }
            case R.id.iv_password_delete: {
                if (null != mPassEditText) {
                    mPassEditText.setText("");
                }
                break;
            }
        }
    }

    private void showProgressDialog() {
//        if (mPd != null) {
//            mPd.dismiss();
//        }
//        if (!LoginActivity.this.isFinishing()) {
//            mPd = CustProgressDialog.show(this, null, null);
//        }
    }

    private void closeDialog() {
//        if (mPd != null) {
//            mPd.dismiss();
//            mPd = null;
//        }
    }

}
