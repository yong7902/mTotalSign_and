package com.kolon.sign2.home;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;

/**
 * 홈 - 각 항목별 건수
 * */
public class HomeCategoryRowView extends LinearLayout {

    private Context mContext;

    private LinearLayout layout_home_category;
    private TextView tv_home_category_title;
    private TextView tv_home_category_num;

    public HomeCategoryRowView(Context context) {
        super(context);
        initView(context);
    }

    public HomeCategoryRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HomeCategoryRowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public HomeCategoryRowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_home_category, this, false);
        layout_home_category = (LinearLayout) v.findViewById(R.id.layout_home_category);
        tv_home_category_title = (TextView) v.findViewById(R.id.tv_home_category_title);
        tv_home_category_num = (TextView) v.findViewById(R.id.tv_home_category_num);

        CommonUtils.textSizeSetting(mContext, tv_home_category_title);
        CommonUtils.textSizeSetting(mContext, tv_home_category_num);

        addView(v);
    }


    public void setData(Res_AP_IF_002_VO.result.APPROVAL_LIST data) {

        CommonUtils.changeTextSize(mContext, tv_home_category_title);
        CommonUtils.changeTextSize(mContext, tv_home_category_num);

        if(TextUtils.isEmpty(data.totalCnt)){
            data.totalCnt = "0";
        }

        tv_home_category_title.setText(data.getTitle());
        tv_home_category_num.setText(String.format(getResources().getString(R.string.txt_home_case), data.totalCnt));
    }
}
