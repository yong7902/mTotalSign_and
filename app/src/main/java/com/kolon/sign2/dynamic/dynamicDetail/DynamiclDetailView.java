package com.kolon.sign2.dynamic.dynamicDetail;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.AttatchFileDialog;
import com.kolon.sign2.dialog.DynamicApprovalDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.view.DynamicDetailAttachItemView;
import com.kolon.sign2.view.DynamicDetailEditItemView;
import com.kolon.sign2.view.DynamicDetailItemView;
import com.kolon.sign2.view.DynamicDetailSingleTextItemView;
import com.kolon.sign2.view.DynamicDetailTxtBtnItemView;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_104_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 동적 상세 화면
 */
public class DynamiclDetailView extends LinearLayout implements View.OnClickListener,
        DynamicDetailEditItemView.DateClickListener, DynamicDetailAttachItemView.AttachViewClickListener, DynamicDetailTxtBtnItemView.KolonTalkClickListener {
    private String TAG = DynamiclDetailView.class.getSimpleName();
    private Context mContext;
    private int DP_10;
    private int DP_11;
    private int DP_12;
    private int DP_16;

    private ConstraintLayout mTitleTypeLayout;
    private LinearLayout parentContainer;
    private TextView uuidTv;
    private TextView kindTv;
    private RelativeLayout progress_bar;
    private LinearLayout mButtonContainer;
    private ConstraintLayout accpetLayout;
    private ConstraintLayout rejectLayout;
    private TextView mRejectTextview;
    private TextView mAcceptTextview;
    private boolean isContentsDivide = false;
    private ArrayList<Res_AP_IF_104_VO.dynamicDetailList> mDetailList;
    private ArrayList<AttachFileListVO> fileList = new ArrayList<>();

    private String mCompanyCode = "";
    private String mUserId;
    private String mSysId;
    private String mMenuId = "";
    private String mDocId;
    private String mParam01;
    private String mParam02;
    private String mParam03;
    private String mParam04;
    private String mParam05;
    private DynamicDetailEditItemView mDynamicEditWorkView;
    private DynamicDetailTxtBtnItemView mDynamicDetailTxtBtnView;
    private NetworkPresenter presenter;

    private HashMap<Integer, String> layoutHm = new HashMap<>();

    private ShimmerFrameLayout mShimmerLayout;

    private int mPosition;
    private boolean mIsFinal = false;

    public DynamiclDetailView(Context context) {
        super(context);
        initView(context);
    }

    public DynamiclDetailView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DynamiclDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_dynamic_detail, this, false);


        parentContainer = v.findViewById(R.id.dynamic_detail_container);
        mTitleTypeLayout = v.findViewById(R.id.cl_title_type_layout);
        mTitleTypeLayout.setVisibility(View.GONE);
        uuidTv = v.findViewById(R.id.dynamic_detail_uuid_tv);
        kindTv = v.findViewById(R.id.dynamic_detail_kind_tv);
        progress_bar = v.findViewById(R.id.progress_bar);
        mButtonContainer = v.findViewById(R.id.ll_dynamic_detail_bottom_btn_container);
        rejectLayout = v.findViewById(R.id.dynamic_detail_reject_btn);
        accpetLayout = v.findViewById(R.id.dynamic_detail_accept_btn);
        mRejectTextview = v.findViewById(R.id.reject_text1);
        mAcceptTextview = v.findViewById(R.id.accept_text1);
        mButtonContainer.setVisibility(View.GONE);
        DP_10 = (int) DpiUtil.convertDpToPixel(mContext, 10);
        DP_11 = (int) DpiUtil.convertDpToPixel(mContext, 11);
        DP_12 = (int) DpiUtil.convertDpToPixel(mContext, 12);
        DP_16 = (int) DpiUtil.convertDpToPixel(mContext, 16);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) v.findViewById(R.id.shimmer_layout);


        CommonUtils.textSizeSetting(mContext, uuidTv);
        CommonUtils.textSizeSetting(mContext, kindTv);

        addView(v);
    }



    private void drawView() {
        Iterator<Res_AP_IF_104_VO.dynamicDetailList> itr = mDetailList.iterator();
        Res_AP_IF_104_VO.dynamicDetailList list;

        ArrayList<Res_AP_IF_104_VO.dynamicDetailList> recyclerItemList = new ArrayList<>();
        ArrayList<Res_AP_IF_104_VO.dynamicDetailList> attachItemList = new ArrayList<>();

        while (itr.hasNext()) {
            list = itr.next();
            switch (list.getItemClass()) {
                case "title": {
                    drawTitle(list.getLeftText(), list.getRightText());
                    break;
                }
                case "divide": {
                    drawDivide(list.getLeftText());
                    break;
                }
                case "text": {
                    drawDynamicDetailView(list.getLeftText(), list.getRightText());
                    break;
                }
                case "appList": {
                    recyclerItemList.add(list);
                    break;
                }
                case "actBtn": {
                    mButtonContainer.setVisibility(View.VISIBLE);
                    initEvnet();
                    drawDynamicDetailButton(list.getLeftText(), list.getRightText(), list.getAttr01(), list.getAttr02());
                    break;
                }
                case "editWork": {
                    drawEditDetailView();
                    break;
                }
                case "txtBtn": {
                    drawTxtBtnDetailView(list.getLeftText(), list.getAttr01());
                    break;
                }
                case "singleText": {
                    drawSingleTextDetailView(list.getLeftText());
                    break;
                }
                case "attach": {
                    attachItemList.add(list);
                    break;
                }

            }
        }

        if (!recyclerItemList.isEmpty()) {
            drawApprovalHistory(recyclerItemList);
        }

        if (!attachItemList.isEmpty()) {
            drawAttachView(attachItemList);
        }
    }

    private void initEvnet() {
        //TODO : 결재 승인 호출
        accpetLayout.setOnClickListener(v -> {
            //공수 입력 체크
            if (null != mDynamicEditWorkView && !checkEditWorkInput()) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_service_desk_err_info), Toast.LENGTH_LONG).show();
                return;
            }
            String dialogTitle = mAcceptTextview.getText().toString() + mContext.getString(R.string.txt_approval_txt_1);
            showApprovalDialog(dialogTitle, mAcceptTextview.getText().toString());
        });

        //TODO : 결재 반려 호출
        rejectLayout.setOnClickListener(v -> {
            String dialogTitle = mRejectTextview.getText().toString() + mContext.getString(R.string.txt_approval_txt_1);
            showCancelDialog(dialogTitle, mRejectTextview.getText().toString());
        });
    }

    private void showCancelDialog(String title, String rightText) {
        DynamicApprovalDialog dialog = DynamicApprovalDialog.newInstance(title, rightText, true);
        dialog.setInterface(new DynamicApprovalDialog.DynamicApprovalListener() {
            @Override
            public void getMessage(String comment) {
                if (null != mRejectTextview.getTag()) {
                    sendApproval(mRejectTextview.getTag().toString(), comment, rightText);
                }
            }
        });
        dialog.show(CommonUtils.getFragmentManager(mContext));
    }

    private void showApprovalDialog(String title, String rightText) {
        DynamicApprovalDialog dialog = DynamicApprovalDialog.newInstance(title, rightText, false);
        dialog.setInterface(new DynamicApprovalDialog.DynamicApprovalListener() {
            @Override
            public void getMessage(String comment) {
                if (null != mAcceptTextview.getTag()) {
                    sendApproval(mAcceptTextview.getTag().toString(), comment, rightText);
                }
            }
        });
        dialog.show(CommonUtils.getFragmentManager(mContext));
    }

    private void sendApproval(String actionType, String actionOpinion, String title) {
        String param01 = mParam01;
        String param02 = mParam02;
        String param03 = mParam03;
        String param04 = mParam04;
        String param05 = mParam05;
        if (null != mDynamicEditWorkView) {
            param02 = mDynamicEditWorkView.getWorkTime(); // 공수 분
            param03 = mDynamicEditWorkView.getContents();  // 작업내역
            param04 = mDynamicEditWorkView.getDate();  // 작업 완료일
//            Toast.makeText(mContext, "workTime = "+ param01 + "contents =" + param02 + "date = " + param03, Toast.LENGTH_SHORT).show();
        }

        getDynamicDetailAction(mUserId, mSysId, mMenuId, mDocId, actionType, actionOpinion, param01, param02, param03, param04, param05, title);
    }


    private void drawTitle(String uuid, String progress) {
        mTitleTypeLayout.setVisibility(View.VISIBLE);
        uuidTv.setText(uuid);
        kindTv.setText(progress);
    }

    private void drawDivide(String divideText) {
        LinearLayout divideLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params;

        if (divideText.isEmpty()) {
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) DpiUtil.convertDpToPixel(mContext, 1));

            if (isContentsDivide) {
                params.setMargins(DP_16, DP_10, DP_16, DP_10);
            } else {
                params.setMargins(0, 0, 0, 0);
                isContentsDivide = true;
            }
        } else {
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(0, 0, 0, 0);
            TextView newTv = createDivideInText(divideText);
            CommonUtils.textSizeSetting(mContext, newTv);
            divideLayout.addView(newTv);
        }

        divideLayout.setLayoutParams(params);
        divideLayout.setBackgroundColor(mContext.getColor(R.color.white_two));

        parentContainer.addView(divideLayout);
        layoutHm.put(parentContainer.getChildCount(), "divide");
    }

    private TextView createDivideInText(String divideText) {
        TextView tv = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(DP_16, DP_12, DP_16, DP_11);


        tv.setText(divideText);
        tv.setTextAppearance(R.style.divideTextStyle);
        tv.setLayoutParams(params);
        return tv;
    }

    private void drawDynamicDetailView(String title, String contents) {
        DynamicDetailItemView dView = new DynamicDetailItemView(mContext);
        dView.mappingData(title, contents);
        parentContainer.addView(dView);
        layoutHm.put(parentContainer.getChildCount(), "text");
    }

    private void drawDynamicDetailButton(String left, String right, String attr01, String attr02) {
        if (left.isEmpty()) {
            rejectLayout.setVisibility(View.GONE);
        } else {
            rejectLayout.setVisibility(View.VISIBLE);
            mRejectTextview.setText(left);
            mRejectTextview.setTag(attr01);
        }
        if (right.isEmpty()) {
            accpetLayout.setVisibility(View.GONE);
        } else {
            accpetLayout.setVisibility(View.VISIBLE);
            mAcceptTextview.setText(right);
            mAcceptTextview.setTag(attr02);
        }
//        DynamicDetailButtonItemView dView = new DynamicDetailButtonItemView(mContext);
//        dView.setViewData(left, right, attr01, attr02, this);
//        parentContainer.addView(dView);
    }

    private void drawEditDetailView() {
        mDynamicEditWorkView = new DynamicDetailEditItemView(mContext);
        mDynamicEditWorkView.setViewData(this);
        parentContainer.addView(mDynamicEditWorkView);
        layoutHm.put(parentContainer.getChildCount(), "editWork");
    }

    private void drawTxtBtnDetailView(String userInfo, String usedId) {
        mDynamicDetailTxtBtnView = new DynamicDetailTxtBtnItemView(mContext);
        mDynamicDetailTxtBtnView.setViewData(userInfo, usedId, this);
        parentContainer.addView(mDynamicDetailTxtBtnView);
        layoutHm.put(parentContainer.getChildCount(), "txtBtn");
    }

    private void drawSingleTextDetailView(String text) {
        DynamicDetailSingleTextItemView sigleTextView = new DynamicDetailSingleTextItemView(mContext);
        sigleTextView.setViewData(text);
        parentContainer.addView(sigleTextView);
        layoutHm.put(parentContainer.getChildCount(), "singleText");
    }

    private void drawApprovalHistory(ArrayList<Res_AP_IF_104_VO.dynamicDetailList> list) {
        RecyclerView recyclerView = new RecyclerView(mContext);
        ApprovalHistoryAdapter adapter = new ApprovalHistoryAdapter(list);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        parentContainer.addView(recyclerView);
        layoutHm.put(parentContainer.getChildCount(), "appList");
    }

    private void drawAttachView(ArrayList<Res_AP_IF_104_VO.dynamicDetailList> list) {
        DynamicDetailAttachItemView attachItemView = new DynamicDetailAttachItemView(mContext);
        attachItemView.setViewData(list, this);
        parentContainer.addView(attachItemView);
        layoutHm.put(parentContainer.getChildCount(), "attach");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    /**
     * 동적 상세 데이터
     */
    public void getDynamicDetailList(String userId, String sysId, String menuId, String docId, String param01, String param02, String param03, String param04, String param05, boolean isFinal, int position) {
        //showProgressBar();
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("sysId", sysId);
        hm.put("menuId", menuId);
        hm.put("docId", docId);
        hm.put("param01", param01);
        hm.put("param02", param02);
        hm.put("param03", param03);
        hm.put("param04", param04);
        hm.put("param05", param05);
        mUserId = userId;
        mSysId = sysId;
        mMenuId = menuId;
        mDocId = docId;
        mParam01 = param01;
        mParam02 = param02;
        mParam03 = param03;
        mParam04 = param04;
        mParam05 = param05;

        mIsFinal = isFinal;
        mPosition = position;

        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getDynamicDetailList(hm, (NetworkPresenter.getDynamicDetailListListener) result -> {
            String errMsg = getResources().getString(R.string.txt_network_error);
            //hideProgressBar();
            shimmerStop();
            if (result != null) {
                if ("200".equals(result.getStatus().getStatusCd())) {
                    if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                        mDetailList = new ArrayList<>();
                        mDetailList = result.getResult().getDynamicDetailList();
                        if (null != mDetailList && !mDetailList.isEmpty()) {
                            drawView();
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
            if (!TextUtils.isEmpty(errMsg)) {
                viewMessage(errMsg, false);
            }
        });
    }

    /**
     * 동적 상세 버튼 액션
     */
    public void getDynamicDetailAction(String userId, String sysId, String menuId, String docId, String actionType, String actionOpinion,
                                       String param01, String param02, String param03, String param04, String param05, String title) {
        showProgressBar();
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("sysId", sysId);
        hm.put("menuId", menuId);
        hm.put("docId", docId);
        hm.put("actionType", actionType);
        hm.put("actionOpinion", actionOpinion);
        hm.put("param01", param01);
        hm.put("param02", param02);
        hm.put("param03", param03);
        hm.put("param04", param04);
        hm.put("param05", param05);
        mUserId = userId;
        mSysId = sysId;
        mMenuId = menuId;
        mDocId = docId;
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getDynamicDetailAction(hm, (NetworkPresenter.getDynamicDetailActionListener) result -> {
            String errMsg = "";
            hideProgressBar();
            if (result != null) {
                if ("200".equals(result.getStatus().getStatusCd())) {
                    if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                        checkRemainApproval(title);
                        //errMsg = result.getResult().getErrorMsg();
                        //viewMessage(errMsg, true);
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
            if (!TextUtils.isEmpty(errMsg)) {
                viewMessage(errMsg, false);
            }
        });
    }

    //남은 결재건수를 체크_yong79
    private void checkRemainApproval(String title) {

        //boolean isFinal = true;
        if (mIsFinal) { //목록으로
            String msg = String.format(getResources().getString(R.string.txt_approval_success), title);
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();

            ((DynamicDetailActivity) mContext).finishDetailActivityParentRefresh();
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
                            ((DynamicDetailActivity) mContext).finishDetailActivityParentRefresh();
                            break;
                        case R.id.btn_right:
                            //다음 결재건
                            dialog.dismiss();
                            ((DynamicDetailActivity) mContext).nextApprovalLoading(mPosition);
                            break;
                    }
                }
            });
            dialog.show(((DynamicDetailActivity) mContext).getSupportFragmentManager());
        }
    }


    //error message
    private void viewMessage(String errMsg, boolean isFinish) {

        TextDialog dialog;
        dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
        dialog.setCancelable(false);
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (isFinish) {
                    ((DynamicDetailActivity) getContext()).finishDetailActivity();
                }
            }
        });
        dialog.show(CommonUtils.getFragmentManager(getContext()));
    }

    public void showProgressBar() {
        progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onDateClickListener(String attr01) {

    }

    @Override
    public void onAttachClickListener(ArrayList<AttachFileListVO> attachList) {
        if (attachList == null || attachList.size() == 0) {
            return;
        }

        AttatchFileDialog dialog = AttatchFileDialog.newInstance();
        dialog.setInterface(new AttatchFileDialog.OnClickListener() {
            @Override
            public void selectPosition(int position) {
                String docId = "mTotalSign_" + mSysId + "_" + mDocId + "_" + position;//서비스_문서id_파일순서
                viewDoc(attachList.get(position), docId);
            }
        });
        dialog.setData(attachList);
        dialog.show(((DynamicDetailActivity) getContext()).getSupportFragmentManager(), "");
    }

    private void viewDoc(AttachFileListVO data, String docId) {
        String docUrl = data.getUrl();

        showProgressBar();
        presenter = new NetworkPresenter();
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

    public void textSizeAdj() {
        CommonUtils.changeTextSize(mContext, uuidTv);
        CommonUtils.changeTextSize(mContext, kindTv);

        //parentContainer기본으로 하나 가지고 있어서 1부터 시작
        for (int i = 1; i < parentContainer.getChildCount() + 1; i++) {
            if (layoutHm.get(i) != null) {
                switch (layoutHm.get(i)) {
                    case "divide": {
                        LinearLayout ll = (LinearLayout) parentContainer.getChildAt(i - 1);
                        if (ll.getChildCount() > 0) {
                            TextView tv = (TextView) ll.getChildAt(0);
                            CommonUtils.changeTextSize(mContext, tv);
                        }
                        break;
                    }
                    case "text": {
                        DynamicDetailItemView ddv = (DynamicDetailItemView) parentContainer.getChildAt(i - 1);
                        ddv.textSizeAdj();
                        break;
                    }
                    case "appList": {
                        RecyclerView recyclerView = (RecyclerView) parentContainer.getChildAt(i - 1);
                        ApprovalHistoryAdapter adapter = (ApprovalHistoryAdapter)recyclerView.getAdapter();
                        adapter.notifyDataSetChanged();
                        break;
                    }
//                    case "actBtn": {
//                        break;
//                    }
                    case "editWork": {
                        DynamicDetailEditItemView ddeiv = (DynamicDetailEditItemView) parentContainer.getChildAt(i - 1);
                        ddeiv.textSizeAdj();
                        break;
                    }
                    case "txtBtn": {
                        DynamicDetailTxtBtnItemView ddtbiv = (DynamicDetailTxtBtnItemView) parentContainer.getChildAt(i - 1);
                        ddtbiv.textSizeAdj();
                        break;
                    }
                    case "singleText": {
                        DynamicDetailSingleTextItemView ddstiv = (DynamicDetailSingleTextItemView) parentContainer.getChildAt(i - 1);
                        ddstiv.textSizeAdj();
                        break;
                    }
//                    case "attach": {
//                        DynamicDetailAttachItemView ddaiv = (DynamicDetailAttachItemView) parentContainer.getChildAt(i - 1);
//                        break;
//                    }
                }
            }
        }
    }

    //필수값을 체크 - 투입공수, 내용, 완료일
    private boolean checkEditWorkInput() {
        //투입공수 체크
        String min = mDynamicEditWorkView.getWorkTime().toString();
        if (TextUtils.isEmpty(min) || Integer.parseInt(min) == 0) {
            return false;
        }
        //내용 체크
        String contents = mDynamicEditWorkView.getContents().toString().trim();
        if (TextUtils.isEmpty(contents)) {
            return false;
        }
        return true;
    }

    @Override
    public void onTalkClickListener(String userId) {
        //Todo KolonTalk 인터페이스 추가하여 CompanyCode값 적용 필요
        CommonUtils.callKolonTalk(getContext(), userId, mCompanyCode);
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
