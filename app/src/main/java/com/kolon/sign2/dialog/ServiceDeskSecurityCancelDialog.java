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

public class ServiceDeskSecurityCancelDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = CommentDialog.class.getSimpleName();
    private Context mContext;
    private EditText comment_edit;
    private RelativeLayout layer_comment;
    private LinearLayout btn_comment1, btn_comment2, btn_comment3;
    private TextView tv_ok;
    private boolean isClose;

    private ServiceDeskDialogListener mInterface;
    public interface ServiceDeskDialogListener{
        void getMessage(String comment);
    }
    public void setInterface(ServiceDeskDialogListener mInterface){
        this.mInterface = mInterface;
    }

    public static ServiceDeskSecurityCancelDialog newInstance() {
        ServiceDeskSecurityCancelDialog dialog = new ServiceDeskSecurityCancelDialog();
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

        View v = inflater.inflate(R.layout.dialog_service_desk_opinion_return, container);

        LinearLayout select = (LinearLayout) v.findViewById(R.id.dialog_service_desk_btn_1);
        select.setOnClickListener(this);
        LinearLayout canel = (LinearLayout) v.findViewById(R.id.dialog_service_desk_btn_cancel);
        canel.setOnClickListener(this);
        LinearLayout ok = (LinearLayout) v.findViewById(R.id.dialog_service_desk_btn_ok);
        ok.setOnClickListener(this);
        tv_ok = (TextView) v.findViewById(R.id.tv_ok);
        comment_edit = (EditText) v.findViewById(R.id.comment_edit);
        comment_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
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
                    comment_edit.setHint(mContext.getResources().getString(R.string.txt_approval_txt_2));
                }
            }
        });


        isClose = false;

        LinearLayout btn_close_comment_layer = (LinearLayout)v.findViewById(R.id.btn_close_comment_layer);
        btn_close_comment_layer.setOnClickListener(this);
        layer_comment = (RelativeLayout) v.findViewById(R.id.layer_comment);
        layer_comment.setOnClickListener(this);
        layer_comment.setVisibility(View.GONE);
        btn_comment1 = (LinearLayout) v.findViewById(R.id.btn_comment1);
        btn_comment1.setOnClickListener(this);
        btn_comment2 = (LinearLayout) v.findViewById(R.id.btn_comment2);
        btn_comment2.setOnClickListener(this);
        btn_comment3 = (LinearLayout) v.findViewById(R.id.btn_comment3);
        btn_comment3.setOnClickListener(this);

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
            case R.id.dialog_service_desk_btn_1:
                layer_comment.setVisibility(View.VISIBLE);
                break;
            case R.id.dialog_service_desk_btn_cancel:
                dismissAllowingStateLoss();
                break;
            case R.id.dialog_service_desk_btn_ok:
                if (isClose) {
                    if(mInterface != null) mInterface.getMessage(comment_edit.getText().toString());
                    dismissAllowingStateLoss();
                } else {
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.txt_approval_txt_2), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layer_comment: case R.id.btn_close_comment_layer:
                layer_comment.setVisibility(View.GONE);
                break;
            case R.id.btn_comment1:
                comment_edit.setText(mContext.getResources().getString(R.string.txt_service_desk_popup_txt3));
                layer_comment.setVisibility(View.GONE);
                break;
            case R.id.btn_comment2:
                comment_edit.setText(mContext.getResources().getString(R.string.txt_service_desk_popup_txt4));
                layer_comment.setVisibility(View.GONE);
                break;
            case R.id.btn_comment3:
                comment_edit.setText(mContext.getResources().getString(R.string.txt_service_desk_popup_txt5));
                layer_comment.setVisibility(View.GONE);
                break;
        }
    }
}
