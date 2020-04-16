package com.kolon.sign2.approval;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.view.ViewPagerFixed;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 전자 결재 상세
 */
public class ApprovalDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ApprovalDetailActivity.class.getSimpleName();
    private Button btn_approval_line;

    private RelativeLayout progress_bar;
    private RequestManager mGlideRequestManager;

    private String userId;
    private String deptId;
    private String companyCd;
    private String category;
    private String containerId;
    private String subQuery;

    private Res_AP_IF_016_VO.result readRowData;
    private ViewPagerFixed pager;
    private ApprovalPagerAdapter adapter;
    private ArrayList<inputData> data;
    private boolean activityMoveOkCheck;

    private TextSizeAdjView view_text_size_adj;

    public static float readedPhotoViewWidth;
    private HashMap<Integer, Boolean> addLineViewHm;
    private boolean isFirst;
    private int nowPosition;

    private ShimmerFrameLayout mShimmerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_detail);

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        mGlideRequestManager = Glide.with(this);
        btn_approval_line = (Button) findViewById(R.id.btn_approval_line);
        btn_approval_line.setVisibility(View.GONE);
        btn_approval_line.setOnClickListener(this);

        progress_bar = (RelativeLayout) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_layout);

        ImageView btn_textsize = (ImageView) findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(this);

        view_text_size_adj = (TextSizeAdjView) findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                adapter.textSizeChange();
            }
        });

        //server data loading..
        Intent it = getIntent();
        if (it == null) return;
        ArrayList<Res_AP_IF_013_VO.result.apprList> obj = (ArrayList<Res_AP_IF_013_VO.result.apprList>) it.getSerializableExtra("object");

        int position = it.getIntExtra("position", 0);
        userId = it.getStringExtra("userId");
        deptId = it.getStringExtra("deptId");
        companyCd = it.getStringExtra("companyCd");
        category = it.getStringExtra("category");
        containerId = it.getStringExtra("containerId");
        subQuery = it.getStringExtra("subQuery");

        ArrayList<String> guidList = it.getStringArrayListExtra("guidList");
        listDataLoading(obj, guidList, position);
        isFirst = true;
        addLineViewHm = new HashMap<>();
    }

    private void listDataLoading(ArrayList<Res_AP_IF_013_VO.result.apprList> obj, ArrayList<String> guidList, int position) {

        data = new ArrayList<>();
        for (int i = 0; i < guidList.size(); i++) {
            inputData input = new inputData();

            String temp[] = guidList.get(i).split("\\|");
            String wfDocId = temp[0];
            String bchamjo = temp[2];

            input.commentView = false;
            input.guid = guidList.get(i);
            input.wfDocId = wfDocId;
            input.bchamjo = bchamjo;

            input.inLine = obj.get(i).getInLine();
            input.isPublic = obj.get(i).getIsPublic();

            input.title = obj.get(i).getTitle();

            data.add(input);
        }

        setData(data, position, category, userId, deptId, companyCd);
    }

    //우측 결재선 표시 유무
    public void setApprovalLineView(int position, boolean isView) {
        addLineViewHm.put(position, isView);
    }


    //상세 데이터를 set
    public void getServerResponse(Res_AP_IF_016_VO result, int position) {
        addLineViewHm.put(position, false);
        if (result != null) {
            if ("200".equals(result.getStatus().getStatusCd())) {
                if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                    if (position == pager.getCurrentItem()) {
                        readRowData = result.getResult();
                        if (readRowData != null && readRowData.getApprDetailItem() != null) {
                            if ("Y".equalsIgnoreCase(readRowData.getApprDetailItem().getApporvalPlag()) && "A02011".equals(readRowData.getApprDetailItem().getState())) {
                                addLineViewHm.put(position, true);
                                if(isFirst){
                                    isFirst = false;
                                    btn_approval_line.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    return;
                }
            }
        }
    }

    private void setData(ArrayList<inputData> data, int position, String category, String userId, String deptId, String companyCd) {
        //readData
        adapter = new ApprovalPagerAdapter(this, mGlideRequestManager, data, category, userId, deptId, companyCd);

        pager = (ViewPagerFixed) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPagerFixed.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                nowPosition = position;
                if(addLineViewHm == null){
                    addLineViewHm = new HashMap<>();
                }
                if(addLineViewHm.get(position) != null){
                    if(addLineViewHm.get(position)){
                        btn_approval_line.setVisibility(View.VISIBLE);
                    }else{
                        btn_approval_line.setVisibility(View.GONE);
                    }
                }

                if (adapter.getItemData().get(position).rowData != null) {
                    readRowData = adapter.getItemData().get(position).rowData;
                }
                if (readRowData == null) {
                    return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    if(addLineViewHm.get(nowPosition) != null){
                        if(addLineViewHm.get(nowPosition)){
                            btn_approval_line.setVisibility(View.VISIBLE);
                        }else{
                            btn_approval_line.setVisibility(View.GONE);
                        }
                    }
                }else{
                    btn_approval_line.setVisibility(View.GONE);
                }
            }
        });
        pager.setCurrentItem(position);
    }


    private void getGuidList() {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("category", category); //개인
        hm.put("userId", userId);
        hm.put("deptId", deptId);
        hm.put("containerId", containerId);
        hm.put("subQuery", subQuery);

        //  showProgressbar();
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getApprovalList(hm, new NetworkPresenter.getApprovalListResult() {
            @Override
            public void onResponse(Res_AP_IF_013_VO result) {
                String errMsg = "";
                //      hideProgressbar();
                if (result != null) {
                    Log.d(TAG, "getGuidList:" + new Gson().toJson(result));
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
//                            Res_AP_IF_013_VO readData = result;
                            if (result.getResult() == null || result.getResult().getApprList() == null || result.getResult().getApprList().size() == 0) {
                                result = null;//실패하면 기존것 사용?
                            } else {
                                //guid 리스트를 가져옴
                                ArrayList<String> guidList = new ArrayList<>();
                                for (int j = 0; j < result.getResult().getApprList().size(); j++) {
                                    guidList.add(result.getResult().getApprList().get(j).getGuid());
                                }
                                listDataLoading(result.getResult().getApprList(), guidList, 0);
                            }
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
                    errMsg = getResources().getString(R.string.txt_network_error);
                }
                //popup
                TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(getSupportFragmentManager());
            }
        });
    }

    public void nextApprovalLoading(int nowPosition) {
        //승인한 아이템 삭제
        adapter.getItemData().remove(nowPosition);
        adapter.notifyDataSetChanged();

        //페이지 이동 처리
        pager.setCurrentItem(nowPosition);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void onBackPressedParentRefresh() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_approval_line:
                //결재선 이동
                gotoApprovalLine();
                break;
            case R.id.btn_textsize:
                view_text_size_adj.show();
                break;
        }
    }

    public void gotoApprovalLine() {
        if (activityMoveOkCheck) {
            return;
        }
        activityMoveOkCheck = true;

        String wfDocId = data.get(pager.getCurrentItem()).wfDocId;
        Intent i = new Intent(this, ApprovalLineActivity.class);
        i.putExtra("object", readRowData);

        i.putExtra("userId", userId);
        i.putExtra("wfDocId", wfDocId);
        i.putExtra("companyCd", companyCd);

        startActivityForResult(i, 0);

        data.get(pager.getCurrentItem()).approvalLineCheck = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        activityMoveOkCheck = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult requestCode:" + requestCode + "  resultCode:" + resultCode + " " + pager.getCurrentItem());
        view_text_size_adj.changeTextSize();
        if (resultCode == RESULT_OK) {// -1
            //결재를 했으므로 재로딩한다.
            adapter.notifyDataSetChanged();
        } else {
            if (resultCode == 10) {
                adapter.textSizeChange();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //상세 표시용 서버 입력 데이터
    public class inputData {
        public String guid;
        public String wfDocId;
        public String bchamjo;

        String isPublic;
        String inLine;
        String title;


        public Res_AP_IF_016_VO.result rowData;

        public boolean commentView;
        public boolean approvalLineCheck;
    }

    public void shimmerStart() {
        //Toast.makeText(mContext, "shimerStart", Toast.LENGTH_LONG).show();
        //lv_home.setVisibility(View.GONE);
        //no_data.setVisibility(View.GONE);
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    public void shimmerStop() {
        //Toast.makeText(mContext, "shimerStop", Toast.LENGTH_LONG).show();
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }
}
