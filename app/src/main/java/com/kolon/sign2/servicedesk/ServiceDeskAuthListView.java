package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.CommentDialog;
import com.kolon.sign2.dialog.ListDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.Req_AP_IF_039_VO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_037_VO;
import com.kolon.sign2.vo.Res_AP_IF_040_VO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 서비스데스크 권한목록
 */
public class ServiceDeskAuthListView extends LinearLayout implements View.OnClickListener, ServiceDeskAuthListAdapter.OnSelectItem {

    private Context mContext;
    //    private RelativeLayout btnAuthTab1, btnAuthTab2, btnAuthTab3, btnAuthTab4;
//    private View autTabLine1, autTabLine2, autTabLine3, autTabLine4;
    private TextView tv_service_desk_all_check;
    private ImageView iv_service_desk_all_check;
    private TextView tv_service_desk_name;
    private TextView tv_title1;
    private String userId, menuId;
    private String selectCode;//D1 : 승인대기, D2 : 승인완료, D3 : 승인반려, D4 : 승인진행

    private ProgressBar progressBar;
    private NetworkPresenter presenter;

    private ArrayList<Res_AP_IF_040_VO.result.dgtnList> dgtnList;//위임자 목록
    private ArrayList<Res_AP_IF_037_VO.result.aprList> aprList;//권한목록
    private RecyclerView rv;
    private ServiceDeskAuthListAdapter adapter;

    private LinearLayout layout_all_select, layout_no_data, layout_bottom_button;
    private SharedPreferenceManager mPref;

    //페이징 처리 -----------------------------------
    boolean pagingProcess = true;//페이징처리여부
    int pageSize = 20;//한번에 보여주는 사이즈
    int pageNum = 0; //0, 1 ,2 ,3 이 아니라 0 20 40 등 시작페이지이고 pageSize만큼 더해저야함.
    boolean loadingMore; //리스트 끝에서 추가 로딩여부
    //-----------------------------------------------

    private OnAuthListOnItemClick mInterface;

    private ShimmerFrameLayout mShimmerLayout;

    public interface OnAuthListOnItemClick {
        //void onSelectItem(String docNo);
        void onSelectItem(ArrayList<Res_AP_IF_037_VO.result.aprList> list, int position);
    }

    public void setInterface(OnAuthListOnItemClick mInterface) {
        this.mInterface = mInterface;
    }

    public ServiceDeskAuthListView(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskAuthListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskAuthListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_service_desk_auth, this, false);
        mPref = SharedPreferenceManager.getInstance(mContext);
        presenter = new NetworkPresenter();
        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.btn_service_desk_cancel);
        cancel.setOnClickListener(this);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.btn_service_desk_confirm);
        ok.setOnClickListener(this);
        layout_bottom_button = (LinearLayout) v.findViewById(R.id.layout_bottom_button);
        layout_all_select = (LinearLayout) v.findViewById(R.id.layout_all_select);
        layout_no_data = (LinearLayout) v.findViewById(R.id.layout_no_data);
        //layout_no_data.setVisibility(View.VISIBLE);

        /*
        SwipeRefreshLayout refreshLayout = v.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);

                shimmerStart();
                setTab(this.menuId);
        });
        */

        rv = (RecyclerView) v.findViewById(R.id.rv_auth_list);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int last = ((LinearLayoutManager) rv.getLayoutManager()).findLastCompletelyVisibleItemPosition() + 1;
                int totalCnt = rv.getAdapter().getItemCount();

                if (last == totalCnt) {
                    if (pagingProcess && loadingMore) {
                        getAuthList(userId, selectCode, "progress");
                    }
                }
            }
        });

        adapter = new ServiceDeskAuthListAdapter(aprList);
        adapter.setInterface(this);
        rv.setAdapter(adapter);

