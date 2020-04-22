package com.kolon.sign2.servicedesk;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_028_VO;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ServiceDeskDetailRow1 extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private NetworkPresenter presenter;
    private TextView tv_key, tv_key_title, tv_name, tv_job, tv_team, tv_state, tv_officer, tv_content, tv_want_date, tv_reg_date, tv_end_date;
    private TextView tv_title1, tv_title2, tv_title3, tv_title4, tv_title5, tv_title6, tv_title7, tv_title8, tv_title9, tv_title10, tv_title11;
    private RelativeLayout progress_bar;
    private EditText edit1, comment_edit;
    private LinearLayout layout_history;
    private RecyclerView rv_history;

    private String key01, key02, talkId, talkCompany;

    public ServiceDeskDetailRow1(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskDetailRow1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskDetailRow1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_service_desk_detail_1, this, false);
        presenter = new NetworkPresenter();
        ImageView btnCall = (ImageView) v.findViewById(R.id.btn_sub_call);
        btnCall.setOnClickListener(this);
        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.btn_service_desk_cancel);
        cancel.setOnClickListener(this);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.btn_service_desk_confirm);
        ok.setOnClickListener(this);
        View div = (View) v.findViewById(R.id.view_div);

        RelativeLayout layout_end_date = (RelativeLayout) v.findViewById(R.id.layout_end_date);
        layout_end_date.setOnClickListener(this);

        progress_bar = (RelativeLayout) v.findViewById(R.id.progress_bar);

        tv_title1 = (TextView)v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView)v.findViewById(R.id.tv_title2);
        tv_title3 = (TextView)v.findViewById(R.id.tv_title3);
        tv_title4 = (TextView)v.findViewById(R.id.tv_title4);
        tv_title5 = (TextView)v.findViewById(R.id.tv_title5);
        tv_title6 = (TextView)v.findViewById(R.id.tv_title6);
        tv_title7 = (TextView)v.findViewById(R.id.tv_title7);
        tv_title8 = (TextView)v.findViewById(R.id.tv_title8);
        tv_title9 = (TextView)v.findViewById(R.id.tv_title9);
        tv_title10 = (TextView)v.findViewById(R.id.tv_title10);
        tv_title11 = (TextView)v.findViewById(R.id.tv_title11);

        // 구분값 - 메뉴클릭시 넘어옴 ////////////////////?? test
        String type = "";
        key01 = "";
        key02 = "";
        /////////////////////////////////////

        if ("".equals(type)) {
            //구분이 협업 CAB일경우 하단 승인버튼만 존재
            cancel.setVisibility(View.GONE);
            div.setVisibility(View.GONE);
        }

        tv_key = (TextView) v.findViewById(R.id.tv_key);
        tv_key_title = (TextView) v.findViewById(R.id.tv_key_title);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_job = (TextView) v.findViewById(R.id.tv_job);
        tv_team = (TextView) v.findViewById(R.id.tv_team);
        tv_state = (TextView) v.findViewById(R.id.tv_state);
        tv_officer = (TextView) v.findViewById(R.id.tv_officer);
        tv_content = (TextView) v.findViewById(R.id.tv_content);
        tv_want_date = (TextView) v.findViewById(R.id.tv_want_date);
        tv_reg_date = (TextView) v.findViewById(R.id.tv_reg_date);
        tv_end_date = (TextView) v.findViewById(R.id.tv_end_date);

        rv_history = (RecyclerView) v.findViewById(R.id.rv_history);
        layout_history = (LinearLayout) v.findViewById(R.id.layout_history);


        edit1 = (EditText) v.findViewById(R.id.edit1);
        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        comment_edit = (EditText) v.findViewById(R.id.comment_edit);
        comment_edit.setHint("(" + mContext.getResources().getString(R.string.txt_approval_must) + ") " + mContext.getResources().getString(R.string.txt_approval_txt_2));
        comment_edit.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    comment_edit.setHint("");
                } else {
                    comment_edit.setHint("(" + mContext.getResources().getString(R.string.txt_approval_must) + ") " + mContext.getResources().getString(R.string.txt_approval_txt_2));
                }
            }
        });
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        CommonUtils.textSizeSetting(mContext, tv_title1);
        CommonUtils.textSizeSetting(mContext, tv_title2);
        CommonUtils.textSizeSetting(mContext, tv_title3);
        CommonUtils.textSizeSetting(mContext, tv_title4);
        CommonUtils.textSizeSetting(mContext, tv_title5);
        CommonUtils.textSizeSetting(mContext, tv_title6);
        CommonUtils.textSizeSetting(mContext, tv_title7);
        CommonUtils.textSizeSetting(mContext, tv_title8);
        CommonUtils.textSizeSetting(mContext, tv_title9);
        CommonUtils.textSizeSetting(mContext, tv_title10);
        CommonUtils.textSizeSetting(mContext, tv_title11);

        CommonUtils.textSizeSetting(mContext, tv_key);
        CommonUtils.textSizeSetting(mContext, tv_key_title);
        CommonUtils.textSizeSetting(mContext, tv_name);
        CommonUtils.textSizeSetting(mContext, tv_job);
        CommonUtils.textSizeSetting(mContext, tv_team);
        CommonUtils.textSizeSetting(mContext, tv_state);
        CommonUtils.textSizeSetting(mContext, tv_officer);
        CommonUtils.textSizeSetting(mContext, tv_content);
        CommonUtils.textSizeSetting(mContext, tv_want_date);
        CommonUtils.textSizeSetting(mContext, tv_reg_date);
        CommonUtils.textSizeSetting(mContext, tv_end_date);

        setData();

        addView(v);
    }

    //필수값을 체크 - 투입공수, 내용, 완료일
    private boolean checkInput() {
        //투입공수 체크
        String min = edit1.getText().toString();
        if (TextUtils.isEmpty(min) || Integer.parseInt(min) == 0) {
            return false;
        }
        //내용 체크
        if (TextUtils.isEmpty(comment_edit.getText().toString())) {
            return false;
        }
        //완료일 체크
        String date = tv_end_date.getText().toString();
        if (TextUtils.isEmpty(date)) {
            return false;
        }
        //날짜 체크 - 오늘보다 이전 날짜일 경우
        date = date.replace("-", "");
        Date cDate = new Date();
        String today = new SimpleDateFormat("yyyyMMdd").format(cDate);
        if (Integer.parseInt(today) > Integer.parseInt(date)) {
            return false;
        }

        return true;
    }


    public void setData() {
        CommonUtils.changeTextSize(mContext, tv_title1);
        CommonUtils.changeTextSize(mContext, tv_title2);
        CommonUtils.changeTextSize(mContext, tv_title3);
        CommonUtils.changeTextSize(mContext, tv_title4);
        CommonUtils.changeTextSize(mContext, tv_title5);
        CommonUtils.changeTextSize(mContext, tv_title6);
        CommonUtils.changeTextSize(mContext, tv_title7);
        CommonUtils.changeTextSize(mContext, tv_title8);
        CommonUtils.changeTextSize(mContext, tv_title9);
        CommonUtils.changeTextSize(mContext, tv_title10);
        CommonUtils.changeTextSize(mContext, tv_title11);

        CommonUtils.changeTextSize(mContext, tv_key);
        CommonUtils.changeTextSize(mContext, tv_key_title);
        CommonUtils.changeTextSize(mContext, tv_name);
        CommonUtils.changeTextSize(mContext, tv_job);
        CommonUtils.changeTextSize(mContext, tv_team);
        CommonUtils.changeTextSize(mContext, tv_state);
        CommonUtils.changeTextSize(mContext, tv_officer);
        CommonUtils.changeTextSize(mContext, tv_content);
        CommonUtils.changeTextSize(mContext, tv_want_date);
        CommonUtils.changeTextSize(mContext, tv_reg_date);
        CommonUtils.changeTextSize(mContext, tv_end_date);

        String type = "detailApp";
        String key01 = this.key01;
        String key02 = this.key02;
        String key03 = "TA";

        HashMap hm = new HashMap();
        hm.put("type", type);
        hm.put("key01", key01);
        hm.put("key02", key02);
        hm.put("key03", key03);

        showProgressBar();

        presenter.getServiceDeskDetail(hm, new NetworkPresenter.getServiceDeskDetailListener() {
            @Override
            public void onResponse(Res_AP_IF_028_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            Res_AP_IF_028_VO.result.approvalDetail readData = result.getResult().getApprovalDetail().get(0);

//                       -     procId	번호	String	번호
//                       -     reqType	요청구분	String	요청구분
//                       -o     phaseStatus	단계/상태	String	단계/상태
//                       -o     customer	요청자	String	요청자
//                       -o     customerCompany	요청자 회사	String	요청자 회사
//                       -o     customerDepart	요청자 부서	String	요청자 부서
//                       -     customerMobile	요청자 모바일	String	요청자 모바일
//                       -     company	대상회사	String	대상회사
//                       -o     assignee	담당자(정)	String	담당자(정)
//                       -     title	제목	String	제목
//                       -o     content	내용	String	내용
//                        o    hopeTime	희망완료일시	String	희망완료일시
//                        o    regDate	등록일시	String	등록일시
                            //        tv_name, tv_job, tv_team, tv_state, tv_officer, tv_content, tv_want_date, tv_reg_date, tv_end_date;

                            tv_key.setText(readData.getProcId());
                            tv_key_title.setText(readData.getReqType());

                            tv_name.setText(readData.getCustomer());
                            tv_job.setText(readData.getCustomerTitle());
                            tv_team.setText(readData.getCustomerDepart());
                            tv_state.setText(readData.getPhaseStatus());
                            tv_officer.setText(readData.getAssignee());
                            tv_content.setText(readData.getContent());
                            tv_want_date.setText(readData.getHopeTime());
                            tv_reg_date.setText(readData.getRegDate());
                            tv_end_date.setText("");


                            talkId = readData.getCustomerId();
                            //사용자 아이디가 있는 경우 회사 코드 가져오기
                            if (!TextUtils.isEmpty(talkId)) {
                                getTalkCompany(talkId);
                            }



//                            ArrayList<Res_AP_IF_028_VO.result.listappHistory> histories = new ArrayList<>();
//                            //history test..
//                            Res_AP_IF_028_VO.result.listappHistory data = new Res_AP_IF_028_VO().new result().new listappHistory();
//
//                            data.setSeq("1");
//                            data.setAppUser("김태희");
//                            data.setAppDate("");
//                            data.setAppStatus("결재 진행중");
//                            histories.add(data);
//                            data = new Res_AP_IF_028_VO().new result().new listappHistory();
//
//                            data.setSeq("2");
//                            data.setAppUser("SAP재무회계 (임선빈, 한현우)");
//                            data.setAppDate("2019-10-22");
//                            data.setAppStatus("결재");
//                            histories.add(data);
//                            data = new Res_AP_IF_028_VO().new result().new listappHistory();
//
//                            data.setSeq("3");
//                            data.setAppUser("홍길동 (SAP재무회계)");
//                            data.setAppDate("2019-10-22");
//                            data.setAppStatus("결재 대기");
//                            histories.add(data);
//                            data = new Res_AP_IF_028_VO().new result().new listappHistory();
//
//                            data.setSeq("4");
//                            data.setAppUser("홍길동 (SAP재무회계)");
//                            data.setAppDate("2019-10-22");
//                            data.setAppStatus("결재 대기");
//                            histories.add(data);
//                            data = new Res_AP_IF_028_VO().new result().new listappHistory();
//
//                            data.setSeq("5");
//                            data.setAppUser("홍길동 (SAP재무회계)");
//                            data.setAppDate("2019-10-22");
//                            data.setAppStatus("결재 대기");
//                            histories.add(data);
                            /////////////


                            ArrayList<Res_AP_IF_028_VO.result.listappHistory> histories = result.getResult().getListappHistory();
                            if (histories == null || histories.size() == 0 || TextUtils.isEmpty(histories.get(0).getSeq())) {
                                layout_history.setVisibility(View.GONE);
                                rv_history.setVisibility(View.GONE);
                            } else {
                                layout_history.setVisibility(View.VISIBLE);
                                rv_history.setVisibility(View.VISIBLE);
                                ServiceDeskConfirmHistoryAdapter adapter = new ServiceDeskConfirmHistoryAdapter(histories);
                                rv_history.setAdapter(adapter);

                                if (histories.size() < 3) {
                                    rv_history.getLayoutParams().height = (int) DpiUtil.convertDpToPixel(mContext, 80 * histories.size());
                                } else {
                                    rv_history.getLayoutParams().height = (int) DpiUtil.convertDpToPixel(mContext, 80 * 4);
                                }
                            }

                            hideProgressBar();
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
                hideProgressBar();
            }
        });
    }

    /**
     * 승인 반려 전송
     *
     * @param key03 승인:OK, 반려:CANCEL
     */
    private void sendServerData(String key03) {
//        "type":"actionApp", >>>고정?
//                "key01":"ilovebbogle",
//                "key02":"SR00201180",
//                "key03":"OK",
//                "key04":"승인테스트입니다." >> comment??
        String type = "actionApp";
        String key01 = this.key01;
        String key02 = this.key02;
//        String key03 ="";
        String key04 = "승인테스트입니다.";

        showProgressBar();
        HashMap hm = new HashMap();
        hm.put("type", type);
        hm.put("key01", key01);
        hm.put("key02", key02);
        hm.put("key03", key03);
        hm.put("key04", key04);

        presenter.getServiceDeskApprove(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            if (key03.equals("OK")) {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_confirm), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                            }
                            hideProgressBar();
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
                hideProgressBar();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sub_call:
                //Todo KolonTalk 인터페이스 추가하여 CompanyCode값 적용 필요
                CommonUtils.callKolonTalk(mContext, talkId,talkCompany);
                break;
            case R.id.btn_service_desk_cancel:
                if (checkInput()) {
                    sendServerData("CANCEL");//승인:OK, 반려:CANCEL
                } else {
                    viewMessage(mContext.getResources().getString(R.string.txt_service_desk_err_info));
                }
                break;
            case R.id.btn_service_desk_confirm:
                if (checkInput()) {
                    sendServerData("OK");//승인:OK, 반려:CANCEL
                } else {
                    viewMessage(mContext.getResources().getString(R.string.txt_service_desk_err_info));
                }
                break;
            case R.id.layout_end_date:
                setDate();
                break;
        }
    }

    private void setDate() {
        DatePicker mDate = new DatePicker(mContext);
        DatePickerDialog dialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                tv_end_date.setText(format.format(calendar.getTime()));
            }
        }, mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth());
        dialog.show();
    }

    private void viewMessage(String errMsg) {
        TextDialog dialog;
        dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show(CommonUtils.getFragmentManager(mContext));
    }


    private void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

