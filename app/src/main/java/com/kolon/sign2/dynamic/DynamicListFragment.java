package com.kolon.sign2.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dynamic.dynamicDetail.DynamicDetailActivity;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.servicedesk.ServiceDeskAuthListView;
import com.kolon.sign2.dynamic.viewmodel.DynamicListViewModel;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.view.ChildOptionListView;
import com.kolon.sign2.view.ChildOptionMenuView;
import com.kolon.sign2.vo.Res_AP_IF_037_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DynamicListFragment extends Fragment implements ServiceDeskAuthListView.OnAuthListOnItemClick {
    private String TAG = getClass().getSimpleName();
    private DynamicListViewModel viewModel;
    private String SELECTED_MENU_KEY = "";
    private SharedPreferenceManager mPref;
    // 현재 5개, 서버측에서 늘어날경우 추가 작업
    private final int MAX_TAB_SIZE = 5;

    private View view;
    private Context mContext;
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArrays;

    //최상단 탭 구성요소
    private LinearLayout groupTabContainer;
    private int[] groupIds;
    private ArrayList<Button> groupTabBtnArr;


    //메뉴 탭 구성요소
    private AppBarLayout tabAppbar;
    private TabLayout menuTabLayout;
    private ArrayList<MenuVO> menuItems;

    //옵션 탭 구성
    private ChildOptionListView mChildOptionListView;

    //세부 메뉴 탭 구성요소
    private LinearLayout childTabContainer;

    private RecyclerView contentsRecyclerView;
    private DynamicListAdapter contentsAdapter;
    private ConstraintLayout mEmptyListView;

    private String AUTO_DETAIL_FLAG = "";
    private String mSysId = "";
    private String mSysNm = "";
    private String mMenuID = "";
    private String mIsAutoList = "";
    private String mUserId = "";
    private String mDeptId = "";
    private boolean isDuplicateCall = false;
    private static final long DUPLICATE_TIME = 300;
    private String mMenuIDParam = "";

    private LinearLayout mCustomLayout;
    private ServiceDeskAuthListView authListView;

    private ShimmerFrameLayout mShimmerLayout;
    private RelativeLayout progressBar;

    private boolean activityMoveOkCheck;
    //
    private int pageSize = 20;//한번에 보여주는 사이즈
//    private int pageNum = 0; //0, 1 ,2 ,3 이 아니라 0 20 40 등 시작페이지이고 pageSize만큼 더해저야함.
//    private boolean loadingMore=true; //리스트 끝에서 추가 로딩여부

    public static DynamicListFragment newInstance(String userId, String deptId, String companyCd, ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArrays, String menuId, String sysNm, String sysId) {
        DynamicListFragment fragment = new DynamicListFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("deptId", deptId);
        args.putString("companyCd", companyCd);
        args.putSerializable("menuArrays", menuArrays);
     //   args.putSerializable("apprDeptList", apprDeptList);
        args.putString("menuId", menuId);
        args.putString("sysNm", sysNm);
        args.putString("sysId", sysId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMenuIDParam = getArguments().getString("menuId");
            mDeptId = getArguments().getString("deptId");
            mSysNm = getArguments().getString("sysNm");
            mSysId = getArguments().getString("sysId");
        }
        mPref = SharedPreferenceManager.getInstance(mContext);
        mUserId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
 //       mUserId = "yong79";
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Context contextTheme = new ContextThemeWrapper(getActivity(), R.style.AppTheme);
        LayoutInflater localInflater = getActivity().getLayoutInflater().cloneInContext(contextTheme);
        view = (ViewGroup) localInflater.inflate(R.layout.fragment_dynamic_list, container, false);
        viewModel = new DynamicListViewModel();

        initView();

        //menuArrays = (ArrayList<Res_AP_IF_102_VO.result.menuArray>) getArguments().getSerializable("menuArrays");
        menuArrays = ((MainActivity)mContext).getMenuArray();
        menuArrays = redividingMenu(mSysId); //List 메뉴에 따른 새로운 저장
        bindingObserver();
        //TODO : MENU를 뭐를 눌렀는지 data Param에서 받아온다.

        drawGroupTab();
        //drawMenuTab(); // initGroupTabItem 의 btn.setOnClickListener 에서도 불러오므로 2번 불러옴(서버 2번호출됨). 이곳은 주석처리

        mMenuIDParam = "";
        return view;
    }

    private void initView() {
        groupTabContainer = view.findViewById(R.id.ll_servicedesk_group_tab_container);
        tabAppbar = view.findViewById(R.id.servicedesk_app_bar);
        menuTabLayout = view.findViewById(R.id.servicedesk_group_menu_tab);

        childTabContainer = view.findViewById(R.id.ll_servicedesk_group_child_menu_container);
        contentsRecyclerView = view.findViewById(R.id.rv_servicedesk_group_main_rv);
        contentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int last = ((LinearLayoutManager) contentsRecyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                int totalCnt = contentsRecyclerView.getAdapter().getItemCount();

                if (totalCnt >= pageSize && last == totalCnt) {
                    viewModel.call_IF_103(mContext,mUserId, mSysId, mMenuID, "loadingMore");
                }

            }
        });

        mEmptyListView = view.findViewById(R.id.cl_empty_list);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_layout);
        progressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            if (!mIsAutoList.isEmpty() && mIsAutoList.equalsIgnoreCase("Y")) {
                shimmerStart();
                viewModel.call_IF_103(mContext,mUserId, mSysId, mMenuID, "");
                //getSystemMenuData();
            } else {
                //Todo autoList가 아닌 경우 List 그려주기 위한 처리 필요
                if(authListView != null){
                    authListView.setTab(mMenuID);
                }
            }
        });

        mCustomLayout = view.findViewById(R.id.custom_layout);
    }

    private void bindingObserver() {
        contentsAdapter = new DynamicListAdapter(mContext, new ArrayList<>());
        contentsRecyclerView.setAdapter(contentsAdapter);

        hideAuthListView();

        //하단 recyclerview contents가 바뀔때마다 자동 호출
        // viewModel.rvLiveData.value가 변경될때마다 호출됨
        viewModel.getRvLiveData().observe(this, lists -> {
            if (lists == null) {
                contentsRecyclerView.setVisibility(View.GONE);
                mEmptyListView.setVisibility(View.GONE);

            //    shimmerStart();
                //((MainActivity) mContext).showProgressBar();

            } else {
                if (lists.isEmpty()) {
                    contentsRecyclerView.setVisibility(View.GONE);
                    mEmptyListView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyListView.setVisibility(View.GONE);
                    contentsRecyclerView.setVisibility(View.VISIBLE);
                    contentsAdapter.updateList(lists, AUTO_DETAIL_FLAG, mUserId, mSysId, mMenuID, mSysNm);
                    contentsAdapter.notifyDataSetChanged();
                }
                shimmerStop();
                //((MainActivity) mContext).hideProgressBar();
            }

        });
    }

    /**
     * @param kind (전자결재, 서비스데스크, 출입관리, 차량관리)
     * @return ArrayList<Res_AP_IF_102_VO.result.menuArray>
     * @see @param에 따라 List 재정렬
     */
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> redividingMenu(String kind) {
        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;

        ArrayList<Res_AP_IF_102_VO.result.menuArray> menuItem = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            if (kind.equals(list.getSysId())) {
                menuItem.add(list);
            }
        }
        return menuItem;
    }

    /**
     * 최상단 groupTab 인자로 있는 메뉴 그려줌
     */
    private void drawGroupTab() {
        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;

        ArrayList<String> groupNames = new ArrayList<>();
        groupIds = getResources().getIntArray(R.array.groupTab_ids);
        groupTabBtnArr = new ArrayList<>();

        int btnPosition = 0;

        while (itr.hasNext()) {
            list = itr.next();
            String groupNameKey = list.getGroupName();
            String menuId = list.getMenuId();
            String isAutoList = list.getAutoListYn();

            if (!(groupNameKey.isEmpty()) && !(groupNames.contains(groupNameKey))) {
                initGroupTabItem(list.getGroupName(), btnPosition);

                btnPosition++;
                if (mMenuIDParam.isEmpty() && groupNames.isEmpty()) {
                    view.findViewById(groupIds[0]).setSelected(true);
                    SELECTED_MENU_KEY = groupNameKey;
                    mMenuID = menuId;
                    mIsAutoList = isAutoList;
                } else {
                    String groupName = "";
                    for (int index = 0; index < menuArrays.size(); index++) {
                        if (mMenuIDParam.equalsIgnoreCase(menuArrays.get(index).getMenuId())) {
                            groupName = menuArrays.get(index).getGroupName();
                            break;
                        }
                    }
                    if (!groupName.isEmpty() && groupName.equalsIgnoreCase(groupNameKey)) {
                        SELECTED_MENU_KEY = groupNameKey;
                        mMenuID = mMenuIDParam;
                        mIsAutoList = isAutoList;
                        groupTabBtnArr.get(btnPosition - 1).performClick();
                    }
                }
                groupNames.add(list.getGroupName());
            }
        }
        if (btnPosition == 0) {
            groupTabContainer.setVisibility(View.GONE);
            mMenuID = mMenuIDParam;
            drawMenuTab();
        }
    }


    /**
     * 최상단 groupTab에서 각 item을 그려준다
     *
     * @param groupName
     * @param position
     */
    private void initGroupTabItem(String groupName, int position) {
        Button btn = new Button(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                (int) DpiUtil.convertDpToPixel(mContext, 36), 1);

        btn.setText(groupName);

        if (position < MAX_TAB_SIZE)
            btn.setId(groupIds[position]);

        btn.setPadding(0, 0, 0, 0);
        btn.setBackground(ContextCompat.getDrawable(mContext, R.drawable.drw_round_select_btn));
        btn.setTextSize(14f);
        btn.setTextColor(ContextCompat.getColorStateList(mContext, R.color.tab_select_text_color));
        btn.setStateListAnimator(null);
        if (SELECTED_MENU_KEY == groupName) {
            btn.setSelected(true);
        } else {
            btn.setSelected(false);
        }
        btn.setLayoutParams(params);
        btn.setOnClickListener(v -> {
            v.setSelected(true);
            SELECTED_MENU_KEY = ((Button) v).getText().toString();
            drawMenuTab();
            for (Button button : groupTabBtnArr) {
                if (!button.equals(v))
                    button.setSelected(false);
            }
        });

        groupTabBtnArr.add(btn);
        groupTabContainer.addView(btn);
    }

    /**
     * MenuName params 기반하여 menu Tab을 그려줌
     */
    private void drawMenuTab() {
        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;

        ArrayList<MenuVO> listMenuItems = new ArrayList<>();
        menuItems = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && !list.getMenuName().isEmpty() && list.getFilterTabOption().equalsIgnoreCase("")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                item.setChildList(findOutChildList(list.getMenuId()));
                menuItems.add(item);

            }
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && !list.getMenuName().isEmpty() && list.getFilterTabOption().equalsIgnoreCase("list")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                listMenuItems.add(item);
            }
        }

        if (!menuItems.isEmpty()) {
            tabAppbar.setVisibility(View.VISIBLE);
            menuTabLayout.setVisibility(View.VISIBLE);

            resetMenuTab();
            setupMenuTabItem();
            setupTabMode();
            setupMenuTabListener();
        } else {
            tabAppbar.setVisibility(View.GONE);
            menuTabLayout.setVisibility(View.GONE);
        }


        if (!listMenuItems.isEmpty()) {
            if (mMenuIDParam.isEmpty()) {
                drawChildList(listMenuItems);
            } else {
                if (mMenuIDParam.equalsIgnoreCase(mMenuID)) {
                    drawChildList(listMenuItems);
                }else{
                    hideAuthListView();
                }
            }
        } else {
            if (!mIsAutoList.isEmpty() && mIsAutoList.equalsIgnoreCase("Y")) {
                if (mMenuIDParam.isEmpty()) {
                    shimmerStart();
                    viewModel.call_IF_103(mContext,mUserId, mSysId, mMenuID, "");
                    //getSystemMenuData();
                }
            } else {
                //Todo autoList가 아닌 경우 List 그려주기 위한 처리 필요
                //menuid에 따라 권한목록을 표시
                viewAuthListView();
            }
        }
    }

    //권한목록 뷰를 붙인다.
    private void viewAuthListView(){
        shimmerStop();
        if ("S06".equals(mMenuID) || "S07".equals(mMenuID) || "S08".equals(mMenuID) || "S09".equals(mMenuID)) {
            //기존 동적 레이아웃 지움
            contentsRecyclerView.setVisibility(View.GONE);
            mEmptyListView.setVisibility(View.GONE);
            childTabContainer.setVisibility(View.GONE);

            //권한 뷰를 붙인다.
            if (authListView == null) {
                authListView = new ServiceDeskAuthListView(mContext);
                authListView.setInterface(this);
                authListView.setData(mMenuID, mSysId);
                mCustomLayout.removeAllViews();
                mCustomLayout.addView(authListView);
            }else{
                if(mCustomLayout.getChildCount() == 0){
                    mCustomLayout.addView(authListView);
                }
                authListView.setTab(mMenuID);
            }
            mCustomLayout.setVisibility(View.VISIBLE);
        }else{
            hideAuthListView();
        }
    }
    private void hideAuthListView(){
        mCustomLayout.removeAllViews();
        mCustomLayout.setVisibility(View.GONE);
        if (authListView != null) {
            authListView.resetData();
        }
    }

    private void setupTabMode() {
        if (menuItems.size() < 6) {
            menuTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            menuTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }
    }

    private void setupMenuTabItem() {
        int postion = 0;
        int count = 0;
        for (MenuVO item : menuItems) {
            TabLayout.Tab tab = menuTabLayout.newTab();
            tab.setText(item.getMenuName());
            //if ("Y".equals(item.getBadgeYn()))
            setupTabBadge(tab, item.getCountNum(), item.getBadgeYn());

            if (mMenuIDParam.equalsIgnoreCase(item.getMenuId())) {
                postion = count;
            }
            menuTabLayout.addTab(tab);
            count++;
        }
        if (!menuItems.isEmpty() && mMenuIDParam.isEmpty()) {
            mMenuID = menuItems.get(0).getMenuId();
            mIsAutoList = menuItems.get(0).getAutoListYN();
        } else {
            final int index = postion;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    menuTabLayout.getTabAt(index).select();
                }
            }, 100);

        }
    }

    private void setupTabBadge(TabLayout.Tab tab, String cnt, String badgeYn) {
        if (!cnt.isEmpty() && !cnt.equals("0") && badgeYn.equals("Y")) {
            BadgeDrawable badge = tab.getOrCreateBadge();
            badge.setVisible(true);
            badge.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tomato));
            badge.setNumber(Integer.parseInt(cnt));
            badge.setVerticalOffset(37);
            badge.setHorizontalOffset(-10);
            badge.setBadgeGravity(badge.BOTTOM_END);

            tab.setText(tab.getText().toString().trim() + " ");

        } else  {
            BadgeDrawable badge = tab.getOrCreateBadge();
            badge.setVisible(false);

            tab.setText(tab.getText().toString().trim());
        }
    }

    private void isDrawChildMenu(int position) {
        MenuVO item = menuItems.get(position);
        if (item.isExistChild()) {
            switch (item.checkChildViewType()) {
                case "menu":
                    drawChildMenu(item.getChildList());
                    break;
                case "list": {
                    ArrayList<MenuVO> childItems = refactorChildListToMenuItem(item);
                    drawChildList(childItems);
                    break;
                }
            }
        } else {
            AUTO_DETAIL_FLAG = item.getAutoDetailYN();
            mMenuID = item.getMenuId();
            mIsAutoList = item.getAutoListYN();
            if (!mIsAutoList.isEmpty() && mIsAutoList.equalsIgnoreCase("Y")) {
//                if(!TextUtils.isEmpty(mMenuIDParam) && !mMenuIDParam.equalsIgnoreCase(mMenuID)) {
//                    mMenuID= mMenuIDParam;
//                    mMenuIDParam = "";
//                }
                shimmerStart();
                viewModel.call_IF_103(mContext,mUserId, mSysId, mMenuID, "");
                //getSystemMenuData();
            } else {
                //Todo autoList가 아닌 경우 List 그려주기 위한 처리 필요
                viewAuthListView();
            }
            removeChildMenu();
        }
    }

    private ArrayList<MenuVO> refactorChildListToMenuItem(MenuVO menuVo) {
        ArrayList<MenuVO> menuItems = new ArrayList<>();
        for (Res_AP_IF_102_VO.result.menuArray list : menuVo.getChildList()) {
            menuItems.add(new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(), list.getAutoListYn()));
        }
        return menuItems;
    }

    private void drawChildMenu(ArrayList<Res_AP_IF_102_VO.result.menuArray> childList) {
        childTabContainer.setVisibility(View.VISIBLE);
        childTabContainer.removeAllViews();

        ChildOptionMenuView view = new ChildOptionMenuView(mContext);

        view.setOnMenuItemClickListener(new ChildOptionMenuView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(String menuItem) {
                //TODO : 1. Dialog 생성, menuItem --> childList를 Dialog로 넘김
                //       2. Dialog에서 목록 보여준다음 클릭한 텍스트를 가져옴
                //       3. 지정 항목에서 Interface 재호출
            }
        });


        childTabContainer.addView(view);
    }

    private void drawChildList(ArrayList<MenuVO> childItems) {
        childTabContainer.setVisibility(View.VISIBLE);
        childTabContainer.removeAllViews();

        mChildOptionListView = new ChildOptionListView(mContext);
        mChildOptionListView.setMenuItems(childItems);

        mChildOptionListView.drawView(v -> {

            //권한쪽 레이아웃을 없앤다.
            hideAuthListView();

            String mName = ((TextView) v).getText().toString();

            mChildOptionListView.setTextColorLoop(v);
            mChildOptionListView.setSELECTED_MENU_KEY(mName);
            mChildOptionListView.setCountNumText(mName);
            if (mMenuIDParam.isEmpty()) {
                mChildOptionListView.setINIT_INDEX(0);
                MenuVO vo = mChildOptionListView.getVoInfo(mChildOptionListView.getSELECTED_MENU_KEY());
                if (null != vo) {
                    AUTO_DETAIL_FLAG = vo.getAutoDetailYN();
                    mMenuID = vo.getMenuId();
                    mIsAutoList = vo.getAutoListYN();
                }
            } else {
                for (int index = 0; index < childItems.size(); index++) {
                    if (mMenuIDParam.equalsIgnoreCase(childItems.get(index).getMenuId())) {
                        mChildOptionListView.setINIT_INDEX(index);
                        AUTO_DETAIL_FLAG = childItems.get(index).getAutoDetailYN();
                        mIsAutoList = childItems.get(index).getAutoListYN();
                        mMenuID = mMenuIDParam;
                        break;
                    }
                }
                mMenuIDParam = "";
            }

            if (!mIsAutoList.isEmpty() && mIsAutoList.equalsIgnoreCase("Y")) {
                if (!isDuplicateCall) {
                    isDuplicateCall = true;
                    shimmerStart();
                    viewModel.call_IF_103(mContext,mUserId, mSysId, mMenuID, "");
                    //getSystemMenuData();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isDuplicateCall = false;

                        }
                    }, DUPLICATE_TIME);
                }
            } else {
                //Todo autoList가 아닌 경우 List 그려주기 위한 처리 필요
            }
        });

        childTabContainer.addView(mChildOptionListView);

    }


    private void resetMenuTab() {
        menuTabLayout.removeAllTabs();
        menuTabLayout.clearOnTabSelectedListeners();
    }

    /**
     * menuTabListener 연결,
     * TabLayout이 최초 1회생성시 클릭이벤트로 안넘어 가기 때문에 onTabSelected를 통해 강제 이벤트 발생
     */
    private void setupMenuTabListener() {
        TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //TODO : 1. CALL NETWORK --> viewModel에서 인터페이스 호출
                //       2. viewModel안의 rvLiveData.setValue(Network.response)
                //       3. bindingObserver () 에서 자동으로 업데이트됨
                isDrawChildMenu(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
//        listener.onTabSelected(menuTabLayout.getTabAt(menuTabLayout.getSelectedTabPosition()));
        menuTabLayout.addOnTabSelectedListener(listener);

    }


    private void removeChildMenu() {
        childTabContainer.removeAllViews();
        childTabContainer.setVisibility(View.GONE);
    }

    /**
     * parentId에 따라 하단 메뉴 구성요소를 새로 뽑아주는 Method
     * @param parentId
     * @return ArrayList<Res_AP_IF_102_VO.result.menuArray>
     */
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> findOutChildList(String parentId) {
        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;
        ArrayList<Res_AP_IF_102_VO.result.menuArray> childList = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            if (list.getParentMenuId().equals(parentId)) {
                childList.add(list);
            }
        }

        return childList;
    }

    /**
     * 서버에서 메뉴 데이터를 가져온다.
     */
    public void getSystemMenuData() {

        HashMap hm = new HashMap();
        hm.put("userId", mUserId);
        hm.put("sysId", mSysId);//시스템 ID
        hm.put("menuId", "");//menu ID
        hm.put("deptId", mDeptId);

        //((MainActivity) mContext).showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getSystemMenu(hm, new NetworkPresenter.getSystemMenuResult() {

            @Override
            public void onResponse(Res_AP_IF_102_VO result) {

                String errMsg = "";
                Log.d(TAG, "#### getSystemMenu:" + new Gson().toJson(result));
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            menuArrays = result.getResult().getMenuArray();
                            updateCount();
                            return;
                        } else {
                            errMsg = result.getResult().getErrorMsg();
                        }
                    } else {
                        errMsg = getResources().getString(R.string.txt_network_error);
                    }
                } else {
                    errMsg = getResources().getString(R.string.txt_network_error);
                }
                //((MainActivity) mContext).hideProgressBar();
                Toast.makeText(mContext, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCount() {
        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;
        ArrayList<MenuVO> listMenuItems = new ArrayList<>();
        menuItems = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && list.getFilterTabOption().equalsIgnoreCase("")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                item.setChildList(findOutChildList(list.getMenuId()));
                menuItems.add(item);
            }
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && /*list.getParentMenuId().isEmpty() &&*/  list.getParentMenuId().equalsIgnoreCase(mMenuID) && list.getFilterTabOption().equalsIgnoreCase("list")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                listMenuItems.add(item);
            }
        }
        if (!menuItems.isEmpty()) {
            for (int index = 0; index < menuItems.size(); index++) {
                TabLayout.Tab tab = menuTabLayout.getTabAt(index);
                tab.setText(menuItems.get(index).getMenuName());
                setupTabBadge(tab, menuItems.get(index).getCountNum(), menuItems.get(index).getBadgeYn());
            }
        }
        if (!listMenuItems.isEmpty()) {
            mChildOptionListView.setMenuItems(listMenuItems);
            mChildOptionListView.updateCount(mMenuID);
        }
    }


    //텍스트 사이즈의 변경
    public void changeTextSize(){
        //권한 목록
        if(mCustomLayout.getVisibility() == View.VISIBLE) {
            if (authListView != null) {
                authListView.textSizeAdj();
            }
        }else{
            contentsAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 권한 목록에서 아이템을 클릭
     * @param list 클릭한 아이템의 docId(docNo)
     * */
    @Override
    public void onSelectItem(ArrayList<Res_AP_IF_037_VO.result.aprList> list, int position) {
        //권한 상세

        //Toast.makeText(mContext, "권한 상세로 이동\n doc no:"+docId, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent((MainActivity) mContext, DynamicDetailActivity.class);

        intent.putExtra("position", position);
        intent.putExtra("userId", mUserId);
        intent.putExtra("sysId", mSysId);
        intent.putExtra("sysNm", mSysNm);
        intent.putExtra("menuId", mMenuID);
        intent.putExtra("docId", list.get(position).getDocNo());
        /*
        intent.putExtra("param01", item.getParam01());
        intent.putExtra("param02", item.getParam02());
        intent.putExtra("param03", item.getParam03());
        intent.putExtra("param04", item.getParam04());
        intent.putExtra("param05", item.getParam05());
        */
        intent.putExtra("listcount", list.size());

        ArrayList<Res_AP_IF_103_VO.dynamicListList> dynamicListLists = new ArrayList<>();
        for (Res_AP_IF_037_VO.result.aprList listVo : list) {
            Res_AP_IF_103_VO.dynamicListList dynamicListList = new Res_AP_IF_103_VO.dynamicListList();
            dynamicListList.setDocId(listVo.getDocNo());
            dynamicListLists.add(dynamicListList);
        }

        intent.putExtra("object", dynamicListLists);

        //startActivity(intent);
        if (activityMoveOkCheck) {
            return;
        }
        activityMoveOkCheck = true;

        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode:" + requestCode + "  resultCode:" + resultCode);

        if (resultCode == Activity.RESULT_OK) {//결재 처리가 된경우만 리프레시 한다.
            //shimmerStart();
            //viewModel.call_IF_103(mContext, mUserId, mSysId, mMenuID, "");
            //권한 목록 리프레시
            if(mCustomLayout.getVisibility() == View.VISIBLE) {
//                ((MainActivity) mContext).updateBadgeCnt(mSysId, "");
//                if (authListView != null) {
//                    authListView.setData(mMenuID);
//                }
                authListRefresh(authListView.getUserId());
            }
        }
    }

    //처리 후 서비스데스크 권한 리스트 갱신
    public void authListRefresh(String pUserId) {
        ((MainActivity) mContext).updateBadgeCnt(mSysId, "S06", pUserId);
        if (authListView != null) {
            //authListView.setData(mMenuID);
            authListView.setTab(mMenuID);
        }
    }

    //상세 처리 후 동적 리스트 갱신
    public void listRefresh() {
        ((MainActivity) mContext).updateBadgeCnt(mSysId, "", "");
        shimmerStart();
        viewModel.call_IF_103(mContext, mUserId, mSysId, mMenuID, "");
    }


    private void detailServiceAuth(String docId) {

        /*
        Intent intent = new Intent(mContext, DynamicDetailActivity.class);

        intent.putExtra("position", getAdapterPosition());
        intent.putExtra("userId", mUserId);
        intent.putExtra("sysId", mSysId);
        intent.putExtra("sysNm", mSysNm);
        intent.putExtra("menuId", mMeunId);
        intent.putExtra("docId", item.getDocId());
        intent.putExtra("param01", item.getParam01());
        intent.putExtra("param02", item.getParam02());
        intent.putExtra("param03", item.getParam03());
        intent.putExtra("param04", item.getParam04());
        intent.putExtra("param05", item.getParam05());
        intent.putExtra("listcount", list.size());
        intent.putExtra("object", list);
        //             itemView.getContext().startActivity(intent);
        if (mContext instanceof Activity) {
            ((Activity) mContext).startActivityForResult(intent, 100);
        }

         */
    }

    private void shimmerStart() {
        contentsRecyclerView.setVisibility(View.GONE);
        mEmptyListView.setVisibility(View.GONE);

        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    private void shimmerStop() {
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }

    public void updateTab(){
        menuArrays = ((MainActivity)mContext).getMenuArray();
        menuArrays = redividingMenu(mSysId); //List 메뉴에 따른 새로운 저장

        Iterator<Res_AP_IF_102_VO.result.menuArray> itr = menuArrays.iterator();
        Res_AP_IF_102_VO.result.menuArray list;

        ArrayList<MenuVO> listMenuItems = new ArrayList<>();
        menuItems = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && !list.getMenuName().isEmpty() && list.getFilterTabOption().equalsIgnoreCase("")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                item.setChildList(findOutChildList(list.getMenuId()));
                menuItems.add(item);

            }
            if (list.getGroupName().equals(SELECTED_MENU_KEY) && !list.getMenuName().isEmpty() && list.getFilterTabOption().equalsIgnoreCase("list")) {
                MenuVO item = new MenuVO(list.getSysId(), list.getMenuId(), list.getMenuName(), list.getCountNum(), list.getBadgeYn(), list.getAutoDetailYn(),list.getAutoListYn());
                listMenuItems.add(item);
            }
        }


        //탭 update
        if (!menuItems.isEmpty()) {
           //menuItems.get(0).setCountNum("99");

            for (int i = 0; i < menuItems.size(); i++) {
                if ("Y".equals(menuItems.get(i).getBadgeYn())) {
                    TabLayout.Tab tab = menuTabLayout.getTabAt(i);
                    setupTabBadge(tab, menuItems.get(i).getCountNum(), menuItems.get(i).getBadgeYn());
                }
            }
        }

        //필터 탭 update
        if (!listMenuItems.isEmpty()){
            //listMenuItems.get(0).setCountNum("99");

            mChildOptionListView.setMenuItems(listMenuItems);
            mChildOptionListView.updateCount(mMenuID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityMoveOkCheck = false;
    }

}