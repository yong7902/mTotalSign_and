package com.kolon.sign2.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.approval.ApprovalDetailActivity;
import com.kolon.sign2.approval.ApprovalFragment;
import com.kolon.sign2.approval.ApprovalSearchPopup;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.dynamic.dynamicDetail.DynamicDetailActivity;
import com.kolon.sign2.home.HomeFragment;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.dynamic.DynamicListFragment;
import com.kolon.sign2.servicedesk.ServiceDeskDetailActivity;
import com.kolon.sign2.setting.SettingAccountManagement;
import com.kolon.sign2.setting.SettingActivity;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SSOUtils;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.view.LeftSlideMenuView;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.view.TopMenuView;
import com.kolon.sign2.vo.LoginResultVO;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_020_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LeftSlideMenuView.LeftMenuClickListener, TopMenuView.TopMenuClickListener {

    private String TAG = MainActivity.class.getSimpleName();

    private RelativeLayout left_slide_menu_layout;
    private LeftSlideMenuView left_slide_menu_view;
    private DrawerLayout drawer_layout;
    private TopMenuView top_menu_view;
    private TextSizeAdjView view_text_size_adj;
    private Button btn_left_menu;
    private Button btn_top_menu;
    private Button btn_search;
    private TextView txt_menu_title;
    private ImageView btn_textsize;
    private RelativeLayout view_background;

    //fragment 상수 정의
    public final int FRAGMENT_HOME = 0;  //home
    public final int FRAGMENT_APPROVAL = 1;  //전자결재
    public final int FRAGMENT_DYNAMIC = 5;//동적 프래그먼트

    private Fragment frgmnt;
    private int fragmentType;

    private RelativeLayout progress_bar;

    private String userId;
    private String deptId;
    private String companyCd;

    private ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray;//슬라이드및 홈 초기 메뉴 데이터
//    private ArrayList<Res_AP_IF_012_VO.result.apprDeptList> apprDeptList;//문서함 목록 데이터 - 항목별 containerId, category, subQuery등을 가져옴

    private ApprovalSearchPopup search;
    private Res_AP_IF_101_VO.result userAuthInfo;//mdm , icon
    private LoginResultVO loginInfo;

    private Animation upAni, downAni;
    private boolean isAnimating;
    private boolean firstStart;

    private SharedPreferenceManager mPref;

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    private boolean activityMoveOkCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstStart = true;
        Constants.isLogin = true;

        //yong79. shimmer
        //shimmerStart();

        registerReceiver();
//        if(Constants.schemeMap != null &&  !TextUtils.isEmpty(Constants.schemeMap.get("sysId"))){
//            gotoDetail(Constants.schemeMap);
//        }

        mPref = SharedPreferenceManager.getInstance(getBaseContext());

        String loginStr = mPref.getStringPreference(Constants.PREF_LOGIN_INFO);

        loginInfo = new Gson().fromJson(loginStr, new TypeToken<LoginResultVO>() {
        }.getType());


        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());

        //화면 캡쳐 방지 적용
        CommonUtils.preventCapture(getBaseContext(), getWindow());

        initView();

        //여러군데 필요정보 저장
        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);
        companyCd = mPref.getStringPreference(Constants.PREF_COMPANY_CD);


        String savedPushTokenValue = mPref.getStringPreference(Constants.PREF_TOKEN_KEY);
        Log.w(TAG, "#### pushToken = " + savedPushTokenValue);
        getSystemMenuData();
    }

    private void drawLayout() {
        changeFragment(FRAGMENT_HOME);
    }

    /**
     * 서버에서 메뉴 데이터를 가져온다(좌측 슬라이드메뉴)
     * param onlyLeftMenu 좌측메뉴 클릭시 메뉴데이터만 로딩
     */
    private void getSystemMenuData(){
        getSystemMenuData(false);
        //설정에서 홈이동시 딜레이 없이 타이틀 적용
        txt_menu_title.setText(getResources().getString(R.string.txt_home_title));
    }
    private void getSystemMenuData(boolean onlyLeftMenu) {

        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);

        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("sysId", "");//시스템 ID
        hm.put("menuId", "");//menu ID
        hm.put("deptId", deptId);//menu ID
        if(!onlyLeftMenu){
            shimmerStart();
            //    showProgressBar();
        }

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getSystemMenu(hm, new NetworkPresenter.getSystemMenuResult() {

            @Override
            public void onResponse(Res_AP_IF_102_VO result) {

                String errMsg = "";
                Log.d(TAG, "#### getSystemMenu:" + new Gson().toJson(result));
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            menuArray = result.getResult().getMenuArray();
                            if(onlyLeftMenu){
                                //hideProgressBar();
                                left_slide_menu_view.setChangeCount(menuArray, userAuthInfo);
                            }else{
                                left_slide_menu_view.setData(menuArray, userAuthInfo);
                                top_menu_view.setData(menuArray, userAuthInfo);
                                drawLayout();

                                if(firstStart){
                                    firstStart = false;
                                    if (Constants.schemeMap != null && !TextUtils.isEmpty(Constants.schemeMap.get("sysId"))) {
                                        gotoDetail(Constants.schemeMap);
                                    }
                                }
                            }
//                            getDeptList();//목록조회
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
                //shimmerStop();
                //hideProgressBar();

                TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show(getSupportFragmentManager());
            }
        });
    }

    /**
     * 부서문서함 목록 조회
     */
