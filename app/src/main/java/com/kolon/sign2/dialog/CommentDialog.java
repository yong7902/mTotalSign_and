package com.kolon.sign2.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kolon.sign2.R;

/**
 * 결재 처리 팝업
 * */
public class CommentDialog extends DialogFragment {
    private static final String TAG = CommentDialog.class.getSimpleName();
    private Context mContext;
    private String title; //타이틀
    private String okStr; //선택 버튼의 텍스트 글자
    private EditText comment_edit;
    private boolean isMust;//의견 필수
    private boolean isClose;

    private CommentDialogListener mInterface;
    public interface CommentDialogListener{
        void getMessage(String comment);
    }
    public void setInterface(CommentDialogListener mInterface){
        this.mInterface = mInterface;
    }

    public static CommentDialog newInstance() {
        CommentDialog dialog = new CommentDialog();
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
        //아웃사이드 클릭시 닫기 해제
        getDialog().setCanceledOnTouchOutside(true);
        //키패드 올라올때 창을 밀어 올림
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View v = inflater.inflate(R.layout.dialog_comment, container);
        TextView titleTv = (TextView)v.findViewById(R.id.tv_title);
        titleTv.setText(title);

        /*
        RelativeLayout dialog_bg = (RelativeLayout)v.findViewById(R.id.dialog_bg);
        dialog_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        */

        Button leftBtn =(Button)v.findViewById(R.id.btn_comment1);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        Button rightBtn =(Button)v.findViewById(R.id.btn_comment2);
        if(!TextUtils.isEmpty(okStr)){
            rightBtn.setText(okStr);
        }
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMust){
                    if(TextUtils.isEmpty(comment_edit.getText().toString())){
                        Toast.makeText(mContext,mContext.getResources().getString(R.string.txt_approval_txt_2), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(mInterface != null){
                    mInterface.getMessage(comment_edit.getText().toString());
                }
                if(isClose){
                    dismiss();
                }
            }
        });

        comment_edit = (EditText)v.findViewById(R.id.comment_edit);
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isMust && count == 0) {
                    isClose = false;
                    rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey));
                } else {
                    isClose = true;
                    rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
                }
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
                    //의견이 필수일때 초기 버튼글자 색
                    if (isMust) {
                        isClose = false;
                        rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey));
                        //hint (필수) 의견을...
                        comment_edit.setHint("("+ mContext.getResources().getString(R.string.txt_approval_must) +") "+mContext.getResources().getString(R.string.txt_approval_txt_2));
                    }else{
                        isClose = true;
                        rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
                        //hint (선택) 의견을...
                        comment_edit.setHint("("+ mContext.getResources().getString(R.string.txt_approval_select) +") "+mContext.getResources().getString(R.string.txt_approval_txt_2));
                    }
                }
            }
        });
        //의견이 필수일때 초기 버튼글자 색
        if (isMust) {
            isClose = false;
            rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey));
            //hint (필수) 의견을...
            comment_edit.setHint("("+ mContext.getResources().getString(R.string.txt_approval_must) +") "+mContext.getResources().getString(R.string.txt_approval_txt_2));
        }else{
            isClose = true;
            rightBtn.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
            //hint (선택) 의견을...
            comment_edit.setHint("("+ mContext.getResources().getString(R.string.txt_approval_select) +") "+mContext.getResources().getString(R.string.txt_approval_txt_2));
        }

        return v;
    }

    public void setData(String title, String okStr, boolean isMust){
        this.title = title;
        this.okStr = okStr;
        this.isMust = isMust;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(this, TAG);
            transaction.commitAllowingStateLoss();
        }
    }
}
