package com.kolon.sign2.setting;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.R;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.setting.adapter.DragHelper;
import com.kolon.sign2.setting.adapter.MenuChangeListAdapter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.LoginParamVO;
import com.kolon.sign2.vo.Req_AP_IF_108_VO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * 메뉴순서
 */
public class SettingMenuChange extends Activity implements View.OnClickListener {
    private LinearLayout mTitleBack;

    private RecyclerView mMenuChangeRecyclerView;
    private MenuChangeListAdapter mMenuChangeListAdapter;
    private ArrayList<LoginParamVO> mMenuArray, oriArray;
    private Res_AP_IF_101_VO.result userAuthInfo;//mdm , icon
    private String userId;
    private SharedPreferenceManager mPref;
    private RelativeLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_menu_seq);

        mTitleBack = findViewById(R.id.ll_setting_title_back);
        mTitleBack.setOnClickListener(this);

        progress_bar = (RelativeLayout)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        mPref = SharedPreferenceManager.getInstance(getBaseContext());
        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());

        initView();

        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
    }

    private void initView() {
        mMenuChangeRecyclerView = findViewById(R.id.rv_menu_change);
        mMenuChangeRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        tempData();
    }

    private void tempData() {
        mMenuArray = new ArrayList<>();
        oriArray = new ArrayList<>();
        for (int i = 0; i < userAuthInfo.getSysArray().size(); i++) {
            LoginParamVO paramVO = new LoginParamVO(userAuthInfo.getSysArray().get(i).getSysName(), "");
            paramVO.iconUrl = userAuthInfo.getSysArray().get(i).getSysIcon();
            paramVO.sysId = userAuthInfo.getSysArray().get(i).getSysId();
            mMenuArray.add(paramVO);
            oriArray.add(paramVO);
        }

        setRecyclerView();
    }

    private void setRecyclerView() {
        mMenuChangeListAdapter = new MenuChangeListAdapter(getBaseContext(), mMenuArray);
        DragHelper dragHelper = new DragHelper(mMenuChangeListAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(dragHelper);
        touchHelper.attachToRecyclerView(mMenuChangeRecyclerView);
        mMenuChangeListAdapter.setTouchHelper(touchHelper);
        mMenuChangeRecyclerView.setAdapter(mMenuChangeListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_setting_title_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {

        //비교
        boolean dataChange = false;
        for(int i=0;i<oriArray.size();i++){
            if(!oriArray.get(i).sysId.equals(mMenuArray.get(i).sysId)){
                dataChange = true;
                break;
            }
        }
        //원데이터와 다르면 전송
        if(dataChange){
            progress_bar.setVisibility(View.VISIBLE);
            ArrayList<Req_AP_IF_108_VO.menuOrder> menuOrder = new ArrayList<>();

            for(int i=0;i<mMenuArray.size();i++){
                Req_AP_IF_108_VO.menuOrder data = new Req_AP_IF_108_VO().new menuOrder();
                data.setOrderBy(String.valueOf(i+1));
                data.setSysId(mMenuArray.get(i).sysId);
                menuOrder.add(data);

                //바뀐데이터 순서대로 저장
                for(int j=0;j<userAuthInfo.getSysArray().size();j++){
                    if(mMenuArray.get(i).sysId.equals(userAuthInfo.getSysArray().get(j).getSysId())){
                        Res_AP_IF_101_VO.result.sysArray temp = userAuthInfo.getSysArray().get(i);
                        userAuthInfo.getSysArray().set(i, userAuthInfo.getSysArray().get(j));
                        userAuthInfo.getSysArray().set(j, temp);
                        break;
                    }
                }
            }



            Req_AP_IF_108_VO req = new Req_AP_IF_108_VO();
            req.setUserId(userId);
            req.setMenuOrder(menuOrder);
            NetworkPresenter presenter = new NetworkPresenter();
            presenter.getUpdateSysOrderMgt(req, new NetworkPresenter.getCommonListener() {
                @Override
                public void onResponse(Res_AP_Empty_VO result) {
                    progress_bar.setVisibility(View.GONE);
                    if (result != null) {
                        if ("200".equals(result.getStatus().getStatusCd())) {
                            if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                                String info = new Gson().toJson(userAuthInfo);
                                mPref.setStringPreference(Constants.PREF_USER_AUTH_INFO, info);
                                mPref.setStringPreference(Constants.PREF_CHANGED_SYS_ORDER, "Y");
                            }
                        }
                    }
                    finish();
                }
            });
        }else{
            finish();
        }
    }
}
