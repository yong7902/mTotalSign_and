package com.kolon.sign2.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AddAccountDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkConstants;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.network.ServerInterface;
import com.kolon.sign2.setting.adapter.AccountSwipeAdapter;
import com.kolon.sign2.setting.adapter.ListEditInterface;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.LoginParamVO;
import com.kolon.sign2.vo.LoginResultVO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 계정 관리 뷰
 */
public class AccountManageView extends LinearLayout {
    private String TAG = "AccountManageView";
    private ProgressBar account_progress_bar;
    private AccountSwipeAdapter mAccountListSwipeAdapter;
    private RecyclerView rv;
    private Context mContext;
    private int type = 0;
    private accountInterface mInterface;

    public interface accountInterface {

        void clickAdd();

        void selectItem(String originId, Res_AP_IF_004_VO.result.multiuserList item);
    }

    public void setInterface(accountInterface mInterface) {
        this.mInterface = mInterface;
    }

    public AccountManageView(Context context) {
        super(context);
        initView(context);
    }

    public AccountManageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AccountManageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setType(int type) {
        this.type = type;
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_account_management, this, false);

        account_progress_bar = (ProgressBar) v.findViewById(R.id.account_progress_bar);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv = (RecyclerView) v.findViewById(R.id.lv_config_account);
        rv.setLayoutManager(linearLayoutManager);

        setData();

