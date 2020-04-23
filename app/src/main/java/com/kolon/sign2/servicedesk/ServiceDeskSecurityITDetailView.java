package com.kolon.sign2.servicedesk;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.AttatchFileDialog;
import com.kolon.sign2.dialog.ServiceDeskSecurityCancelDialog;
import com.kolon.sign2.dialog.ServiceDeskSecurityConfirmDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_028_VO;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 서비스데스크 - 보안 & IT 상세
 */
public class ServiceDeskSecurityITDetailView extends LinearLayout implements View.OnClickListener {
    private String TAG = "ServiceDeskSecurityITDetalView";
    private Context mContext;
    private TextView tv_title1, tv_title2, tv_title3, tv_title4;
    private TextView tv_name, tv_job, tv_team;
    private ImageView btn_call;

    private TextView tv_date, tv_detail, attach_file_num;

    private RecyclerView rv_service_detail2;
    private LinearLayout layout_history, layout_attach_file;
    private View layout_attach_file_div;
    private RelativeLayout progress_bar, popup_background;
    private TextView dialog_content;

    private Res_AP_IF_028_VO.result.approvalDetail readData;

    private ArrayList<Res_AP_IF_028_VO.result.approvalAttachList> attachList;
    private ArrayList<Res_AP_IF_028_VO.result.listappHistory> histories;
    private String key01, key02, talkId, talkCompany;

    private String menuId; //yong79, 승인버튼 제어를 위해 추가

    private LinearLayout layout_btn;

    //yong79. 승인 시 날짜 수정 여부
    private String usedate1Flag = "N";
    private String usedate2Flag = "N";

    private int position;
    private boolean isFinal = false;

    private ShimmerFrameLayout mShimmerLayout;

    public ServiceDeskSecurityITDetailView(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskSecurityITDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskSecurityITDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_service_desk_detail_2, this, false);

