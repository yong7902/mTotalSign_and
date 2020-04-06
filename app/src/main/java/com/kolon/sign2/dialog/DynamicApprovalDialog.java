package com.kolon.sign2.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class DynamicApprovalDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = CommentDialog.class.getSimpleName();
    private Context mContext;
    private EditText comment_edit;
    private TextView tv_ok;
    private TextView mDialogTitle;
    private boolean isClose;
    private String mTitleText = "";
    private String mRightBtnText = "";
    private boolean mCommentRequire = true;

    private DynamicApprovalListener mInterface;
    public interface DynamicApprovalListener{
        void getMessage(String comment);
    }
    public void setInterface(DynamicApprovalListener mInterface){
        this.mInterface = mInterface;
    }

    public static DynamicApprovalDialog newInstance(String title, String rightBtnText, boolean commentRequire) {
        DynamicApprovalDialog dialog = new DynamicApprovalDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("rightBtnText", rightBtnText);
        args.putBoolean("commentRequire", commentRequire);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = getActivity();
        if (getArguments() != null) {
            mTitleText = getArguments().getString("title");
            mRightBtnText = getArguments().getString("rightBtnText");
            mCommentRequire = getArguments().getBoolean("commentRequire");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //아웃사이드 클릭시 닫기
        getDialog().setCanceledOnTouchOutside(true);
        //키패드 올라올때 창을 밀어 올림
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View v = inflater.inflate(R.layout.dialog_dynamic_approval, container);

        mDialogTitle = v.findViewById(R.id.tv_dynamic_approval_title);
        mDialogTitle.setText(mTitleText);
        LinearLayout canel = v.findViewById(R.id.btn_dynamic_approval_cancel);
        canel.setOnClickListener(this);
        LinearLayout ok = v.findViewById(R.id.btn_dynamic_approval_ok);
        ok.setOnClickListener(this);
        tv_ok = v.findViewById(R.id.tv_dynamic_approval_right_text);
        tv_ok.setText(mRightBtnText);
        comment_edit = v.findViewById(R.id.tv_dynamic_approval_comment_edit);

        if (mCommentRequire) {
            comment_edit.setHint(mContext.getResources().getString(R.string.txt_app_reject_hint));
            tv_ok.setTextColor(ContextCompat.getColor(mContext, R.color.greyish));
            isClose = false;
        }
        else {
            comment_edit.setHint(mContext.getResources().getString(R.string.txt_app_ok_hint));
            tv_ok.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
            isClose = true;
        }

        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && mCommentRequire) {
                    isClose = false;
                    tv_ok.setTextColor(ContextCompat.getColor(mContext, R.color.greyish));
                } else {
                    isClose = true;
                    tv_ok.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
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
                    if (mCommentRequire)
                        comment_edit.setHint(mContext.getResources().getString(R.string.txt_app_reject_hint));
                    else
                        comment_edit.setHint(mContext.getResources().getString(R.string.txt_app_ok_hint));
                }
            }
        });

        return v;
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
            case R.id.btn_dynamic_approval_cancel:
                dismissAllowingStateLoss();
                break;
            case R.id.btn_dynamic_approval_ok:
                if (isClose) {
                    if(mInterface != null) mInterface.getMessage(comment_edit.getText().toString());
                    dismissAllowingStateLoss();
                } else {
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.txt_approval_txt_2), Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }
}
