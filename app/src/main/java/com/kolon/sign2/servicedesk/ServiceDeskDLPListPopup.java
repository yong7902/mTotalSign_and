package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_032_VO;

import java.util.ArrayList;

public class ServiceDeskDLPListPopup extends DialogFragment {
    private String TAG = ServiceDeskDLPListPopup.class.getSimpleName();

    private Context mContext;
    ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList;

    private OnDocClickListener mInterface;
    public void setInterface(OnDocClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public static ServiceDeskDLPListPopup newInstance() {
        ServiceDeskDLPListPopup dialog = new ServiceDeskDLPListPopup();
        Bundle args = new Bundle();
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_NoActionBar);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AnimationPopupStyle;

        View v = inflater.inflate(R.layout.dialog_service_desk_dlp_list_poup, container);

        ImageView close = (ImageView) v.findViewById(R.id.btn_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.popup_rv);
        ServiceDeskDLPListAdapter adapter = new ServiceDeskDLPListAdapter(detectList);
        adapter.setInterface(new OnDocClickListener() {
            @Override
            public void onFileClick(int position, String url) {
                if (mInterface != null) mInterface.onFileClick(position, url);
            }

            @Override
            public void onChecked(int position, boolean isChecked) {
                if (mInterface != null) mInterface.onChecked(position, isChecked);
            }
        });
        adapter.setInterface(new OnDocClickListener() {
            @Override
            public void onFileClick(int position, String url) {
                if (mInterface != null) mInterface.onFileClick(position, url);
            }

            @Override
            public void onChecked(int position, boolean isChecked) {
                if (mInterface != null) mInterface.onChecked(position, isChecked);
            }
        });

        rv.setAdapter(adapter);

        return v;
    }

    public void setData(ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList) {
        this.detectList = detectList;
    }
}
