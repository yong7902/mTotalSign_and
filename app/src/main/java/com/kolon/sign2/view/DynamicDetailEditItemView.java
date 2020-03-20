package com.kolon.sign2.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DynamicDetailEditItemView extends LinearLayout {

    EditText mDynamicMinute;
    EditText mDynamicContents;
    RelativeLayout mDynamicDateLayout;
    TextView mDynamicDate, tv_title9, tv_title10;

    private DateClickListener mListener;
    private Calendar mCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
    private DatePickerDialog.OnDateSetListener dynamicDateSetListener;
    public interface DateClickListener {
        void onDateClickListener(String attr01);
    }
    public DynamicDetailEditItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailEditItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailEditItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailEditItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_detail_edit_item, this, false);

        mDynamicMinute = view.findViewById(R.id.et_dynamic_detail_edit_minute);
        mDynamicContents = view.findViewById(R.id.et_dynamic_detail_edit_contents);
        mDynamicDateLayout = view.findViewById(R.id.rl_dynamic_date_layout);
        mDynamicDate = view.findViewById(R.id.tv_dynamic_detail_edit_date);
        mDynamicDateLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(getContext(),
                        dynamicDateSetListener,
                        mCalendar.get(Calendar.YEAR),
                        mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        tv_title9 = (TextView)view.findViewById(R.id.tv_title9);
        tv_title10 = (TextView)view.findViewById(R.id.tv_title10);

        CommonUtils.textSizeSetting(getContext(), mDynamicDate);
        CommonUtils.textSizeSetting(getContext(), tv_title9);
        CommonUtils.textSizeSetting(getContext(), tv_title10);

        addView(view);
    }

    public void setViewData(DateClickListener listener) {
        if (listener != null) {
            mListener = listener;
        }
        initDatePickaer();
    }

    private void initDatePickaer() {
        /*try {
            SimpleDateFormat sdfTemp = new SimpleDateFormat("yyyyMMdd");
            Date date = sdfTemp.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            mCalendar = cal;
        } catch (ParseException e) {
            mCalendar = Calendar.getInstance();
        }*/

        mCalendar = Calendar.getInstance();
        mDynamicDate.setText(sdf.format(mCalendar.getTime()));
        // create an OnDateSetListener
        dynamicDateSetListener = (view, year, month, dayOfMonth) -> {
            Calendar tempCal  = Calendar.getInstance();
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, month);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mDynamicDate.setText(sdf.format(mCalendar.getTime()));
        };
    }

    public String getWorkTime() {
        if (mDynamicMinute.getText().length() > 0) {
            return mDynamicMinute.getText().toString();
        } else {
            return "";
        }
    }
    public String getContents() {
        if (mDynamicContents.getText().length() > 0) {
            return mDynamicContents.getText().toString();
        } else {
            return "";
        }
    }
    public String getDate() {
        if (mDynamicDate.getText().length() > 0) {
            return mDynamicDate.getText().toString();
        } else {
            return "";
        }
    }


    public void textSizeAdj(){
        CommonUtils.changeTextSize(getContext(), mDynamicDate);
        CommonUtils.changeTextSize(getContext(), tv_title9);
        CommonUtils.changeTextSize(getContext(), tv_title10);
    }
}
