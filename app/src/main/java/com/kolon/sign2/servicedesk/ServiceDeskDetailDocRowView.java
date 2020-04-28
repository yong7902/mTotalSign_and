package com.kolon.sign2.servicedesk;

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
import com.kolon.sign2.vo.Res_AP_IF_032_VO;

public class ServiceDeskDetailDocRowView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private ImageView iv_check;
    private TextView tv_file_name, tv_ban, tv_jumin, tv_telno, tv_email, tv_drive_no, tv_passport_no, tv_foreign_no, tv_bank_no, tv_card_no;
    private Res_AP_IF_032_VO.result.dlpDetailFile data;
    private int selectPosition;
    private TextView tv_title1, tv_title2, tv_title3, tv_title4, tv_title5, tv_title6, tv_title7, tv_title8, tv_title9, tv_title10;

    private OnDocClickListener mInterface;

//    public interface OnDocClickListener {
//        void onFileClick(int position, String url);
//
//        void onChecked(int position, boolean isChecked);
//    }

    public void setInterface(OnDocClickListener mInterface) {
        this.mInterface = mInterface;
    }


    public ServiceDeskDetailDocRowView(Context context) {
        super(context);
        initView(context);
    }

    public ServiceDeskDetailDocRowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ServiceDeskDetailDocRowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.row_service_detail_doc_view, this, false);

        LinearLayout layout_file = (LinearLayout) v.findViewById(R.id.layout_file);
        layout_file.setOnClickListener(this);
        LinearLayout layout_check = (LinearLayout) v.findViewById(R.id.layout_check);
        layout_check.setOnClickListener(this);

        iv_check = (ImageView) v.findViewById(R.id.iv_check);
        iv_check.setOnClickListener(this);
        TextView tv_check = (TextView) v.findViewById(R.id.tv_check);
        tv_check.setOnClickListener(this);

        tv_title1 = (TextView)v.findViewById(R.id.tv_title1);
        tv_title2 = (TextView)v.findViewById(R.id.tv_title2);
        tv_title3 = (TextView)v.findViewById(R.id.tv_title3);
        tv_title4 = (TextView)v.findViewById(R.id.tv_title4);
        tv_title5 = (TextView)v.findViewById(R.id.tv_title5);
        tv_title6 = (TextView)v.findViewById(R.id.tv_title6);
        tv_title7 = (TextView)v.findViewById(R.id.tv_title7);
        tv_title8 = (TextView)v.findViewById(R.id.tv_title8);
        tv_title9 = (TextView)v.findViewById(R.id.tv_title9);
        tv_title10 = (TextView)v.findViewById(R.id.tv_title10);


        tv_file_name = (TextView) v.findViewById(R.id.tv_file_name);
        tv_ban = (TextView) v.findViewById(R.id.tv_ban);
        tv_jumin = (TextView) v.findViewById(R.id.tv_jumin);
        tv_telno = (TextView) v.findViewById(R.id.tv_telno);
        tv_email = (TextView) v.findViewById(R.id.tv_email);
        tv_drive_no = (TextView) v.findViewById(R.id.tv_drive_no);
        tv_passport_no = (TextView) v.findViewById(R.id.tv_passport_no);
        tv_foreign_no = (TextView) v.findViewById(R.id.tv_foreign_no);
        tv_bank_no = (TextView) v.findViewById(R.id.tv_bank_no);
        tv_card_no = (TextView) v.findViewById(R.id.tv_card_no);

        CommonUtils.textSizeSetting(mContext, tv_title1);
        CommonUtils.textSizeSetting(mContext, tv_title2);
        CommonUtils.textSizeSetting(mContext, tv_title3);
        CommonUtils.textSizeSetting(mContext, tv_title4);
        CommonUtils.textSizeSetting(mContext, tv_title5);
        CommonUtils.textSizeSetting(mContext, tv_title6);
        CommonUtils.textSizeSetting(mContext, tv_title7);
        CommonUtils.textSizeSetting(mContext, tv_title8);
        CommonUtils.textSizeSetting(mContext, tv_title9);
        CommonUtils.textSizeSetting(mContext, tv_title10);

        CommonUtils.textSizeSetting(mContext, tv_file_name);
        CommonUtils.textSizeSetting(mContext, tv_ban);
        CommonUtils.textSizeSetting(mContext, tv_jumin);
        CommonUtils.textSizeSetting(mContext, tv_telno);
        CommonUtils.textSizeSetting(mContext, tv_email);
        CommonUtils.textSizeSetting(mContext, tv_drive_no);
        CommonUtils.textSizeSetting(mContext, tv_passport_no);
        CommonUtils.textSizeSetting(mContext, tv_foreign_no);
        CommonUtils.textSizeSetting(mContext, tv_bank_no);
        CommonUtils.textSizeSetting(mContext, tv_card_no);

        addView(v);
    }

    public void setData(Res_AP_IF_032_VO.result.dlpDetailFile data, int position) {
        this.data = data;
        selectPosition = position;
        tv_file_name.setText(String.format(mContext.getResources().getString(R.string.txt_service_desk_file_name), data.getFileName()));
        tv_jumin.setText(setDetect(data.getDetectCnt1()));
        tv_telno.setText(setDetect(data.getDetectCnt2()));
        tv_email.setText(setDetect(data.getDetectCnt3()));
        tv_drive_no.setText(setDetect(data.getDetectCnt4()));
        tv_passport_no.setText(setDetect(data.getDetectCnt5()));
        tv_foreign_no.setText(setDetect(data.getDetectCnt6()));
        tv_bank_no.setText(setDetect(data.getDetectCnt7()));
        tv_card_no.setText(setDetect(data.getDetectCnt8()));
        tv_ban.setText(setDetect(data.getDetectKeyword()));

        CommonUtils.changeTextSize(mContext, tv_title1);
        CommonUtils.changeTextSize(mContext, tv_title2);
        CommonUtils.changeTextSize(mContext, tv_title3);
        CommonUtils.changeTextSize(mContext, tv_title4);
        CommonUtils.changeTextSize(mContext, tv_title5);
        CommonUtils.changeTextSize(mContext, tv_title6);
        CommonUtils.changeTextSize(mContext, tv_title7);
        CommonUtils.changeTextSize(mContext, tv_title8);
        CommonUtils.changeTextSize(mContext, tv_title9);
        CommonUtils.changeTextSize(mContext, tv_title10);

        CommonUtils.changeTextSize(mContext, tv_file_name);
        CommonUtils.changeTextSize(mContext, tv_ban);
        CommonUtils.changeTextSize(mContext, tv_jumin);
        CommonUtils.changeTextSize(mContext, tv_telno);
        CommonUtils.changeTextSize(mContext, tv_email);
        CommonUtils.changeTextSize(mContext, tv_drive_no);
        CommonUtils.changeTextSize(mContext, tv_passport_no);
        CommonUtils.changeTextSize(mContext, tv_foreign_no);
        CommonUtils.changeTextSize(mContext, tv_bank_no);
        CommonUtils.changeTextSize(mContext, tv_card_no);

        drawCheckBox();
    }

    private String setDetect(String data) {
        if (TextUtils.isEmpty(data)) {
            return "X";
        } else {
            return String.valueOf(data);
        }
    }

    private void drawCheckBox() {
        if (data.isChecked) {
            iv_check.setBackgroundResource(R.drawable.checkbox_checked);
        } else {
            iv_check.setBackgroundResource(R.drawable.checkbox_unchecked);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_file:
                if (mInterface != null) {
                    mInterface.onFileClick(selectPosition, data.getFilePath());
                }
                data.isChecked = true;
                drawCheckBox();
                break;
            case R.id.iv_check:
            case R.id.tv_check:
            case R.id.layout_check:
                if (data.isChecked) {
                    data.isChecked = false;
                } else {
                    data.isChecked = true;
                }
                drawCheckBox();
                if (mInterface != null) mInterface.onChecked(selectPosition, data.isChecked);
                break;
        }
    }
}
