package com.kolon.sign2.approval;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.ListDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_023_VO;
import com.kolon.sign2.vo.Res_AP_IF_201_VO;
import com.kolon.sign2.vo.Res_AP_IF_202_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;
import com.kolon.sign2.vo.Res_AP_IF_204_VO;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * 결재선 추가
 */
public class ApprovalLineAddActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = ApprovalLineAddActivity.class.getSimpleName();
    private NetworkPresenter presenter;
    private ProgressBar progress_bar;
    private HorizontalScrollView tab_layout;
    private LinearLayout tabList;
    private ArrayList<Res_AP_IF_023_VO.result.apprApParentList> tabData;
    private TextView lay_top_txt;
    private ImageView btn_search;
    private RecyclerView rv;
    private ApprovalLindAddAdapter lineAddAdapter;
    private ApprovalLineSearchPopup popup;

    private String companyCd;
    private ArrayList<ApprovalTotalVO> totalVO;
    private ArrayList<Res_AP_IF_016_VO.result.apprLineList> existLine;
    private String state;

    private TextSizeAdjView view_text_size_adj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_line_add);

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btn_search = (ImageView) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);

        lay_top_txt = (TextView) findViewById(R.id.lay_top_txt);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        presenter = new NetworkPresenter();

        tab_layout = (HorizontalScrollView) findViewById(R.id.lay_tab);

        //
        tab_layout.setHorizontalScrollBarEnabled(false);
        tab_layout.setFadingEdgeLength(0);
        tab_layout.setVerticalScrollBarEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv = (RecyclerView) findViewById(R.id.rv_line_add);
        rv.setLayoutManager(linearLayoutManager);

        Intent it = getIntent();
        if (it == null) return;

        existLine = (ArrayList<Res_AP_IF_016_VO.result.apprLineList>) it.getSerializableExtra("apprLineList");
        state = it.getStringExtra("state");


        view_text_size_adj = (TextSizeAdjView)findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
