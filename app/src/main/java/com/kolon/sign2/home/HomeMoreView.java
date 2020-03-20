package com.kolon.sign2.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;

public class HomeMoreView extends LinearLayout {
    private TextView tv_more;
    public HomeMoreView(Context context) {
        super(context);
        initView(context);
    }
    public HomeMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public HomeMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_home_more, this, false);
        tv_more = (TextView)v.findViewById(R.id.tv_more);

        CommonUtils.textSizeSetting(context, tv_more);

        addView(v);
    }
}
