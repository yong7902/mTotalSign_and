package com.kolon.sign2.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.approval.ApprovalDetailActivity;
import com.kolon.sign2.dialog.AttachFileViewerDialog;
import com.kolon.sign2.dialog.AttatchFileDialog;
import com.kolon.sign2.dynamic.dynamicDetail.DynamicDetailActivity;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_104_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.ArrayList;

public class DynamicDetailAttachItemView extends LinearLayout {

    TextView mDynamicAttachNum;
    LinearLayout mDynamicAttachLayout;
    String mUserId = "";
    private AttachViewClickListener mListener;
    ArrayList<AttachFileListVO> fileList = new ArrayList<>();
    public interface AttachViewClickListener {
        void onAttachClickListener(ArrayList<AttachFileListVO> attachList);
    }
    public DynamicDetailAttachItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailAttachItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailAttachItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailAttachItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_attach_item, this, false);

        mDynamicAttachLayout = view.findViewById(R.id.ll_dynamic_detail_attach_file);
        mDynamicAttachNum = view.findViewById(R.id.tv_dynamic_detail_attach_file_num);
        mDynamicAttachLayout.setOnClickListener(v -> {
            showAttachDailog();
        });


        addView(view);
    }

    public void setViewData(ArrayList<Res_AP_IF_104_VO.dynamicDetailList> list, AttachViewClickListener listener) {
        int count = 0;
        if (null != list && !list.isEmpty()) {
            count = list.size();
            fileList = new ArrayList<>();
            for (int index =- 0;index < list.size(); index++) {
                AttachFileListVO vo = new AttachFileListVO();
                vo.setChecked(false);
                vo.setName(list.get(index).getLeftText());
                vo.setUrl(list.get(index).getRightText());
                fileList.add(vo);
            }
        }
        if (null != listener) {
            mListener = listener;
        }
        mDynamicAttachNum.setText(String.valueOf(count));
    }

    private void showAttachDailog() {
        if (null != mListener) {
            mListener.onAttachClickListener(fileList);
        }
    }
}
