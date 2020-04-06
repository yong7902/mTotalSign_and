package com.kolon.sign2.approval;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.kolon.sign2.vo.Req_AP_IF_022_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_022_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;

import java.util.ArrayList;



public class ApprovalLineActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = ApprovalLineActivity.class.getSimpleName();

    private Res_AP_IF_016_VO.result readRowData;

    private ApprovalLineAdapter adapter;
    private ItemTouchHelper touchHelper;
    private String userId, wfDocId, companyCd;
    private boolean activityMoveOkCheck;

    private TextSizeAdjView view_text_size_adj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_line);

        Intent i = getIntent();
        if (i == null) return;

        userId = i.getStringExtra("userId");
        wfDocId = i.getStringExtra("wfDocId");
        companyCd = i.getStringExtra("companyCd");


        /*
         * 넘어온 데이터의 가공---------
         * 1.데이터가 없으면 기안자를 붙인다.
         * 2.데이터에서 첫행의 userID가 사용자와 틀린경우 1. 과 같은 상태로 구성함
         * 3.결재 문서가 공문(state='A02011') 인 경우 : I/F 결재선 데이터 모두 구성함
         * 4.결재선 데이터에서 사용자의 부서코드deptID만 들어간 데이터(userID빈값)의 sn값 + '-' 로 시작하는 sn을 가진 결재선데이터로 구성
         * !!! 4번항의 경우 애초 3번항의 공문일경우에만 이리로 들어오니 필요없음
         * */
        readRowData = (Res_AP_IF_016_VO.result) i.getSerializableExtra("object");
        if (readRowData == null) return;
        //1.데이터가 없으면 기안자(로그인 유저)를 붙인다. 또는 2.데이터에서 첫행의 userID가 사용자와 틀린경우.
        if (readRowData.getApprLineList() == null || readRowData.getApprLineList().size() == 0
                || !readRowData.getApprLineList().get(0).getUserId().equals(userId)) {

            SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());
            String loginInfo = mPref.getStringPreference(Constants.PREF_LOGIN_IF_INFO);
            Res_AP_IF_004_VO.result.multiuserList user = new Gson().fromJson(loginInfo, new TypeToken< Res_AP_IF_004_VO.result.multiuserList>(){}.getType());

            Res_AP_IF_016_VO.result.apprLineList data = new Res_AP_IF_016_VO().new result().new apprLineList();

            String deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);
            //전송시 필수
            data.setAprtype("A03001");//일반결재
            data.setDeptId(deptId);
            data.setUserId(userId);
            data.setSn("1");

            //표시 필수
            data.setName(user.getUserName());
            data.setActionType(getResources().getString(R.string.txt_approval_line_array1));
            data.setActivity(getResources().getString(R.string.txt_approval_status_progress));//진행
            data.setDepartment(user.getDeptName());

            String position = user.getTitleName();
            if (!TextUtils.isEmpty(user.getRoleName())) {
                position = user.getRoleName() + "/" + position;
            }
            data.setPosition(position);
            ArrayList<Res_AP_IF_016_VO.result.apprLineList> list = new ArrayList<>();
            list.add(data);

            readRowData.setApprLineList(list);
        }


        Button btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        Button btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        LinearLayout btnAdd = (LinearLayout) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(this);

