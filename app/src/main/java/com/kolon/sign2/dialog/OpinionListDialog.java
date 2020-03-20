package com.kolon.sign2.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

/**
 * 의견 리스트 보기 팝업
 * */
public class OpinionListDialog extends DialogFragment {

    private static final String TAG = OpinionListDialog.class.getSimpleName();
    private Context mContext;
    private ArrayList<Res_AP_IF_016_VO.result.apprLineList>  data;

    private OnClickListener mInterface;
    public interface OnClickListener {
        void selectPosition(int position);
    }
    public void setInterface(OnClickListener mInterface){
        this.mInterface = mInterface;
    }

    public static OpinionListDialog newInstance() {
        OpinionListDialog dialog = new OpinionListDialog();
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

        TextView titleTxt = (TextView)v.findViewById(R.id.dialog_title);
        titleTxt.setText(getResources().getString(R.string.txt_approval_opinion));
        TextView closeTxt = (TextView)v.findViewById(R.id.tv_close);
        closeTxt.setText(getResources().getString(R.string.txt_alert_confirm));
        closeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));

        RecyclerView rv = (RecyclerView)v.findViewById(R.id.dialog_recyclerview);
        rv.setLayoutManager(new LinearLayoutManager(mContext));

        OpinionListDialogAdapter adapter = new OpinionListDialogAdapter(data);
//        adapter.setInterface(new OpinionListDialogAdapter.ListTabClickListener() {
//            @Override
//            public void selectPosition(int position) {
//                if(mInterface != null){
//                    mInterface.selectPosition(position);
//                }
//                dismiss();
//            }
//        });
        rv.setAdapter(adapter);

        LinearLayout closeBtn = (LinearLayout)v.findViewById(R.id.btn_cancel);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    public void setData(ArrayList<Res_AP_IF_016_VO.result.apprLineList> data){
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
