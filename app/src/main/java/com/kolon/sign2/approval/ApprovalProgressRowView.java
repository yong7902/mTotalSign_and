package com.kolon.sign2.approval;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_020_VO;

/**
 * 결재진행상태, 의견보기 row
 */
public class ApprovalProgressRowView extends LinearLayout {
    private Context mContext;

    private Button btn_state;
    private TextView tv_name, tv_state, tv_team, tv_time, tv_num;
    private LinearLayout lay_comment;
    private TextView tv_comment;
    private View div;

    public ApprovalProgressRowView(Context context) {
        super(context);
        initView(context);
    }

    public ApprovalProgressRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ApprovalProgressRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_approval_progress, this, false);


        btn_state = (Button) v.findViewById(R.id.btn_state);
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_state = (TextView) v.findViewById(R.id.tv_state);
        tv_team = (TextView) v.findViewById(R.id.tv_team);
        tv_time = (TextView) v.findViewById(R.id.tv_time);
        tv_num = (TextView) v.findViewById(R.id.tv_num);
        lay_comment = (LinearLayout) v.findViewById(R.id.lay_comment);
        tv_comment = (TextView) v.findViewById(R.id.tv_comment);
        div = (View) v.findViewById(R.id.view_div);


        CommonUtils.textSizeSetting(context, tv_name);
        CommonUtils.textSizeSetting(context, tv_state);
        CommonUtils.textSizeSetting(context, tv_team);
        CommonUtils.textSizeSetting(context, tv_time);
        CommonUtils.textSizeSetting(context, tv_num);
        CommonUtils.textSizeSetting(context, tv_comment);



        addView(v);
    }

    //결재 진행상태 , 의견
    public void setData(Res_AP_IF_016_VO.result.apprLineList data, int position) {
        /**
         * 기안자 부터 상단 노출됨
         * 결재 순번: 서버에서 내려주는 값으로 표시
         * 결재상태: 기안자-기안, 결재자-가결/진행/미결, 참조자-확인/미확인
         * 결재유형: 참조, 일반결재, 개인협조, 개인협조(병렬)
         * 표시, 기안자 직책/직급, 팀명, 기안자는 기안일시, 결재자는 결재일시, 참조자는 확인일시 노출
         * 결재 전/확인 전일 경우 결재일시/확인일시 없음
         *
         */

        //4자이상일때 - 예)개인협조 병렬
        if (data.getActionType() != null && data.getActionType().length() > 4) {
            btn_state.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, (int) DpiUtil.convertDpToPixel(mContext, 17)));
        }

        btn_state.setText(data.getActionType());
        /*
        if (position == 0) {
            //기안자
            btn_state.setBackgroundResource(R.drawable.drw_round_blue_btn);
            btn_state.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            div.setVisibility(View.VISIBLE);
        } else {
            btn_state.setBackgroundResource(R.drawable.drw_round_stroke_grey2_btn);
            btn_state.setTextColor(ContextCompat.getColor(mContext, R.color.brownish_grey));
            div.setVisibility(View.GONE);
        }
        */

        if(data.getActionType() == "기안"){ //기안인 경우 파란색
            btn_state.setBackgroundResource(R.drawable.drw_round_blue_btn);
            btn_state.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            div.setVisibility(View.VISIBLE);
        }else{
            btn_state.setBackgroundResource(R.drawable.drw_round_stroke_grey2_btn);
            btn_state.setTextColor(ContextCompat.getColor(mContext, R.color.brownish_grey));
            div.setVisibility(View.GONE);
        }

        if(position == 0){ //1번 결재인경우 두꺼운 구분선
            div.setVisibility(View.VISIBLE);
        }else{
            div.setVisibility(View.GONE);
        }

        tv_num.setText(data.getSn());//num...

        if (TextUtils.isEmpty(data.getName()) || "-".equals(data.getName())) {
            //이름이 없으면 부서명을 넣고 부서칸은 공란 처리
            tv_name.setText(data.getDepartment());
            tv_team.setText("");
        } else {
            tv_name.setText(data.getName() + " " + data.getPosition());//이름
            tv_team.setText(data.getDepartment());
        }

        //상태
        if (mContext.getResources().getString(R.string.txt_approval_status_progress).equals(data.getActivity())) {
            //진행
            tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
        } else {
            tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey_two));
        }
        tv_state.setText(data.getActivity());
        tv_time.setText(data.getActionTime());

        if (TextUtils.isEmpty(data.getComment())) {
            lay_comment.setVisibility(View.GONE);
        } else {
            lay_comment.setVisibility(View.VISIBLE);
            tv_comment.setText(data.getComment());
        }

        CommonUtils.changeTextSize(mContext, tv_name);
        CommonUtils.changeTextSize(mContext, tv_state);
        CommonUtils.changeTextSize(mContext, tv_team);
        CommonUtils.changeTextSize(mContext, tv_time);
        CommonUtils.changeTextSize(mContext, tv_num);
        CommonUtils.changeTextSize(mContext, tv_comment);
    }
}
