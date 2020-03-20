package com.kolon.sign2.approval;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_013_VO.result.apprList;

/**
 * 전자결재 결재목록리스트 row
 * */
public class ApprovalPersonalRowView extends LinearLayout {
    private Context mContext;

    private TextView tv_approval_txt;
    private TextView tv_approval_state;
    private TextView tv_approval_name;
    private TextView tv_approval_time;

    private ImageView iv_approval_lock, iv_approval_file, iv_approval_comment;

    public ApprovalPersonalRowView(Context context) {
        super(context);
        initView(context);
    }
    public ApprovalPersonalRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public ApprovalPersonalRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_approval_personal, this, false);

        tv_approval_txt = (TextView)v.findViewById(R.id.tv_approval_txt);
        tv_approval_state = (TextView)v.findViewById(R.id.tv_approval_state);
        tv_approval_name = (TextView)v.findViewById(R.id.tv_approval_name);
        tv_approval_time = (TextView)v.findViewById(R.id.tv_approval_time);

        tv_approval_txt.setTag(tv_approval_txt.getTextSize());
        tv_approval_state.setTag(tv_approval_state.getTextSize());
        tv_approval_name.setTag(tv_approval_name.getTextSize());
        tv_approval_time.setTag(tv_approval_time.getTextSize());


        iv_approval_lock = (ImageView)v.findViewById(R.id.iv_approval_lock);
        iv_approval_file = (ImageView)v.findViewById(R.id.iv_approval_file);
        iv_approval_comment = (ImageView)v.findViewById(R.id.iv_approval_comment);

        addView(v);
    }

    public void setData(apprList data, int position){

        //text size
        CommonUtils.changeTextSize(mContext, tv_approval_txt);
        CommonUtils.changeTextSize(mContext, tv_approval_state);
        CommonUtils.changeTextSize(mContext, tv_approval_name);
        CommonUtils.changeTextSize(mContext, tv_approval_time);

        tv_approval_txt.setText(data.getTitle());
        tv_approval_state.setText(data.getStatus());
        tv_approval_name.setText(data.getAuthor());
        tv_approval_time.setText(data.getPubDate());

        if("Y".equalsIgnoreCase(data.getHasattachYn())){
            iv_approval_file.setVisibility(View.VISIBLE);
        }else{
            iv_approval_file.setVisibility(View.GONE);
        }

        if("Y".equalsIgnoreCase(data.getHasopinionYn())){
            iv_approval_comment.setVisibility(View.VISIBLE);
        }else{
            iv_approval_comment.setVisibility(View.GONE);
        }

        //기밀문서
        if ("N".equalsIgnoreCase(data.getIsPublic())) {
            iv_approval_lock.setVisibility(View.VISIBLE);
        }else{
            iv_approval_lock.setVisibility(View.GONE);
        }

    }
}
