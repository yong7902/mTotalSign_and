package com.kolon.sign2.approval;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dialog.ListDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.Res_AP_IF_012_VO;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_020_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 전자결재
 */
public class ApprovalFragment extends Fragment implements View.OnClickListener, ApprovalDataAdapter.ListTabClickListener, ApprovalScrollTabView.OnTabListener {

    private final int REQ_DETAIL = 1234;
    private final int REQ_SEARCH = 1;

    private String TAG = ApprovalFragment.class.getSimpleName();
    private Context mContext;
    private Button btn_approval_personal_select;
    private Button btn_approval_depart_select;

    private RelativeLayout lay_depart_tab;
    private TextView txt_depart_tab;
    private TextView txt_depart_tab_cnt;
    private RecyclerView lv_approval;
    private ApprovalDataAdapter adapter;
    private LinearLayout no_data;

    private ApprovalScrollTabView tabView;

    private ArrayList<Res_AP_IF_013_VO.result.apprList> readData;
 //   private Res_AP_IF_013_VO readData;
    boolean isLoading = false;
    //  private Res_AP_IF_102_VO menuData;

    private ArrayList<Res_AP_IF_102_VO.result.menuArray> personalTabData;//개인결재함 탭데이터
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> departTabData;//부서결재함 탭데이터
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> depart_SubTabData;//부서결재함 메뉴 탭데이터
    private int sub_tab_position;//서브탭 위치
    private int menu_tab_position = 0; //메뉴탭 위치

    private String userId, deptId, companyCd, menuId;
    private HashMap<String, String> menuListReq;//목록 데이터 요청
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArrays;
   // private ArrayList<Res_AP_IF_012_VO.result.apprDeptList> apprDeptList;//문서함 목록 데이터 - 항목별 containerId, category, subQuery등을 가져옴

    private boolean activityMoveOkCheck;

    private String subTitle = "";//메뉴선택 좌측 타이틀표시 텍스트

    private SharedPreferenceManager mPref;

    private int pageSize = 20;//한번에 보여주는 사이즈
    private int pageNum = 0; //0, 1 ,2 ,3 이 아니라 0 20 40 등 시작페이지이고 pageSize만큼 더해저야함.
    private boolean loadingMore; //리스트 끝에서 추가 로딩여부

    private ShimmerFrameLayout mShimmerLayout;

    private RelativeLayout deptMenuTab;

