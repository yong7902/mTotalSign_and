package com.kolon.sign2.approval;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.dialog.OpinionListDialogAdapter;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

public class ApprovalOpinionPopupView extends LinearLayout {
    private Context mContext;
    private ArrayList<Res_AP_IF_016_VO.result.apprLineList>  data;
    private RecyclerView rv;

    private OnClickListener mInterface;
    public interface OnClickListener {
        void onClick();
    }
    public void setInterface(OnClickListener mInterface){
        this.mInterface = mInterface;
    }

    public ApprovalOpinionPopupView(Context context) {
        super(context);
        initView(context);
    }
    public ApprovalOpinionPopupView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public ApprovalOpinionPopupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_list, this, false);

        TextView titleTxt = (TextView)v.findViewById(R.id.dialog_title);
        titleTxt.setText(getResources().getString(R.string.txt_approval_opinion));
        TextView closeTxt = (TextView)v.findViewById(R.id.tv_close);
        closeTxt.setText(getResources().getString(R.string.txt_alert_confirm));
        closeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));

        rv = (RecyclerView)v.findViewById(R.id.dialog_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(mContext));

        LinearLayout closeBtn = (LinearLayout)v.findViewById(R.id.btn_cancel);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibility(View.GONE);
                if(mInterface != null){
                    mInterface.onClick();
                }
            }
        });
        addView(v);
    }

    public void setData(ArrayList<Res_AP_IF_016_VO.result.apprLineList> data){
        setVisibility(View.VISIBLE);
        this.data = data;
        OpinionListDialogAdapter adapter = new OpinionListDialogAdapter(data);
        rv.setAdapter(adapter);
    }
}
