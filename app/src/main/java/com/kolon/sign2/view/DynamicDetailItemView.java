package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;

public class DynamicDetailItemView extends LinearLayout {

    TextView titleTv;
    TextView contentsTv;

    public DynamicDetailItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_detail_item, this, false);

        titleTv = (TextView) view.findViewById(R.id.detail_item_title);
        contentsTv = (TextView) view.findViewById(R.id.detail_item_contents);

        CommonUtils.textSizeSetting(getContext(), titleTv);
        CommonUtils.textSizeSetting(getContext(), contentsTv);

        addView(view);
    }

    public void mappingData(String title, String contents) {
        titleTv.setText(title);
        contentsTv.setText(contents);
    }

    public void textSizeAdj(){
        CommonUtils.changeTextSize(getContext(), titleTv);
        CommonUtils.changeTextSize(getContext(), contentsTv);
    }

}