//        btnAuthTab1 = (RelativeLayout) v.findViewById(R.id.btn_service_desk_auth_tap1);
//        btnAuthTab1.setOnClickListener(this);
//        btnAuthTab2 = (RelativeLayout) v.findViewById(R.id.btn_service_desk_auth_tap2);
//        btnAuthTab2.setOnClickListener(this);
//        btnAuthTab3 = (RelativeLayout) v.findViewById(R.id.btn_service_desk_auth_tap3);
//        btnAuthTab3.setOnClickListener(this);
//        btnAuthTab4 = (RelativeLayout) v.findViewById(R.id.btn_service_desk_auth_tap4);
//        btnAuthTab4.setOnClickListener(this);
//
//        autTabLine1 = (View) v.findViewById(R.id.btn_service_desk_auth_tap_line1);
//        autTabLine2 = (View) v.findViewById(R.id.btn_service_desk_auth_tap_line2);
//        autTabLine3 = (View) v.findViewById(R.id.btn_service_desk_auth_tap_line3);
//        autTabLine4 = (View) v.findViewById(R.id.btn_service_desk_auth_tap_line4);

        iv_service_desk_all_check = (ImageView) v.findViewById(R.id.iv_service_desk_all_check);
        iv_service_desk_all_check.setOnClickListener(this);
        tv_service_desk_all_check = (TextView) v.findViewById(R.id.tv_service_desk_all_check);
        tv_service_desk_all_check.setOnClickListener(this);

        LinearLayout btn_service_desk_name = (LinearLayout) v.findViewById(R.id.btn_service_desk_name);
        btn_service_desk_name.setOnClickListener(this);
        tv_service_desk_name = (TextView) v.findViewById(R.id.tv_service_desk_name);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);

        tv_title1 = (TextView) v.findViewById(R.id.tv_title1);

        CommonUtils.textSizeSetting(context, tv_title1);

        addView(v);
    }

    //맨처음 시작시 - 위임자 로딩후 하단리스트 로딩
    public void setData(String menuId) {
        pageNum = 0;
        this.menuId = menuId;
        //서버 또는 전단계에서 넘어온 데이터 - 위임자 목록
        /*
        String loginInfo = mPref.getStringPreference(Constants.PREF_LOGIN_IF_INFO);
        Res_AP_IF_004_VO.result.multiuserList user = new Gson().fromJson(loginInfo, new TypeToken< Res_AP_IF_004_VO.result.multiuserList>(){}.getType());
        userId = user.getUserId();
        String userNm = user.getUserName();
        */

        userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        String userNm = mPref.getStringPreference(Constants.PREF_USER_NAME);


        //?? testid/////////////////////////////
    //    userId = "cyj03";
        ////////////////////////////////////////////
        if ("S06".equals(menuId)) {
            selectCode = "D1";
            iv_service_desk_all_check.setSelected(false);
            layout_all_select.setVisibility(View.VISIBLE);
            layout_bottom_button.setVisibility(View.VISIBLE);
        } else if ("S07".equals(menuId)) {
            selectCode = "D2";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        } else if ("S08".equals(menuId)) {
            selectCode = "D3";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        } else if ("S09".equals(menuId)) {
            selectCode = "D4";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        }

        CommonUtils.changeTextSize(mContext, tv_title1);

        getAuthorizerList(userId, userNm);

    }

    //하단 리스트만 재로딩
    public void setTab(String menuId) {
        this.menuId = menuId;
        if ("S06".equals(menuId)) {
            selectCode = "D1";
        //    layout_all_select.setVisibility(View.VISIBLE);
        //    layout_bottom_button.setVisibility(View.VISIBLE);
        } else if ("S07".equals(menuId)) {
            selectCode = "D2";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        } else if ("S08".equals(menuId)) {
            selectCode = "D3";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        } else if ("S09".equals(menuId)) {
            selectCode = "D4";
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
        }
        pageNum = 0;
        aprList = new ArrayList<>();
        //adapter.notifyDataSetChanged();

        //adapter.setData(aprList, selectCode);
        //adapter.notifyItemMoved(0,adapter.getItemCount()-1);
        //adapter.notifyDataSetChanged();
        //adapter.refreshData();

        adapter = null;
        adapter = new ServiceDeskAuthListAdapter(aprList);
        adapter.setInterface(this);
        rv.setAdapter(adapter);

        getAuthList(userId, selectCode, "");
    }

    public void resetData() {
        aprList = new ArrayList<>();
        adapter.notifyDataSetChanged();
        layout_no_data.setVisibility(View.VISIBLE);
        layout_all_select.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.INVISIBLE);
    }

    //위임자목록
    private void getAuthorizerList(String userId, String userNm) {
        //progressBar.setVisibility(View.VISIBLE);
        shimmerStart();

        HashMap hm = new HashMap();
        hm.put("userId", userId);

        presenter.getServiceDeskDgtnList(hm, new NetworkPresenter.getServiceDeskDgtnListener() {
            @Override
            public void onResponse(Res_AP_IF_040_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            dgtnList = result.getResult().getDgtnList();
                            if (dgtnList == null || dgtnList.size() == 0) {
                                // 없으면 본인
                                Res_AP_IF_040_VO.result.dgtnList data = new Res_AP_IF_040_VO().new result().new dgtnList();
                                data.setUserId(userId);
                                data.setUserNm(userNm);
                                dgtnList = new ArrayList<>();
                                dgtnList.add(data);
                            }

                            setAuthName();

                            //탭의 표시 & 하단 리스트를 불러온다.
//                            if (selectCode.equals("D1")) {
//                                drawTab(btnAuthTab1);
//                            } else if (selectCode.equals("D2")) {
//                                drawTab(btnAuthTab2);
//                            } else if (selectCode.equals("D3")) {
//                                drawTab(btnAuthTab3);
//                            } else {
//                                drawTab(btnAuthTab4);
//                            }

                            setTab("");

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
                //progressBar.setVisibility(View.GONE);
                viewMessage(errMsg);
            }
        });
    }

    //권한 목록
    private void getAuthList(String userId, String code, String loadingAction) {
        //progressBar.setVisibility(View.VISIBLE);

        if (loadingAction == "progress")
            progressBar.setVisibility(View.VISIBLE);
        else
            shimmerStart();

        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("code", code);
        if (pagingProcess) {
            hm.put("pageNum", pageNum); //page num
            hm.put("pageCnt", pageSize);//page size

            pageNum = pageNum + pageSize;//처음0 그다음 pageSize만큼 증가 20 , 40, 60...
        } else {
            hm.put("pageNum", ""); //page num
            hm.put("pageCnt", "");//page size
        }
        presenter.getServiceDeskAprList(hm, new NetworkPresenter.getServiceDeskAprListListener() {
            @Override
            public void onResponse(Res_AP_IF_037_VO result) {

                if (loadingAction == "progress")
                    progressBar.setVisibility(View.GONE);
                else
                    shimmerStop();

                //progressBar.setVisibility(View.VISIBLE);
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            loadingMore = true;
                            ArrayList<Res_AP_IF_037_VO.result.aprList> readData = result.getResult().getAprList();

                            if (pagingProcess) {
                                if (readData == null || readData.size() == 0) {
                                    if (pageNum == pageSize) {//맨처음
                                        //데이터가 없음
                                        aprList = readData;
                                    } else {
                                        //리스트가 끝
                                        loadingMore = false;
                                        return;
                                    }
                                } else {
                                    // data가 있지만 페이징수에 모자를 경우 페이지끝으로 판단
                                    // ex) 한페이지당 20개씩 로딩하는데 나온데이터수가 17개등으로...20개에 못미칠경우 데이터가 끝이라 판단
                                    if (readData.size() < pageSize) {
                                        loadingMore = false;
                                    }

                                    //원래 리스트에 끼워 넣어야 함
                                    if (aprList == null) {
                                        aprList = new ArrayList<>();
                                    }
                                    aprList.addAll(readData);
                                }
                            } else {
                                aprList = readData;
                            }

                            setListData();

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
                viewMessage(errMsg);
            }
        });
    }

    private void setListData() {

        adapter.setData(aprList, selectCode);

        if (aprList == null || aprList.size() == 0) {
            layout_no_data.setVisibility(View.VISIBLE);
            layout_all_select.setVisibility(View.GONE);
            layout_bottom_button.setVisibility(View.GONE);
            rv.setVisibility(View.INVISIBLE);
        } else {

            if ("S06".equals(menuId)) {
                selectCode = "D1";
                iv_service_desk_all_check.setSelected(false);
                layout_all_select.setVisibility(View.VISIBLE);
                layout_bottom_button.setVisibility(View.VISIBLE);
            } else if ("S07".equals(menuId)) {
                selectCode = "D2";
                layout_all_select.setVisibility(View.GONE);
            } else if ("S08".equals(menuId)) {
                selectCode = "D3";
                layout_all_select.setVisibility(View.GONE);
            } else if ("S09".equals(menuId)) {
                selectCode = "D4";
                layout_all_select.setVisibility(View.GONE);
            }

            layout_no_data.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }

        //progressBar.setVisibility(View.GONE);
        //shimmerStop();
    }


    //error message
    private void viewMessage(String errMsg) {

        //에러가 생겼으므로 이전 이력은 지운다.
        layout_no_data.setVisibility(View.VISIBLE);
        layout_all_select.setVisibility(View.INVISIBLE);
        rv.setVisibility(View.INVISIBLE);


        TextDialog dialog;
        dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

//    private void drawTab(View v) {
//        //private String selectCode;//D1 : 승인대기, D2 : 승인완료, D3 : 승인반려, D4 : 승인진행
//        btnAuthTab1.setSelected(false);
//        btnAuthTab2.setSelected(false);
//        btnAuthTab3.setSelected(false);
//        btnAuthTab4.setSelected(false);
//
//        autTabLine1.setVisibility(View.GONE);
//        autTabLine2.setVisibility(View.GONE);
//        autTabLine3.setVisibility(View.GONE);
//        autTabLine4.setVisibility(View.GONE);
//        switch (v.getId()) {
//            case R.id.btn_service_desk_auth_tap1:
//                btnAuthTab1.setSelected(true);
//                autTabLine1.setVisibility(View.VISIBLE);
//                selectCode = "D1";
//                break;
//            case R.id.btn_service_desk_auth_tap2:
//                btnAuthTab2.setSelected(true);
//                autTabLine2.setVisibility(View.VISIBLE);
//                selectCode = "D2";
//                break;
//            case R.id.btn_service_desk_auth_tap3:
//                btnAuthTab3.setSelected(true);
//                autTabLine3.setVisibility(View.VISIBLE);
//                selectCode = "D3";
//                break;
//            case R.id.btn_service_desk_auth_tap4:
//                btnAuthTab4.setSelected(true);
//                autTabLine4.setVisibility(View.VISIBLE);
//                selectCode = "D4";
//                break;
//        }
//
//        pageNum = 0;
//        aprList = new ArrayList<>();
//
//        getAuthList(userId, selectCode);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_service_desk_auth_tap1:
//            case R.id.btn_service_desk_auth_tap2:
//            case R.id.btn_service_desk_auth_tap3:
//            case R.id.btn_service_desk_auth_tap4:
//                drawTab(v);
//                break;
            case R.id.iv_service_desk_all_check:
            case R.id.tv_service_desk_all_check:
                if (iv_service_desk_all_check.isSelected()) {
                    iv_service_desk_all_check.setSelected(false);
                    for (int i = 0; i < aprList.size(); i++) {
                        aprList.get(i).isChecked = false;
                    }
                } else {
                    iv_service_desk_all_check.setSelected(true);
                    for (int i = 0; i < aprList.size(); i++) {
                        aprList.get(i).isChecked = true;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_service_desk_name:
                //위임자목록팝업
                ArrayList<String> input = new ArrayList<>();
                for (int i = 0; i < dgtnList.size(); i++) {
                    input.add(dgtnList.get(i).getUserNm() + "(" + dgtnList.get(i).getUserId() + ")");
                }


                ListDialog dialog = ListDialog.newInstance();
                dialog.setData(input);
                dialog.setTitle(getResources().getString(R.string.txt_service_desk_auth5));
                dialog.setInterface(new ListDialog.OnClickListener() {
                    @Override
                    public void selectPosition(int position) {
                        //기존과 다른 아이디일때 새로이 로딩
                        if (!userId.equals(dgtnList.get(position).getUserId())) {

                            userId = dgtnList.get(position).getUserId();
                            pageNum = 0;
                            aprList = new ArrayList<>();

                            setAuthName();
                            getAuthList(userId, selectCode, "");
                        }
                    }
                });
                dialog.show(getFragmentManager(mContext));

                break;
            case R.id.btn_service_desk_cancel:
                //반려
                sendConfirm("R");
                break;
            case R.id.btn_service_desk_confirm:
                //승인
                sendConfirm("A");
                break;
        }
    }

    /**
     * 승인 또는 반려를 전송
     *
     * @param apprGb A:승인, R:반려
     */
    private void sendConfirm(String apprGb) {
        int cnt = 0;
        if (aprList == null) return;
        for (int i = 0; i < aprList.size(); i++) {
            if (aprList.get(i).isChecked) {
                cnt++;
            }
        }
        if (cnt == 0) return;

        String title = cnt + mContext.getResources().getString(R.string.txt_service_desk_popup_txt6) + " " + mContext.getResources().getString(R.string.txt_service_confirm);
        String okStr = mContext.getResources().getString(R.string.txt_service_confirm);
        boolean isMust = false;
        if (apprGb.equals("R")) {
            okStr = mContext.getResources().getString(R.string.txt_service_cancel);
            isMust = true;
            title = cnt + mContext.getResources().getString(R.string.txt_service_desk_popup_txt6) + " " + mContext.getResources().getString(R.string.txt_service_cancel);
        }

        CommentDialog dialog = CommentDialog.newInstance();
        dialog.setData(title, okStr, isMust);
        dialog.setInterface(new CommentDialog.CommentDialogListener() {

            @Override
            public void getMessage(String comment) {
                dialog.dismiss();
                sendConfirmServer(apprGb, comment);
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

    private void sendConfirmServer(String apprGb, String comment) {
        progressBar.setVisibility(View.VISIBLE);

        ArrayList<Req_AP_IF_039_VO.aprApproveList> listData = new ArrayList<>();
        for (int i = 0; i < aprList.size(); i++) {
            if (aprList.get(i).isChecked) {
                Req_AP_IF_039_VO.aprApproveList info = new Req_AP_IF_039_VO().new aprApproveList();
                info.setCtnt(comment);
                info.setDocNo(aprList.get(i).getDocNo());
                listData.add(info);
            }
        }

        Req_AP_IF_039_VO input = new Req_AP_IF_039_VO();
        input.setUserId(userId);
        input.setApprGb(apprGb);
        input.setAprApproveList(listData);

        presenter.getServiceDeskAprApprove(input, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            //리스트 다시 가져옴..
                            String msg = String.format(getResources().getString(R.string.txt_approval_success), mContext.getResources().getString(R.string.txt_service_confirm));
                            if (apprGb.equals("R")) {
                                msg = String.format(getResources().getString(R.string.txt_approval_success), mContext.getResources().getString(R.string.txt_service_cancel));
                            }
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                            getAuthList(userId, selectCode, "");
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
                viewMessage(errMsg);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setAuthName() {
        for (int i = 0; i < dgtnList.size(); i++) {
            if (dgtnList.get(i).getUserId().equals(userId)) {
                tv_service_desk_name.setText(dgtnList.get(i).getUserNm() + "(" + dgtnList.get(i).getUserId() + ")");
                break;
            }
        }
    }

    public FragmentManager getFragmentManager(Context context) {
        try {
            final AppCompatActivity activity = (AppCompatActivity) context;

            //  return activity.getFragmentManager();

            return activity.getSupportFragmentManager();

        } catch (ClassCastException e) {
            Log.d("ServiceDeskAuthListView", "#### Can't get the fragment manager with this");
            return null;
        }
    }

    //check
    @Override
    public void checkPosition(int position) {
        if (aprList.get(position).isChecked) {
            aprList.get(position).isChecked = false;
        } else {
            aprList.get(position).isChecked = true;
        }
        adapter.notifyDataSetChanged();
    }

    //move
    @Override
    public void selectPosition(int position) {
        if (mInterface != null) {
            //mInterface.onSelectItem(aprList.get(position).getDocNo());
            mInterface.onSelectItem(aprList, position);
        }
    }

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, tv_title1);
        adapter.notifyDataSetChanged();
    }

    public void shimmerStart() {
        //Toast.makeText(mContext, "shimerStart", Toast.LENGTH_LONG).show();
        //lv_home.setVisibility(View.GONE);
        //no_data.setVisibility(View.GONE);
        layout_no_data.setVisibility(View.GONE);
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    public void shimmerStop() {
        //Toast.makeText(mContext, "shimerStop", Toast.LENGTH_LONG).show();
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }
}
