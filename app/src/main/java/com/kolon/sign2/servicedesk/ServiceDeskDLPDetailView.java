package com.kolon.sign2.servicedesk;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.CommentDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_032_VO;
import com.kolon.sign2.vo.Res_AP_IF_033_VO;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 서비스데스크 - 문서반출상세
 */
public class ServiceDeskDLPDetailView extends LinearLayout implements View.OnClickListener, OnDocClickListener {
    private Context mContext;
    private TextView tv_name, tv_job, tv_team, tv_period, tv_offer, tv_offer_type, tv_subject, tv_reason;
    private TextView tv_title1, tv_title2, tv_title3, tv_title4, tv_title5, tv_title6, tv_title7, tv_title8, tv_title9;
    private RelativeLayout progress_bar, popup_background;
    private TextView dialog_content;
    private LinearLayout btn_more;
    private NetworkPresenter presenter;
    private ServiceDeskDetailDocRowView view_detect1, view_detect2;

    private ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList;

    private String appIdx, chkIdx, talkId, talkCompany, userId;

    private ShimmerFrameLayout mShimmerLayout;

    public ServiceDeskDLPDetailView(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskDLPDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskDLPDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_service_desk_detail_3, this, false);

        popup_background = (RelativeLayout)v.findViewById(R.id.popup_background);
        popup_background.setVisibility(View.GONE);
        dialog_content = (TextView)v.findViewById(R.id.dialog_content);
        RelativeLayout btn_ok = (RelativeLayout)v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        ImageView btncall = (ImageView) v.findViewById(R.id.btn_sub_call);
        btncall.setOnClickListener(this);
        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.btn_service_desk_cancel);
        cancel.setOnClickListener(this);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.btn_service_desk_confirm);
        ok.setOnClickListener(this);
        btn_more = (LinearLayout) v.findViewById(R.id.btn_more);
        btn_more.setOnClickListener(this);

        tv_title1 = (TextView)v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView)v.findViewById(R.id.tv_title2);
        tv_title3 = (TextView)v.findViewById(R.id.tv_title3);
        tv_title4 = (TextView)v.findViewById(R.id.tv_title4);
        tv_title5 = (TextView)v.findViewById(R.id.tv_title5);
        tv_title6 = (TextView)v.findViewById(R.id.tv_title6);
        tv_title7 = (TextView)v.findViewById(R.id.tv_title7);
        tv_title8 = (TextView)v.findViewById(R.id.tv_title8);
        tv_title9 = (TextView)v.findViewById(R.id.tv_title9);

        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_job = (TextView) v.findViewById(R.id.tv_job);
        tv_team = (TextView) v.findViewById(R.id.tv_team);

        tv_period = (TextView) v.findViewById(R.id.tv_period);
        tv_offer = (TextView) v.findViewById(R.id.tv_offer);
        tv_offer_type = (TextView) v.findViewById(R.id.tv_offer_type);
        tv_subject = (TextView) v.findViewById(R.id.tv_subject);
        tv_reason = (TextView) v.findViewById(R.id.tv_reason);

        view_detect1 = (ServiceDeskDetailDocRowView) v.findViewById(R.id.view_detect1);
        view_detect2 = (ServiceDeskDetailDocRowView) v.findViewById(R.id.view_detect2);
        view_detect1.setInterface(this);
        view_detect2.setInterface(this);

        progress_bar = (RelativeLayout) v.findViewById(R.id.progress_bar);
        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);

        CommonUtils.textSizeSetting(mContext, dialog_content);

        CommonUtils.textSizeSetting(mContext, tv_title1);
        CommonUtils.textSizeSetting(mContext, tv_title2);
        CommonUtils.textSizeSetting(mContext, tv_title3);
        CommonUtils.textSizeSetting(mContext, tv_title4);
        CommonUtils.textSizeSetting(mContext, tv_title5);
        CommonUtils.textSizeSetting(mContext, tv_title6);
        CommonUtils.textSizeSetting(mContext, tv_title7);
        CommonUtils.textSizeSetting(mContext, tv_title8);
        CommonUtils.textSizeSetting(mContext, tv_title9);

        CommonUtils.textSizeSetting(mContext, tv_name);
        CommonUtils.textSizeSetting(mContext, tv_job);
        CommonUtils.textSizeSetting(mContext, tv_team);
        CommonUtils.textSizeSetting(mContext, tv_period);
        CommonUtils.textSizeSetting(mContext, tv_offer);
        CommonUtils.textSizeSetting(mContext, tv_offer_type);
        CommonUtils.textSizeSetting(mContext, tv_subject);
        CommonUtils.textSizeSetting(mContext, tv_reason);

        addView(v);
    }


    public void setData(String userId, String appIdx, String chkIdx) {
        this.appIdx = appIdx;
        this.chkIdx = chkIdx;

        //showProgressBar();
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("chkIdx", chkIdx);
        hm.put("appIdx", appIdx);

        presenter = new NetworkPresenter();
        presenter.getServiceDeskDlpDetail(hm, new NetworkPresenter.getServiceDeskDlpDetailListener() {
            @Override
            public void onResponse(Res_AP_IF_032_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            Res_AP_IF_032_VO.result.dlpDetailClass readData = result.getResult().getDlpDetail();

                            String start = "";
                            String end = "";
                            if (!TextUtils.isEmpty(readData.getUsesDate())) {
                                String[] temp = readData.getUsesDate().split(" ");
                                start = temp[0];
                            }
                            if (!TextUtils.isEmpty(readData.getUseeDate())) {
                                String[] temp = readData.getUsesDate().split(" ");
                                end = temp[0];
                            }
                            String period = start + " ~ " + end;
                            tv_period.setText(period);
                            tv_offer.setText(readData.getProReceiver());
                            tv_offer_type.setText(readData.getProMeans());
                            tv_subject.setText(readData.getAppTitle());
                            tv_reason.setText(readData.getAppContent());

                            detectList = result.getResult().getDlpDetailFile();

                            drawDetectList(detectList);


                            ArrayList<Res_AP_IF_032_VO.result.dlpDetailOrder> detailOrder = result.getResult().getDlpDetailOrder();
                            if(detailOrder != null && detailOrder.size() != 0){
                                tv_name.setText(detailOrder.get(0).getOrderNm());

                                String job = detailOrder.get(0).getOrderJbtt();
                                if (!TextUtils.isEmpty(detailOrder.get(0).getOrderDuty())) {
                                    job = detailOrder.get(0).getOrderDuty() + "/" + detailOrder.get(0).getOrderJbtt();
                                }
                                tv_job.setText(job);
                                tv_team.setText(detailOrder.get(0).getOrderDept());
                                talkId = detailOrder.get(0).getOrderId();
                                //사용자 아이디가 있는 경우 회사 코드 가져오기
                                if (!TextUtils.isEmpty(talkId)) {
                                    getTalkCompany(talkId);
                                }

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

                popup_background.setVisibility(View.VISIBLE);
                dialog_content.setText(errMsg);
//                viewMessage(errMsg);
                //hideProgressBar();
                shimmerStop();
            }
        });
    }

    private void drawDetectList(ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList) {
        //첨부파일 2개이하일 경우 더보기 없음
        btn_more.setVisibility(View.GONE);
        if (detectList == null || detectList.size() == 0) {
            view_detect1.setVisibility(View.GONE);
            view_detect2.setVisibility(View.GONE);
        } else if (detectList.size() == 1) {
            view_detect1.setVisibility(View.VISIBLE);
            view_detect1.setData(detectList.get(0), 0);
            view_detect2.setVisibility(View.GONE);
        } else if (detectList.size() == 2) {
            view_detect1.setVisibility(View.VISIBLE);
            view_detect2.setVisibility(View.VISIBLE);
            view_detect1.setData(detectList.get(0), 0);
            view_detect2.setData(detectList.get(1), 1);
        } else {
            view_detect1.setVisibility(View.VISIBLE);
            view_detect2.setVisibility(View.VISIBLE);
            view_detect1.setData(detectList.get(0), 0);
            view_detect2.setData(detectList.get(1), 1);

            btn_more.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sub_call:
                //Todo KolonTalk 인터페이스 추가하여 CompanyCode값 적용 필요ㅛㅐㅜㅎ
                CommonUtils.callKolonTalk(mContext, talkId, talkCompany);
                break;
            case R.id.btn_service_desk_cancel:
                clickCancel();
                Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_service_desk_confirm:
                clickOk();
                break;
            case R.id.btn_more:
                ServiceDeskDLPListPopup popup = ServiceDeskDLPListPopup.newInstance();
                popup.setData(detectList);
                popup.setInterface(this);
                popup.show(CommonUtils.getFragmentManager(mContext), "");
                break;
            case R.id.btn_ok:
                popup_background.setVisibility(View.GONE);
                ((Activity) mContext).onBackPressed();
                break;
        }
    }

    private void clickCancel() {
        String title = mContext.getResources().getString(R.string.txt_service_cancel);
        String okStr = mContext.getResources().getString(R.string.txt_service_cancel);

        CommentDialog dialog = CommentDialog.newInstance();
        dialog.setData(title, okStr, true);
        dialog.setInterface(new CommentDialog.CommentDialogListener() {

            @Override
            public void getMessage(String comment) {
                //send message..
                sendServer("300", comment);//appJob 결재 : 200, 반려 : 300
            }
        });
        dialog.show(CommonUtils.getFragmentManager(mContext));
    }

    private void clickOk() {
        boolean confirmOk = true;
        for (int i = 0; i < detectList.size(); i++) {
            if (!detectList.get(i).isChecked) {
                confirmOk = false;
            }
        }
        if (confirmOk) {
            String title = mContext.getResources().getString(R.string.txt_service_confirm);
            String okStr = mContext.getResources().getString(R.string.txt_service_confirm);

            CommentDialog dialog = CommentDialog.newInstance();
            dialog.setData(title, okStr, false);
            dialog.setInterface(new CommentDialog.CommentDialogListener() {

                @Override
                public void getMessage(String comment) {
                    //send message..
                    sendServer("200", comment);//appJob 결재 : 200, 반려 : 300
                }
            });
            dialog.show(CommonUtils.getFragmentManager(mContext));
        } else {
            viewMessage(mContext.getResources().getString(R.string.txt_service_desk_file_check));
        }
    }

    /**
     * 승인 반려 서버 전송
     *
     * @param appJob 결재 : 200, 반려 : 300
     *               appIdx		신청서 번호
     *               chkIdx		결재서 번호
     *               userId		IKEN_ID
     *               appJob		결재 작업		결재 : 200, 반려 : 300
     *               appReason	결재 사유
     */
    private void sendServer(String appJob, String comment) {
        HashMap hm = new HashMap();
        hm.put("appIdx", appIdx);
        hm.put("chkIdx", chkIdx);
        hm.put("userId", userId);
        hm.put("appJob", appJob);
        hm.put("appReason", comment);

        showProgressBar();
        presenter.getServiceDeskDlpApprove(hm, new NetworkPresenter.getServiceDeskDlpApproveListener() {
            @Override
            public void onResponse(Res_AP_IF_033_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            if (appJob.equals("200")) {
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

    private void viewDoc(AttachFileListVO data, String docId) {
        String docUrl = data.getUrl();

     //   Log.d("ServiceDeskDLPDetailView", "#### selectAttachFile   url:" + docUrl + "  id:" + docId);

        showProgressBar();
        presenter.getDocViewerKey(docUrl, docId, new NetworkPresenter.getDocViewerKeyLisetner() {
            @Override
            public void onResponse(Res_Doc_Viewer result) {
                hideProgressBar();
                if (result != null) {
                    String key = result.getKey();
                    AttachFileViewerDialog dialog = new AttachFileViewerDialog(mContext);
                    dialog.setData(data, key);
                    dialog.show();
                }
            }
        });
    }
    @Override
    public void onFileClick(int position, String url) {
        detectList.get(position).isChecked = true;

        AttachFileListVO data = new AttachFileListVO();
        data.setName(detectList.get(position).getFileName());
        data.setUrl(url);
        String docId = "mTotalSign_servicedesk_" + appIdx + "_" + position;//서비스_문서id_파일순서
        viewDoc(data, docId);

        drawDetectList(detectList);
    }

    @Override
    public void onChecked(int position, boolean isChecked) {
        detectList.get(position).isChecked = isChecked;
        drawDetectList(detectList);
    }

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, tv_title1);
        CommonUtils.changeTextSize(mContext, tv_title2);
        CommonUtils.changeTextSize(mContext, tv_title3);
        CommonUtils.changeTextSize(mContext, tv_title4);
        CommonUtils.changeTextSize(mContext, tv_title5);
        CommonUtils.changeTextSize(mContext, tv_title6);
        CommonUtils.changeTextSize(mContext, tv_title7);
        CommonUtils.changeTextSize(mContext, tv_title8);
        CommonUtils.changeTextSize(mContext, tv_title9);

        CommonUtils.changeTextSize(mContext, tv_name);
        CommonUtils.changeTextSize(mContext, tv_job);
        CommonUtils.changeTextSize(mContext, tv_team);
        CommonUtils.changeTextSize(mContext, tv_period);
        CommonUtils.changeTextSize(mContext, tv_offer);
        CommonUtils.changeTextSize(mContext, tv_offer_type);
        CommonUtils.changeTextSize(mContext, tv_subject);
        CommonUtils.changeTextSize(mContext, tv_reason);
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
