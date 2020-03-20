package com.kolon.sign2.dialog;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kolon.sign2.R;
import com.kolon.sign2.view.DynamicDetailEditItemView;
import com.kolon.sign2.vo.Res_AP_IF_028_VO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceDeskSecurityConfirmDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = CommentDialog.class.getSimpleName();
    private Context mContext;
    private EditText comment_edit;

    private TextView content_date1_1;
    private TextView content_date1_2;
    private TextView content_date2_1;
    private TextView content_date2_2;

    private Res_AP_IF_028_VO.result.approvalDetail readData;

    private ServiceDeskDialogListener mInterface;

    private Calendar mCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN);
    private DatePickerDialog.OnDateSetListener datePickerCallbackMethod;
    private TextView selDatePicker_tv;

    public interface ServiceDeskDialogListener {
        void getMessage(String comment, String content_date1_1, String content_date1_2, String content_date2_1, String content_date2_2);
    }

    public void setInterface(ServiceDeskDialogListener mInterface) {
        this.mInterface = mInterface;
    }

    public static ServiceDeskSecurityConfirmDialog newInstance() {
        ServiceDeskSecurityConfirmDialog dialog = new ServiceDeskSecurityConfirmDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //아웃사이드 클릭시 닫기
        getDialog().setCanceledOnTouchOutside(true);
        //키패드 올라올때 창을 밀어 올림
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View v = inflater.inflate(R.layout.dialog_service_desk_opinion_return2, container);

        //다이얼로그 취소 버튼 listener
        LinearLayout canel = (LinearLayout) v.findViewById(R.id.dialog_service_desk_btn_cancel);
        canel.setOnClickListener(this);

        //다이얼로그 승인 버튼 listener
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.dialog_service_desk_btn_ok);
        ok.setOnClickListener(this);

        comment_edit = (EditText) v.findViewById(R.id.comment_edit);
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        comment_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    comment_edit.setHint("");
                } else {
                    comment_edit.setHint(mContext.getResources().getString(R.string.txt_approval_txt_2));
                }
            }
        });

        LinearLayout content_lay1 = (LinearLayout)v.findViewById(R.id.content_lay1);
        LinearLayout content_lay2 = (LinearLayout)v.findViewById(R.id.content_lay2);

        LinearLayout layout_date1_1 = (LinearLayout)v.findViewById(R.id.layout_date1_1);
        LinearLayout layout_date1_2 = (LinearLayout)v.findViewById(R.id.layout_date1_2);
        LinearLayout layout_date2_1 = (LinearLayout)v.findViewById(R.id.layout_date2_1);
        LinearLayout layout_date2_2 = (LinearLayout)v.findViewById(R.id.layout_date2_2);
        layout_date1_1.setOnClickListener(this);
        layout_date1_2.setOnClickListener(this);
        layout_date2_1.setOnClickListener(this);
        layout_date2_2.setOnClickListener(this);

        TextView content_tv1 = (TextView)v.findViewById(R.id.content_tv1);
        content_date1_1 = (TextView)v.findViewById(R.id.content_date1_1);
        content_date1_2 = (TextView)v.findViewById(R.id.content_date1_2);

        TextView content_tv2 = (TextView)v.findViewById(R.id.content_tv2);
        content_date2_1 = (TextView)v.findViewById(R.id.content_date2_1);
        content_date2_2 = (TextView)v.findViewById(R.id.content_date2_2);

        if(!TextUtils.isEmpty(readData.getUsedate1Code())){
            content_lay1.setVisibility(View.VISIBLE);
            content_tv1.setText(readData.getUsedate1Name());
            content_date1_1.setText(readData.getUsedate1From());
            content_date1_2.setText(readData.getUsedate1To());
        }else{
            content_lay1.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(readData.getUsedate2Code())){
            content_lay2.setVisibility(View.VISIBLE);
            content_tv2.setText(readData.getUsedate2Name());
            content_date2_1.setText(readData.getUsedate2From());
            content_date2_2.setText(readData.getUsedate2To());
        }else{
            content_lay2.setVisibility(View.GONE);
        }

        InitializeDatePicker();

        return v;
    }

    public void setData(Res_AP_IF_028_VO.result.approvalDetail readData){
        this.readData = readData;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(this, TAG);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_service_desk_btn_cancel:
                dismissAllowingStateLoss();
                break;
            case R.id.dialog_service_desk_btn_ok:

                if (mInterface != null)
                    mInterface.getMessage(comment_edit.getText().toString(),
                            content_date1_1.getText().toString(),
                            content_date1_2.getText().toString(),
                            content_date2_1.getText().toString(),
                            content_date2_2.getText().toString()
                    );
                dismissAllowingStateLoss();

                break;
            case R.id.layout_date1_1:
                try {showDatePicker(content_date1_1);}
                catch (Exception e) {e.printStackTrace();}
                break;
            case R.id.layout_date1_2:
                try {showDatePicker(content_date1_2);}
                catch (Exception e) {e.printStackTrace();}
                break;
            case R.id.layout_date2_1:
                try {showDatePicker(content_date2_1);}
                catch (Exception e) {e.printStackTrace();}
                break;
            case R.id.layout_date2_2:
                try {showDatePicker(content_date2_2);}
                catch (Exception e) {e.printStackTrace();}
                break;

        }
    }


    private void InitializeDatePicker()
    {
        datePickerCallbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selDatePicker_tv.setText(sdf.format(mCalendar.getTime()));

                //시작날짜가 종료날짜보다 크면 종료날짜를 시작 날짜로 맞춰준다.
                if (selDatePicker_tv.equals(content_date1_1)) {
                    try {
                        if (stringToDate(content_date1_1.getText().toString()).compareTo(stringToDate(content_date1_2.getText().toString())) > 0)
                            content_date1_2.setText(sdf.format(mCalendar.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (selDatePicker_tv.equals(content_date2_1)) {
                    try {
                        if (stringToDate(content_date2_1.getText().toString()).compareTo(stringToDate(content_date2_2.getText().toString())) > 0)
                            content_date2_2.setText(sdf.format(mCalendar.getTime()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        };
    }

    private void showDatePicker(TextView tv) throws Exception
    {
        selDatePicker_tv = tv;

        //TextView 날짜 셋팅
        String dateStr = selDatePicker_tv.getText().toString();
        mCalendar = Calendar.getInstance();
        try {
            Date tempDate = sdf.parse(dateStr);
            mCalendar.setTime(tempDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                datePickerCallbackMethod,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        //minDate 설정
        Calendar minDate = Calendar.getInstance();

        //1. fromDate를 선택한 경우 minDates는 현재날짜로 지정
        if (selDatePicker_tv.equals(content_date1_1) || selDatePicker_tv.equals(content_date2_1))
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());

        //2. toDate를 선택한 경우 minDate는 fromDate로 지정
        if (selDatePicker_tv.equals(content_date1_2)) {
            String tmpStr = content_date1_1.getText().toString();
            Date tempDate = sdf.parse(tmpStr);
            minDate.setTime(tempDate);
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
        }
        if (selDatePicker_tv.equals(content_date2_2)) {
            String tmpStr = content_date2_1.getText().toString();
            Date tempDate = sdf.parse(tmpStr);
            minDate.setTime(tempDate);
            datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
        }

        datePickerDialog.show();
    }

    private Date stringToDate(String txt) throws Exception{
        Date date = null;
        try {
            date = sdf.parse(txt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
