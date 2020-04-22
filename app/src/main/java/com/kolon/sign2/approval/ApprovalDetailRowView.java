package com.kolon.sign2.approval;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.AttatchFileDialog;
import com.kolon.sign2.dialog.OpinionListDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_014_VO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_018_VO;
import com.kolon.sign2.vo.Res_AP_IF_019_VO;
import com.kolon.sign2.vo.Res_AP_IF_021_VO;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 전자결재 상세 화면
 */
public class ApprovalDetailRowView extends LinearLayout implements View.OnClickListener {
    private String TAG = ApprovalDetailRowView.class.getSimpleName();
    private Context mContext;

    private Res_AP_IF_016_VO.result readData;
    private NetworkPresenter presenter;
    private ApprovalOpinionPopupView popup_view;
    private LinearLayout detail_layout;
    private RelativeLayout progress_bar;
    private RelativeLayout btn_approval_detail_open;
    private LinearLayout layout_approval_open;
    private ImageView btn_approval_detail_plus;
    private TextView tv_approval_detail_open;
    private LinearLayout btn_approval_detail_opinion, btn_approval_detail_attach_file, layout_opinion_comment, layout_approval_detail_refer;
    private TextView tv_approval_detail_title, tv_approval_detail_name, tv_approval_detail_team;
    private TextView tv_approval_detail_attach_num, tv_approval_detail_attach;
    private TextView tv_approval_detail_opinion_num, tv_approval_detail_opinion;
    private TextView tv_approval_detail_time, tv_approval_detail_line, tv_approval_detail_refer;
    private SubsamplingScaleImageView form_image;
    private ApprovalBottomMenuView bottomMenuView;

    private TextView tv_gyuljaesun, tv_colon, tv_chamjo;
    private RelativeLayout popup_background, btn_ok;
    private TextView dialog_content;

    private String formUrl;
    private String userId;
    private String deptId;
    private String category;
    private int commentCnt = 0;
    private String wfDocId = "";
    private String companyCd;
    private int position;
    private String talkCompany;
    private ApprovalDetailActivity.inputData input;
    private RequestManager mGlideRequestManager;
    private ArrayList<AttachFileListVO> fileList = new ArrayList<>();
    private OnRowViewState mInterface;

    private boolean isFinal = false;

    public interface OnRowViewState {
        void setApprovalLineView(boolean isView);//우측 결재선 표시 유무

        void getServerResponse(Res_AP_IF_016_VO result);//서버상태 리턴

        void onTextSizeChange();
    }

    public void setInterface(OnRowViewState mInterface) {
        this.mInterface = mInterface;
    }

    public ApprovalDetailRowView(Context context) {
        super(context);
        initView(context);
    }

