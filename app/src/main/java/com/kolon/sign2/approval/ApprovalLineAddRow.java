package com.kolon.sign2.approval;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_201_VO;
import com.kolon.sign2.vo.Res_AP_IF_202_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;

import java.util.ArrayList;

/**
 * 결재선 추가 하위 row
 * */
public class ApprovalLineAddRow extends LinearLayout {

    private Context context;
    private LinearLayout approval_line_add_lay1, approval_line_add_lay2;
    private TextView approval_line_add_txt1, approval_line_add_txt2, approval_line_add_txt3;

    public ApprovalLineAddRow(Context context) {
        super(context);
        initView(context);
    }
    public ApprovalLineAddRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public ApprovalLineAddRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_approval_line_add, this, false);
        approval_line_add_lay1 = (LinearLayout)v.findViewById(R.id.approval_line_add_lay1);

        approval_line_add_lay2 = (LinearLayout)v.findViewById(R.id.approval_line_add_lay2);

        approval_line_add_txt1 = (TextView)v.findViewById(R.id.approval_line_add_txt1);
        approval_line_add_txt2 = (TextView)v.findViewById(R.id.approval_line_add_txt2);
        approval_line_add_txt3 = (TextView)v.findViewById(R.id.approval_line_add_txt3);

        CommonUtils.textSizeSetting(context, approval_line_add_txt1);
        CommonUtils.textSizeSetting(context, approval_line_add_txt2);
        CommonUtils.textSizeSetting(context, approval_line_add_txt3);

        addView(v);
    }

    private void changeTextSize(){
        CommonUtils.changeTextSize(context, approval_line_add_txt1);
        CommonUtils.changeTextSize(context, approval_line_add_txt2);
        CommonUtils.changeTextSize(context, approval_line_add_txt3);
    }


    public void setData(Res_AP_IF_203_VO.result.apprSpList data){
        approval_line_add_txt1.setText(data.getName());
        //roleName
        String team = data.getJobTitle();
        if (!TextUtils.isEmpty(data.getRoleName())) {
            team = data.getRoleName()+"/"+data.getJobTitle();
        }
        approval_line_add_txt2.setText(team);

        approval_line_add_lay1.setVisibility(View.VISIBLE);
        approval_line_add_lay2.setVisibility(View.GONE);

        changeTextSize();
    }

    //회사
    public void setData1(Res_AP_IF_201_VO.result.apprCompList data){
        approval_line_add_txt1.setText(data.getName());
        approval_line_add_txt2.setVisibility(View.GONE);
        approval_line_add_lay1.setVisibility(View.VISIBLE);
        approval_line_add_lay2.setVisibility(View.GONE);

        changeTextSize();
    }

    //부서
    public void setData2(Res_AP_IF_202_VO.result.apprOrganizationList data, int position){

        approval_line_add_lay1.setVisibility(View.GONE);
        approval_line_add_lay2.setVisibility(View.VISIBLE);

        approval_line_add_txt3.setText(data.getDeptName());

        changeTextSize();
    }

    //사람
    public void setData3(Res_AP_IF_203_VO.result.apprSpList data){
        approval_line_add_txt1.setText(data.getName());
        //roleName
        String team = data.getJobTitle();
        if (!TextUtils.isEmpty(data.getRoleName())) {
            team = data.getRoleName()+"/"+data.getJobTitle();
        }
        approval_line_add_txt2.setText(team);

        approval_line_add_lay1.setVisibility(View.VISIBLE);
        approval_line_add_lay2.setVisibility(View.GONE);

        changeTextSize();
    }

    public void setData(ApprovalTotalVO data){
        if(data.header){
            //사람
            approval_line_add_txt1.setText(data.getName());
            //roleName
            String team = data.getJobTitle();
            if (!TextUtils.isEmpty(data.getRoleName())) {
                team = data.getRoleName()+"/"+data.getJobTitle();
            }
            approval_line_add_txt2.setText(team);

            approval_line_add_lay1.setVisibility(View.VISIBLE);
            approval_line_add_lay2.setVisibility(View.GONE);
        }else{
            //부서
            approval_line_add_lay1.setVisibility(View.GONE);
            approval_line_add_lay2.setVisibility(View.VISIBLE);

            approval_line_add_txt3.setText(data.getDeptName());
        }

        changeTextSize();
    }
}
