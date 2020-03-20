package com.kolon.sign2.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kolon.sign2.R;

/**
 * 홈 - 최상단 총미승인건수 항목
 * */
public class HomeTopTotalRowView extends LinearLayout {

    private Context mContext;
    private TextView tv_home_qty;

    public HomeTopTotalRowView(Context context) {
        super(context);
        initView(context);
    }

    public HomeTopTotalRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeTopTotalRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public HomeTopTotalRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_home_top_total, this, false);

        tv_home_qty = (TextView)v.findViewById(R.id.tv_home_qty);


        addView(v);
    }

    public void setData(String totalCnt) {
        String totalQty = "000";
        if (TextUtils.isEmpty(totalQty)) {
            totalQty = "0";
        }

        tv_home_qty.setText(String.format(getResources().getString(R.string.txt_home_data_qty), String.valueOf(totalCnt)));
    }
}
