package com.kolon.sign2.dialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AttatchFileDialog extends DialogFragment {
    private Context mContext;
    private ArrayList<AttachFileListVO> data;
    private boolean checkProcess, allCheck;
    private LinearLayout layout_ok;
    private OnClickListener mInterface;

    public interface OnClickListener {
        void selectPosition(int position);
    }
    public void setInterface(OnClickListener mInterface) {
        this.mInterface = mInterface;
    }

    private OnCheckClickListener checkListener;
    public interface OnCheckClickListener{
        void checkPosition(int position, boolean isChecked);
        void onCancel();
        void onConfirm();
    }
    public void setCheckListener(OnCheckClickListener checkListener){
        this.checkListener = checkListener;
    }


    public static AttatchFileDialog newInstance() {
        AttatchFileDialog dialog = new AttatchFileDialog();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //전체화면
        // setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
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

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View v = inflater.inflate(R.layout.dialog_attach_file, container);

        RelativeLayout dialog_bg = (RelativeLayout)v.findViewById(R.id.dialog_bg);
        dialog_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        RelativeLayout close = (RelativeLayout) v.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv_attach_dialog);
        rv.setLayoutManager(linearLayoutManager);
        AttatchFileListAdapter adapter = new AttatchFileListAdapter(data, checkProcess);
        adapter.setInterface(new AttatchFileListAdapter.ListTabClickListener() {
            @Override
            public void selectPosition(int position) {
                dismiss();
                if (mInterface != null) mInterface.selectPosition(position);
            }

            @Override
            public void checkPosition(int position, boolean isChecked) {
                if (checkListener != null) checkListener.checkPosition(position, isChecked);
            }
        });
        rv.setAdapter(adapter);


        LinearLayout layout_two_btn = (LinearLayout)v.findViewById(R.id.layout_two_btn);
        LinearLayout cancel = (LinearLayout)v.findViewById(R.id.layout_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkListener != null) checkListener.onCancel();
            }
        });
        layout_ok = (LinearLayout)v.findViewById(R.id.layout_ok);
        layout_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkListener != null) checkListener.onConfirm();
            }
        });
        layout_ok.setSelected(false);
        if(checkProcess){
            layout_two_btn.setVisibility(View.VISIBLE);
            close.setVisibility(View.GONE);
            if(allCheck){
                setEnableBtn(true);
            }
        }else{
            layout_two_btn.setVisibility(View.GONE);
            close.setVisibility(View.VISIBLE);
        }

        return v;
    }

    //우측버튼의 활성화여부
    public void setEnableBtn(boolean isEnable){
        layout_ok.setSelected(isEnable);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
    public void setData(ArrayList<AttachFileListVO> data){
        setData(data, false);
    }
    public void setData(ArrayList<AttachFileListVO> data, boolean checkProcess){
        this.data = data;
        this.checkProcess = checkProcess;
        boolean confirmOk = true;
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).isChecked()) {
                confirmOk = false;
                break;
            }
        }
        if(confirmOk){
            allCheck = true;
        }else{
            allCheck = false;
        }
    }
}
