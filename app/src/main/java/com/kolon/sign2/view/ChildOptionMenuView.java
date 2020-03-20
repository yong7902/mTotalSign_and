package com.kolon.sign2.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;


public class ChildOptionMenuView extends LinearLayout {

    public interface OnMenuItemClickListener {
         void onMenuItemClicked(String menuItem);
    }

    private OnMenuItemClickListener onMenuItemClickListener;

    private Button menuSelectBtn;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        onMenuItemClickListener = listener;
    }

    public ChildOptionMenuView(Context context) {
        super(context);
        initView();
    }

    public ChildOptionMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChildOptionMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_child_option_menu, this, false);

        menuSelectBtn = (Button) view.findViewById(R.id.depart_tab_select_menu_btn);

        addView(view);
    }


}
