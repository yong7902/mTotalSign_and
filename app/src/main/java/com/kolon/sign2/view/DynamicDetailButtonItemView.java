package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kolon.sign2.R;

public class DynamicDetailButtonItemView extends LinearLayout {

    /**
     * not used
     * */
    ConstraintLayout leftBtn;
    ConstraintLayout rightBtn;
    TextView leftBtnTextView;
    TextView rightBtnTextView;
    private ButtonClickListener mListener;
    private String mArrt01 = "";
    private String mArrt02 = "";

    public interface ButtonClickListener {
        void onLeftBtnClickListener(String attr01);
        void onRightBtnClickListener(String attr02);
    }
    public DynamicDetailButtonItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailButtonItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailButtonItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailButtonItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_detail_button_item, this, false);

        leftBtn = view.findViewById(R.id.dynamic_detail_reject_btn);
        rightBtn = view.findViewById(R.id.dynamic_detail_accept_btn);
        leftBtnTextView = view.findViewById(R.id.reject_text1);
        rightBtnTextView = view.findViewById(R.id.accept_text1);
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLeftBtnClickListener(mArrt01);
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRightBtnClickListener(mArrt02);
            }
        });

        addView(view);
    }

    public void setViewData(String left, String right, String attr01, String attr02,ButtonClickListener listener) {
        if (listener != null) {
            mListener = listener;
        }
        mArrt01 = attr01;
        mArrt02 = attr02;
        if (left.isEmpty()) {
            leftBtn.setVisibility(View.GONE);
        } else {
            leftBtn.setVisibility(View.VISIBLE);
            leftBtnTextView.setText(left);
        }
        if (right.isEmpty()) {
            rightBtn.setVisibility(View.GONE);
        } else {
            rightBtn.setVisibility(View.VISIBLE);
            rightBtnTextView.setText(right);
        }

    }


}