        popup_background = (RelativeLayout)v.findViewById(R.id.popup_background);
        popup_background.setVisibility(View.GONE);
        dialog_content = (TextView)v.findViewById(R.id.dialog_content);
        RelativeLayout btn_ok = (RelativeLayout)v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        tv_title1 = (TextView)v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView)v.findViewById(R.id.tv_title2);
        tv_title3 = (TextView)v.findViewById(R.id.tv_title3);
        tv_title4 = (TextView)v.findViewById(R.id.tv_title4);

        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.btn_service_desk_cancel);
        cancel.setOnClickListener(this);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.btn_service_desk_confirm);
        ok.setOnClickListener(this);

        ImageView btn_call = (ImageView) v.findViewById(R.id.btn_call);
        btn_call.setOnClickListener(this);

        progress_bar = (RelativeLayout) v.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);

        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_job = (TextView) v.findViewById(R.id.tv_job);
        tv_team = (TextView) v.findViewById(R.id.tv_team);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_detail = (TextView) v.findViewById(R.id.tv_detail);

        attach_file_num = (TextView) v.findViewById(R.id.attach_file_num);

        rv_service_detail2 = (RecyclerView) v.findViewById(R.id.rv_service_detail2);
        layout_history = (LinearLayout) v.findViewById(R.id.layout_history);
        layout_attach_file = (LinearLayout) v.findViewById(R.id.layout_attach_file);
        layout_attach_file.setOnClickListener(this);
        layout_attach_file_div = (View) v.findViewById(R.id.layout_attach_file_div);

        layout_btn = (LinearLayout) v.findViewById(R.id.layout_btn);

        CommonUtils.textSizeSetting(mContext, dialog_content);

        CommonUtils.textSizeSetting(mContext, tv_title1);
        CommonUtils.textSizeSetting(mContext, tv_title2);
        CommonUtils.textSizeSetting(mContext, tv_title3);
        CommonUtils.textSizeSetting(mContext, tv_title4);

        CommonUtils.textSizeSetting(mContext, tv_name);
        CommonUtils.textSizeSetting(mContext, tv_team);
        CommonUtils.textSizeSetting(mContext, tv_date);
        CommonUtils.textSizeSetting(mContext, tv_detail);

        addView(v);
    }

    public void setData(String key01, String key02, String menuId, int position, boolean isFinal) {
        this.key01 = key01;//userid
        this.key02 = key02;//docNo
        this.menuId = menuId;//docNo

        this.position = position;
        this.isFinal = isFinal;

        String type = "detailApp";
        String key03 = "BOAN";//SR : 요청, CO : 변경, BOAN : 보안 , WANTED : 납기승인, CANCEL_KGC : 취소승인,

        //showProgressBar();


        HashMap hm = new HashMap();
        hm.put("type", type);
        hm.put("key01", key01);
        hm.put("key02", key02);
        hm.put("key03", key03);

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getServiceDeskDetail(hm, new NetworkPresenter.getServiceDeskDetailListener() {
            @Override
            public void onResponse(Res_AP_IF_028_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            readData = result.getResult().getApprovalDetail().get(0);
                            Res_AP_IF_028_VO.result.approvalDetail data = result.getResult().getApprovalDetail().get(0);

                            String str = data.getReqName();
                            String name = "";
                            String team = "";
                            if (!TextUtils.isEmpty(str)) {
                                String temp[] = str.split("/");
                                if (temp.length == 2) {
                                    name = temp[0];
                                    team = temp[1].trim();
                                } else {
                                    name = str;
                                }
                            }

                            talkId = data.getCustomerId();
                            //사용자 아이디가 있는 경우 회사 코드 가져오기
                            if (!TextUtils.isEmpty(talkId)) {
                                getTalkCompany(talkId);
                            }

                            tv_name.setText(name);
                            tv_team.setText(team);

                            tv_date.setText(data.getReqDate());
                            tv_detail.setText(Html.fromHtml(data.getDetail()));


                            attachList = result.getResult().getApprovalAttachList();

                            if (attachList == null || attachList.size() == 0) {
                                layout_attach_file.setVisibility(View.GONE);
                                layout_attach_file_div.setVisibility(View.GONE);
                            } else {
                                layout_attach_file.setVisibility(View.VISIBLE);
                                layout_attach_file_div.setVisibility(View.VISIBLE);
                                attach_file_num.setText(String.valueOf(attachList.size()));
                            }

                            histories = result.getResult().getListappHistory();

                            if (histories == null || histories.size() == 0) {
                                layout_history.setVisibility(View.GONE);
                                rv_service_detail2.setVisibility(View.GONE);
                            } else {
                                layout_history.setVisibility(View.VISIBLE);
                                rv_service_detail2.setVisibility(View.VISIBLE);
                                ServiceDeskConfirmHistoryAdapter adapter = new ServiceDeskConfirmHistoryAdapter(histories);
                                rv_service_detail2.setAdapter(adapter);

//                                if (histories.size() < 3) {
//                                    rv_service_detail2.getLayoutParams().height = (int) DpiUtil.convertDpToPixel(mContext, 80 * histories.size());
//                                } else {
//                                    rv_service_detail2.getLayoutParams().height = (int) DpiUtil.convertDpToPixel(mContext, 80 * 4);
//                                }
                            }

                            if ("S03_01".equals(menuId)){
                                layout_btn.setVisibility(View.VISIBLE);
                            }

                            //hideProgressBar();
                            shimmerStop();
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
                //hideProgressBar();
                shimmerStop();
            }
        });

    }

    private void showProgressBar() {
        ((ServiceDeskDetailActivity)mContext).showProgressBar();
    }

    private void hideProgressBar() {
        ((ServiceDeskDetailActivity)mContext).hideProgressBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call:
                //Todo KolonTalk 인터페이스 추가하여 CompanyCode값 적용 필요
                CommonUtils.callKolonTalk(mContext, talkId, talkCompany);
                break;

            case R.id.btn_service_desk_cancel:
                clickCancel();
                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_service_desk_confirm:
                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_confirm), Toast.LENGTH_SHORT).show();
                clickOk();
                break;
            case R.id.layout_attach_file:
                ArrayList<AttachFileListVO> data = new ArrayList<>();
                for (int i = 0; i < attachList.size(); i++) {
                    AttachFileListVO vo = new AttachFileListVO();
                    vo.setName(attachList.get(i).getFileName());
                    vo.setUrl(attachList.get(i).getFileUrl());
                    data.add(vo);
                }

                AttatchFileDialog dialog = AttatchFileDialog.newInstance();
                dialog.setData(data);
                dialog.setInterface(new AttatchFileDialog.OnClickListener() {
                    @Override
                    public void selectPosition(int position) {
                        String docId = "mTotalSign_servicedesk_" + key02 + "_" + position;//서비스_문서id_파일순서
                        viewDoc(data.get(position), docId);
//                        Toast.makeText(mContext, "url:" + attachList.get(position).getFileUrl(), Toast.LENGTH_SHORT).show();
                        dialog.dismissAllowingStateLoss();
                    }
                });
                dialog.show(getFragmentManager(mContext), "");

                break;
            case R.id.btn_ok:
                popup_background.setVisibility(View.GONE);
                ((Activity) mContext).onBackPressed();
                break;
        }
    }

    private void viewDoc(AttachFileListVO data, String docId){
        String docUrl = data.getUrl();
        Log.d(TAG, "#### selectAttachFile url:" + docUrl+"  id:"+docId);
        NetworkPresenter presenter = new NetworkPresenter();
        showProgressBar();
        presenter.getDocViewerKey(docUrl, docId, new NetworkPresenter.getDocViewerKeyLisetner() {
            @Override
            public void onResponse(Res_Doc_Viewer result) {
                hideProgressBar();
                if(result != null){
                    String key = result.getKey();
                    AttachFileViewerDialog dialog = new AttachFileViewerDialog(mContext);
                    dialog.setData(data, key);
                    dialog.show();
                }
            }
        });
    }

    private void clickCancel() {
        ServiceDeskSecurityCancelDialog dialog = ServiceDeskSecurityCancelDialog.newInstance();
        dialog.setInterface(new ServiceDeskSecurityCancelDialog.ServiceDeskDialogListener() {
            @Override
            public void getMessage(String comment) {
                sendServerData("N", comment);
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

    private void clickOk() {
        ServiceDeskSecurityConfirmDialog dialog = ServiceDeskSecurityConfirmDialog.newInstance();
        dialog.setData(readData);
        dialog.setInterface(new ServiceDeskSecurityConfirmDialog.ServiceDeskDialogListener() {
            @Override
            public void getMessage(String comment, String content_date1_1, String content_date1_2, String content_date2_1, String content_date2_2) {
                //yong79. 승인 다이어로그에서 날짜가 변경되었는지 체크한다.
                if (!readData.getUsedate1Code().isEmpty()) {
                    if (!readData.getUsedate1From().equals(content_date1_1) || !readData.getUsedate1To().equals(content_date1_2)) {
                        readData.setUsedate1From(content_date1_1);
                        readData.setUsedate1To(content_date1_2);
                        usedate1Flag = "Y";
                    }
                }
                if (!readData.getUsedate2Code().isEmpty()) {
                    if (!readData.getUsedate2From().equals(content_date2_1) || !readData.getUsedate2To().equals(content_date2_2)) {
                        readData.setUsedate2From(content_date2_1);
                        readData.setUsedate2To(content_date2_2);
                        usedate2Flag = "Y";
                    }
                }

                sendServerData("Y", comment);
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

    private void sendServerData(String resultCd, String comment){
//        type		I/F 구분자				actionApp
//        key01		로그인 IKEN ID				ilovebbogle
//        key02		승인건 고유번호				SR00002735(요청/납기승인/취소승인), CO000002444(변경), TA00005230(테스트)
//        key03		승인여부				승인:OK, 반려:CANCEL
//        key04		의견				승인합니다~!
//        key05		공수 - 분
//        key06		공수 - 내용
//        key07		공수 - 일자

   //     String key04 = "";
        /*
        String key05 = "";
        String key06 = "";
        String key07 = "";

        showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        hm.put("type", "actionApp");
        hm.put("key01", key01);
        hm.put("key02", key02);
        hm.put("key03", key03);
        hm.put("key04", key04);
        hm.put("key05", key05);
        hm.put("key06", key06);
        hm.put("key07", key07);
        */


        /* yong79. 200302. 파라미터 변경 개선 
        userId		사용자 아이디
        pw		사용자 비밀번호
        seqno		승인건 고유번호
        result		승인여부	요청영역	요청영역
        appgubun		승인구분	요청내용	요청내용
        appstatus		승인단계
        comment		승인코멘트
        usedate1Code		사용일자1_코드
        usedate1From		사용일자1_시작
        usedate1To		사용일자1_종료
        usedate1Flag		사용일자1_수정여부
        usedate2Code		사용일자2_코드
        usedate2From		사용일자2_시작
        usedate2To		사용일자2_종료
        usedate2Flag		사용일자2_수정여부
        */

        showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        hm.put("userId", key01);
        hm.put("pw", "");
        hm.put("seqno", key02);
        hm.put("result", resultCd);
        hm.put("appgubun", readData.getAppGubun());
        hm.put("appstatus", readData.getAppStatus());
        hm.put("comment", comment);
        hm.put("usedate1Code", readData.getUsedate1Code());
        hm.put("usedate1From", readData.getUsedate1From());
        hm.put("usedate1To", readData.getUsedate1To());
        hm.put("usedate1Flag", usedate1Flag);
        hm.put("usedate2Code", readData.getUsedate2Code());
        hm.put("usedate2From", readData.getUsedate2From());
        hm.put("usedate2To", readData.getUsedate2To());
        hm.put("usedate2Flag", usedate2Flag);

        presenter.getServiceDeskBoanApprove(hm, new NetworkPresenter.getCommonListener() {
            @Override
            public void onResponse(Res_AP_Empty_VO result) {
                hideProgressBar();
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {


                            if(resultCd.equals("Y")){
                                ((ServiceDeskDetailActivity) mContext).checkRemainApproval(mContext.getResources().getString(R.string.txt_service_confirm), isFinal, position);
                                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_confirm), Toast.LENGTH_SHORT).show();
                            }else{
                                ((ServiceDeskDetailActivity) mContext).checkRemainApproval(mContext.getResources().getString(R.string.txt_service_cancel), isFinal, position);
                                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                            }
                            //((ServiceDeskDetailActivity)mContext).onBackPressed();
                            //((ServiceDeskDetailActivity) mContext).checkRemainApproval(title, isFinal, position);
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

    //error message
    private void viewMessage(String errMsg) {

        //에러가 생겼으므로 이전 이력은 지운다.
//        layout_no_data.setVisibility(View.VISIBLE);
//        layout_all_select.setVisibility(View.INVISIBLE);
//        rv.setVisibility(View.INVISIBLE);

        popup_background.setVisibility(View.VISIBLE);
        dialog_content.setText(errMsg);


//        TextDialog dialog;
//        dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
//        dialog.setCancelable(false);
//        dialog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show(getFragmentManager(mContext));
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

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, tv_title1);
        CommonUtils.changeTextSize(mContext, tv_title2);
        CommonUtils.changeTextSize(mContext, tv_title3);
        CommonUtils.changeTextSize(mContext, tv_title4);

        CommonUtils.changeTextSize(mContext, tv_name);
        CommonUtils.changeTextSize(mContext, tv_team);
        CommonUtils.changeTextSize(mContext, tv_date);
        CommonUtils.changeTextSize(mContext, tv_detail);
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