//        signCount	결재칸수
//        hapyuiCount	개인협조칸수
//        dhapyuiCount	부서협조칸수
        TextView info = (TextView) findViewById(R.id.tv_approval_line_txt);
        String infoTxt = getResources().getString(R.string.txt_approval_line_txt1) + readRowData.getApprDetailItem().getSignCount() + ","
                + getResources().getString(R.string.txt_approval_line_txt2) + readRowData.getApprDetailItem().getHapyuiCount() + ","
                + getResources().getString(R.string.txt_approval_line_txt3) + readRowData.getApprDetailItem().getDhapyuiCount();
        info.setText(infoTxt);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv_approval_line);

        adapter = new ApprovalLineAdapter(this, new StartDragListener() {

            @Override
            public void requestDrag(RecyclerView.ViewHolder viewHolder) {
                touchHelper.startDrag(viewHolder);
            }
        }, readRowData.getApprLineList());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        adapter.setInterface(new ApprovalLineAdapter.onClick() {
            @Override
            public void onEdit(int position) {
                //edit popup
                int dataPosition = position;
                String state = readRowData.getApprDetailItem().getState();
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

                ListDialog dialog = ListDialog.newInstance();
                dialog.setData(input);
                dialog.setTitle(getResources().getString(R.string.txt_approval_line_array_edit_title));
                dialog.setInterface(new ListDialog.OnClickListener() {
                    @Override
                    public void selectPosition(int position) {
                        //position > 팝업종류의 위치. 일반, 참조,개인협조,병렬
                        String aprtype = "";
                        String actionType = "";
                        switch (position){
                            case 0: //일반결재
                                aprtype = "A03001";
                                actionType = getResources().getString(R.string.txt_approval_line_array1);
                                break;
                            case 1: //참조
                                aprtype = "A03007";
                                actionType = getResources().getString(R.string.txt_approval_line_array2);
                                break;
                            case 2:
                                if ("A02005".equals(state)) {//부서협조
                                    aprtype = "A03011";
                                    actionType = getResources().getString(R.string.txt_approval_line_array3_1);
                                } else {//개인협조
                                    aprtype = "A03008";
                                    actionType = getResources().getString(R.string.txt_approval_line_array3);
                                }
                                break;
                            case 3:
                                if ("A02005".equals(state)) {//부서협조 병렬
                                    aprtype = "A03012";
                                    actionType = getResources().getString(R.string.txt_approval_line_array4_1);
                                } else {//개인협조 병렬
                                    aprtype = "A03009";
                                    actionType = getResources().getString(R.string.txt_approval_line_array4);
                                }
                                break;
                        }
                        dialog.dismiss();
                        readRowData.getApprLineList().get(dataPosition).setAprtype(aprtype);
                        readRowData.getApprLineList().get(dataPosition).setActionType(actionType);

                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.show(getSupportFragmentManager());

            }
        });

        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);


//        Req_AP_IF_022_VO req = new Req_AP_IF_022_VO();
//
//        Gson g = new Gson();
//
//        String str = g.toJson(readRowData.getApprLineList());
//        ArrayList<Req_AP_IF_022_VO.approvalLine> al2 = g.fromJson(str, new TypeToken<ArrayList<Req_AP_IF_022_VO.approvalLine>>(){}.getType());
//
//        req.setApprovalLine(al2);



        view_text_size_adj = (TextSizeAdjView)findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                adapter.notifyDataSetChanged();
            }
        });
        ImageView btn_textsize = (ImageView)findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(this);

    }

    //결재선 보냄
    private void sendApprovalLine() {
        /**
         * userId		사용자ID
         * wfDocId		결재마스터ID
         * companyCd		회사코드
         * approvalLine
         * 	userNum	순번
         * 	userId	Iken ID
         * 	deptId	부서 ID
         * 	aprType	결재타입				일반결재, 개인협조, 개인협조(병렬), 부서협조, 부서협조(병렬)) 등등.
         * 	aprState	결재상태				(진행, 대기) [접수자만 진행으로 주시면 됩니다.
         * */

        Req_AP_IF_022_VO req = new Req_AP_IF_022_VO();
        req.setUserId(userId);
        req.setWfDocId(wfDocId);
        req.setCompanyCd(companyCd);

        ArrayList<Req_AP_IF_022_VO.approvalLine> al = new ArrayList<>();
        for(int i=0;i<readRowData.getApprLineList().size();i++){
            Req_AP_IF_022_VO.approvalLine line = new Req_AP_IF_022_VO().new approvalLine();
            if(i == 0){
                line.setAprState(getResources().getString(R.string.txt_approval_status_progress));//접수자만 진행, 나머지는 대기
            }else{
                line.setAprState(getResources().getString(R.string.txt_approval_status_wait));//접수자만 진행, 나머지는 대기
            }
            line.setAprType(readRowData.getApprLineList().get(i).getAprtype());
            line.setDeptId(readRowData.getApprLineList().get(i).getDeptId());
            line.setUserId(readRowData.getApprLineList().get(i).getUserId());
            line.setUserNum(String.valueOf(i+1));
            al.add(line);
        }
        req.setApprovalLine(al);

        Log.d(TAG, "getApprovalLineSave in:" + new Gson().toJson(req));

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getApprovalLineSave(req, new NetworkPresenter.getApprovalLineSaveListener() {

            @Override
            public void onResponse(Res_AP_IF_022_VO result) {
        //        hideProgressBar();
                Log.d(TAG, "#### getApprovalLineSave:" + new Gson().toJson(result));
                String errMsg = "";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            //detail을 refresh
                            Intent returndata = new Intent();
                            setResult(RESULT_OK, returndata);
                            finish();
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Only if you need to restore open/close state when
        // the orientation is changed
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_confirm:
                //결재칸 초과 검사

                /**
                 * - 결재칸 수 : A03001  - 개인협조칸 수 : A03008, A03009 - 부서협조칸 수 : A03011, A03012
                 * */
                //        signCount	결재칸수
                //        hapyuiCount	개인협조칸수
                //        dhapyuiCount	부서협조칸수

                int signCount = 0;
                int hapyuiCount = 0;
                int dhapyuiCount = 0;
                for (int i = 0; i < readRowData.getApprLineList().size(); i++) {
                    if ("A03001".equals(readRowData.getApprLineList().get(i).getAprtype())) {
                        //결재칸수
                        signCount++;
                    } else if ("A03008".equals(readRowData.getApprLineList().get(i).getAprtype()) || "A03009".equals(readRowData.getApprLineList().get(i).getAprtype())) {
                        //개인협조칸수
                        hapyuiCount++;
                    } else if ("A03011".equals(readRowData.getApprLineList().get(i).getAprtype()) || "A03012".equals(readRowData.getApprLineList().get(i).getAprtype())) {
                        //부서협조칸수
                        dhapyuiCount++;
                    }
                }

                Log.d(TAG,"#### signCount:"+signCount+"   hapyuiCount:"+hapyuiCount+"   dhapyuiCount:"+dhapyuiCount);
                Log.d(TAG,"#### getSignCount:"+readRowData.getApprDetailItem().getSignCount()+"   getHapyuiCount:"+readRowData.getApprDetailItem().getHapyuiCount()+
                        "   getDhapyuiCount:"+readRowData.getApprDetailItem().getDhapyuiCount());
                String errMsg = "";
                boolean err = false;
                if (signCount > Integer.parseInt(readRowData.getApprDetailItem().getSignCount())) {//결재 칸수
                    err = true;
                    errMsg = String.format(getResources().getString(R.string.txt_approval_line_txt7), getResources().getString(R.string.txt_approval_txt), readRowData.getApprDetailItem().getSignCount());//"(개인협조)칸 수는(3) 입니다. 다시 확인해주세요.";
                }
                if (hapyuiCount > Integer.parseInt(readRowData.getApprDetailItem().getHapyuiCount())) {//개인협조
                    err = true;
                    errMsg = String.format(getResources().getString(R.string.txt_approval_line_txt7), getResources().getString(R.string.txt_approval_line_array3), readRowData.getApprDetailItem().getHapyuiCount());
                }
                if (dhapyuiCount > Integer.parseInt(readRowData.getApprDetailItem().getDhapyuiCount())) {//부서협조
                    err = true;
                    errMsg = String.format(getResources().getString(R.string.txt_approval_line_txt7), getResources().getString(R.string.txt_approval_line_array3_1), readRowData.getApprDetailItem().getDhapyuiCount());
                }

                if (err) {
                    // err popup
                    TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));

                    dialog.setCancelable(false);
                    dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show(getSupportFragmentManager());
                } else {
                    sendApprovalLine();
                }
                break;
            case R.id.btn_add:

                if(activityMoveOkCheck) return;
                activityMoveOkCheck = true;

                String companyCd ="";
                String deptCd ="";

                Intent i = new Intent(this, ApprovalLineAddActivity.class);
                i.putExtra("companyCd", companyCd);
                i.putExtra("deptCd", deptCd);

                i.putExtra("state", readRowData.getApprDetailItem().getState());
                i.putExtra("apprLineList", readRowData.getApprLineList());

                startActivityForResult(i, 0);
                break;
            case R.id.btn_textsize:
                view_text_size_adj.show();
                break;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        activityMoveOkCheck = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "onActivityResult requestCode:"+requestCode+"  resultCode:"+resultCode);
        if (resultCode == RESULT_OK) {

            /**
             * 1.표시 필요부분
             * 결재 순번 - 다음번호?, 결재상태, 결재유형 표시, 기안자 name 직책/직급 rolemane jobtitle, 팀명 orgunit
             * 2.data 넘김
             * 	    "userNum": "1", >만듬..
             * 	    "userId": "jisun", > 있음
             * 	    "deptId": "LS_A00017", >부서코드
             * 	    "aprType": "A03001", > state
             * 	    "aprState": "진행" > ?
             * 	    aprType	결재타입				일반결재, 개인협조, 개인협조(병렬), 부서협조, 부서협조(병렬)) 등등.
             * aprState	결재상태				(진행, 대기) [접수자만 진행으로 주시면 됩니다.
             * */

            Log.d(TAG,"#### aprType:"+intent.getStringExtra("aprType"));

            Res_AP_IF_203_VO.result.apprSpList returnData = (Res_AP_IF_203_VO.result.apprSpList)intent.getExtras().getSerializable("lineData");
            String aprtype = intent.getStringExtra("aprtype"); // state..
            String aprstate = intent.getStringExtra("aprstate");//..

            Res_AP_IF_016_VO.result.apprLineList info = new Res_AP_IF_016_VO().new result().new apprLineList();

            //표시 부 결재 순번, 결재상태, 결재유형 표시, 기안자 직책/직급, 팀명
            info.setSn(String.valueOf(readRowData.getApprLineList().size()+1));//순번
            info.setActivity(getResources().getString(R.string.txt_approval_status_wait));//결재상태 - 진행
            info.setActionType(aprstate);//결재유형 - 기안 일반결재 완료 >>???
            info.setName(returnData.getName());//기안자
            info.setPosition(returnData.getJobTitle());//직책직급
            info.setDepartment(returnData.getOrgUnit());//팀명

            //서버전송시 필요 - 순번 , id, 부서id, 결재타입, 결재상태 - 진행
            info.setUserId(returnData.getIkenId());
            info.setDeptId(returnData.getDeptCode());
            info.setAprtype(aprtype);

            readRowData.getApprLineList().add(info);

            saveRecently(returnData);
        }
        adapter.notifyDataSetChanged();
        view_text_size_adj.changeTextSize();
    }

    //최근 검색 내용 저장
    private void saveRecently(Res_AP_IF_203_VO.result.apprSpList returnData){
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());
        String json = mPref.getStringPreference(Constants.PREF_RECENTLY_SEARCH_DATA);
        Gson g = new Gson();
        ArrayList<Res_AP_IF_203_VO.result.apprSpList> readData;
        if(TextUtils.isEmpty(json)){
            //새로 생성
            readData = new ArrayList<>();
        }else{
            readData = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_203_VO.result.apprSpList>>() {
            }.getType());
        }

        //동일 아이디는 삭제 후 저장
        for(int i=0;i<readData.size();i++){
            if(returnData.getIkenId().equals(readData.get(i).getIkenId())){
                readData.remove(i);
            }
        }

        //저장
        readData.add(0,returnData);

        mPref.setStringPreference(Constants.PREF_RECENTLY_SEARCH_DATA, g.toJson(readData));
    }
}
