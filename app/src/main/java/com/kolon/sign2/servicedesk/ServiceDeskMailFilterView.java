package com.kolon.sign2.servicedesk;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.AttatchFileDialog;
import com.kolon.sign2.dialog.CommentDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_035_VO;
import com.kolon.sign2.vo.Res_AP_IF_036_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceDeskMailFilterView extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    private String TAG = "ServiceDeskMailFilterView";
    private Context mContext;
    private NetworkPresenter presenter;
    private TextView tv_sender, tv_receiver, tv_title, tv_send_date, tv_detect_detail, attach_file_num;
    private TextView tv_title1, tv_title2, tv_title3, tv_title4, tv_title5, tv_title6, tv_title7;
    private LinearLayout layout_detect_detail, layout_attach_file;
    private View layout_attach_file_div;
    private RelativeLayout progress_bar, popup_background;
    private TextView dialog_content;
    private WebView wb;
    private Res_AP_IF_035_VO.result.mailFilterDetailClass readData;
    private ArrayList<Res_AP_IF_035_VO.result.files> filelist;

    private String rv, seq;

    private ScrollView sv;

    private ShimmerFrameLayout mShimmerLayout;

    private int position;
    private boolean isFinal = false;

    public ServiceDeskMailFilterView(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskMailFilterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskMailFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_service_desk_detail_4, this, false);

        popup_background = (RelativeLayout) v.findViewById(R.id.popup_background);
        popup_background.setVisibility(View.GONE);
        dialog_content = (TextView) v.findViewById(R.id.dialog_content);
        RelativeLayout btn_ok = (RelativeLayout) v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);

        tv_title1 = (TextView) v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView) v.findViewById(R.id.tv_title2);
        tv_title3 = (TextView) v.findViewById(R.id.tv_title3);
        tv_title4 = (TextView) v.findViewById(R.id.tv_title4);
        tv_title5 = (TextView) v.findViewById(R.id.tv_title5);
        tv_title6 = (TextView) v.findViewById(R.id.tv_title6);
        tv_title7 = (TextView) v.findViewById(R.id.tv_title7);

        sv = (ScrollView) v.findViewById(R.id.sv);

        tv_sender = (TextView) v.findViewById(R.id.tv_sender);
        tv_receiver = (TextView) v.findViewById(R.id.tv_receiver);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_send_date = (TextView) v.findViewById(R.id.tv_send_date);
        tv_detect_detail = (TextView) v.findViewById(R.id.tv_detect_detail);

        attach_file_num = (TextView) v.findViewById(R.id.attach_file_num);
        progress_bar = (RelativeLayout) v.findViewById(R.id.progress_bar);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);

        layout_attach_file = (LinearLayout) v.findViewById(R.id.layout_attach_file);
        layout_attach_file.setOnClickListener(this);
        layout_attach_file_div = (View) v.findViewById(R.id.layout_attach_file_div);

        LinearLayout ok = (LinearLayout) v.findViewById(R.id.btn_service_desk_confirm);
        ok.setOnClickListener(this);
        LinearLayout cancel = (LinearLayout) v.findViewById(R.id.btn_service_desk_cancel);
        cancel.setOnClickListener(this);

        LinearLayout btn_view_main_content = (LinearLayout) v.findViewById(R.id.btn_view_main_content);
        btn_view_main_content.setOnClickListener(this);

        layout_detect_detail = (LinearLayout) v.findViewById(R.id.layout_detect_detail);

        wb = (WebView) v.findViewById(R.id.wv_main_detal);
        wb.setOnTouchListener(this);
        wb.getSettings().setJavaScriptEnabled(true);

        presenter = new NetworkPresenter();
        CommonUtils.textSizeSetting(mContext, dialog_content);

        CommonUtils.textSizeSetting(mContext, tv_title1);
        CommonUtils.textSizeSetting(mContext, tv_title2);
        CommonUtils.textSizeSetting(mContext, tv_title3);
        CommonUtils.textSizeSetting(mContext, tv_title4);
        CommonUtils.textSizeSetting(mContext, tv_title5);
        CommonUtils.textSizeSetting(mContext, tv_title6);
        CommonUtils.textSizeSetting(mContext, tv_title7);

        CommonUtils.textSizeSetting(mContext, tv_sender);
        CommonUtils.textSizeSetting(mContext, tv_receiver);
        CommonUtils.textSizeSetting(mContext, tv_title);
        CommonUtils.textSizeSetting(mContext, tv_send_date);
        CommonUtils.textSizeSetting(mContext, tv_detect_detail);

        addView(v);
    }

    public void setData(String rv, String seq, int position, boolean isFinal) {

        this.rv = rv+"@kolon.com";
        this.seq = seq;

        this.position = position;
        this.isFinal = isFinal;

        //showProgressBar();

        HashMap hm = new HashMap();
        hm.put("rv", this.rv);
        hm.put("cmd", "read");
        hm.put("seq", seq);
        hm.put("format", "app");
        hm.put("viewMode", "html");

        presenter.getServiceDeskMailFilterDetail(hm, new NetworkPresenter.getServiceDeskMainFilterDetailListener() {
            @Override
            public void onResponse(Res_AP_IF_035_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            readData = result.getResult().getMailFilterDetail();

                            tv_sender.setText(readData.getSender());
                            tv_receiver.setText(readData.getReceiver());
                            tv_title.setText(readData.getSubject());
                            tv_send_date.setText(readData.getDate());

                            if (TextUtils.isEmpty(readData.getPrvInfo())) {
                                layout_detect_detail.setVisibility(View.GONE);
                                tv_detect_detail.setVisibility(View.GONE);
                            } else {
                                String str = readData.getPrvInfo();
                                str = str.replaceAll(": ", "\n");

                                layout_detect_detail.setVisibility(View.VISIBLE);
                                tv_detect_detail.setVisibility(View.VISIBLE);
                                tv_detect_detail.setText(str);
                            }

                            wb.loadData(readData.getContents(), "text/html;charset=UTF-8", null);

                            filelist = result.getResult().getFiles();

                            if (filelist == null || filelist.size() == 0) {
                                layout_attach_file.setVisibility(View.GONE);
                                layout_attach_file_div.setVisibility(View.GONE);
                            } else {
                                layout_attach_file.setVisibility(View.VISIBLE);
                                layout_attach_file_div.setVisibility(View.VISIBLE);
                                attach_file_num.setText(String.valueOf(filelist.size()));
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

                //         viewMessage(errMsg);
                //hideProgressBar();
                shimmerStop();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_service_desk_cancel:
                clickCancel();
                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_service_desk_confirm:
                clickOk();
                break;
            case R.id.btn_view_main_content:
                if (readData == null) {
                    return;
                }
                ServiceDeskMailFilterPopup popup = ServiceDeskMailFilterPopup.newInstance();
                popup.setData(readData.getContents());
                popup.show(getFragmentManager(mContext), "");
                break;
            case R.id.layout_attach_file:
                ArrayList<AttachFileListVO> data = new ArrayList<>();
                if (filelist == null || filelist.size() == 0) return;
                for (int i = 0; i < filelist.size(); i++) {
                    AttachFileListVO vo = new AttachFileListVO();
                    vo.setChecked(false);
                    vo.setSize(filelist.get(i).getSize());
                    vo.setName(filelist.get(i).getName());
                    vo.setUrl(filelist.get(i).getLink());
                    vo.setChecked(filelist.get(i).isChecked);
                    data.add(vo);
                }

                AttatchFileDialog dialog = AttatchFileDialog.newInstance();
                dialog.setData(data, true);
                dialog.setInterface(new AttatchFileDialog.OnClickListener() {
                    @Override
                    public void selectPosition(int position) {
                        //   Toast.makeText(mContext, "url:" + filelist.get(position).getLink(), Toast.LENGTH_SHORT).show();
                        dialog.dismissAllowingStateLoss();
                        String docId = "mTotalSign_servicedesk_" + seq + "_" + position;//서비스_문서id_파일순서
                        viewDoc(data.get(position), docId);
                    }
                });
                dialog.setCheckListener(new AttatchFileDialog.OnCheckClickListener() {
                    @Override
                    public void checkPosition(int position, boolean isChecked) {
                        data.get(position).setChecked(isChecked);
                        filelist.get(position).isChecked = isChecked;

                        //모두 check되면 버튼 색을 변화
                        boolean checkingFail = false;
                        for (int i = 0; i < filelist.size(); i++) {
                            if (!filelist.get(i).isChecked) {
                                checkingFail = true;
                                break;
                            }
                        }

                        if (checkingFail) {
                            dialog.setEnableBtn(false);
                        } else {
                            dialog.setEnableBtn(true);
                        }
                    }

                    @Override
                    public void onCancel() {
                        clickCancel();
                    }

                    @Override
                    public void onConfirm() {
                        clickOk();
                        /*
                        //모두 체크되어야만 진행
                        boolean confirmOk = true;
                        for (int i = 0; i < filelist.size(); i++) {
                            if (!filelist.get(i).isChecked) {
                                confirmOk = false;
                                break;
                            }
                        }
                        if (confirmOk) {
                            //send server....
                            dialog.dismissAllowingStateLoss();

                            String title = mContext.getResources().getString(R.string.txt_service_confirm);
                            String okStr = mContext.getResources().getString(R.string.txt_service_confirm);
                            CommentDialog dialog = CommentDialog.newInstance();
                            dialog.setData(title, okStr, true);
                            dialog.setInterface(new CommentDialog.CommentDialogListener() {

                                @Override
                                public void getMessage(String comment) {
                                    //send message..
                                    sendServerConfirm(comment);
                                }
                            });

                            //sendServerConfirm();
                        } else {
                            viewMessage(mContext.getResources().getString(R.string.txt_service_desk_file_check));
                        }
                        */
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

    private void viewDoc(AttachFileListVO data, String docId) {
        String docUrl = data.getUrl();
        Log.d(TAG, "#### selectAttachFile   url:" + docUrl + "  id:" + docId);
        showProgressBar();
        presenter.getDocViewerKey(docUrl, docId, new NetworkPresenter.getDocViewerKeyLisetner() {
            @Override
            public void onResponse(Res_Doc_Viewer result) {
                hideProgressBar();
                if (result != null) {
                    String key = result.getKey();
                    Log.d(TAG, "#### selectAttachFile url key:" + key + "  " + result.getResultDirPath());
                    AttachFileViewerDialog dialog = new AttachFileViewerDialog(mContext);
                    dialog.setData(data, key);
                    dialog.show();
                }
            }
        });
    }

    private void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
        //((ServiceDeskDetailActivity)mContext).showProgressBar();
    }

    private void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
        //((ServiceDeskDetailActivity)mContext).hideProgressBar();
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
                sendServerCancel(comment);
            }
        });
        dialog.show(getFragmentManager(mContext));
    }

    private void clickOk() {
        boolean confirmOk = true;
        for (int i = 0; i < filelist.size(); i++) {
            if (!filelist.get(i).isChecked) {
                confirmOk = false;
                break;
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
                    sendServerConfirm(comment);
                }
            });
            dialog.show(CommonUtils.getFragmentManager(mContext));
            //sendServerConfirm();
        } else {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_desk_file_check), Toast.LENGTH_SHORT).show();
            //viewMessage(mContext.getResources().getString(R.string.txt_service_desk_file_check));
        }
    }

    /**
     * 승인 반려 전송 파라메터
     * rv			이메일주소			O	bhlee1@kolon.com
     * cmd			승인, 반려 프로토콜			O	승인(approval_do), 반려(refusal_do)
     * comment			승인, 반려 사유			승인 : X, 반려 : O	텍스트(ex: 승인하였습니다., 반려하였습니다.)
     * seq			메일 구분 시퀀스			O	메일 시퀀스 (복수 처리 가능 -> ","로 붙여 쓴다.)
     * format			app 접근 여부			O	value : app
     **/
    //승인 전송
    private void sendServerConfirm(String comment) {
        //test
        String rv = this.rv;
        String cmd = "approval_do";
        String seq = this.seq;
        String format = "app";
        //String comment = "";

        HashMap hm = new HashMap();
        hm.put("rv", rv);
        hm.put("cmd", cmd);
        hm.put("seq", seq);
        hm.put("format", format);
        hm.put("comment", comment);
        transServerData(hm);
    }

    //반려 전송
    private void sendServerCancel(String comment) {
        //test
        String rv = this.rv;
        String cmd = "refusal_do";
        String seq = this.seq;
        String format = "app";

        HashMap hm = new HashMap();
        hm.put("rv", rv);
        hm.put("cmd", cmd);
        hm.put("seq", seq);
        hm.put("format", format);
        hm.put("comment", comment);
        transServerData(hm);
    }

    private void transServerData(HashMap hm) {
        showProgressBar();
        presenter.getServiceDeskMailFilterApprove(hm, new NetworkPresenter.getServiceDeskMainFilterApproveListener() {
            @Override
            public void onResponse(Res_AP_IF_036_VO result) {
                String errMsg = getResources().getString(R.string.txt_network_error);
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            if (hm.get("cmd").equals("approval_do")) {
                                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_confirm), Toast.LENGTH_SHORT).show();
                                ((ServiceDeskDetailActivity) mContext).checkRemainApproval(mContext.getResources().getString(R.string.txt_service_confirm), isFinal, position);

                            } else {
                                //Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_cancel), Toast.LENGTH_SHORT).show();
                                ((ServiceDeskDetailActivity) mContext).checkRemainApproval(mContext.getResources().getString(R.string.txt_service_cancel), isFinal, position);

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

    //error message
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
        dialog.show(getFragmentManager(mContext));
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            sv.requestDisallowInterceptTouchEvent(false);
        } else {
            sv.requestDisallowInterceptTouchEvent(true);
        }
        return false;
    }

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, tv_title1);
        CommonUtils.changeTextSize(mContext, tv_title2);
        CommonUtils.changeTextSize(mContext, tv_title3);
        CommonUtils.changeTextSize(mContext, tv_title4);
        CommonUtils.changeTextSize(mContext, tv_title5);
        CommonUtils.changeTextSize(mContext, tv_title6);
        CommonUtils.changeTextSize(mContext, tv_title7);

        CommonUtils.changeTextSize(mContext, tv_sender);
        CommonUtils.changeTextSize(mContext, tv_receiver);
        CommonUtils.changeTextSize(mContext, tv_title);
        CommonUtils.changeTextSize(mContext, tv_send_date);
        CommonUtils.changeTextSize(mContext, tv_detect_detail);

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