        addView(v);
    }

    private void setData(){
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(mContext);
        String userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        HashMap hm = new HashMap();
        hm.put("userId", mPref.getStringPreference(Constants.PREF_USER_ID));//유저조회시 주아이디 사용
        NetworkPresenter presenter = new NetworkPresenter();
        account_progress_bar.setVisibility(View.VISIBLE);
        presenter.getMultiUserList(hm, new NetworkPresenter.getMultiUserResult() {
            @Override
            public void onResponse(Res_AP_IF_004_VO result) {
                account_progress_bar.setVisibility(View.GONE);
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            //활성화된 계정표시 - 유저아이디와 계정리스트상 아이디가 같은것을 체크표시
                            ArrayList<Res_AP_IF_004_VO.result.multiuserList> data = result.getResult().getMultiuserList();

                            if(data != null){
                                for (int i = 0; i < data.size(); i++) {
                                    if (userId.equals(data.get(i).getSubUserId())) {
                                        data.get(i).isChecked = true;
                                        break;
                                    }
                                }
                            }

                            mAccountListSwipeAdapter = new AccountSwipeAdapter(mContext, data, new ListEditInterface() {
                                @Override
                                public void refreshScreen() {
                                }

                                @Override
                                public void deleteList(int position) {
                                    deleteAccount(data, position);
                                }

                                @Override
                                public void defaultList() {
                                }

                                @Override
                                public void selectPosition(int position) {
                                    String originId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);//변경전 아이디
                                    //변경된 정보를 저장
                                    String deptCd = data.get(position).getDeptCode();
                                //    String userId = data.get(position).getSubUserId();//변경된 userid
                                    String companyCd = data.get(position).getCompanyCd();
                                    String deptId = companyCd + "_" + deptCd;

                                    mPref.setStringPreference(Constants.PREF_USER_IF_ID, data.get(position).getSubUserId());//변경된 userid
                                    mPref.setStringPreference(Constants.PREF_DEPT_CD, deptCd);
                                    mPref.setStringPreference(Constants.PREF_DEPT_ID, deptId);
                                    mPref.setStringPreference(Constants.PREF_COMPANY_CD, companyCd);
                                    mPref.setStringPreference(Constants.PREF_USER_NAME, data.get(position).getUserName());

                                    String loginIfStr = new Gson().toJson(data);
                                    mPref.setStringPreference(Constants.PREF_LOGIN_IF_INFO, loginIfStr);

                                    if (mInterface != null)
                                        mInterface.selectItem(originId, data.get(position));
                                }

                                @Override
                                public void addAccount() {
                                    if (mInterface != null)
                                        mInterface.clickAdd();

                                    if (type == 0) {
                                        //설정에서는 다이얼로그
                                        AddAccountDialog dialog = new AddAccountDialog();
                                        dialog.show(getFragmentManager(mContext), "");
                                        dialog.setInterface(new AddAccountDialog.AddAccountInterface() {
                                            @Override
                                            public void addAccount(String id, String pass) {
                                                dialog.dismiss();
                                                //받은 계정으로 로그인을 시도해서 정상계정인지 확인
                                                doLogin(id, pass);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void errMessage(String msg) {
                                    alertValidationDialog(msg);
                                }
                            });
                            rv.setAdapter(mAccountListSwipeAdapter);
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

    //계정 추가
    private void sendAddAccount(String inputId) {
//        account_progress_bar.setVisibility(View.VISIBLE);

        NetworkPresenter presenter = new NetworkPresenter();
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(mContext);
        String userId = mPref.getStringPreference(Constants.PREF_USER_ID);
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("subUserId", inputId);

        presenter.getMultiUserInsert(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {

                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            //계정 추가 성공 -> 전체 계정을 다시 가져온다
                            setData();
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
                account_progress_bar.setVisibility(View.GONE);
                alertValidationDialog(errMsg);
            }
        });
    }

    private void deleteAccount(ArrayList<Res_AP_IF_004_VO.result.multiuserList> data, int position) {
        TextDialog dialog = TextDialog.newInstance("", mContext.getResources().getString(R.string.txt_account_delete_alert),
                mContext.getResources().getString(R.string.txt_alert_cancel),
                mContext.getResources().getString(R.string.txt_alert_confirm));
        dialog.show(getFragmentManager(mContext));
        dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_left:
                        dialog.dismiss();
                        break;
                    case R.id.btn_right:
                        //send delete account
                        dialog.dismiss();
                        sendServerDeleteAccount(data, position);
                        break;
                }
            }
        });
    }

    private void sendServerDeleteAccount(ArrayList<Res_AP_IF_004_VO.result.multiuserList> data, int position) {
        String deleteId = data.get(position).getUserId();
        account_progress_bar.setVisibility(View.VISIBLE);
        NetworkPresenter presenter = new NetworkPresenter();
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(mContext);
        String userId = mPref.getStringPreference(Constants.PREF_USER_ID);

        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("subUserId", deleteId);

        presenter.getMultiUserDelete(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                account_progress_bar.setVisibility(View.GONE);
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            //계정 재로딩
                            data.remove(position);
                            mAccountListSwipeAdapter.changeList(data);

                            //멀티계정 재 저장

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

    private void doLogin(final String inputId, final String passwd) {
        Log.d(TAG, "#### dologin inputid:" + inputId + "   ");
        account_progress_bar.setVisibility(View.VISIBLE);
        LoginParamVO paramVO = new LoginParamVO();
        paramVO.setAccount(inputId);
        paramVO.setPassword(passwd);
        ServerInterface service = initNetwork(BuildConfig.LoginURL).create(ServerInterface.class);
        final Call<LoginResultVO> repos = service.userLogin(BuildConfig.r_key, paramVO);
        repos.enqueue(new Callback<LoginResultVO>() {

            @Override
            public void onResponse(Call<LoginResultVO> call, Response<LoginResultVO> response) {
                String errMsg = "";
                Log.d(TAG, "#### dologin result:" + new Gson().toJson(response.body()));
                try {
                    if (null != response.body().account && !TextUtils.isEmpty(response.body().account)) { // 로그인 성공
                        //로그인 성공 = 정상 계정
                        //계정 추가
                        sendAddAccount(inputId);
                        return;
                    } else {
                        errMsg = mContext.getResources().getString(R.string.txt_login_alert_fail);
                    }
                } catch (NullPointerException e) {
                    errMsg = mContext.getResources().getString(R.string.txt_login_alert_fail);
                    e.printStackTrace();
                }
                account_progress_bar.setVisibility(View.GONE);
                alertValidationDialog(errMsg);
            }

            @Override
            public void onFailure(Call<LoginResultVO> call, Throwable t) {
                account_progress_bar.setVisibility(View.GONE);
                alertValidationDialog(mContext.getResources().getString(R.string.txt_network_error));
            }
        });
    }

    private Retrofit initNetwork(String endPoint) {
        return new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public FragmentManager getFragmentManager(Context context) {
        try {
            final AppCompatActivity activity = (AppCompatActivity) context;

            //  return activity.getFragmentManager();

            return activity.getSupportFragmentManager();

        } catch (ClassCastException e) {
            Log.d(TAG, "#### Can't get the fragment manager with this");
            return null;
        }
    }

    private void alertValidationDialog(String message) {
        TextDialog dialog = TextDialog.newInstance("", message, getResources().getString(R.string.txt_alert_confirm));
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

}
