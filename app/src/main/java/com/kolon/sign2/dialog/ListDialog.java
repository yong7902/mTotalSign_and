package com.kolon.sign2.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;

import java.util.ArrayList;

public class ListDialog extends DialogFragment {

    private static final String TAG = ListDialog.class.getSimpleName();
    private Context mContext;
    private ArrayList<String> data;
    private TextView dialog_title;
    private String title;

    private OnClickListener mInterface;
    public interface OnClickListener {
        void selectPosition(int position);
    }
    public void setInterface(OnClickListener mInterface){
        this.mInterface = mInterface;
    }

    public static ListDialog newInstance() {
        ListDialog dialog = new ListDialog();
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
        View v = inflater.inflate(R.layout.dialog_list, container);

        dialog_title = (TextView)v.findViewById(R.id.dialog_title);
        dialog_title.setText(title);

        RelativeLayout layout_background = (RelativeLayout)v.findViewById(R.id.layout_background);
        layout_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.dialog_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(mContext));

        ListDialogAdapter adapter = new ListDialogAdapter(data);
        adapter.setInterface(new ListDialogAdapter.ListTabClickListener() {
            @Override
            public void selectPosition(int position) {
                if(mInterface != null){
                    mInterface.selectPosition(position);
                }
                dismiss();
            }
        });
        rv.setAdapter(adapter);

        LinearLayout close = (LinearLayout)v.findViewById(R.id.btn_cancel);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setData(ArrayList<String> data){
        this.data = data;
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(this, TAG);
            transaction.commitAllowingStateLoss();
        }
    }
}
