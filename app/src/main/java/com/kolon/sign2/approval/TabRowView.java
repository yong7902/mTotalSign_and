package com.kolon.sign2.approval;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

public class TabRowView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private TextView tv_tab_row;
    private Button btn_tab_no;
    private View tab_bottom_select;

    private LinearLayout lay;

    public TabRowView(Context context) {
        super(context);
        initView(context);
    }
    public TabRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public TabRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_tab_view, this, false);

        tv_tab_row = (TextView)v.findViewById(R.id.tv_tab_row);
        btn_tab_no = (Button)v.findViewById(R.id.btn_tab_no);
        tab_bottom_select = (View)v.findViewById(R.id.tab_bottom_select);

        lay = (LinearLayout)v.findViewById(R.id.lay_tab);

        addView(v);
    }

    public void setData(Res_AP_IF_102_VO.result.menuArray data, int position){
        int leftMargin = (int)DpiUtil.convertDpToPixel(mContext, 21);
        int rightMargin = (int)DpiUtil.convertDpToPixel(mContext, 21);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (position == 0) {
            //첫째칸 좌측은 14
            params.setMargins((int) DpiUtil.convertDpToPixel(mContext, 14), 0, rightMargin, 0);
        } else if (position == 4) {
            //마지막칸 우측은 14
            params.setMargins(leftMargin, 0, (int) DpiUtil.convertDpToPixel(mContext, 14), 0);
        } else {
            //중간은 좌우 25씩
            params.setMargins(leftMargin, 0, rightMargin, 0);
        }
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        lay.setLayoutParams(params);


        tv_tab_row.setText(data.getMenuName());

        boolean isBadge = true;
        if (TextUtils.isEmpty(data.getCountNum()) || "0".equals(data.getCountNum()))
            isBadge = false;
        if (!"Y".equals((data.getBadgeYn())))
            isBadge = false;
        if (!isBadge) {
            btn_tab_no.setVisibility(View.GONE);
        } else {
            btn_tab_no.setVisibility(View.VISIBLE);
            btn_tab_no.setText(data.getCountNum());
        }


        if (data.isSelected) {
            tv_tab_row.setSelected(true);
            tab_bottom_select.setVisibility(View.VISIBLE);
        } else {
            tv_tab_row.setSelected(false);
            tab_bottom_select.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

    }
}