    public static ApprovalFragment newInstance(String userId, String deptId, String companyCd, ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArrays, String menuId) {
        ApprovalFragment fragment = new ApprovalFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("deptId", deptId);
        args.putString("companyCd", companyCd);
        args.putSerializable("menuArrays", menuArrays);
    //    args.putSerializable("apprDeptList", apprDeptList);
        args.putString("menuId", menuId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mPref = SharedPreferenceManager.getInstance(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            deptId = getArguments().getString("deptId");
            companyCd = getArguments().getString("companyCd");
            menuId = getArguments().getString("menuId");
            //ArrayList<Res_AP_IF_102_VO.result.menuArray> temp = (ArrayList<Res_AP_IF_102_VO.result.menuArray>) getArguments().getSerializable("menuArrays");
            ArrayList<Res_AP_IF_102_VO.result.menuArray> temp = ((MainActivity)mContext).getMenuArray();
            menuArrays = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                if ("sign".equals(temp.get(i).getSysId())) {
                    menuArrays.add(temp.get(i));
                }
            }
      //      apprDeptList = (ArrayList<Res_AP_IF_012_VO.result.apprDeptList>) getArguments().getSerializable("apprDeptList");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.fragment_approval, container, false);

        btn_approval_personal_select = (Button) view.findViewById(R.id.btn_approval_personal_select);
        btn_approval_personal_select.setOnClickListener(this);
        btn_approval_personal_select.setSelected(true);
        btn_approval_depart_select = (Button) view.findViewById(R.id.btn_approval_depart_select);
        btn_approval_depart_select.setOnClickListener(this);

        tabView = (ApprovalScrollTabView) view.findViewById(R.id.tabview);
        tabView.setInterface(this);

        lay_depart_tab = (RelativeLayout)view.findViewById(R.id.lay_depart_tab);
        txt_depart_tab = (TextView)view.findViewById(R.id.txt_depart_tab);
        txt_depart_tab_cnt = (TextView)view.findViewById(R.id.txt_depart_tab_cnt);
        Button menuBtn = (Button)view.findViewById(R.id.btn_depart_tab_select_menu);
        menuBtn.setOnClickListener(this);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_layout);

        no_data = (LinearLayout) view.findViewById(R.id.no_data);
        no_data.setVisibility(View.GONE);

        lv_approval = (RecyclerView) view.findViewById(R.id.lv_approval);
        lv_approval.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int last = ((LinearLayoutManager) lv_approval.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                int totalCnt = lv_approval.getAdapter().getItemCount();

                if (last == totalCnt) {
                    if (loadingMore) {
                        getInputMapData(sub_tab_position);
                        getDataList("progress");
                    }
                }
            }
        });

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readData = new ArrayList<>();
                refreshLayout.setRefreshing(false);
                pageNum = 0;
                getInputMapData(sub_tab_position);
                getDataList("shimmer");
            }
        });

        //set data..
        personalTabData = new ArrayList<>();
        departTabData = new ArrayList<>();
        for (int i = 0; i < menuArrays.size(); i++) {
            if (!TextUtils.isEmpty(menuArrays.get(i).getMenuId()) && menuArrays.get(i).getMenuId().length() == 1) {
                //개인
                personalTabData.add(menuArrays.get(i));
            } else if (!TextUtils.isEmpty(menuArrays.get(i).getMenuId()) && menuArrays.get(i).getMenuId().length() == 3) {
                //부서
                departTabData.add(menuArrays.get(i));
            } else {
                //
            }
        }

        if (TextUtils.isEmpty(menuId)) {
            //초기 목록 데이터 요청
            menuListReq = getHashMapByCategory(personalTabData.get(0).getMenuId());
            selectPersonalTab();
        } else {
            //좌측 메뉴로 직접 들어올때
            menuListReq = getHashMapByCategory(menuId);
            //개인, 부서 구별
            if (menuId.length() == 1) {
                //개인
                int selectPos = 0;
                btn_approval_personal_select.setSelected(true);
                btn_approval_depart_select.setSelected(false);

                for (int i = 0; i < personalTabData.size(); i++) {
                    if (menuId.equals(personalTabData.get(i).getMenuId())) {
                        selectPos = i;
                        personalTabData.get(i).isSelected = true;
                    } else {
                        personalTabData.get(i).isSelected = false;
                    }
                }
                tabView.setData(personalTabData);// 0 개인
                tabView.setMove(selectPos);
                //sub_tab_position = i;
                sub_tab_position = selectPos;

                getDataList("shimmer");//데이터 요청

                //탭메뉴로 이동....
            } else {
                //부서중 서브의 처리...
                String id = menuId;
                boolean isSub = false;

                if (menuId.length() > 3) {
                    //서브
                    id = menuId.substring(0, 3);
                    isSub = true;
                }

                int selectPos = 0;
                btn_approval_personal_select.setSelected(false);
                btn_approval_depart_select.setSelected(true);
                for (int i = 0; i < departTabData.size(); i++) {
                    if (id.equals(departTabData.get(i).getMenuId())) {
                        departTabData.get(i).isSelected = true;
                        selectPos = i;
                    } else {
                        departTabData.get(i).isSelected = false;
                    }
                }

                tabView.setData(departTabData);// 1 부서
                tabView.setMove(selectPos);
                sub_tab_position = selectPos;

                if (isSub) {
                    depart_SubTabData = new ArrayList<>();
                    for (int i = 0; i < menuArrays.size(); i++) {
                        /**
                         * 서버데이터 확인 parentMenuId 로 구별
                         **/
                        String parentMenuId = menuArrays.get(i).getParentMenuId();
                        if (!TextUtils.isEmpty(parentMenuId)) {
                            if (menuArrays.get(i).getMenuId().length() > 3) {
                                //menuid가 4자이상
                                String readFirst = menuArrays.get(i).getMenuId().substring(0, 3);// 메뉴아이디 앞3자리를 읽어온다
                                Log.d(TAG, "#### sub:" + readFirst);
                                if (readFirst.equals(departTabData.get(selectPos).getMenuId())) {
                                    Log.d(TAG, "#### add sub:" + menuArrays.get(i).getMenuId());
                                    depart_SubTabData.add(menuArrays.get(i));

//                                    menuTabDataSet(selectPos, menuArrays.get(i).getMenuName());
//                                    if (menuId.equals(menuArrays.get(i).getMenuId())) {
//                                        menuTabDataSet(depart_SubTabData.indexOf(), menuArrays.get(i).getMenuName());
//                                    }
                                }
                                if (menuId.equals(menuArrays.get(i).getMenuId())) {
                                    //subTitle = menuArrays.get(i).getMenuName();
                                    String cnt = "";
                                    if ("Y".equals(menuArrays.get(i).getCountYn())) {
                                        cnt = menuArrays.get(i).getCountNum();
                                    }
                                    menuTabDataSet(depart_SubTabData.indexOf(menuArrays.get(i))+1, menuArrays.get(i).getMenuName(), cnt);
                                }
                            }
                        }
                    }
                }
                getDataList("shimmer");
            }
        }

        adapter = new ApprovalDataAdapter(readData);
        adapter.setInterface(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_approval.setLayoutManager(linearLayoutManager);
        lv_approval.setAdapter(adapter);

        return view;
    }

    private HashMap getHashMapByCategory(String category) {
//        String sub[] = category.split("_");
//        boolean isSubQuery = false;
//        if(sub.length == 2){
//            isSubQuery = true;
//        }

        String containerId = "";
        String subQuery = "";
        HashMap hm = new HashMap();

        for(int i=0;i<menuArrays.size();i++){
            if(category.equals(menuArrays.get(i).getMenuId())){
                containerId = menuArrays.get(i).getAttr01();
                subQuery = menuArrays.get(i).getAttr02();
                break;
            }
        }
//        if(isSubQuery){
//            for(int i=0;i<apprDeptList.size();i++){
//                if(sub[1].equals(apprDeptList.get(i).fixQuery)){
//                    containerId = apprDeptList.get(i).getContainerId();
//                    subQuery = apprDeptList.get(i).getSubQuery();
//                    break;
//                }
//            }
//        }

        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);

        hm.put("category", category);
        hm.put("userId", userId);
        hm.put("deptId", deptId);
        hm.put("containerId", containerId);
        hm.put("subQuery", subQuery);


        hm.put("pageNum", pageNum); //page num
        hm.put("pageCnt", pageSize);//page size

        pageNum = pageNum + pageSize;//처음0 그다음 pageSize만큼 증가 20 , 40, 60...
        return hm;
    }

    private void setData(ArrayList<Res_AP_IF_013_VO.result.apprList> data) {

        if (btn_approval_depart_select.isSelected()) {
            //메뉴선택부분 작업
            depart_SubTabData = new ArrayList<>();
            String menuName = "";
            for (int i = 0; i < menuArrays.size(); i++) {
                String category = menuListReq.get("category");
                /**
                 * 서버데이터 확인 parentMenuId 로 구별
                 * */
                String parentMenuId = menuArrays.get(i).getParentMenuId();
                if (!TextUtils.isEmpty(parentMenuId)) {
                    //parentMenuId가 있고 category가 4자이상이면 category도 잘라서 비교
                    if (category.equals(menuArrays.get(i).getMenuId())) {
                        menuName = menuArrays.get(i).getMenuName();
                    }

                    if (category.length() >= 3) {
                        category = category.substring(0, 3);
                    }

                    if (menuArrays.get(i).getMenuId().length() >= 3) {
                        //menuid가 4자이상
                        String readFirst = menuArrays.get(i).getMenuId().substring(0, 3);// 메뉴아이디 앞3자리를 읽어온다
                        Log.d(TAG, "sub:" + readFirst);
                        if (readFirst.equals(category)) {
                            Log.d(TAG, "add sub:" + menuArrays.get(i).getMenuId());
                            depart_SubTabData.add(menuArrays.get(i));
                        }
                    }
                }
            }

            if(pageNum == 0 || pageNum == pageSize) {
                if (depart_SubTabData != null && depart_SubTabData.size() != 0) {
                    Res_AP_IF_013_VO.result.apprList info = new Res_AP_IF_013_VO().new result().new apprList();
                    info.isHeader = true;

                    if (TextUtils.isEmpty(menuName)) {
                        info.setTitle(getResources().getString(R.string.txt_approval_tab_total));
                    } else {
                        info.setTitle(menuName);
                    }

                    //부서결재함일경우 맨앞에 헤더(메뉴선택)를 넣는다
                    /////readData.add(0, info);

                    lay_depart_tab.setVisibility(View.VISIBLE);
                }
                else {
                    lay_depart_tab.setVisibility(View.GONE);
                }
            }

        }

        adapter.setData(readData);
    }

    /**
     * 전자결재 리스트를 가져온다.
     */
    private void getDataList(String loadingAction) {

        if (loadingAction == "progress")
            ((MainActivity) mContext).showProgressBar();
        else
            shimmerStart();

        NetworkPresenter presenter = new NetworkPresenter();

        /**
         * 개인결재 미처리 기결 기안 완료 기타 - 4
         * 부서결재 미처리 진행 결재 발신 수신 협조 부결 - 7
         * */

        Log.d(TAG, "#### req data:" + menuListReq);

        presenter.getApprovalList(menuListReq, new NetworkPresenter.getApprovalListResult() {
            @Override
            public void onResponse(Res_AP_IF_013_VO result) {
                //stop progress
                if (loadingAction == "progress")
                    ((MainActivity) mContext).hideProgressBar();
                else
                    shimmerStop();

                String errMsg = "";
                menuId = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            loadingMore = true;
                            ArrayList<Res_AP_IF_013_VO.result.apprList> getApprList = result.getResult().getApprList();
                            if (getApprList == null || getApprList.size() == 0) {
                                if (pageNum == pageSize) {//맨처음
                                    //데이터가 없음
                                    readData = getApprList;
                                    loadingMore = false;
                                    no_data.setVisibility(View.VISIBLE);

                                    if(depart_SubTabData != null){
                                        lv_approval.setVisibility(View.VISIBLE);
                                        ArrayList<Res_AP_IF_013_VO.result.apprList> arr = new ArrayList<>();
                                        result.getResult().setApprList(arr);
                                        setData(arr);
                                        readData = result.getResult().getApprList();
                                    }else{
                                        lv_approval.setVisibility(View.GONE);
                                        readData = null;
                                    }

                                } else {
                                    //리스트가 끝
                                    loadingMore = false;
                                    return;
                                }
                            }else{

                                if(getApprList.size() < pageSize){
                                    loadingMore = false;
                                }

                                if(readData == null ||  pageNum == pageSize){
                                    readData = new ArrayList<>();
                                }

                                readData.addAll(getApprList);

                                no_data.setVisibility(View.GONE);
                                lv_approval.setVisibility(View.VISIBLE);
                                setData(readData);
                            }

                            //stop progress
                            ((MainActivity) mContext).hideProgressBar();
                            return;
                        } else {
                            //error
                            errMsg = result.getResult().getErrorMsg();
                        }
                    } else {
                        //error
                        errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
                    }
                } else {
                    //error
                    errMsg = mContext.getResources().getString(R.string.txt_network_error);
                }

                no_data.setVisibility(View.VISIBLE);
                lv_approval.setVisibility(View.GONE);
                readData = null;


                //popup
                TextDialog dialog = TextDialog.newInstance("", errMsg, mContext.getResources().getString(R.string.txt_alert_confirm));
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(getFragmentManager());
            }
        });
    }

    private void selectPersonalTab() {
        lay_depart_tab.setVisibility(View.GONE);

        btn_approval_personal_select.setSelected(true);
        btn_approval_depart_select.setSelected(false);

        for (int i = 0; i < personalTabData.size(); i++) {
            personalTabData.get(i).isSelected = false;
        }
        personalTabData.get(0).isSelected = true;         //기본 미결함
        tabView.setData(personalTabData);// 0 개인
        sub_tab_position = 0;
        getInputMapData(0);
        getDataList("shimmer");
    }

    private void selectDepartTab() {
        lay_depart_tab.setVisibility(View.GONE);
        menuTabDataSet(0, mContext.getResources().getString(R.string.txt_approval_tab_total), "");

        btn_approval_personal_select.setSelected(false);
        btn_approval_depart_select.setSelected(true);
        for (int i = 0; i < departTabData.size(); i++) {
            departTabData.get(i).isSelected = false;
        }
        departTabData.get(0).isSelected = true;         //기본 미처리

        tabView.setData(departTabData);// 1 부서
        sub_tab_position = 0;
        getInputMapData(0);
        getDataList("shimmer");
    }

    //요청 데이터 map을 만든다
    private void getInputMapData(int position, int subPosition) {
        if (btn_approval_personal_select.isSelected()) {
            //개인
            menuListReq = getHashMapByCategory(personalTabData.get(position).getMenuId());
        } else {
            //부서
            depart_SubTabData = new ArrayList<>();
            for (int i = 0; i < menuArrays.size(); i++) {
                /**
                 * 서버데이터 확인 parentMenuId 로 구별
                 * */
                String parentMenuId = menuArrays.get(i).getParentMenuId();
                if (!TextUtils.isEmpty(parentMenuId)) {
                    if (menuArrays.get(i).getMenuId().length() >= 3) {
                        //menuid가 4자이상
                        String readFirst = menuArrays.get(i).getMenuId().substring(0, 3);// 메뉴아이디 앞3자리를 읽어온다
                        Log.d(TAG, "sub:" + readFirst);
                        if (readFirst.equals(departTabData.get(position).getMenuId())) {
                            Log.d(TAG, "add sub:" + menuArrays.get(i).getMenuId());
                            depart_SubTabData.add(menuArrays.get(i));
                        }
                    }
                }
            }

            if (subPosition == 0) {
                menuListReq = getHashMapByCategory(departTabData.get(position).getMenuId());
            } else {
                menuListReq = getHashMapByCategory(depart_SubTabData.get(subPosition - 1).getMenuId());
            }

        }
    }

    private void getInputMapData(int position) {
        //getInputMapData(position, 0);
        getInputMapData(position, menu_tab_position);
    }

    //search 결과
    public void gotoDetail(ArrayList<Res_AP_IF_020_VO.result.apprList> list, int position) {
        //기존 guid list를 바꾼후 position을 넘김
        ArrayList<Res_AP_IF_013_VO.result.apprList> newApprList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Res_AP_IF_013_VO.result.apprList data = new Res_AP_IF_013_VO().new result().new apprList();
            data.setCategory(list.get(i).getCategory());
            data.setIsPublic(list.get(i).getIsPublic());
            data.setInLine(list.get(i).getInLine());
            data.setHasopinionYn(list.get(i).getHasopinionYn());
            data.setHasattachYn(list.get(i).getHasattachYn());
            data.setStatus(list.get(i).getStatus());
            data.setPubDate(list.get(i).getPubDate());
            data.setAuthor(list.get(i).getAuthor());
            data.setTitle(list.get(i).getTitle());
            data.setGuid(list.get(i).getGuid());

            newApprList.add(data);
        }
        readData = new ArrayList<>();
        readData.addAll(newApprList);

   //     readData.getResult().setApprList(newApprList);
        selectPosition(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_approval_personal_select:
                //개인 결재함
                pageNum = 0;
                selectPersonalTab();
                break;
            case R.id.btn_approval_depart_select:
                //부서 결재함
                pageNum = 0;
                selectDepartTab();
                break;
            case R.id.btn_depart_tab_select_menu:
                //리스트 메뉴
                clickMenu();
                break;
        }
    }

    @Override
    public void selectPosition(int position) {
        //권한체크 : isPublic=N 이고 inline=N 인 경우 열람 불가 팝업
        if ("N".equalsIgnoreCase(readData.get(position).getInLine()) && "N".equalsIgnoreCase(readData.get(position).getIsPublic())) {
            //열람권없음 - 팝업
            TextDialog dialog = TextDialog.newInstance("", mContext.getResources().getString(R.string.txt_approval_no_auth), mContext.getResources().getString(R.string.txt_alert_confirm));
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show(getFragmentManager());
        } else {
            //열람권있음 - 이동
            //상세에서 이전,다음 결제 건을 바로 넘어가기 위해 guid컬럼 array를 만들어서 전달
            //guid id를 넘기고 해당 액티비티에서 리스트로 받아서 동일 아이디를 찾아서 이동.
            // Res_AP_IF_013_VO.result.apprList obj = readData.getResult().getApprList().get(position);
            Intent it = new Intent(mContext, ApprovalDetailActivity.class);

            ArrayList<String> guidList = new ArrayList<>();
            for (int j = 0; j < readData.size(); j++) {
                if(!readData.get(j).isHeader){
                    guidList.add(readData.get(j).getGuid());
                }
            }

            String containerId = "";
            String subQuery = "";
            String category = menuListReq.get("category");

            for(int i=0;i<menuArrays.size();i++){
                if(category.equals(menuArrays.get(i).getMenuId())){
                    containerId = menuArrays.get(i).getAttr01();
                    subQuery = menuArrays.get(i).getAttr02();
                    break;
                }
            }


//            if(category.length() >3){
//                String readsub[] = category.split("_");
//                if(readsub.length >1){
//                    for(int i=0;i<apprDeptList.size();i++){
//                        if(readsub[1].equals(apprDeptList.get(i).fixQuery)){
//                            containerId = apprDeptList.get(i).getContainerId();
//                            subQuery = apprDeptList.get(i).getSubQuery();
//                            break;
//                        }
//                    }
//                }
//            }

            it.putExtra("companyCd", companyCd);
            it.putExtra("guidList", guidList);
            it.putExtra("position", position);
            it.putExtra("userId", userId);
            it.putExtra("deptId", deptId);
            it.putExtra("containerId", containerId);
            it.putExtra("subQuery", subQuery);
            it.putExtra("category", category);
            it.putExtra("object", readData);

            if (activityMoveOkCheck) {
                return;
            }
            activityMoveOkCheck = true;
            startActivityForResult(it, REQ_DETAIL);
        }
    }


    //부서결재함 - 메뉴선택을 클릭
    @Override
    public void clickMenu() {
        if (depart_SubTabData == null) return;

        ArrayList<String> input = new ArrayList<>();
        input.add(mContext.getResources().getString(R.string.txt_approval_tab_total));//전체
        for (int i = 0; i < depart_SubTabData.size(); i++) {
            input.add(depart_SubTabData.get(i).getMenuName());
        }

        ListDialog dialog = ListDialog.newInstance();
        dialog.setTitle(getResources().getString(R.string.txt_approval_select_menu));
        dialog.setData(input);
        dialog.setInterface(new ListDialog.OnClickListener() {
            @Override
            public void selectPosition(int position) {

                pageNum = 0;
                if (position == 0) {
                    //yong79 아래 why??
                    //adapter.getAdapterData().get(0).setTitle(mContext.getResources().getString(R.string.txt_approval_tab_total));

                    menuTabDataSet(position, mContext.getResources().getString(R.string.txt_approval_tab_total), "");
                } else {
                    //yong79 아래 why??
                    //adapter.getAdapterData().get(0).setTitle(depart_SubTabData.get(position - 1).getMenuName());

                    String cnt = "";
                    if ("Y".equals(depart_SubTabData.get(position - 1).getCountYn())) {
                        cnt = depart_SubTabData.get(position - 1).getCountNum().toString();
                    }

                    menuTabDataSet(position, depart_SubTabData.get(position - 1).getMenuName(), cnt);
                }
                adapter.notifyItemChanged(0);
                getInputMapData(sub_tab_position, position);
                getDataList("shimmer");
            }
        });
        dialog.show(getFragmentManager());
    }

    public HashMap getServerInputData() {
        return menuListReq;
    }


    @Override
    public void onResume() {
        super.onResume();
        activityMoveOkCheck = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult requestCode:" + requestCode + "  resultCode:" + resultCode);

        if (resultCode == Activity.RESULT_OK) {//결재 처리가 된경우만 리프레시 한다.
            ((MainActivity) mContext).updateBadgeCnt("sign", "", "");
            getDataList("shimmer");
        }

        /*
        switch (requestCode) {
            case REQ_DETAIL:
                //재 로딩 - 실제로는 항목부터 로딩해야함..
                getDataList("shimmer");
                break;
            case REQ_SEARCH:
                break;
        }
        */
    }

    /**
     * 보조 메뉴(가로 스크롤) 탭 선택
     *
     * @param position 위치
     */
    @Override
    public void select(int position) {
        pageNum = 0;
        sub_tab_position = position;
        menu_tab_position = 0;

        getInputMapData(position);
        getDataList("shimmer");

        menuTabDataSet(0, mContext.getResources().getString(R.string.txt_approval_tab_total), "");
    }

    public void changeTextSize(){
        adapter.notifyDataSetChanged();
    }

    private void shimmerStart() {
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    private void shimmerStop() {
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }

    private void menuTabDataSet(int position, String title, String cnt) {
        menu_tab_position = position;
        txt_depart_tab.setText(title);
        txt_depart_tab_cnt.setText(cnt);
    }

    public void updateTab(){
        //전체 메뉴 데이터 갱신
        ArrayList<Res_AP_IF_102_VO.result.menuArray> temp = ((MainActivity)mContext).getMenuArray();
        menuArrays = new ArrayList<>();
        for (int i = 0; i < temp.size(); i++) {
            if ("sign".equals(temp.get(i).getSysId())) {
                menuArrays.add(temp.get(i));
            }
        }

        //스크롤 메뉴 탭 update
        personalTabData = new ArrayList<>();
        departTabData = new ArrayList<>();
        for (int i = 0; i < menuArrays.size(); i++) {
            if (!TextUtils.isEmpty(menuArrays.get(i).getMenuId()) && menuArrays.get(i).getMenuId().length() == 1) {
                //개인
                personalTabData.add(menuArrays.get(i));
            } else if (!TextUtils.isEmpty(menuArrays.get(i).getMenuId()) && menuArrays.get(i).getMenuId().length() == 3) {
                //부서
                departTabData.add(menuArrays.get(i));
            }
        }
        if (btn_approval_personal_select.isSelected()) {
            //개인
            tabView.setData(personalTabData);
        } else {
            //부서
            tabView.setData(departTabData);
        }

        //필터 메뉴 탭 update
        if (lay_depart_tab.getVisibility()==View.VISIBLE) {
            //메뉴선택부분 작업
            depart_SubTabData = new ArrayList<>();
            String menuName = "";
            for (int i = 0; i < menuArrays.size(); i++) {
                String category = menuListReq.get("category");

                String parentMenuId = menuArrays.get(i).getParentMenuId();
                if (!TextUtils.isEmpty(parentMenuId)) {
                    //parentMenuId가 있고 category가 4자이상이면 category도 잘라서 비교
                    if (category.equals(menuArrays.get(i).getMenuId())) {
                        menuName = menuArrays.get(i).getMenuName();
                    }

                    if (category.length() >= 3) {
                        category = category.substring(0, 3);
                    }

                    if (menuArrays.get(i).getMenuId().length() >= 3) {
                        //menuid가 4자이상
                        String readFirst = menuArrays.get(i).getMenuId().substring(0, 3);// 메뉴아이디 앞3자리를 읽어온다
                        Log.d(TAG, "sub:" + readFirst);
                        if (readFirst.equals(category)) {
                            Log.d(TAG, "add sub:" + menuArrays.get(i).getMenuId());
                            depart_SubTabData.add(menuArrays.get(i));
                        }
                    }
                }
            }
            if (menu_tab_position != 0) {
                txt_depart_tab_cnt.setText(depart_SubTabData.get(menu_tab_position-1).getCountNum());
            }
        }


    }
}