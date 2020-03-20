package com.kolon.sign2.dialog;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.kolon.sign2.R;

public class AddAccountDialog extends DialogFragment {
    private Context mContext;
    private TextView tv_warning;
    private EditText ed_id, ed_pass;

    private AddAccountInterface mInterface;
    public interface AddAccountInterface{
        void addAccount(String id, String pass);
    }
    public void setInterface(AddAccountInterface mInterface){
        this.mInterface = mInterface;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //전체화면
    //    setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
        mContext = getActivity();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //뷰 이벤트 연결
        //아웃사이드 클릭시 닫기
        getDialog().setCanceledOnTouchOutside(true);

        View v = inflater.inflate(R.layout.dialog_add_account, container);

        tv_warning = (TextView)v.findViewById(R.id.tv_warning);
        tv_warning.setVisibility(View.GONE);

        ed_id = (EditText)v.findViewById(R.id.edit_id);
        ed_pass = (EditText)v.findViewById(R.id.edit_pass);

        Button btn1 = (Button)v.findViewById(R.id.btn_cancel);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button btn2 = (Button)v.findViewById(R.id.btn_ok);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(ed_id.getText().toString()) || TextUtils.isEmpty(ed_pass.getText().toString())){
                    tv_warning.setVisibility(View.VISIBLE);
                    return;
                }

                //계정 추가
                if(mInterface != null) mInterface.addAccount(ed_id.getText().toString(), ed_pass.getText().toString());
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = ActionBar.LayoutParams.MATCH_PARENT;
        int dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

}
