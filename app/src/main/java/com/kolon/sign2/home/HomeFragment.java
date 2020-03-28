package com.kolon.sign2.home;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * 홈 (KOLON 결재)
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private Context mContext;

    /**
     * row view의 종류
     * type 1: 전체 미승인건수 표시
     * type 2: 항목전체 건수(전자결재 , 서비스테스크, 출입관리, 차량관리)
     * type 3: 각 항목에 관한 로우 4가지 타입 (3전자결재 , 4서비스테스크, 5출입관리, 6차량관리)
     * type 7: 더보기
     * 총7개
     */
    public static final int HOME_TOP_TOTAL = 0;
    public static final int HOME_CATEGORY = 1;
    public static final int HOME_DATA = 2;
    public static final int HOME_MORE_VIEW = 3;
    public static final int HOME_HIDE_VIEW = 4;

    private ArrayList<Res_AP_IF_101_VO.result.sysArray> sysArrays;
    private ArrayList<Res_AP_IF_002_VO.result.APPROVAL_LIST> data;
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> menuData;
    private RecyclerView lv_home;
    private HomeViewListApater adapter;
    private Button btn_home_top;
    private LinearLayout no_data;

    private ShimmerFrameLayout mShimmerLayout;

    public static HomeFragment newInstance(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray, ArrayList<Res_AP_IF_101_VO.result.sysArray> sysArrays) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable("menuArray", menuArray);
        args.putSerializable("sysArrays", sysArrays);
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
            menuData = (ArrayList<Res_AP_IF_102_VO.result.menuArray>) getArguments().getSerializable("menuArray");
            sysArrays = (ArrayList<Res_AP_IF_101_VO.result.sysArray>) getArguments().getSerializable("sysArrays");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.home_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
                setData();
            }
        });

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);

        btn_home_top = (Button) v.findViewById(R.id.btn_home_top);
        btn_home_top.setOnClickListener(this);
        btn_home_top.setVisibility(View.GONE);

        no_data = (LinearLayout) v.findViewById(R.id.no_data);
        no_data.setVisibility(View.GONE);
        lv_home = (RecyclerView) v.findViewById(R.id.lv_home);

        setData();

        return v;
    }


    private void setData() {
        shimmerStart();
        //showProgressBar();
        //server에서 데이터를 가져옴 - AP_IF_002
//        userId		사용자아이디			필수
//        pageNum		페이지번호	페이지번호	페이지번호
//        pageCnt		페이지건수	페이지건수	페이지건수		3
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(mContext);
        String userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        String pageNum = "";
        String pageCnt = "";// 3건 -> 더보기를 위해 사실상 모든 페이지를 가져와야 함.
        HashMap hm = new HashMap();
        //userId = "jisun";//??test //test
        hm.put("userId", userId);
        hm.put("pageNum", pageNum);
        hm.put("pageCnt", pageCnt);

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getMainList(hm, new NetworkPresenter.getMainListResult() {
            @Override
            public void onResponse(Res_AP_IF_002_VO result) {
                //hideProgressBar();
                ((MainActivity) mContext).shimmerStop();
                shimmerStop();
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            int totalCnt = 0;
                            if (result.getResult().getApprovalList() == null || result.getResult().getApprovalList().size() == 0) {
                                //데이터 없음
                                lv_home.setVisibility(View.GONE);
                                no_data.setVisibility(View.VISIBLE);
                                totalCnt = 0;
                            } else {
                                lv_home.setVisibility(View.VISIBLE);
                                no_data.setVisibility(View.GONE);
                                totalCnt = result.getResult().getApprovalList().size();//추후는 데이터를 줄 예정

                                drawBottomList(result.getResult().getApprovalList(), totalCnt);
                            }
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
                viewMessage(errMsg, false);
            }
        });

    }

    private void drawBottomList(ArrayList<Res_AP_IF_002_VO.result.APPROVAL_LIST> readData, int totalCount) {
        //top button
//        lv_home.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                switch (newState) {
//                    case RecyclerView.SCROLL_STATE_IDLE:
//                        if (recyclerView.computeVerticalScrollOffset() == 0) {
//                            btn_home_top.setVisibility(View.GONE);
//                        } else {
//                            btn_home_top.setVisibility(View.VISIBLE);
//                        }
//                        break;
//                    default:
//                        btn_home_top.setVisibility(View.GONE);
//                        break;
//                }
//            }
//        });

        /*
         * 받아온 데이터의 가공
         * 1.최상단 전체건수 row data를 먼저 만들어 붙임
         * 2.데이터 형식의 목록을 센후 각각 목록 건수를 붙임 (type:HOME_CATEGORY) - ex)전자결재 5건
         * 3.3개이상이면 더보기 row data를 만들어 붙임 type:HOME_MORE_VIEW) , 4번째 항목부터 isView = false 처리함 ex)전자결재가 5건이면 3건까지는 isView =true 4,5번은 false
         * 4. 2의 항으로 돌아가 반복
         * */

        LinkedHashMap<String, String> hm = new LinkedHashMap();
        for (int i = 0; i < sysArrays.size(); i++) {
            hm.put(sysArrays.get(i).getSysId(), sysArrays.get(i).getSysName());
        }
        ArrayList<String> categoryCount = new ArrayList();//각항목 총합 저장용
        data = new ArrayList<>();
        int num = 3; //초기 보여주는 건수

        //일단 0건은 아니기 때문에 최상단 총건수 로우를 넣는다
        Res_AP_IF_002_VO.result.APPROVAL_LIST info = new Res_AP_IF_002_VO().new result().new APPROVAL_LIST();
        info.isView = true;
        info.type = HOME_TOP_TOTAL;
        info.totalCnt = String.valueOf(totalCount);
        data.add(info);

        int cnt = 0;
        for (int i = 0; i < sysArrays.size(); i++) {
            String sysId = sysArrays.get(i).getSysId();
            boolean isTitle = true;
            cnt = 0;
            for (int j = 0; j < readData.size(); j++) {
                if (sysId.equals(readData.get(j).getSysId())) {
                    //타이틀 제작
                    if (isTitle) {
                        isTitle = false;
                        info = new Res_AP_IF_002_VO().new result().new APPROVAL_LIST();
                        info.type = HOME_CATEGORY;
                        info.isView = true;
                        info.setSysId(sysId);
                        info.setTitle(hm.get(sysId));
                        data.add(info);
                    }
                    cnt++;
                    //3건 이하만 표시한다
                    if (cnt <= num) {
                        readData.get(j).isView = true;
                    } else {
                        readData.get(j).isView = false;
                    }
                    readData.get(j).type = HOME_DATA;
                    data.add(readData.get(j));
                }
            }
            //3개 이상이면 더보기를 만들어 붙인다
            if (cnt > 3) {
                info = new Res_AP_IF_002_VO().new result().new APPROVAL_LIST();
                info.type = HOME_MORE_VIEW;
                info.setSysId(sysId);
                info.isView = true;
                data.add(info);
            }
            //나중에 각 항목의 총합 cnt표시용
            if (cnt != 0) {
                categoryCount.add(String.valueOf(cnt));
            }
        }

        //각 항목의 총합을 넣기 위한 작업
        cnt = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).type == HOME_CATEGORY) {
                data.get(i).totalCnt = categoryCount.get(cnt);
                cnt++;
            }
        }

        adapter = new HomeViewListApater(data);
        lv_home.setAdapter(adapter);
        adapter.setInterface(new HomeViewListApater.HomeViewListTabClickListener() {
            @Override
            public void selectPosition(int type, int position) {
                String sysId = data.get(position).getSysId();
                if (sysId == null) sysId = "";

                switch (type) {
                    case HOME_TOP_TOTAL:// not used
                        break;
                    case HOME_CATEGORY:
                        //해당 목록 부분으로 이동
                        ((MainActivity) mContext).selectFragment(sysId);
                        break;
                    case HOME_DATA:
                        if ("sign".equals(sysId)) {
                            //전자 결재일경우 권한체크 : isPublic=N 이고 inline=N 인 경우 열람 불가 팝업
                            if ("N".equalsIgnoreCase(data.get(position).getInLine()) && "N".equalsIgnoreCase(data.get(position).getIsPublic())) {
                                viewMessage(mContext.getResources().getString(R.string.txt_approval_no_auth), false);
                            } else {
                                //해당 데이터 부분으로 이동 - 단 스와이프는 없고 디테일로 직접 들어간다
                                ((MainActivity) mContext).gotoDetail(sysId, data.get(position));
                            }
                        } else {
                            ((MainActivity) mContext).gotoDetail(sysId, data.get(position));
                        }
                        break;
                    case HOME_MORE_VIEW:
                        //해당 포지션의 view를 false시키고 해당 sysId 데이터의 isView부분을 모두 true로 바꾸어준다.
                        for (int i = 0; i < data.size(); i++) {
                            if (sysId.equals(data.get(i).getSysId())) {
                                data.get(i).isView = true;
                            }
                        }
                        data.get(position).isView = false;//더보기는 없앤다.
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void viewMessage(String msg, boolean finish) {
        ((MainActivity) mContext).viewMessage(msg, finish);
    }

    /*
    private void showProgressBar() {
        ((MainActivity) mContext).showProgressBar();
    }

    private void hideProgressBar() {
        ((MainActivity) mContext).hideProgressBar();
    }
    */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_home_top:
                lv_home.scrollToPosition(0);
                break;
        }
    }

    public void changeTextSize() {
        adapter.notifyDataSetChanged();
    }

    private void shimmerStart() {
        //Toast.makeText(mContext, "shimerStart", Toast.LENGTH_LONG).show();
        lv_home.setVisibility(View.GONE);
        no_data.setVisibility(View.GONE);
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    private void shimmerStop() {
        //Toast.makeText(mContext, "shimerStop", Toast.LENGTH_LONG).show();
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }

}
