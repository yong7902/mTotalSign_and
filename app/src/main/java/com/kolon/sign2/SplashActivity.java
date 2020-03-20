package com.kolon.sign2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.kolon.sign2.activity.LoginActivity;
import com.kolon.sign2.activity.NoticeActivity;
import com.kolon.sign2.network.NetworkConstants;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.network.ServerInterface;
import com.kolon.sign2.network.StringUTF8Request;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SSOUtils;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.vo.CommonResultVO;
import com.kolon.sign2.vo.LoginParamVO;
import com.kolon.sign2.vo.LoginResultVO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import kr.co.miaps.external.updateapp.UpdateAppManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sunho_kim on 2019-12-02.
 */
public class SplashActivity extends AppCompatActivity {
//    private CustProgressDialog mPd = null;
    private static final int MSG_UPDATE_CHECK_DONE = 500;
    private static final int MSG_UPDATE_CANCEL = 501;

    public static final int CODE_PERMISSIONS_REQUEST = 600;
    public static final String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /// 작업공지 확인시 실행여부
    private String mExecuteYn = null;

    private SharedPreferenceManager mPref;
    private Retrofit mRetrofit;

    private static final String TAG_LOG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash);
        mPref = SharedPreferenceManager.getInstance(getBaseContext());
        Constants.isLogin = false;
        checkPermission();


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

    private void checkPermission() {

        boolean isChecked = false;
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                isChecked = true;
            }
        }

        if (isChecked) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, CODE_PERMISSIONS_REQUEST);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestNotice();

                }
            }, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CODE_PERMISSIONS_REQUEST: {
                // If request is cancelled, isGranged is false.
                boolean isGranted = true;
                for (int index = 0; index < PERMISSIONS.length;  index++) {
                    if (grantResults.length > 0 && grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                    }
                }
                if (isGranted) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            requestNotice();
                        }
                    }, 1000);
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle(getString(R.string.txt_detail_decision_dialog_title));
                    builder.setMessage(getString(R.string.txt_permission_content));
                    builder.setPositiveButton(getString(R.string.txt_confirm),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    builder.show();
                }
                break;
            }
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkAutoLogin() {
        String userId = "";
        String password = "";
        boolean isIkenAppInstall;
        SSOUtils.is_App_Link = false;
        if (SSOUtils.findIKENApp(getBaseContext())) {
            isIkenAppInstall = true;
        } else {
            isIkenAppInstall = false;
        }
        if ("Y".equalsIgnoreCase(SSOUtils.getKolonProviderValue(getBaseContext(), "APPLINK", "N"))) {
            String param = SSOUtils.getKolonProviderValue(getBaseContext(), "PARAM", "");
            try {
                if(param.length() > 0 && param.contains("^")) {
                    String[] arr = param.split("\\^");
                    userId = arr[0].replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
                    password = arr[1].replace("&eq", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
                    SSOUtils.putKolonProviderValue(getBaseContext(), "APPLINK", "N");
                    SSOUtils.putKolonProviderValue(getBaseContext(), "PARAM", "");
                    SSOUtils.is_App_Link = true;
                    mPref.setStringPreference(Constants.PREF_USER_ID, userId);
                    mPref.setStringPreference(Constants.PREF_USER_PW, password);
                    doLogin(userId, password);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        } else if ("Y".equalsIgnoreCase(SSOUtils.getKolonProviderValue(getBaseContext(), "AUTOLOGIN", "N"))) {
            // IKEN app 자동로그인을 TRUE로 설정한 경우
            userId = SSOUtils.getKolonProviderValue(getBaseContext(), "ID", "").replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
            password = SSOUtils.getKolonProviderValue(getBaseContext(), "PW", "").replace("&eq;", "=").replace("&pi;", "|").replace("&sq;", "^").replace("&ps;", "%");
            mPref.setStringPreference(Constants.PREF_USER_ID, userId);
            mPref.setStringPreference(Constants.PREF_USER_PW, password);
            mPref.setBooleanPreference(Constants.PREF_SAVE_ID, true);
            mPref.setBooleanPreference(Constants.PREF_AUTO_LOGIN, true);
            doLogin(userId, password);
        } else if (mPref.getBooleanPreference(Constants.PREF_AUTO_LOGIN)) {
            if (!isIkenAppInstall) {
                userId = mPref.getStringPreference(Constants.PREF_USER_ID);
                password = mPref.getStringPreference(Constants.PREF_USER_PW);
                mPref.setStringPreference(Constants.PREF_USER_ID, userId);
                mPref.setStringPreference(Constants.PREF_USER_PW, password);
                mPref.setBooleanPreference(Constants.PREF_SAVE_ID, true);
                mPref.setBooleanPreference(Constants.PREF_AUTO_LOGIN, true);
                doLogin(userId, password);
            } else {
                startLoginActivity();
                return;
            }
        } else {
            startLoginActivity();
            return;
        }
    }

    private void doLogin(final String userId, final String passwd) {
        initNetwork(BuildConfig.LoginURL);
        showProgressDialog();

        LoginParamVO paramVO = new LoginParamVO();
        paramVO.setAccount(userId);
        paramVO.setPassword(passwd);
        ServerInterface service = mRetrofit.create(ServerInterface.class);
        final Call<LoginResultVO> repos = service.userLogin(BuildConfig.r_key, paramVO);
        repos.enqueue(new Callback<LoginResultVO>() {
            @Override
            public void onResponse(Call<LoginResultVO> call, Response<LoginResultVO> response) {
                closeDialog();
                try {
                    if (null != response.body().account && !TextUtils.isEmpty(response.body().account)) { // 로그인 성공
                        mPref.setStringPreference(Constants.PREF_USER_ID, userId);
                        mPref.setStringPreference(Constants.PREF_USER_IF_ID, userId);
                        mPref.setStringPreference(Constants.PREF_USER_PW, passwd);



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

                        Log.d("SplashActivity", "#### companyCd:"+companyCd+"  deptCd:"+deptCd+"  deptId:"+deptId);

                        mPref.setStringPreference(Constants.PREF_DEPT_ID, deptId);// company code _ depart code
                        mPref.setStringPreference(Constants.PREF_DEPT_CD, deptCd);
                        mPref.setStringPreference(Constants.PREF_COMPANY_CD, companyCd);
                        mPref.setStringPreference(Constants.PREF_USER_NAME, response.body().getName());

                        getUserAuthSearch(userId, passwd);

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


    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void alertValidationDialog(String message) {
        if (!SplashActivity.this.isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.tx_login_alert_title));
            builder.setMessage(message);
            builder.setPositiveButton(getString(R.string.txt_confirm),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startLoginActivity();
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }
    }

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            String[] data = (String[])msg.obj;//data[1]이 서버의 appVersion

            if(data != null) {
                if(msg.what == UpdateAppManager.MSG_RESULT_END) {

                    if(null != msg.obj) {
                        // 버전체크후 버전업데이트할게 없으면 이곳으로 들어온다.(정상적으로 성공해서 진행하는 경우)
                        Log.d(TAG_LOG, "no version update: "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]) ;

                        Message message = Message.obtain();
                        message.what = MSG_UPDATE_CHECK_DONE;
                        handler.sendMessage(message);
                    }
                    else {
                        // 버전관리시 예외사항이 발생할 경우 이곳으로 들어온다.
                        // 버전업데이트 메시지창에서 취소를 눌렀을 경우 이곳으로 들어온다.(강제 업데이트 제외)

//                     Log.d(TAG_LOG, "cancel version update: "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]) ;

                        Message message = Message.obtain();
                        message.what = MSG_UPDATE_CHECK_DONE;
                        handler.sendMessage(message);

                    }
                } else if(msg.what == UpdateAppManager.MSG_RESULT_EXIT) {
                    // 종료해야 하는 상황인 경우(강제업데이트 메시지에서 취소)
                    Log.d(TAG_LOG, "exit version update: "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]) ;
                    finish();
                    return ;
                } else if(msg.what == UpdateAppManager.MSG_RESULT_INSTALL) {

                    // 설치화면으로 이동한 경우
                    Log.d(TAG_LOG, "install version update: "+data[0]+", "+data[1]+", "+data[2]+", "+data[3]) ;
                }
            } else {
                // MiAPS 업데이트 체크 실패한 경우.
                Log.e(TAG_LOG, "msg.obj is null!!!!!");

                Message message = Message.obtain();
                message.what = MSG_UPDATE_CHECK_DONE;
                handler.sendMessage(message);

            }
        }
    };

    private void checkAppUpdate() {

        // 안드로이드 폰 : 2000012 / 테블릿 : 2000022
        UpdateAppManager manager = new UpdateAppManager(this, getString(R.string.txt_svc_app_version_check), "2000012" , "",updateHandler);

        manager.setMsg(	getResources().getString(R.string.txt_chk_app_update_normal),
                getResources().getString(R.string.txt_chk_app_update_force),
                getResources().getString(R.string.txt_confirm),
                getResources().getString(R.string.txt_cancel),
                getResources().getString(R.string.txt_chk_app_update_error),
                getResources().getString(R.string.txt_chk_app_update_downloading));

        manager.startUpdateApp();
    }


    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch(msg.what) {

                case MSG_UPDATE_CHECK_DONE:
                    //Check Permission
                    checkAutoLogin();
                    break;
                case MSG_UPDATE_CANCEL :
                    Toast.makeText(getApplicationContext(), R.string.txt_update_guide_msg, Toast.LENGTH_LONG).show();
                    finish();
                    break;

            }
        }
    };


    /**
     * 공지사항을 확인한다.
     */
    private void requestNotice() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringUTF8Request stringRequest = new StringUTF8Request(Request.Method.POST, getString(R.string.txt_svc_app_notice_check),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG_LOG, response);

                        /// start
                        HashMap<String, String> responseMap =  parserNotice(response);
                        String resultCode = responseMap.get("errCode");
                        if (TextUtils. equals(resultCode, "S")) {

                            String contents = responseMap.get("contents");
                            mExecuteYn = responseMap.get("execute_yn");

                            if (!TextUtils.isEmpty(contents)) {
                                // 공지사항을 보여준다.
                                Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                                intent.putExtra("content", contents);
                                startActivityForResult(intent, NetworkConstants.REQUEST_CODE_NOTICE_POPUP);
                            }
                            else {
                                checkAppUpdate();
                            }

                        }
                        else {
                            checkAppUpdate();
                        }
                        /// end
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkAppUpdate();
            }
        }){
            @Override
            public Map<String, String> getParams(){

                /**
                 *<파라메타>
                 •	cmd - null이 아니고 빈 값("")이 아니어야 함 (select)
                 •	uid - 사용자 ID
                 •	deviceid - 장치 ID
                 •	time - 실행시각 (yyyyMMddHHmmss)
                 •	autocommit - false
                 •	target - 업무 ID. 어드민센터의 업무구분 메뉴 (miaps_target_url.url_id)
                 •	classname - 실행할 클래스 이름. 전체 패키지를 포함하여 입력
                 •	methodname - 실행할 메소드 이름. 클래스에 포함된 메소드임
                 •	커넥터 파라메타 - 커넥터 메소드에 전달될 파라메타
                 */

                Map<String, String> params = new HashMap<String, String>();
                params.put("cmd", "select");
//		params.put("uid", "");
//		params.put("deviceid", "");
                params.put("time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
                params.put("autocommit", "false");
                params.put("classname", "com.mink.connectors.kolon.admin.connector.ConnectorAdminService");
                params.put("methodname", "noticeData");
                params.put("target", "Board-noticeData");
                params.put("packageNm", getApplicationContext().getPackageName());
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /**
     * 씽크엠 파서(결과문자열은 테이블 형식, 레코드 구분:0x0C, 필드 구분:0x08)
     * @param string Miaps서버로 부터 전달받은 본문내용
     * @return 파싱된 데이터 값
     */
    private HashMap<String, String> parserNotice(String string) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        ArrayList<String> keyArray = new ArrayList<String>();

        if(TextUtils.isEmpty(string)) {
            return resultMap;
        }

        String[] recodeArray = string.split("\f");

        for(String recode : recodeArray){
            //DebugLog.out(CLASSNAME, "data = " + recode);
            // {{{+ 는 skip
            if (recode.startsWith("{{{+")) {
                continue;
            }

            String[] fieldArray = recode.split("\b");
            int fieldCount = fieldArray.length;

            if (keyArray.isEmpty()) {
                keyArray = new ArrayList<String>(Arrays.asList(fieldArray));
                keyArray.removeAll(Arrays.asList(null,""));
            }
            else {

                if (keyArray.size() == fieldCount) {
                    for (int i=0; i < fieldCount; i++) {
                        //결과 Dictionary에 저장
                        resultMap.put(keyArray.get(i), fieldArray[i]);
                    }
                }
                // Key Array 초기화
                keyArray = new ArrayList<String>();
            }
        }

        return resultMap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case NetworkConstants.REQUEST_CODE_NOTICE_POPUP: {
                // 실행여부에 따라 앱을 종료 or 실행한다.
                if(TextUtils.equals(mExecuteYn, "N")) {
                    // 앱 종료
                    finish();
                }else{
                    checkAppUpdate();
                }
                break;
            }
            default:
                break;
        }
    }

    private void showProgressDialog() {
//        if (mPd != null) {
//            mPd.dismiss();
//        }
//        if (!SplashActivity.this.isFinishing()) {
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