//    private void getDeptList() {
//        NetworkPresenter presenter = new NetworkPresenter();
//        HashMap hm = new HashMap();
//        hm.put("userId", userId);
//        hm.put("deptId", deptId);
//        Log.d(TAG, "#### getDeptList in:" + hm);
//        presenter.getApprovalDeptList(hm, new NetworkPresenter.getApprovalDeptListResult() {
//
//            @Override
//            public void onResponse(Res_AP_IF_012_VO result) {
//                Log.d(TAG, "#### getDeptList:" + new Gson().toJson(result));
//                hideProgressBar();
//                String errMsg = "";
//                if (result != null) {
//                    if ("200".equals(result.getStatus().getStatusCd())) {
//                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
//
//                            apprDeptList = result.getResult().getApprDeptList();
//
//                            //비교하기 편하게 미리 subquery내부의 숫자를 따로 집어넣는다.
//                            for(int i=0;i<apprDeptList.size();i++){
//                                if(TextUtils.isEmpty(apprDeptList.get(i).getSubQuery())){
//                                    apprDeptList.get(i).fixQuery = "";
//                                }else{
//                                    String readSubQ[] = apprDeptList.get(i).getSubQuery().split("'");
//                                    if(readSubQ.length>1){
//                                        apprDeptList.get(i).fixQuery = readSubQ[1];
//                                    }
//                                }
//                            }
//
//                            drawLayout();
//                            return;
//                        } else {
//                            errMsg = result.getResult().getErrorMsg();
//                        }
//                    } else {
//                        errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
//                    }
//                } else {
//                    errMsg = getResources().getString(R.string.txt_network_error);
//                }
//
//                TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
//                dialog.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//                dialog.show(getSupportFragmentManager());
//            }
//        });
//    }

    /**
     * layout setting
     */
    private void initView() {
        //left menu layout
        left_slide_menu_layout = (RelativeLayout) findViewById(R.id.left_slide_menu_layout);
        left_slide_menu_view = (LeftSlideMenuView) findViewById(R.id.left_slide_menu_view);
        left_slide_menu_view.setInterface(this);
        left_slide_menu_view.setLoginInfo(loginInfo);

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);//addDrawerListener
        drawer_layout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //menu loading
                getSystemMenuData(true);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        view_background = (RelativeLayout) findViewById(R.id.view_background);
        view_background.setVisibility(View.GONE);
        view_background.setOnClickListener(this);
        //top menu layout
        top_menu_view = (TopMenuView) findViewById(R.id.top_menu_view);
        top_menu_view.setInterface(this);
        isAnimating = true;
        upAni = AnimationUtils.loadAnimation(this, R.anim.topmenu_up_ani);
        upAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = true;
                top_menu_view.setVisibility(View.GONE);
                view_background.setVisibility(View.GONE);
                view_text_size_adj.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        downAni = AnimationUtils.loadAnimation(this, R.anim.topmenu_down_ani);
        downAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view_text_size_adj = (TextSizeAdjView) findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                if (fragmentType == FRAGMENT_HOME) {
                    //홈
                    HomeFragment f = (HomeFragment) frgmnt;
                    f.changeTextSize();
                } else if (fragmentType == FRAGMENT_APPROVAL) {
                    //전자결재
                    ApprovalFragment f = (ApprovalFragment) frgmnt;
                    f.changeTextSize();
                } else if (fragmentType == FRAGMENT_DYNAMIC) {
                    //서비스데스크
                    DynamicListFragment f = (DynamicListFragment) frgmnt;
                    f.changeTextSize();
                }
            }

        });
        //좌측 메뉴 버튼
        btn_left_menu = (Button) findViewById(R.id.btn_left_menu);
        btn_left_menu.setOnClickListener(this);
        //상단 메뉴 버튼
        btn_top_menu = (Button) findViewById(R.id.btn_top_menu);
        btn_top_menu.setOnClickListener(this);
        //찾기
        btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        //상단 타이틀
        txt_menu_title = (TextView) findViewById(R.id.txt_menu_title);
        txt_menu_title.setOnClickListener(this);

        btn_textsize = (ImageView) findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(this);

        progress_bar = (RelativeLayout) findViewById(R.id.progress_bar);
        progress_bar.setOnClickListener(this);
    }


    /**
     * fragment 교체
     * 각 fragment의 교체 및 상단 타이틀 변경, 우측 서치버튼 표시유무를 정한다.
     *
     * @param type fragment 종류
     */

    public void changeFragment(int type) {
        changeFragment(type, "", "");
    }

    public void changeFragment(int type, String menuId, String sysId) {

        String title = "";
        frgmnt = null;
        btn_search.setVisibility(View.GONE);//search button 표시
        fragmentType = type;

        if (type != FRAGMENT_HOME) {
            for (int i = 0; i < userAuthInfo.getSysArray().size(); i++) {
                if (sysId.equals(userAuthInfo.getSysArray().get(i).getSysId())) {
                    title = userAuthInfo.getSysArray().get(i).getSysName();
                }
            }
        }

        switch (type) {
            case FRAGMENT_HOME: //홈
                title = getResources().getString(R.string.txt_home_title);
                frgmnt = HomeFragment.newInstance(menuArray, userAuthInfo.getSysArray());
                break;
            case FRAGMENT_APPROVAL: //전자결재
                btn_search.setVisibility(View.VISIBLE);
                frgmnt = ApprovalFragment.newInstance(userId, deptId, companyCd, menuArray, menuId);
                break;
            case FRAGMENT_DYNAMIC: //동적프래그먼트
                frgmnt = DynamicListFragment.newInstance(userId, deptId, companyCd, menuArray, menuId, title, sysId);
                break;
        }

        txt_menu_title.setText(title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_layer, frgmnt);
        transaction.commitAllowingStateLoss();


        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            //좌측 메뉴가 열려있다면 닫는다.
            drawer_layout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityMoveOkCheck = false;

        if (view_text_size_adj != null) {
            view_text_size_adj.changeTextSize();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_menu:
                //left menu open
                drawer_layout.openDrawer(left_slide_menu_layout);
                break;
            case R.id.btn_top_menu:
            case R.id.txt_menu_title:
                //top menu open/close
                if (!isAnimating) return;
                isAnimating = false;
                //글자크기조절뷰가 있으면 닫는다
                if (view_text_size_adj.getVisibility() == View.VISIBLE) {
                    view_text_size_adj.setVisibility(View.GONE);
                }

                if (top_menu_view.getVisibility() == View.VISIBLE) {
                    top_menu_view.startAnimation(upAni);
                    btn_top_menu.setSelected(false);
                } else {
                    view_background.setVisibility(View.VISIBLE);
                    top_menu_view.setVisibility(View.VISIBLE);
                    top_menu_view.startAnimation(downAni);
                    btn_top_menu.setSelected(true);
                }
                break;
            case R.id.btn_search:
                //search
                //전자결재에서 검색
                if (search != null && search.isShowing()) {
                    return;
                }
                search = new ApprovalSearchPopup(this);

                ApprovalFragment fragment = (ApprovalFragment) frgmnt;
                HashMap<String, String> hm = fragment.getServerInputData();


                //String category, String userId, String deptId, String containerId, String subQuery
                search.setData(hm.get("category"), hm.get("userId"), hm.get("deptId"), hm.get("containerId"), hm.get("subQuery"));
                search.setInterface(new ApprovalSearchPopup.SearchPopupInterface() {
                    @Override
                    public void onClick(ArrayList<Res_AP_IF_020_VO.result.apprList> list, int position) {//ArrayList pos
                        fragment.gotoDetail(list, position);
                    }
                });
                search.show();

                break;
            case R.id.btn_textsize:

                //top menu가 나와있으면 닫는다
                if (top_menu_view.getVisibility() == View.VISIBLE) {
                    top_menu_view.setVisibility(View.GONE);
                    btn_top_menu.setSelected(false);
                }

                if (!isAnimating) return;
                isAnimating = false;

                if (view_text_size_adj.getVisibility() == View.VISIBLE) {
                    view_text_size_adj.startAnimation(upAni);
                } else {
                    view_text_size_adj.setVisibility(View.VISIBLE);
                    view_text_size_adj.startAnimation(downAni);
                    view_background.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.view_background:
                if (!isAnimating) return;
                isAnimating = false;

                if (view_text_size_adj.getVisibility() == View.VISIBLE) {
                    view_text_size_adj.startAnimation(upAni);
                }

                if (top_menu_view.getVisibility() == View.VISIBLE) {
                    top_menu_view.startAnimation(upAni);
                    btn_top_menu.setSelected(false);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            //좌측 메뉴가 열려있다면 닫는다.
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            //위치가 홈이면 닫고 아니면 홈으로 이동
            if (fragmentType == FRAGMENT_HOME) {

                // 기존 뒤로가기 버튼의 기능을 막기위해 주석처리 또는 삭제
                // super.onBackPressed();

                // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
                // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
                // 2000 milliseconds = 2 seconds
                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    toast = Toast.makeText(this, R.string.tx_quit_app, Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
                // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
                // 현재 표시된 Toast 취소
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    finish();
                    toast.cancel();
                }


            } else {
                changeFragment(FRAGMENT_HOME);
            }
        }

    }

    /**
     * left slide menu click event
     */
    @Override
    public void onTabClickLeftMenu(int resId, Res_AP_IF_102_VO.result.menuArray item) {
        switch (resId) {
            case R.id.btn_left_menu_setting:
                Intent i = new Intent(this, SettingActivity.class);

                if (activityMoveOkCheck) {
                    return;
                }
                activityMoveOkCheck = true;
                startActivityForResult(i, 1);

                closeEtcPopup();

                break;

            case R.id.btn_close:
                drawer_layout.closeDrawer(GravityCompat.START);
                break;
            case R.id.btn_menu_home:
                //슬라이드 메뉴 홈버튼
                changeFragment(FRAGMENT_HOME);
                break;
            case R.id.btn_menu_iken:
                //슬라이드 메뉴 IKEN
                boolean isDev = false;
                String url = "";
                if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
                    isDev = true;
                }
                if (SSOUtils.findIKENApp(getBaseContext())) {
                    //있으면 실행
                    url = "ikenapp2://";
                    if (isDev) {
                        url = "ikenapp2dev://";
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    //없으면 설치
                    IkenInstallPopup();
                }
                break;
            case 0:
                String sysId = item.getSysId();
                if ("sign".equals(item.getSysId())) {
                    changeFragment(FRAGMENT_APPROVAL, item.getMenuId(), sysId);
                } else {
                    changeFragment(FRAGMENT_DYNAMIC, item.getMenuId(), sysId);
                }

                break;
        }
    }

    //계정관리로 이동
    public void gotoAddAccount() {
        //계정추가
        Intent add = new Intent(this, SettingAccountManagement.class);

        if (activityMoveOkCheck) {
            return;
        }
        activityMoveOkCheck = true;
        startActivityForResult(add, 1);

        closeEtcPopup();
    }

    //계정 변경
    public void changeAccount(Res_AP_IF_004_VO.result.multiuserList item) {

        //여러군데 필요정보 저장
        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);
        companyCd = mPref.getStringPreference(Constants.PREF_COMPANY_CD);

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            //좌측 메뉴가 열려있다면 닫는다.
            drawer_layout.closeDrawer(GravityCompat.START);
        }

        getSystemMenuData();
        if (left_slide_menu_view != null) {
            left_slide_menu_view.showBoxLayer();
        }
    }


    public void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    public void selectFragment(String sysId) {
        if ("home".equals(sysId)) {
            //홈
            changeFragment(FRAGMENT_HOME);
        } else {
            String menuid = "";
            //menuarray  에서 각 항목의 첫번째
            for (int i = 0; i < menuArray.size(); i++) {
                if (sysId.equals(menuArray.get(i).getSysId())) {
                    menuid = menuArray.get(i).getMenuId();
                    break;
                }
            }

            if ("sign".equals(sysId)) {
                changeFragment(FRAGMENT_APPROVAL, menuid, sysId);
            } else {
                changeFragment(FRAGMENT_DYNAMIC, menuid, sysId);
            }
        }
    }

    /**
     * top menu click event
     */
    @Override
    public void onClickTopMenu(String sysId) {
        selectFragment(sysId);

        if (!isAnimating) return;
        isAnimating = false;
        if (top_menu_view.getVisibility() == View.VISIBLE) {
            top_menu_view.startAnimation(upAni);
            btn_top_menu.setSelected(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("#### requestCode:" + requestCode + "  resultCode:" + resultCode + "  " + mPref.getStringPreference(Constants.PREF_USER_IF_ID));
        if (requestCode == 0) { //계정 외 업무 상세에서 복귀
            if (resultCode == RESULT_OK) //결재 처리가 된경우만 리프레시 한다.
                drawLayout();
        } else if (requestCode == 1) {
            //계정관련 작업후 리로딩..
            String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
            userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
            }.getType());

            //getSystemMenuData();
            // 계정을 변경 하였거나, 시스템 순서가 변경 되었을 경우 화면을 리프레시 한다.
            if (!mPref.getStringPreference(Constants.PREF_CHANGED_ACCOUNT_DATA).isEmpty() || !mPref.getStringPreference(Constants.PREF_CHANGED_SYS_ORDER).isEmpty()) {
                getSystemMenuData();
                mPref.setStringPreference(Constants.PREF_CHANGED_SYS_ORDER, "");
            }

            if (left_slide_menu_view != null) {
                left_slide_menu_view.showBoxLayer();
                String changeInfo = mPref.getStringPreference(Constants.PREF_CHANGED_ACCOUNT_DATA);

                //계정을 변경한 경우, 계정정보 적용 및 리프레시
                if (!changeInfo.isEmpty()) {

                    Type type = new TypeToken<Res_AP_IF_004_VO.result.multiuserList>() {
                    }.getType();
                    Res_AP_IF_004_VO.result.multiuserList changeInfoData = new Gson().fromJson(changeInfo, type);
                    left_slide_menu_view.setInfo(changeInfoData.getUserName(), changeInfoData.getTitleName(), changeInfoData.getDeptName(), changeInfoData.getRoleName());
                    //적용한후 지워버림.
                    mPref.setStringPreference(Constants.PREF_CHANGED_ACCOUNT_DATA, "");
                }

                //계정설정이 변경 되었을경우, leftmenu 리스트를 갱신한다. (계정목록)
                if ("Y".equals(mPref.getStringPreference(Constants.PREF_MOD_ACCOUNT_DATA))){
                    left_slide_menu_view.refreshAccountList();
                    mPref.setStringPreference(Constants.PREF_MOD_ACCOUNT_DATA, "");
                }
            }

            //글자 크기 변경 적용
            if (!mPref.getStringPreference(Constants.PREF_CHANGED_TEXT_SIZE).isEmpty()) {
                mPref.setStringPreference(Constants.PREF_CHANGED_TEXT_SIZE, "");
                if (fragmentType == FRAGMENT_HOME) {
                    //홈
                    HomeFragment f = (HomeFragment) frgmnt;
                    f.changeTextSize();
                } else if (fragmentType == FRAGMENT_APPROVAL) {
                    //전자결재
                    ApprovalFragment f = (ApprovalFragment) frgmnt;
                    f.changeTextSize();
                } else if (fragmentType == FRAGMENT_DYNAMIC) {
                    //서비스데스크
                    DynamicListFragment f = (DynamicListFragment) frgmnt;
                    f.changeTextSize();
                }
            }

        }else if(requestCode == 100){
            //동적 리스트에서 상세 이동시 복귀
            ((DynamicListFragment)frgmnt).changeTextSize();
            if (resultCode == RESULT_OK) {//결재 처리가 된경우만 리프레시 한다.
                ((DynamicListFragment) frgmnt).listRefresh();
            }
        }
    }

    public void authListRefresh(String pUserId) {
        ((DynamicListFragment) frgmnt).authListRefresh(pUserId);
    }

    //디테일 화면으로 이동
    public void gotoDetail(String sysId, Res_AP_IF_002_VO.result.APPROVAL_LIST data) {

        if ("sign".equals(sysId)) {
            //전자결재
            Intent it = new Intent(this, ApprovalDetailActivity.class);

            ArrayList<String> guidList = new ArrayList<>();
            //00000000000000115040|00000000000000115040|0|0
            guidList.add(data.getDocId()+"|"+data.getDocId()+"|"+data.getParam01());

            String containerId = "";
            String subQuery = "";
            String category = data.getMenuId();

            ArrayList<Res_AP_IF_013_VO.result.apprList> obj = new ArrayList<>();
            Res_AP_IF_013_VO.result.apprList info = new Res_AP_IF_013_VO().new result().new apprList();
            info.setTitle(data.getTitle());
            info.setAuthor(data.getRequester());
            info.setGuid(data.getDocId());
            info.setPubDate(data.getReqDatetime());
            info.setStatus(data.getStatus());
            info.setHasattachYn(data.getHasattachYN());
            info.setHasopinionYn(data.getHasopinionYN());
            info.setInLine(data.getInLine());
            info.setIsPublic(data.getIsPublic());
            info.setCategory(category);
            obj.add(info);

            it.putExtra("companyCd", companyCd);
            it.putExtra("guidList", guidList);
            it.putExtra("position", 0);
            it.putExtra("userId", userId);
            it.putExtra("deptId", deptId);
            it.putExtra("containerId", containerId);
            it.putExtra("subQuery", subQuery);
            it.putExtra("category", category);
            it.putExtra("object", obj);

            if (activityMoveOkCheck) {
                return;
            }
            activityMoveOkCheck = true;
            startActivityForResult(it, 0);

        } else {
            String menuId = data.getMenuId();
            String mSysNm = "";
            for (int i = 0; i < userAuthInfo.getSysArray().size(); i++) {
                if (userAuthInfo.getSysArray().get(i).getSysId().equals(sysId)) {
                    mSysNm = userAuthInfo.getSysArray().get(i).getSysName();
                    break;
                }
            }

            ArrayList<Res_AP_IF_103_VO.dynamicListList> dynamicListList = new ArrayList<>();
            Res_AP_IF_103_VO.dynamicListList info = new Res_AP_IF_103_VO.dynamicListList();
            info.setApprovalCase(data.getApprovalCase());
            info.setApprovalId(data.getApprovalId());
            info.setDocId(data.getDocId());
            info.setHasattachYN(data.getHasattachYN());
            info.setHasopinionYN(data.getHasopinionYN());
            info.setInLine(data.getInLine());
            info.setIsPublic(data.getIsPublic());
            info.setParam01(data.getParam01());
            info.setParam02(data.getParam02());
            info.setParam03(data.getParam03());
            info.setParam04(data.getParam04());
            info.setParam05(data.getParam05());
            info.setReqDatetime(data.getReqDatetime());
            info.setRequester(data.getRequester());
            info.setStatus(data.getStatus());
            info.setTitle(data.getTitle());
            dynamicListList.add(info);

            if (activityMoveOkCheck) {
                return;
            }
            activityMoveOkCheck = true;
            //서비스데스크 중 보안(), 권한 S03,04,05
            if (menuId.startsWith("S03") || menuId.startsWith("S04") || menuId.startsWith("S05")) {
                Intent intent = new Intent(this, ServiceDeskDetailActivity.class);
                intent.putExtra("data", dynamicListList);
                intent.putExtra("menuId", menuId);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 0);
            } else {
                //그외...
                Intent intent = new Intent(this, DynamicDetailActivity.class);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                intent.putExtra("sysId", sysId);
                intent.putExtra("sysNm", mSysNm);
                intent.putExtra("menuId", data.getMenuId());
                intent.putExtra("docId", data.getDocId());
                intent.putExtra("param01", data.getParam01());
                intent.putExtra("param02", data.getParam02());
                intent.putExtra("param03", data.getParam03());
                intent.putExtra("param04", data.getParam04());
                intent.putExtra("param05", data.getParam05());
                intent.putExtra("listcount", 1);
                intent.putExtra("object", dynamicListList);
                startActivityForResult(intent, 0);
            }
        }
    }
    //디테일 화면으로 이동 - scheme or push
    private void gotoDetail(HashMap<String, String> getParamMap) {
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());
        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        Res_AP_IF_101_VO.result userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());


        String userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        String deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);
        String companyCd = mPref.getStringPreference(Constants.PREF_COMPANY_CD);
        String menuId = getParamMap.get("menuId");
        String docId = getParamMap.get("docId");
        String sysId = getParamMap.get("sysId");
        String param01 = getParamMap.get("param01");
        String param02 = getParamMap.get("param02");
        String param03 = getParamMap.get("param03");
        String param04 = getParamMap.get("param04");
        String param05 = getParamMap.get("param05");

        if(TextUtils.isEmpty(menuId)){
            //둘다 없을경우 - top menu처럼 항목으로 이동 docid만 있을경우도 둘다없는거로 취급
            selectFragment(sysId);
            return;
        }else if(TextUtils.isEmpty(docId)){
            //docid만 없을 경우 - 해당 menuid까지 이동
            if ("sign".equals(sysId)) {
                changeFragment(FRAGMENT_APPROVAL, menuId, sysId);
            }else if ("home".equals(sysId)) {
                changeFragment(FRAGMENT_HOME);
            } else {
                changeFragment(FRAGMENT_DYNAMIC, menuId, sysId);
            }
            return;
        }

        // String sysId = getParamMap.get("sysId");
        if ("sign".equals(sysId)) {
            //전자결재
            Intent it = new Intent(this, ApprovalDetailActivity.class);

            ArrayList<String> guidList = new ArrayList<>();

            guidList.add(docId + "|" + docId + "|" + param01);

            String containerId = "";
            String subQuery = "";
            String category = menuId;

            ArrayList<Res_AP_IF_013_VO.result.apprList> obj = new ArrayList<>();
            Res_AP_IF_013_VO.result.apprList info = new Res_AP_IF_013_VO().new result().new apprList();
            info.setTitle(getParamMap.get("title"));
            info.setAuthor(getParamMap.get("requester"));
            info.setGuid(docId);
            info.setPubDate(getParamMap.get("reqDatetime"));
            info.setStatus(getParamMap.get("status"));
            info.setHasattachYn(getParamMap.get("hasattachYN"));
            info.setHasopinionYn(getParamMap.get("hasopinionYN"));
            info.setInLine(getParamMap.get("inLine"));
            info.setIsPublic(getParamMap.get("isPublic"));
            info.setCategory(category);
            obj.add(info);

            it.putExtra("companyCd", companyCd);
            it.putExtra("guidList", guidList);
            it.putExtra("position", 0);
            it.putExtra("userId", userId);
            it.putExtra("deptId", deptId);
            it.putExtra("containerId", containerId);
            it.putExtra("subQuery", subQuery);
            it.putExtra("category", category);
            it.putExtra("object", obj);

            if (activityMoveOkCheck) {
                return;
            }
            activityMoveOkCheck = true;
            startActivityForResult(it, 0);
        } else {
            String mSysNm = "";
            for (int i = 0; i < userAuthInfo.getSysArray().size(); i++) {
                if (userAuthInfo.getSysArray().get(i).getSysId().equals(sysId)) {
                    mSysNm = userAuthInfo.getSysArray().get(i).getSysName();
                    break;
                }
            }

            ArrayList<Res_AP_IF_103_VO.dynamicListList> dynamicListList = new ArrayList<>();
            Res_AP_IF_103_VO.dynamicListList info = new Res_AP_IF_103_VO.dynamicListList();
            info.setApprovalCase(getParamMap.get("approvalCase"));
            info.setApprovalId(getParamMap.get("approvalId"));
            info.setDocId(docId);
            info.setHasattachYN(getParamMap.get("hasattachYN"));
            info.setHasopinionYN(getParamMap.get("hasopinionYN"));
            info.setInLine(getParamMap.get("isPublic"));
            info.setIsPublic(getParamMap.get("isPublic"));
            info.setParam01(getParamMap.get("param01"));
            info.setParam02(getParamMap.get("param02"));
            info.setParam03(getParamMap.get("param03"));
            info.setParam04(getParamMap.get("param04"));
            info.setParam05(getParamMap.get("param05"));
            info.setReqDatetime(getParamMap.get("reqDatetime"));
            info.setRequester(getParamMap.get("requester"));
            info.setStatus(getParamMap.get("status"));
            info.setTitle(getParamMap.get("title"));
            dynamicListList.add(info);

            if (activityMoveOkCheck) {
                return;
            }
            activityMoveOkCheck = true;
            //서비스데스크 중 보안(), 권한 S03,04,05
            if (menuId.startsWith("S03") || menuId.startsWith("S04") || menuId.startsWith("S05")) {
                Intent intent = new Intent(this, ServiceDeskDetailActivity.class);
                intent.putExtra("data", dynamicListList);
                intent.putExtra("menuId", menuId);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, 0);
            } else {
                //그외...
                Intent intent = new Intent(this, DynamicDetailActivity.class);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                intent.putExtra("sysId", sysId);
                intent.putExtra("sysNm", mSysNm);
                intent.putExtra("menuId", menuId);
                intent.putExtra("docId", docId);
                intent.putExtra("param01", param01);
                intent.putExtra("param02", param02);
                intent.putExtra("param03", param03);
                intent.putExtra("param04", param04);
                intent.putExtra("param05", param05);
                intent.putExtra("listcount", 1);
                intent.putExtra("object", dynamicListList);
                startActivityForResult(intent, 0);
            }
        }
        Constants.schemeMap = new HashMap<>();
    }

    //이동시 각종 창이나 팝업 닫기
    private void closeEtcPopup() {
        isAnimating = true;

        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        }

        if (view_text_size_adj.getVisibility() == View.VISIBLE) {
            view_text_size_adj.setVisibility(View.GONE);
        }

        if (top_menu_view.getVisibility() == View.VISIBLE) {
            top_menu_view.setVisibility(View.GONE);
        }
    }

    //팝업표시
    public void viewMessage(String msg) {
        viewMessage(msg, false);
    }

    public void viewMessage(String msg, boolean finish) {
        TextDialog dialog = TextDialog.newInstance("", msg, getResources().getString(R.string.txt_alert_confirm));
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (finish) finish();
            }
        });
        dialog.show(getSupportFragmentManager());
    }

    private void IkenInstallPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.txt_kolonIken_not_installed));
        builder.setPositiveButton(getString(R.string.txt_login_alert_close),
                (dialog, which) -> {
                    CommonUtils.callKolonApps(this);
                    dialog.dismiss();
                });
        builder.setNegativeButton(getString(R.string.txt_cancel),
                (dialog, which) -> {
                    dialog.dismiss();
                });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
        Constants.isLogin = false;
    }


    private BroadcastReceiver mReceiver = null;
    /**
     * 동적으로(코드상으로) 브로드 캐스트를 등록한다.
     * SchemeActivity에서 push 클릭 확인 용도
     *  **/
    private void registerReceiver(){

        if(mReceiver != null) return;

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(Constants.BROADCAST_MESSAGE);

        this.mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int receviedData = intent.getIntExtra("value",0);
                if(intent.getAction().equals(Constants.BROADCAST_MESSAGE)){
                    gotoDetail(Constants.schemeMap);
                }
            }
        };

        this.registerReceiver(this.mReceiver, theFilter);

    }
    private void unregisterReceiver() {
        if(mReceiver != null){
            this.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    public void shimmerStart() {
        ShimmerFrameLayout mShimmerLayout = findViewById(R.id.shimmer_layout);
        mShimmerLayout.startShimmer();
        mShimmerLayout.setVisibility(View.VISIBLE);

        RelativeLayout fragmentLayout = findViewById(R.id.fragment_layer);
        fragmentLayout.setVisibility(View.GONE);
    }

    public void shimmerStop() {
        ShimmerFrameLayout mShimmerLayout = findViewById(R.id.shimmer_layout);
        mShimmerLayout.stopShimmer();
        mShimmerLayout.setVisibility(View.GONE);

        RelativeLayout fragmentLayout = findViewById(R.id.fragment_layer);
        fragmentLayout.setVisibility(View.VISIBLE);
    }

    public void setMenuArray(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray) {
        this.menuArray = menuArray;
    }

    public ArrayList<Res_AP_IF_102_VO.result.menuArray> getMenuArray() {
        return menuArray;
    }

    public void updateBadgeCnt(String sysId, String menuId, String pUserId) {

        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);

        HashMap hm = new HashMap();
        if (TextUtils.isEmpty(pUserId))
            hm.put("userId", userId);
        else
            hm.put("userId", pUserId);

        hm.put("sysId", sysId);//시스템 ID
        hm.put("menuId", menuId);//menu ID
        hm.put("deptId", deptId);

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getSystemMenu(hm, new NetworkPresenter.getSystemMenuResult() {
            @Override
            public void onResponse(Res_AP_IF_102_VO result) {
                Log.d(TAG, "#### updateBadgeCnt:" + new Gson().toJson(result));
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            //menuArray 업데이트
                            for (Res_AP_IF_102_VO.result.menuArray menu : result.getResult().getMenuArray()) {
                                boolean isBadgeUpTarget = true;
                                //badgeYn 체크
                                if (!"Y".equals(menu.getBadgeYn()))
                                    isBadgeUpTarget = false;
                                //sysId 검사
                                if (!TextUtils.isEmpty(sysId) && !sysId.equals(menu.getSysId()))
                                    isBadgeUpTarget = false;
                                //menuId 검사
                                if (!TextUtils.isEmpty(menuId) && !menuId.equals(menu.getMenuId()))
                                    isBadgeUpTarget = false;

                                if (isBadgeUpTarget) {
                                    String menuId= menu.getMenuId();
                                    for (int i=0; i < menuArray.size(); i++) {
                                        if (menuId.equals(menuArray.get(i).getMenuId())) {
                                            menuArray.get(i).setCountNum(menu.getCountNum());
                                            break;
                                        }
                                    }
                                }
                            }

                            //뱃지 업데이트
                            if (fragmentType == FRAGMENT_APPROVAL) {
                                //전자결재
                                ((ApprovalFragment)frgmnt).updateTab();
                            } else if (fragmentType == FRAGMENT_DYNAMIC) {
                                //다이나믹
                                ((DynamicListFragment)frgmnt).updateTab();
                            }
                        }
                    }
                }
            }
        });
    }

    public void setActivityMoveOkCheck(boolean activityMoveOkCheck){
        this.activityMoveOkCheck = activityMoveOkCheck;
    }
    public boolean getActivityMoveOkCheck(){
        return this.activityMoveOkCheck;
    }

}