//    procId	번호	String	번호		테스트	테스트
//    reqType	요청구분	String	요청구분		테스트	테스트
//    phaseStatus	단계/상태	String	단계/상태		테스트	테스트
//    customer	요청자	String	요청자		테스트	테스트
//    customerCompany	요청자 회사	String	요청자 회사		테스트	테스트
//    customerDepart	요청자 부서	String	요청자 부서		테스트	테스트
//    customerMobile	요청자 모바일	String	요청자 모바일		테스트	테스트
//    company	대상회사	String	대상회사		테스트	테스트
//    assignee	담당자(정)	String	담당자(정)		테스트	테스트
//    title	제목	String	제목		테스트	테스트
//    content	내용	String	내용		테스트	테스트
//    hopeTime	희망완료일시	String	희망완료일시		테스트	테스트
//    regDate	등록일시	String	등록일시		테스트	테스트

    //iken talk에 필요한 회사코드 가져오기
    public void getTalkCompany(String userId){
        HashMap hm = new HashMap();
        hm.put("userId",userId);

        talkCompany = "";
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getUserCompSearch(hm, new NetworkPresenter.getUserCompSearchResult() {
            @Override
            public void onResponse(Res_AP_IF_107_VO result) {
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            talkCompany = result.getResult().getCompanyCd();
                        }
                    }
                }
            }
        });
    }

}
