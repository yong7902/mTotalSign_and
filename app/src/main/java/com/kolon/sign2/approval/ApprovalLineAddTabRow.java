package com.kolon.sign2.approval;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kolon.sign2.R;

public class ApprovalLineAddTabRow extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private TextView keyword;
    private ImageView iv, home_img;
    private View.OnClickListener l;

    public ApprovalLineAddTabRow(Context context) {
        super(context);
        initView(context);
    }
    public ApprovalLineAddTabRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public ApprovalLineAddTabRow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.row_approval_line_add_tab, this, false);

        keyword = (TextView)v.findViewById(R.id.keyword_txt);
        setTextColor(false);
        iv = (ImageView)v.findViewById(R.id.keyword_image);
        home_img = (ImageView)v.findViewById(R.id.home_img);

        setOnClickListener(this);

        addView(v);
    }

    public void setTabOnClickListener(View.OnClickListener l){
        this.l = l;
    }

    public void setTabText(String txt){
        keyword.setText(txt);
    }

    public void setViewDiv(boolean isView){
        if(isView){
            iv.setVisibility(View.VISIBLE);
        }else{
            iv.setVisibility(View.INVISIBLE);
        }
    }

    public void setHomeImg(boolean isView){
        if(isView){
            home_img.setVisibility(View.VISIBLE);
        }else{
            home_img.setVisibility(View.GONE);
        }
    }

    public void setTextColor(boolean isSelect){
        if (isSelect) {
            keyword.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
        } else {
            keyword.setTextColor(ContextCompat.getColor(mContext, R.color.black_3));
        }
    }

    public String getTabText(){
        return keyword.getText().toString();
    }

    @Override
    public void onClick(View view) {
        if(l != null) l.onClick(view);
    }
}
