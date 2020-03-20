package com.kolon.sign2.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;

public class HomeDataRowView extends LinearLayout {
    private Context mContext;

    private LinearLayout home_lay1, btn_state;
    private TextView tv_num, tv_title, tv_state2, tv_name, tv_time, tv_state;
    private ImageView iv_approval_lock, iv_approval_file, iv_approval_comment;

    public HomeDataRowView(Context context) {
        super(context);
        initView(context);
    }

    public HomeDataRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeDataRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_home_data, this, false);
        home_lay1 = (LinearLayout)v.findViewById(R.id.home_lay1);

        tv_num = (TextView) v.findViewById(R.id.tv_num);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_state2 = (TextView) v.findViewById(R.id.tv_state2);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_time = (TextView) v.findViewById(R.id.tv_time);

        btn_state = (LinearLayout)v.findViewById(R.id.btn_state);
        tv_state = (TextView) v.findViewById(R.id.tv_state);

        iv_approval_lock = (ImageView)v.findViewById(R.id.iv_approval_lock);
        iv_approval_file = (ImageView)v.findViewById(R.id.iv_approval_file);
        iv_approval_comment = (ImageView)v.findViewById(R.id.iv_approval_comment);

        CommonUtils.textSizeSetting(mContext, tv_num);
        CommonUtils.textSizeSetting(mContext, tv_title);
        CommonUtils.textSizeSetting(mContext, tv_state2);
        CommonUtils.textSizeSetting(mContext, tv_name);
        CommonUtils.textSizeSetting(mContext, tv_time);

        addView(v);
    }

    public void setData(Res_AP_IF_002_VO.result.APPROVAL_LIST data) {
        //text size
        CommonUtils.changeTextSize(mContext, tv_num);
        CommonUtils.changeTextSize(mContext, tv_title);
        CommonUtils.changeTextSize(mContext, tv_state2);
        CommonUtils.changeTextSize(mContext, tv_name);
        CommonUtils.changeTextSize(mContext, tv_time);

        //1단계 approvalId, approvalCase를 비교
        if (TextUtils.isEmpty(data.getApprovalId()) && TextUtils.isEmpty(data.getApprovalCase())) {
            //둘다 없을때
            home_lay1.setVisibility(View.GONE);
        } else {
            home_lay1.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(data.getApprovalId())) {
                tv_num.setText("");
            } else {
                tv_num.setText(data.getApprovalId());
            }
            if (TextUtils.isEmpty(data.getApprovalCase())) {
                btn_state.setVisibility(View.GONE);
            } else {
                btn_state.setVisibility(View.VISIBLE);
                tv_state.setText(data.getApprovalCase());
            }
        }

        //2단계 title, status를 비교
        if (TextUtils.isEmpty(data.getTitle())) {
            tv_title.setText("");
        } else {
            tv_title.setText(data.getTitle());
        }
        if (TextUtils.isEmpty(data.getStatus())) {
            tv_state2.setText("");
        } else {
            tv_state2.setText(data.getStatus());
        }
        //3단계 requester, reqDatetime 비교 sysid = sign일경우 첨부파일등도 비교
        if (TextUtils.isEmpty(data.getRequester())) {
            tv_name.setText("");
        } else {
            tv_name.setText(data.getRequester());
        }
        if (TextUtils.isEmpty(data.getReqDatetime())) {
            tv_time.setText("");
        } else {
            tv_time.setText(data.getReqDatetime());
        }

        if("Y".equalsIgnoreCase(data.getHasattachYN())){
            iv_approval_file.setVisibility(View.VISIBLE);
        }else{
            iv_approval_file.setVisibility(View.GONE);
        }
        if("Y".equalsIgnoreCase(data.getHasopinionYN())){
            iv_approval_comment.setVisibility(View.VISIBLE);
        }else{
            iv_approval_comment.setVisibility(View.GONE);
        }
        if("N".equalsIgnoreCase(data.getIsPublic())){
            //자물쇠
            iv_approval_lock.setVisibility(View.VISIBLE);
        }else{
            iv_approval_lock.setVisibility(View.GONE);
        }

        //전자결재(sign)일 때만 파일,의견,자물쇠 이미지를 보여준다
//        if("sign".equals(data.getSysId())){
//            if("Y".equalsIgnoreCase(data.getHasattachYN())){
//                iv_approval_file.setVisibility(View.VISIBLE);
//            }else{
//                iv_approval_file.setVisibility(View.GONE);
//            }
//            if("Y".equalsIgnoreCase(data.getHasopinionYN())){
//                iv_approval_comment.setVisibility(View.VISIBLE);
//            }else{
//                iv_approval_comment.setVisibility(View.GONE);
//            }
//            if("Y".equalsIgnoreCase(data.getIsPublic())){
//                //자물쇠
//                iv_approval_lock.setVisibility(View.VISIBLE);
//            }else{
//                iv_approval_lock.setVisibility(View.GONE);
//            }
//        }else{
//            iv_approval_file.setVisibility(View.GONE);
//            iv_approval_comment.setVisibility(View.GONE);
//            iv_approval_lock.setVisibility(View.GONE);
//        }
    }
}