//                view_text_size_adj.changeTextSize();
                lineAddAdapter.notifyDataSetChanged();
            }
        });
        ImageView btn_textsize = (ImageView)findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(this);

        showProgressbar();
        getTabListData();
    }

    private void drawTab() {
        tab_layout.removeAllViews();

        tabList = new LinearLayout(this);
        tabList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        tabList.setOrientation(LinearLayout.HORIZONTAL);
        tabList.setGravity(Gravity.CENTER);

        for (int i = 0; i < tabData.size(); i++) {
            ApprovalLineAddTabRow rowView = new ApprovalLineAddTabRow(this);
            rowView.setTabText(tabData.get(i).getDeptName());
            rowView.setId(i);
            rowView.setHomeImg(false);
            rowView.setTabOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTabClick(((ApprovalLineAddTabRow) view).getTabText());
                }
            });

            if (i == 0) {
                rowView.setHomeImg(true);
                if (tabData.size() == 1) {
                    rowView.setViewDiv(false);
                } else {
                    rowView.setViewDiv(true);
                }
                rowView.setTextColor(false);
            } else if (i == tabData.size() - 1) {
                rowView.setViewDiv(false);
                rowView.setTextColor(true);
            } else {
                rowView.setViewDiv(true);
                rowView.setTextColor(false);
            }

            rowView.setGravity(Gravity.CENTER);
            tabList.addView(rowView);
        }

        lay_top_txt.setText(tabData.get(tabData.size() - 1).getDeptName());

        tab_layout.addView(tabList);

        tab_layout.postDelayed(new Runnable() {
            public void run() {
                tab_layout.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);

    }

    private void onTabClick(String title) {
        //tabData
        int size = tabList.getChildCount();
        int selectPos = 0;
        for (int i = 0; i < size; i++) {
            ApprovalLineAddTabRow view = (ApprovalLineAddTabRow) tabList.getChildAt(i);
            if (title.equals(view.getTabText())) {
                view.setTextColor(true);
                selectPos = i;
                break;
            } else {
                view.setTextColor(false);
            }
        }

        //뒷쪽은 지움
        for (int i = size - 1; i > selectPos; i--) {
            tabData.remove(i);
        }

        drawTab();

        getBottomListData(selectPos);
    }

    //상단 탭 데이터
    private void getTabListData() {

        //로그인시 저장되었던 정보
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());
        companyCd = mPref.getStringPreference(Constants.PREF_COMPANY_CD);
        String deptCd = mPref.getStringPreference(Constants.PREF_DEPT_CD);

        HashMap hm = new HashMap();
        hm.put("companyCd", companyCd);
        hm.put("deptCd", deptCd);
        //  AA00 AFC0 A00017
        Log.d(TAG, "getApprovalSpparents in:" + hm);
        totalVO = new ArrayList<>();
        presenter.getApprovalSpparents(hm, new NetworkPresenter.getApprovalSpparentsListener() {

            @Override
            public void onResponse(Res_AP_IF_023_VO result) {
                hideProgressbar();
                Log.d(TAG, "getApprovalSpparents:" + new Gson().toJson(result));
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            tabData = new ArrayList<>();
                            Res_AP_IF_023_VO.result.apprApParentList info = new Res_AP_IF_023_VO().new result().new apprApParentList();
                            info.setDeptName(getResources().getString(R.string.txt_company));
                            tabData.add(info);

                            for (int i = 0; i < result.getResult().getApprApParentList().size(); i++) {
                                tabData.add(result.getResult().getApprApParentList().get(i));
                            }

                            drawTab();

                            //초기데이터
                            getSplitList(companyCd, tabData.get(tabData.size() - 1).getDeptCd(), false);

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


    //tab click..
    private void getBottomListData(int position) {
        if (position == 0) {
            //회사
            getCompanyList(tabData.get(position).getDeptCd());
        } else {
            getSplitList(companyCd, tabData.get(position).getDeptCd(), true);
        }
    }

    private void getCompanyList(String companyCd) {
        HashMap hm = new HashMap();
        this.companyCd = companyCd;
        hm.put("companyCd", companyCd);

        Log.d(TAG, "### getCompanyList in:" + hm);
        presenter.getApprovalListcomps(hm, new NetworkPresenter.getApprovalListcompsListener() {

            @Override
            public void onResponse(Res_AP_IF_201_VO result) {
                //   ApprovalLindAddAdapter addAdapter = new ApprovalLindAddAdapter(result.getResult().getApprCompList());
                //   rv.setAdapter(addAdapter);
                Log.d(TAG, "getApprovalListcomps:" + new Gson().toJson(result));
                String json = new Gson().toJson(result.getResult().getApprCompList());
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                drawBottomList(json, 0);
            }
        });
    }

    private void getDeptList(String companyCd, String deptCd) {
        getDeptList(companyCd, deptCd, false);
    }

    private void getDeptList(String companyCd, String deptCd, boolean isHeader) {
        HashMap hm = new HashMap();
        hm.put("companyCd", companyCd);
        hm.put("deptCd", deptCd);
        Log.d(TAG, "#### getDeptList in:" + hm);
        presenter.getApprovalSporganizationApproval(hm, new NetworkPresenter.getApprovalSporganizationApprovalListener() {
            @Override
            public void onResponse(Res_AP_IF_202_VO result) {
                Log.d(TAG, "#### getApprovalSporganizationApproval:" + new Gson().toJson(result));
                String json = new Gson().toJson(result.getResult().getApprOrganizationList());
                if (TextUtils.isEmpty(json)) {
                    return;
                }

                if (result.getResult().getApprOrganizationList() != null) {
                    for (int i = 0; i < result.getResult().getApprOrganizationList().size(); i++) {
                        ApprovalTotalVO data = new ApprovalTotalVO();
                        data.header = false;
                        data.setDeptCd(result.getResult().getApprOrganizationList().get(i).getDeptCd());
                        data.setDeptName(result.getResult().getApprOrganizationList().get(i).getDeptName());
                        data.setParentCd(result.getResult().getApprOrganizationList().get(i).getParentCd());
                        data.setChildDept(result.getResult().getApprOrganizationList().get(i).getChildDept());
                        totalVO.add(data);
                    }
                }

                drawBottomList(json, 1);
            }
        });
    }

    private void getSplitList(String companyCd, String deptCd) {
        getSplitList(companyCd, deptCd, false);
    }

    private void getSplitList(String companyCd, String deptCd, boolean isHeader) {
        HashMap hm = new HashMap();
        hm.put("companyCd", companyCd);
        hm.put("deptCd", deptCd);
        Log.d(TAG, "#### getSplitList in:" + hm);
        presenter.getApprovalSplistApproval(hm, new NetworkPresenter.getApprovalSplistApprovalListener() {

            @Override
            public void onResponse(Res_AP_IF_203_VO result) {
                Log.d(TAG, "#### getApprovalSplistApproval:" + new Gson().toJson(result));
                String json = new Gson().toJson(result.getResult().getApprSpList());
                if (TextUtils.isEmpty(json)) {
                    return;
                }

                if (isHeader) {
                    totalVO = new ArrayList<>();
                    if (result.getResult().getApprSpList() != null) {
                        for (int i = 0; i < result.getResult().getApprSpList().size(); i++) {
                            ApprovalTotalVO data = new ApprovalTotalVO();
                            data.header = true;
                            data.setIkenId(result.getResult().getApprSpList().get(i).getIkenId());
                            data.setName(result.getResult().getApprSpList().get(i).getName());
                            data.setMobile(result.getResult().getApprSpList().get(i).getMobile());
                            data.setEmail(result.getResult().getApprSpList().get(i).getEmail());
                            data.setDeptCd(result.getResult().getApprSpList().get(i).getDeptCode());
                            data.setOrgUnit(result.getResult().getApprSpList().get(i).getOrgUnit());
                            data.setJobTitle(result.getResult().getApprSpList().get(i).getJobTitle());
                            data.setRoleName(result.getResult().getApprSpList().get(i).getRoleName());
                            totalVO.add(data);
                        }
                    }
                    getDeptList(companyCd, deptCd, false);
                } else {
                    drawBottomList(json, 2);
                }
            }
        });
    }


    private void drawBottomList(String json, int type) {
        Gson g = new Gson();
        if (type == 1) {
            lineAddAdapter = new ApprovalLindAddAdapter(totalVO, type);
        } else {
            lineAddAdapter = new ApprovalLindAddAdapter(json, type);
        }

        rv.setAdapter(lineAddAdapter);
        lineAddAdapter.setInterface(new ApprovalLindAddAdapter.ListTabClickListener() {
            @Override
            public void selectPosition(int position, String IKenID) {
                if (type == 0) {
                    //회사
                    //선택한 데이터 타이틀을 가져옴
                    ArrayList<Res_AP_IF_201_VO.result.apprCompList> data = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_201_VO.result.apprCompList>>() {
                    }.getType());

                    String name = data.get(position).getName(); //>>이 이름을 상단에 올림
                    //그리고 하단은??
                    Res_AP_IF_023_VO.result.apprApParentList info = new Res_AP_IF_023_VO().new result().new apprApParentList();
                    info.setDeptName(name);
                    info.setDeptCd(data.get(position).getCompanyCd());
                    // info.setChildDept(data.get(position).);
                    tabData.add(info);
                    drawTab();
                    companyCd = data.get(position).getCompanyCd();
                    totalVO = new ArrayList<>();
                    getDeptList(companyCd, companyCd);
                } else if (type == 1) {
                    if (TextUtils.isEmpty(IKenID)) {

                        /*
                        ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList> data = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList>>() {
                        }.getType());
                        */

                        String name = "";
                        String deptCd = "";
                        String child = "";

                        name = totalVO.get(position).getDeptName();
                        deptCd = totalVO.get(position).getDeptCd();
                        child = totalVO.get(position).getChildDept();

                        Res_AP_IF_023_VO.result.apprApParentList info = new Res_AP_IF_023_VO().new result().new apprApParentList();
                        info.setDeptName(name);
                        info.setDeptCd(deptCd);
                        info.setChildDept(child);
                        tabData.add(info);

                        drawTab();
                        if ("Y".equalsIgnoreCase(totalVO.get(position).getChildDept())) {
                            getSplitList(companyCd, totalVO.get(position).getDeptCd(), true);
                        } else {
                            getSplitList(companyCd, totalVO.get(position).getDeptCd());
                        }

                    } else {
                        //totalvo 가 null이 아니면등등..
                        if(totalVO == null || totalVO.size() == 0){
                            ArrayList<Res_AP_IF_203_VO.result.apprSpList> data = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_203_VO.result.apprSpList>>() {
                            }.getType());
                            viewProcessSelectPopup(IKenID, data.get(position));
                        }else{
                            Res_AP_IF_203_VO.result.apprSpList setdata = new Res_AP_IF_203_VO().new result().new apprSpList();
                            setdata.setRoleName(totalVO.get(position).getRoleName());
                            setdata.setOrgUnit(totalVO.get(position).getOrgUnit());
                            setdata.setName(totalVO.get(position).getName());
                            setdata.setMobile(totalVO.get(position).getMobile());
                            setdata.setJobTitle(totalVO.get(position).getJobTitle());
                            setdata.setIkenId(totalVO.get(position).getIkenId());
                            setdata.setEmail(totalVO.get(position).getEmail());
                            setdata.setDeptCode(totalVO.get(position).getDeptCode());
                            viewProcessSelectPopup(IKenID, setdata);
                        }
                    }
                } else if (type == 2) {
                    //사람 - 결재선 비교
                    ArrayList<Res_AP_IF_203_VO.result.apprSpList> data = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_203_VO.result.apprSpList>>() {
                    }.getType());

                    drawTab();

                    viewProcessSelectPopup(data.get(position).getIkenId(), data.get(position));

                } else {
                    String name = totalVO.get(position).getDeptName();
                    String deptCd = totalVO.get(position).getDeptCd();
                    String child = totalVO.get(position).getChildDept();

//                    ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList> data = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList>>() {
//                    }.getType());
//                    String name = data.get(position).getDeptName();
                    Res_AP_IF_023_VO.result.apprApParentList info = new Res_AP_IF_023_VO().new result().new apprApParentList();
                    info.setDeptName(name);
                    info.setDeptCd(deptCd);
                    info.setChildDept(child);
                    tabData.add(info);
                    drawTab();
                }
            }
        });
    }

    private boolean isDuplicatedId(String IkenId) {
        boolean duplicateId = false;
        //기존 결재선에 있는지 비교 existLine
        for (int i = 0; i < existLine.size(); i++) {
            if (existLine.get(i).getUserId().equals(IkenId)) {
                duplicateId = true;
                break;
            }
        }
        if (duplicateId) {
            //이미 결재선에 아이디가 있음
            TextDialog dialog = TextDialog.newInstance("", getResources().getString(R.string.txt_approval_line_txt6), "", getResources().getString(R.string.txt_confirm));
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager());
            return true;
        } else {
            return false;
        }
    }

    private void viewProcessSelectPopup(String IkenId, Res_AP_IF_203_VO.result.apprSpList data) {
        //  String id = data.get(position).getIkenId();
        boolean duplicateId = false;
        //기존 결재선에 있는지 비교 existLine
        for (int i = 0; i < existLine.size(); i++) {
            if (existLine.get(i).getUserId().equals(IkenId)) {
                duplicateId = true;
                break;
            }
        }
        if (duplicateId) {
            //이미 결재선에 아이디가 있음
            TextDialog dialog = TextDialog.newInstance("", getResources().getString(R.string.txt_approval_line_txt6), "", getResources().getString(R.string.txt_confirm));
            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show(getSupportFragmentManager());
        } else {
            /**
             * State변수는 상위화면인 결재상세의 I/F에서 가져온 데이터 활용
             *
             * State : A02005
             * - 일반결재, A03001
             * - 참조, A03007  - 부서협조, A03011 - 부서협조(병렬), A03012
             *
             * State : A02011, A02012
             * - 일반결재, A03001
             * - 참조, A03007
             * - 개인협조, A03008 - 개인협조(병렬), A03009
             * */

            Intent returndata = new Intent();
            returndata.putExtra("lineData", data);
//            returndata.putExtra("lineData", data.get(position));

            ArrayList<String> input = new ArrayList<>();
            input.add(getResources().getString(R.string.txt_approval_line_array1));
            input.add(getResources().getString(R.string.txt_approval_line_array2));
            if ("A02005".equals(state)) {
                input.add(getResources().getString(R.string.txt_approval_line_array3_1));
                input.add(getResources().getString(R.string.txt_approval_line_array4_1));
            } else {
                input.add(getResources().getString(R.string.txt_approval_line_array3));
                input.add(getResources().getString(R.string.txt_approval_line_array4));
            }

            String aprstate = "";
            ListDialog dialog = ListDialog.newInstance();
            dialog.setData(input);
            dialog.setTitle(getResources().getString(R.string.txt_approval_line_array_title));
            dialog.setInterface(new ListDialog.OnClickListener() {
                @Override
                public void selectPosition(int position) {
                    switch (position) {
                        case 0: //일반결재
                            returndata.putExtra("aprtype", "A03001");
                            returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array1));
                            break;
                        case 1: //참조
                            returndata.putExtra("aprtype", "A03007");
                            returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array2));
                            break;
                        case 2:
                            if ("A02005".equals(state)) {//부서협조
                                returndata.putExtra("aprtype", "A03011");
                                returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array3_1));
                            } else {//개인협조
                                returndata.putExtra("aprtype", "A03008");
                                returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array3));
                            }
                            break;
                        case 3:
                            if ("A02005".equals(state)) {//부서협조 병렬
                                returndata.putExtra("aprtype", "A03012");
                                returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array4_1));
                            } else {//개인협조 병렬
                                returndata.putExtra("aprtype", "A03009");
                                returndata.putExtra("aprstate", getResources().getString(R.string.txt_approval_line_array4));
                            }
                            break;
                    }

                    dialog.dismiss();
                    setResult(RESULT_OK, returndata);
                    finish();
                }
            });
            dialog.show(getSupportFragmentManager());
        }

    }

    private void showProgressbar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_search:
                if(popup != null && popup.isVisible()){
                    return;
                }
                popup = ApprovalLineSearchPopup.newInstance();
                popup.setInterface(new ApprovalLineSearchPopup.OnTabClickListener() {
                    @Override
                    public void selectPosition(int position, String userId, Res_AP_IF_204_VO.result.apprSpSearchList data) {
                        Res_AP_IF_203_VO.result.apprSpList setData = new Res_AP_IF_203_VO().new result().new apprSpList();
                        setData.setDeptCode(data.getDeptCode());
                        setData.setEmail(data.getEmail());
                        setData.setIkenId(data.getUserAccount());
                        setData.setJobTitle(data.getTitleName());
                        setData.setMobile(data.getMobileNum());
                        setData.setName(data.getUserName());
                        setData.setOrgUnit(data.getDeptName());
                        setData.setRoleName(data.getRoleName());
                        setData.companyCd = data.getCompanyCd();
                        setData.orgName = data.getCompanyName();
                        viewProcessSelectPopup(userId, setData);
                    }

                    @Override
                    public void selectRecentlyPosition(String userId, Res_AP_IF_203_VO.result.apprSpList data) {
                        viewProcessSelectPopup(userId, data);
                    }
                });
                popup.show(getSupportFragmentManager(), "");
                break;
            case R.id.btn_textsize:
                view_text_size_adj.show();
                break;
        }
    }
}