    public ApprovalDetailRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ApprovalDetailRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void setData(RequestManager mGlideRequestManager, ArrayList<ApprovalDetailActivity.inputData> data, int position, String category, String userId, String deptId, String companyCd) {
//        category = "P";
//        wfDocId = "00000000000000081852";
//        userId = "jisun";
//        deptId = "LS_A00017";
        this.mGlideRequestManager = mGlideRequestManager;
        input = data.get(position);
        this.userId = userId;
        this.deptId = deptId;
        this.category = category;
        this.position = position;

        if (data.size() == position+1) //마지막 페이지인지 여부
            isFinal = true;

        //String data[] = guidList.get(position).split("\\|");

        this.companyCd = companyCd;
        wfDocId = data.get(position).wfDocId;
        String bchamjo = data.get(position).bchamjo;
        //      wfDocId = "00000000000000081852";//??test
        if ("N".equalsIgnoreCase(input.inLine) && "N".equalsIgnoreCase(input.isPublic)) {
            showPopup(mContext.getResources().getString(R.string.txt_approval_no_auth));
        } else {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("category", category);
            hm.put("wfDocId", wfDocId);
            hm.put("userId", userId);
            hm.put("deptId", deptId);
            hm.put("bchamjo", bchamjo);

            //showProgressbar();

            presenter = new NetworkPresenter();
            presenter.getApprovalDetail(hm, new NetworkPresenter.getApprovalDetailResult() {

                @Override
                public void onResponse(Res_AP_IF_016_VO result) {
                    String errMsg = "";
                    Log.d(TAG, "#### get detail:" + new Gson().toJson(result));
                    if (mInterface != null) {
                        mInterface.getServerResponse(result);
                    }
                    if (result != null) {
                        if ("200".equals(result.getStatus().getStatusCd())) {
                            if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                                readData = result.getResult();
                                if (readData == null) {
                                    hideProgressbar();
                                    return;
                                }
                                drawLayout();
                                getFormUrl(position);
                                getAttachFile(wfDocId);
                                getTalkCompany();
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

                    hideProgressbar();

                    showPopup(errMsg);
                }
            });
        }
    }

    // 문서주소를 가져온다
    private void getFormUrl(int position) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wfDocId", wfDocId);
        hm.put("userId", userId);
        hm.put("deptId", deptId);

        presenter.getApprovalFormUrl(hm, new NetworkPresenter.getApprovalFormUrlListener() {

            @Override
            public void onResponse(Res_AP_IF_018_VO result) {
                Log.d(TAG, "#### body img url:" + new Gson().toJson(readData));
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        drawForm(result.getResult().getApprBody().getBodyimage());
                    }
                }
            }
        });
    }

    //첨부파일을 가져온다.
    private void getAttachFile(String wfDocId) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("wfDocId", wfDocId);
        presenter.getApprovalAttachs(hm, new NetworkPresenter.getApprovalAttachsListener() {
            @Override
            public void onResponse(Res_AP_IF_019_VO result) {
                fileList = new ArrayList<>();
                if (result != null && result.getResult() != null && result.getResult().getApprAttFileList() != null) {
                    ArrayList<Res_AP_IF_019_VO.result.apprAttFileList> read = result.getResult().getApprAttFileList();

                    for (int i = 0; i < result.getResult().getApprAttFileList().size(); i++) {
                        AttachFileListVO vo = new AttachFileListVO();
                        vo.setName(read.get(i).getFileName());
                        vo.setUrl(read.get(i).getOriginFilePath());
                        vo.setId(read.get(i).getWfInstanceId());
                        fileList.add(vo);
                    }
                }

                //첨부파일
                if (fileList == null || fileList.size() == 0) {
                    btn_approval_detail_attach_file.setSelected(false);
                    tv_approval_detail_attach_num.setVisibility(View.GONE);
                    tv_approval_detail_attach.setSelected(false);
                    tv_approval_detail_attach.setText(mContext.getResources().getString(R.string.txt_approval_no_case));
                } else {
                    btn_approval_detail_attach_file.setSelected(true);
                    tv_approval_detail_attach_num.setVisibility(View.VISIBLE);
                    tv_approval_detail_attach_num.setText(String.valueOf(readData.getApprAttFileList().size()));
                    tv_approval_detail_attach.setSelected(true);
                    tv_approval_detail_attach.setText(mContext.getResources().getString(R.string.txt_approval_case));
                }
            }
        });
    }


    private void drawForm(String url) {
        formUrl = url;
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .override(0, 0);

     //   mGlideRequestManager = Glide.with(this);
        mGlideRequestManager
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        form_image.setImage(ImageSource.bitmap(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
    }


    private void showFormFullScreen() {
        if (form_image == null) {
            return;
        }

        float baseWidth = form_image.getSWidth();
        float photoViewWidth = form_image.getWidth();
        if (photoViewWidth == 0) {
            photoViewWidth = ApprovalDetailActivity.readedPhotoViewWidth;
        } else {
            ApprovalDetailActivity.readedPhotoViewWidth = photoViewWidth;
        }

        double adjScale = photoViewWidth / baseWidth;
        adjScale = Math.round(adjScale * 100.0) / 100.0;

        PointF center = new PointF();
        form_image.setScaleAndCenter((float) adjScale, center);
    }

    /**
     * 받은 데이터로 화면을 그린다.
     */
    private void drawLayout() {
        hideProgressbar();

        detail_layout.setVisibility(View.VISIBLE);

        // readData
        tv_approval_detail_title.setText(readData.getApprDetailItem().getTitle());//제목
        tv_approval_detail_name.setText(readData.getApprDetailItem().getAuthor());//기안자
        tv_approval_detail_team.setText(readData.getApprDetailItem().getWriterdeptname());//부서명
        tv_approval_detail_time.setText(readData.getApprDetailItem().getPubDate());// 날짜

        //actionType이 참조인경우 참조. 그외엔 결재선으로 붙임(기안제외)
        //결재선
        int lineCnt = 0;
        int chamjoCnt = 0;
        commentCnt = 0;
        String strLine = "";
        String strChamjo = "";
        String chamjo = mContext.getResources().getString(R.string.txt_approval_refer).replaceAll(" ", "");
        String gian = mContext.getResources().getString(R.string.txt_approval_draft).replaceAll(" ", "");

        if (readData.getApprLineList() != null) {
            for (int i = 0; i < readData.getApprLineList().size(); i++) {
                if (chamjo.equals(readData.getApprLineList().get(i).getActionType().replaceAll(" ", ""))) {
                    //참조
                    chamjoCnt++;
                    strChamjo += readData.getApprLineList().get(i).getName() + "(" + readData.getApprLineList().get(i).getPosition() + ")-";
                } else {
                    //결재선
                    lineCnt++;
                    if (TextUtils.isEmpty(readData.getApprLineList().get(i).getName()) || "-".equals(readData.getApprLineList().get(i).getName())) {
                        strLine += readData.getApprLineList().get(i).getDepartment() + "-";
                    } else {
                        strLine += readData.getApprLineList().get(i).getName() + "(" + readData.getApprLineList().get(i).getPosition() + ")-";
                    }

                    //의견
                    if (!TextUtils.isEmpty(readData.getApprLineList().get(i).getComment())) {
                        commentCnt++;
                    }
                }
            }
        }

        if (lineCnt == 0) {
            tv_approval_detail_line.setText(mContext.getResources().getString(R.string.txt_approval_no_case));
        } else {
            strLine = strLine.substring(0, strLine.length() - 1);
            tv_approval_detail_line.setText(strLine);
        }
        //참조
        if (chamjoCnt == 0) {
            layout_approval_detail_refer.setVisibility(View.GONE);
            tv_approval_detail_refer.setText(mContext.getResources().getString(R.string.txt_approval_no_case));
        } else {
            layout_approval_detail_refer.setVisibility(View.VISIBLE);
            strChamjo = strChamjo.substring(0, strChamjo.length() - 1);
            tv_approval_detail_refer.setText(strChamjo);
        }

        //의견
        if (commentCnt == 0) {
            btn_approval_detail_opinion.setSelected(false);
            tv_approval_detail_opinion_num.setVisibility(View.GONE);
            tv_approval_detail_opinion.setSelected(false);
            tv_approval_detail_opinion.setText(mContext.getResources().getString(R.string.txt_approval_no_case));
        } else {
            btn_approval_detail_opinion.setSelected(true);
            tv_approval_detail_opinion_num.setVisibility(View.VISIBLE);
            tv_approval_detail_opinion_num.setText(String.valueOf(commentCnt));
            tv_approval_detail_opinion.setSelected(true);
            tv_approval_detail_opinion.setText(mContext.getResources().getString(R.string.txt_approval_case));

            //의견이 있을경우 팝업을 띄움
            if (!input.commentView) {
//                selectOpinion(position);
                if (commentCnt != 0) {
                    //팝업
                    popup_view.setData(readData.getApprLineList());
                    popup_view.setVisibility(View.VISIBLE);
                    popup_view.setInterface(new ApprovalOpinionPopupView.OnClickListener() {

                        @Override
                        public void onClick() {
                            input.commentView = true;
                        }
                    });
                }
            }
        }

        //첨부파일리스트 부분 - 기본형이 없는 형이고 실 데이터는 서버에서 첨부파일 데이터를 받아와서 적용시킴
        btn_approval_detail_attach_file.setSelected(false);
        tv_approval_detail_attach_num.setVisibility(View.GONE);
        tv_approval_detail_attach.setSelected(false);
        tv_approval_detail_attach.setText(mContext.getResources().getString(R.string.txt_approval_no_case));

//        //첨부파일
//        if (fileList == null || fileList.size() == 0) {
////        if (readData.getApprAttFileList() == null || readData.getApprAttFileList().size() == 0) {
//            btn_approval_detail_attach_file.setSelected(false);
//            tv_approval_detail_attach_num.setVisibility(View.GONE);
//            tv_approval_detail_attach.setSelected(false);
//            tv_approval_detail_attach.setText(mContext.getResources().getString(R.string.txt_approval_no_case));
//        } else {
//            btn_approval_detail_attach_file.setSelected(true);
//            tv_approval_detail_attach_num.setVisibility(View.VISIBLE);
//            tv_approval_detail_attach_num.setText(String.valueOf(readData.getApprAttFileList().size()));
//            tv_approval_detail_attach.setSelected(true);
//            tv_approval_detail_attach.setText(mContext.getResources().getString(R.string.txt_approval_case));
//        }

        //처리버튼 노출 처리
        if("Y".equalsIgnoreCase(readData.getApprDetailItem().getApporvalPlag())){
            bottomMenuView.setVisibility(View.VISIBLE);

            //결재선 확인여부
            if (TextUtils.isEmpty(tv_approval_detail_line.getText().toString()) ||
                    mContext.getResources().getString(R.string.txt_approval_no_case).equals(tv_approval_detail_line.getText().toString())) {
                bottomMenuView.setApprovalLineOk(false);
            } else {
                //결재선이 있어도 일단 한번 들어가야함
                if (input.approvalLineCheck) {
                    bottomMenuView.setApprovalLineOk(true);
                } else {
                    bottomMenuView.setApprovalLineOk(false);
                }
            }

            bottomMenuView.setData(readData, userId);
        }else{
            bottomMenuView.setVisibility(View.GONE);
        }
    }

    //결재진행상태 이동
    private void gotoApprovalProcess() {
        Intent i = new Intent(mContext, ApprovalProgressActivity.class);
        i.putExtra("apprLineList", readData.getApprLineList());
        i.putExtra("title", mContext.getResources().getString(R.string.txt_approval_progress));
        //      mContext.startActivity(i);

        ((ApprovalDetailActivity) mContext).startActivityForResult(i, 10);
    }

    //문서 보기 이동
    private void gotoFormView() {
        Intent i = new Intent(mContext, ApprovalFormActivity.class);
        i.putExtra("url", formUrl);
        mContext.startActivity(i);
    }

    //의견 보기 화면 이동
    private void selectOpinion(int position) {
        if (commentCnt != 0) {
//            Intent i = new Intent(mContext, ApprovalProgressActivity.class);
//            i.putExtra("apprLineList", readData.getApprLineList());
//            i.putExtra("title", mContext.getResources().getString(R.string.txt_approval_opinion_view));
//            //     mContext.startActivity(i);
//
//            ((ApprovalDetailActivity) mContext).startActivityForResult(i, 10);

            //commentView
            OpinionListDialog dialog = OpinionListDialog.newInstance();
            dialog.setData(readData.getApprLineList());
            dialog.show(((ApprovalDetailActivity)mContext).getSupportFragmentManager());
        }
    }

    //첨부 파일 팝업
    private void selectAttachFile() {
        if (readData.getApprAttFileList() != null && readData.getApprAttFileList().size() != 0) {
            //popup
            if (fileList == null || fileList.size() == 0) {
                return;
            }

            AttatchFileDialog dialog = AttatchFileDialog.newInstance();
            dialog.setInterface(new AttatchFileDialog.OnClickListener() {
                @Override
                public void selectPosition(int position) {
                    //     Log.d(TAG, "#### selectAttachFile pos>>:" + position + "   url:" + readData.getApprAttFileList().get(position).getUrl()+"  id:"+readData.getApprAttFileList().get(position).getUid());

                    String docId = "mTotalSign_sign_" + wfDocId + "_" + position;//서비스_문서id_파일순서
                    viewDoc(fileList.get(position), docId);
                }
            });
            dialog.setData(fileList);
            dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager(), "");
        }
    }

    private void viewDoc(AttachFileListVO data, String docId) {
        String docUrl = data.getUrl();

        Log.d(TAG, "#### selectAttachFile   url:" + docUrl + "  id:" + docId);

        showProgressbar();
        presenter.getDocViewerKey(docUrl, docId, new NetworkPresenter.getDocViewerKeyLisetner() {
            @Override
            public void onResponse(Res_Doc_Viewer result) {
                hideProgressbar();
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_approval_detail_open:
                //펼치기
                if (btn_approval_detail_open.isSelected()) {
                    btn_approval_detail_open.setSelected(false);
                    tv_approval_detail_open.setText(mContext.getResources().getString(R.string.txt_approval_expand));
                    layout_approval_open.setVisibility(View.GONE);
                } else {
                    btn_approval_detail_open.setSelected(true);
                    tv_approval_detail_open.setText(mContext.getResources().getString(R.string.txt_approval_close));
                    layout_approval_open.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_approval_detail_plus:
                //결재진행상태 이동
                gotoApprovalProcess();
                break;
            case R.id.btn_form_expand:
                //문서보기 확대
                gotoFormView();
                break;
            case R.id.btn_approval_detail_opinion:
                selectOpinion(position);
                break;
            case R.id.btn_approval_detail_attach_file:
                selectAttachFile();
                break;
            case R.id.btn_call:
            case R.id.tv_approval_detail_name:
                //Todo KolonTalk 인터페이스 추가하여 CompanyCode값 적용 필요
                CommonUtils.callKolonTalk(mContext, readData.getApprDetailItem().getWriterId(),talkCompany);
                break;
            case R.id.btn_ok:
                ((ApprovalDetailActivity) mContext).onBackPressed();
                break;
        }
    }

    private void showPopup(String msg) {
        popup_background.setVisibility(View.VISIBLE);
        dialog_content.setText(msg);
    }

    private void hidePopup() {
        popup_background.setVisibility(View.GONE);
    }

    private void sendApprovalProcessing(String title, String action, String comment) {
        /**
         * action Action Code				"가결 = 1, 반송 =2, 부결 =3, 참조 = 4(미사용!!, detail에서 bChamjo 보내는걸로 변경) 보류 = 5"
         * wfDocId DOCID				DOCID key
         * comment 메시지 내용				반송/부결 의견내용
         * companyCd 회사 코드					*/
        HashMap<String, String> hm = new HashMap<>();
        hm.put("userId", userId);
        hm.put("action", action);
        hm.put("wfDocId", wfDocId);
        hm.put("comment", comment);
        hm.put("companyCd", companyCd);
        Log.d(TAG, "approval process in:" + hm);

        showProgressbar();
        presenter.getApprovalProceesing(hm, new NetworkPresenter.getApprovalProcessingResult() {

            @Override
            public void onResponse(Res_AP_IF_014_VO result) {
                String errMsg = "";
                hideProgressbar();
                if (result != null) {
                    Log.d(TAG, "approval process re:" + new Gson().toJson(result));
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            /**
                             *결재처리 완료 알림 및 이동 안내
                             * [Tap] ‘목록으로’ 선택 시 미결함 목록으로 이동
                             * ‘다음 결재 건’ 선택 시 미결함 목록 내 다름 건으로 이동
                             * 미결함 목록이 0건이 될 때까지 이동함
                             * 더 이상 결재건이 없을 경우 결재완료 Toast 팝업 띄움
                             * */
                            //checkRemainApprovalCount(title);
                            checkRemainApproval(title);

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

                TextDialog dialog = TextDialog.newInstance("", errMsg, mContext.getResources().getString(R.string.txt_alert_confirm));
                dialog.setCancelable(false);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
            }
        });
    }

    //남은 결재건수를 체크_yong79
    private void checkRemainApproval(String title) {
        //boolean isFinal = true;
        //결재 1건이상 처리로 목록 복귀 리프레시 set
        ((ApprovalDetailActivity) mContext).setRefresh();

        if (isFinal) { //목록으로
            String msg = String.format(getResources().getString(R.string.txt_approval_success), title);
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

            ((ApprovalDetailActivity) mContext).onBackPressed();
        } else { //다음 결재
            String msg = String.format(getResources().getString(R.string.txt_approval_success), title) + "\n" + mContext.getResources().getString(R.string.txt_approval_next_process);
            TextDialog dialog = TextDialog.newInstance("", msg, mContext.getResources().getString(R.string.txt_approval_next_process_2),
                    mContext.getResources().getString(R.string.txt_approval_next_process_3));
            dialog.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_left:
                            //목록으로
                            dialog.dismiss();
                            ((ApprovalDetailActivity) mContext).onBackPressed();
                            break;
                        case R.id.btn_right:
                            //다음 결재건
                            dialog.dismiss();
                            showProgressbar();
                            ((ApprovalDetailActivity) mContext).nextApprovalLoading(position);
                            break;
                    }
                }
            });
            dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
        }
    }

    //남은 결재건수를 체크
    private void checkRemainApprovalCount(String title) {

        NetworkPresenter presenter = new NetworkPresenter();

        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("deptId", deptId);
        hm.put("category", category);
        hm.put("containerId", "");
        hm.put("subQuery", "");
        showProgressbar();
        presenter.getApprovalCountAll(hm, new NetworkPresenter.getApprovalCountAllListener() {

            @Override
            public void onResponse(Res_AP_IF_021_VO result) {
                String errMsg = "";
                hideProgressbar();
                if (result != null) {
                    Log.d(TAG, "#### remain cnt:" + new Gson().toJson(result));
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            int cnt = Integer.parseInt(result.getResult().getApprCntList().get(0).getCnt());
                            if (cnt == 0) {
                                String msg = String.format(getResources().getString(R.string.txt_approval_success), title);
                                Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
                            } else {
                                String msg = String.format(getResources().getString(R.string.txt_approval_success), title) + "\n" + mContext.getResources().getString(R.string.txt_approval_next_process);
                                TextDialog dialog = TextDialog.newInstance("", msg, mContext.getResources().getString(R.string.txt_approval_next_process_2),
                                        mContext.getResources().getString(R.string.txt_approval_next_process_3));
                                dialog.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        switch (view.getId()) {
                                            case R.id.btn_left:
                                                //목록으로
                                                dialog.dismiss();
                                                ((ApprovalDetailActivity) mContext).onBackPressed();
                                                break;
                                            case R.id.btn_right:
                                                //다음 결재건
                                                dialog.dismiss();
                                                showProgressbar();
                                                ((ApprovalDetailActivity) mContext).nextApprovalLoading(position);
                                                break;
                                        }
                                    }
                                });
                                dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
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

                TextDialog dialog = TextDialog.newInstance("", errMsg, mContext.getResources().getString(R.string.txt_alert_confirm));
                dialog.setCancelable(false);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
            }
        });
    }

    //iken talk에 필요한 회사코드 가져오기
    private void getTalkCompany(){
        HashMap hm = new HashMap();
        hm.put("userId",readData.getApprDetailItem().getWriterId());
        talkCompany = "";
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

    public Res_AP_IF_016_VO.result getRowData() {
        return readData;
    }

    public void showProgressbar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressbar() {
        progress_bar.setVisibility(View.GONE);
    }

    private void initView(Context context) {
        mContext = context;

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_approval_detail, this, false);

        ImageView callBtn = (ImageView) v.findViewById(R.id.btn_call);
        callBtn.setOnClickListener(this);

        popup_view = (ApprovalOpinionPopupView) v.findViewById(R.id.popup_view);
        popup_view.setVisibility(View.GONE);

        detail_layout = (LinearLayout) v.findViewById(R.id.detail_layout);
        detail_layout.setVisibility(View.GONE);
        progress_bar = (RelativeLayout) v.findViewById(R.id.progress_bar);
        hideProgressbar();

        btn_approval_detail_open = (RelativeLayout) v.findViewById(R.id.btn_approval_detail_open);
        btn_approval_detail_open.setOnClickListener(this);
        layout_approval_open = (LinearLayout) v.findViewById(R.id.layout_approval_open);
        tv_approval_detail_open = (TextView) v.findViewById(R.id.tv_approval_detail_open);
        btn_approval_detail_plus = (ImageView) v.findViewById(R.id.btn_approval_detail_plus);
        btn_approval_detail_plus.setOnClickListener(this);

        btn_approval_detail_open.setSelected(false);
        layout_approval_open.setVisibility(View.GONE);

        layout_opinion_comment = (LinearLayout) v.findViewById(R.id.layout_opinion_comment);
        layout_opinion_comment.setVisibility(View.VISIBLE);

        btn_approval_detail_opinion = (LinearLayout) v.findViewById(R.id.btn_approval_detail_opinion);
        btn_approval_detail_opinion.setOnClickListener(this);
        btn_approval_detail_attach_file = (LinearLayout) v.findViewById(R.id.btn_approval_detail_attach_file);
        btn_approval_detail_attach_file.setOnClickListener(this);

        tv_approval_detail_title = (TextView) v.findViewById(R.id.tv_approval_detail_title);//제목
        tv_approval_detail_name = (TextView) v.findViewById(R.id.tv_approval_detail_name);//기안자
        tv_approval_detail_name.setOnClickListener(this);
        tv_approval_detail_team = (TextView) v.findViewById(R.id.tv_approval_detail_team);//부서명

        CommonUtils.textSizeSetting(context, tv_approval_detail_title);
        CommonUtils.textSizeSetting(context, tv_approval_detail_name);
        CommonUtils.textSizeSetting(context, tv_approval_detail_team);


        tv_approval_detail_attach_num = (TextView) v.findViewById(R.id.tv_approval_detail_attach_num);//첨부파일 수
        tv_approval_detail_attach = (TextView) v.findViewById(R.id.tv_approval_detail_attach);//첨부파일 없음 또는 건

        tv_approval_detail_opinion_num = (TextView) v.findViewById(R.id.tv_approval_detail_opinion_num);//의견 수
        tv_approval_detail_opinion = (TextView) v.findViewById(R.id.tv_approval_detail_opinion);//의견 없음 또는 건

        tv_approval_detail_time = (TextView) v.findViewById(R.id.tv_approval_detail_time);//기안날짜
        tv_approval_detail_line = (TextView) v.findViewById(R.id.tv_approval_detail_line);//결재선 이름
        tv_approval_detail_refer = (TextView) v.findViewById(R.id.tv_approval_detail_refer);//참조인 이름
        layout_approval_detail_refer = (LinearLayout) v.findViewById(R.id.layout_approval_detail_refer);//참조라인

        CommonUtils.textSizeSetting(context, tv_approval_detail_time);
        CommonUtils.textSizeSetting(context, tv_approval_detail_line);
        CommonUtils.textSizeSetting(context, tv_approval_detail_refer);

        tv_gyuljaesun = (TextView) v.findViewById(R.id.tv_gyuljaesun);//결재선 글자
        tv_colon = (TextView) v.findViewById(R.id.tv_colon);//: 기호
        tv_chamjo = (TextView) v.findViewById(R.id.tv_chamjo);//참조 글자

        CommonUtils.textSizeSetting(context, tv_gyuljaesun);
        CommonUtils.textSizeSetting(context, tv_colon);
        CommonUtils.textSizeSetting(context, tv_chamjo);


        Button btnForm = (Button) v.findViewById(R.id.btn_form_expand);
        btnForm.setOnClickListener(this);

        bottomMenuView = (ApprovalBottomMenuView) v.findViewById(R.id.view_approval_bottom_menu);
        bottomMenuView.setInterface(new ApprovalBottomMenuView.onBottomMenuClickListener() {
            @Override
            public void onClickApprovalLine() {
                //결재선으로 이동
                ((ApprovalDetailActivity) mContext).gotoApprovalLine();
            }

            @Override
            public void onSendMessage(String title, String actionCode, String comment) {
                sendApprovalProcessing(title, actionCode, comment);
            }

            @Override
            public void setApprovalLineView(boolean isView) {
                if (mInterface != null) {
                    mInterface.setApprovalLineView(isView);
                }
            }
        });

        form_image = (SubsamplingScaleImageView) v.findViewById(R.id.form_image);
        form_image.setMaxScale(7);
        form_image.setOnImageEventListener(new SubsamplingScaleImageView.OnImageEventListener() {
            @Override
            public void onReady() {

            }

            @Override
            public void onImageLoaded() {
                form_image.post(new Runnable() {
                    @Override
                    public void run() {
                        showFormFullScreen();
                    }
                });
            }

            @Override
            public void onPreviewLoadError(Exception e) {

            }

            @Override
            public void onImageLoadError(Exception e) {

            }

            @Override
            public void onTileLoadError(Exception e) {

            }

            @Override
            public void onPreviewReleased() {

            }
        });

        popup_background = (RelativeLayout) v.findViewById(R.id.popup_background);
        btn_ok = (RelativeLayout) v.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        dialog_content = (TextView) v.findViewById(R.id.dialog_content);

        RelativeLayout layout_approval_form = (RelativeLayout) v.findViewById(R.id.layout_approval_form);
        layout_approval_form.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (mInterface != null) mInterface.onTextSizeChange();

                if (ApprovalDetailActivity.readedPhotoViewWidth == 0)
                    ApprovalDetailActivity.readedPhotoViewWidth = form_image.getWidth();
            }
        });

        hidePopup();

        addView(v);
    }

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, tv_approval_detail_title);
        CommonUtils.changeTextSize(mContext, tv_approval_detail_name);
        CommonUtils.changeTextSize(mContext, tv_approval_detail_team);
        CommonUtils.changeTextSize(mContext, tv_approval_detail_time);
        CommonUtils.changeTextSize(mContext, tv_approval_detail_line);
        CommonUtils.changeTextSize(mContext, tv_approval_detail_refer);
        CommonUtils.changeTextSize(mContext, tv_gyuljaesun);
        CommonUtils.changeTextSize(mContext, tv_colon);
        CommonUtils.changeTextSize(mContext, tv_chamjo);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mInterface != null) mInterface.onTextSizeChange();
    }
//    private void textSizeSetting(TextView inputTextView){
//        inputTextView.setTag(inputTextView.getTextSize());
//        inputTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, CommonUtils.getUserTextSize(mContext, inputTextView.getTag()));
//    }

}
