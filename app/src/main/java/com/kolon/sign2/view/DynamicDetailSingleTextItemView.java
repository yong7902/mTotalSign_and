package com.kolon.sign2.view;

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

public class DynamicDetailSingleTextItemView extends LinearLayout {

    TextView mDynamicSingleText;
    public DynamicDetailSingleTextItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailSingleTextItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailSingleTextItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailSingleTextItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_singletext_item, this, false);

        mDynamicSingleText = view.findViewById(R.id.tv_detail_single_text_item);
        CommonUtils.textSizeSetting(getContext(), mDynamicSingleText);
        addView(view);
    }

    public void setViewData(String text) {
        mDynamicSingleText.setText(text);
    }

    public void textSizeAdj(){
        CommonUtils.changeTextSize(getContext(), mDynamicSingleText);
    }
}
