package com.kolon.sign2.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.view.GravityCompat;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.LoginResultVO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 좌측 메뉴
 */

public class LeftSlideMenuView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private SharedPreferenceManager mPref;
    private LinearLayout select_box_list_layer, select_account_list_layer;
    private TextView tv_name, tv_job, tv_team;// tv_team, tv_team, tv_team, tv_team;
    private LinearLayout layout_main_menu;
    private LinearLayout btn_select_accout;
    private AccountManageView accView;
    private LoginResultVO loginInfo;

    private LeftMenuClickListener mInterface;

    public interface LeftMenuClickListener {
        void onTabClickLeftMenu(int resId, Res_AP_IF_102_VO.result.menuArray item);
    }

    public void setInterface(LeftMenuClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public LeftSlideMenuView(Context context) {
        super(context);
        initView(context);
    }

    public LeftSlideMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LeftSlideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public LeftSlideMenuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_left_slide_menu, this, false);

        ImageView btnSetting = (ImageView) v.findViewById(R.id.btn_left_menu_setting);
        btnSetting.setOnClickListener(this);

        select_box_list_layer = (LinearLayout) v.findViewById(R.id.select_box_list_layer);
        select_account_list_layer = (LinearLayout) v.findViewById(R.id.select_account_list_layer);


        ImageView btn_close = (ImageView) v.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(this);

        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_job = (TextView) v.findViewById(R.id.tv_job);
        tv_team = (TextView) v.findViewById(R.id.tv_team);

        LinearLayout btn_menu_home = (LinearLayout) v.findViewById(R.id.btn_menu_home);
        btn_menu_home.setOnClickListener(this);
        LinearLayout btn_menu_iken = (LinearLayout) v.findViewById(R.id.btn_menu_iken);
        btn_menu_iken.setOnClickListener(this);

        btn_select_accout = (LinearLayout) v.findViewById(R.id.btn_select_accout);
        btn_select_accout.setOnClickListener(this);

        layout_main_menu = (LinearLayout) v.findViewById(R.id.layout_main_menu);


        RelativeLayout left_slide_menu_top_layout = (RelativeLayout)v.findViewById(R.id.left_slide_menu_top_layout);
        left_slide_menu_top_layout.setOnClickListener(this);

        mPref = SharedPreferenceManager.getInstance(mContext);

        accView = (AccountManageView)v.findViewById(R.id.view_account_manager);
        accView.setInterface(new AccountManageView.accountInterface() {
            @Override
            public void clickAdd() {
                ((MainActivity)mContext).gotoAddAccount();
            }

            @Override
            public void selectItem(String originId, Res_AP_IF_004_VO.result.multiuserList item) {
              //  String userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);//현 사용아이디

                if (item.getSubUserId().equals(originId)) {
                    //유저아이디가 같을 경우 메뉴트리로 전환됨
                    showBoxLayer();
                } else {
                    //변경된 정보 표시
                    setInfo(item.getUserName(), item.getTitleName(), item.getDeptName(), item.getRoleName());
//                    tv_name.setText(item.getUserName());
//                    String job = item.getTitleName();
//
//                    if (!TextUtils.isEmpty(item.getRoleName())) {
//                        job = item.getRoleName() + "/" + item.getTitleName();
//                    }
//                    tv_job.setText(job);
//                    tv_team.setText(item.getDeptName());

                    ((MainActivity) mContext).changeAccount(item);
                }
            }

            @Override
            public void modData() {
            }
        });
        accView.setType(1);

        showBoxLayer();

        addView(v);

    }

    public void refreshAccountList() {
        accView.setData();
    }

    public void setInfo(String name, String job, String dept, String role){
        tv_name.setText(name);

        if (!TextUtils.isEmpty(role)) {
            job = role + "/" + job;
        }
        tv_job.setText(job);
        tv_team.setText(dept);
    }

    public void setLoginInfo(LoginResultVO loginInfo) {
        this.loginInfo = loginInfo;
        setInfo(loginInfo.getName(), loginInfo.getJobTitle(), loginInfo.getDepartmentName(), loginInfo.getRoleName());

//        tv_name.setText(loginInfo.getName());
//        String job = loginInfo.getJobTitle();
//        if (!TextUtils.isEmpty(loginInfo.getRoleName())) {
//            job = loginInfo.getRoleName() + "/" + loginInfo.getJobTitle();
//        }
//        tv_job.setText(job);
//        tv_team.setText(loginInfo.getDepartmentName());
    }

    public void setData(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray, Res_AP_IF_101_VO.result userAuthInfo) {
        // this.menuArray = menuArray;
    //    accView.setData();

        layout_main_menu.removeAllViews();

        int size = userAuthInfo.getSysArray().size(); // 총 메뉴 갯수

        for(int i=0;i<size;i++){
            LeftSlideMenuItemView menuView = new LeftSlideMenuItemView(mContext);
            menuView.setInterface(new LeftSlideMenuItemView.OnTabClick() {
                @Override
                public void selectItem(Res_AP_IF_102_VO.result.menuArray item) {
                    if (mInterface != null) {
                        mInterface.onTabClickLeftMenu(0, item);
                    }
                }
            });
            menuView.setData(menuArray, userAuthInfo.getSysArray().get(i));
            layout_main_menu.addView(menuView);
        }
    }

    //데이터 변경부분 변경적용 - 주로 카운트
    public void setChangeCount(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray, Res_AP_IF_101_VO.result userAuthInfo){
        //기존에 있는 데이터 뷰를 꺼내어 카운트를 적용시킴
        int size = userAuthInfo.getSysArray().size(); // 총 메뉴 갯수

        for(int i=0;i<size;i++){
            LeftSlideMenuItemView menuView = (LeftSlideMenuItemView)layout_main_menu.getChildAt(i);//하단이 펼치치는데 add
            LinearLayout expLayer = menuView.findViewById(R.id.lv_expand_menu_layer);
            for(int j=0;j<expLayer.getChildCount();j++){
                View cellview = expLayer.getChildAt(j);
                TextView num = cellview.findViewById(R.id.tv_menu_item_num);
                TextView subNum = cellview.findViewById(R.id.tv_sub_menu_item_num);

                for(int k =0;k<menuArray.size();k++){
                    if(menuArray.get(k).getMenuId().equals(num.getTag())){
                        if (TextUtils.isEmpty(menuArray.get(k).getCountNum())) {
                            num.setVisibility(View.GONE);
                        } else {
                            num.setVisibility(View.VISIBLE);
                            num.setText(menuArray.get(k).getCountNum());
                        }
                    }else if(menuArray.get(k).getMenuId().equals(subNum.getTag())){
                        if (TextUtils.isEmpty(menuArray.get(k).getCountNum())) {
                            subNum.setVisibility(View.GONE);
                        } else {
                            subNum.setVisibility(View.VISIBLE);
                            subNum.setText(menuArray.get(k).getCountNum());
                        }
                    }
                }
            }
        }
    }

    //결재함 리스트를 보여줌
    public void showBoxLayer() {
        btn_select_accout.setSelected(false);
        select_box_list_layer.setVisibility(View.VISIBLE);
        select_account_list_layer.setVisibility(View.GONE);
    }

    //계정선택화면을 보여줌
    private void showAccountLayer() {
        btn_select_accout.setSelected(true);
        select_box_list_layer.setVisibility(View.GONE);
        select_account_list_layer.setVisibility(View.VISIBLE);

        getAccountList();
    }

    /**
     * 계정 조회
     */
    private void getAccountList() {
        //show progress bar...
        ((MainActivity) mContext).showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        String userId = mPref.getStringPreference(Constants.PREF_USER_ID);//주 아이디
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        presenter.getMultiUserList(hm, new NetworkPresenter.getMultiUserResult() {
            @Override
            public void onResponse(Res_AP_IF_004_VO result) {
                ((MainActivity) mContext).hideProgressBar();
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
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

                TextDialog dialog;
                dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
                dialog.setCancelable(false);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(((MainActivity) mContext).getSupportFragmentManager());

            }
        });
    }

    /**
     * 계정 삭제
     */
    private void deleteAccount(String subUserId) {
        ((MainActivity) mContext).showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        String userId = mPref.getStringPreference(Constants.PREF_USER_ID);//주아이디
        hm.put("userId", userId); // 주아이디(불변)
        hm.put("subUserId", subUserId); //삭제될 아이디
        presenter.getMultiUserDelete(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                ((MainActivity) mContext).hideProgressBar();
            }
        });
    }

    /**
     * 계정 등록
     */
    private void insertAccount(String subUserId) {
        ((MainActivity) mContext).showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        String userId = mPref.getStringPreference(Constants.PREF_USER_ID);
        hm.put("userId", userId);// 주아이디(불변)
        hm.put("subUserId", subUserId);//등록될 아이디
        presenter.getMultiUserInsert(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                ((MainActivity) mContext).hideProgressBar();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_select_accout) {
            if (btn_select_accout.isSelected()) {
                showBoxLayer();
            } else {
                showAccountLayer();
            }
        } else {
            if (mInterface != null) {
                mInterface.onTabClickLeftMenu(view.getId(), null);
            }
        }
    }

}
