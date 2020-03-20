package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;

/**
 * 글자크기 조절
 */
public class TextSizeAdjView extends LinearLayout {

    private RelativeLayout main_view, bg_view;
    private View top_div;
    private TextView tv1, tv2, tv3;
    private float textSize, oldTextSize;
    private SharedPreferenceManager mPref;

    private OnSizeClickListener mInterface;
    private Context mContext;

    private Animation upAni, downAni;
    private boolean isAnimating;

    public interface OnSizeClickListener {
        void onSize(float size);
    }

    public void setInterface(OnSizeClickListener mInterface) {
        this.mInterface = mInterface;
    }


    public TextSizeAdjView(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public TextSizeAdjView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    public TextSizeAdjView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {
        mPref = SharedPreferenceManager.getInstance(context);

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_textsize_adj, this, false);
        oldTextSize = mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0);
        ImageView btn_text_size_minus = (ImageView) v.findViewById(R.id.btn_text_size_minus);
        btn_text_size_minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize = mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0);
                textSize--;
                if (textSize <= -1) {
                    textSize = -1;
                }
                mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, textSize);
                changeTextSize();

                if(textSize != oldTextSize){
                    if (mInterface != null) mInterface.onSize(textSize);
                }
                oldTextSize = textSize;
            }
        });
        ImageView btn_text_size_plus = (ImageView) v.findViewById(R.id.btn_text_size_plus);
        btn_text_size_plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize = mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0);
                textSize++;
                if (textSize >= 2) {
                    textSize = 2;
                }

                mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, textSize);
                changeTextSize();

                if(textSize != oldTextSize){
                    if (mInterface != null) mInterface.onSize(textSize);
                }
                oldTextSize = textSize;
            }
        });
        top_div = (View)v.findViewById(R.id.top_div);
        main_view = (RelativeLayout)v.findViewById(R.id.main_view);
        bg_view= (RelativeLayout)v.findViewById(R.id.bg_view);
        bg_view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAnimating) return;
                isAnimating = false;

                if (getVisibility() == View.VISIBLE) {
                    main_view.startAnimation(upAni);
                }
            }
        });

        tv1 = (TextView) v.findViewById(R.id.tv_info_txt1);
        tv2 = (TextView) v.findViewById(R.id.tv_info_txt2);
        tv3 = (TextView) v.findViewById(R.id.tv_info_txt3);
        tv1.setTag(tv1.getTextSize());
        tv2.setTag(tv2.getTextSize());
        tv3.setTag(tv3.getTextSize());

        changeTextSize();
        addView(v);

        isAnimating = true;
        upAni = AnimationUtils.loadAnimation(mContext, R.anim.topmenu_up_ani);
        upAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = true;
                setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        downAni = AnimationUtils.loadAnimation(mContext, R.anim.topmenu_down_ani);
        downAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setTopDivView(boolean isView){
        if(isView){
            top_div.setVisibility(View.VISIBLE);
        }else{
            top_div.setVisibility(View.GONE);
        }
    }
    public void changeTextSize() {
        if (-1 == mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0)) {
            tv1.setText(mContext.getString(R.string.txt_size_adj_3));
        } else if (0 == mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0)) {
            tv1.setText(mContext.getString(R.string.txt_size_adj_4));
        } else if (1 == mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0)) {
            tv1.setText(mContext.getString(R.string.txt_size_adj_5));
        } else if (2 == mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0)) {
            tv1.setText(mContext.getString(R.string.txt_size_adj_6));
        }
        float textsize1 = CommonUtils.getUserTextSize(mContext, tv1.getTag());
        float textsize2 = CommonUtils.getUserTextSize(mContext, tv2.getTag());
        float textsize3 = CommonUtils.getUserTextSize(mContext, tv3.getTag());

        tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textsize1);
        tv2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textsize2);
        tv3.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textsize3);
    }

    public void show(){
        if (!isAnimating) return;
        isAnimating = false;

        if (getVisibility() == View.VISIBLE) {
            main_view.startAnimation(upAni);
        } else {
            setVisibility(View.VISIBLE);
            main_view.startAnimation(downAni);
        }
    }

    public void hide(){
        isAnimating = true;
        bg_view.setVisibility(View.GONE);
        setVisibility(View.GONE);
    }
}
