package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.kolon.sign2.R;


public class ServiceDeskMailFilterPopup extends DialogFragment {
    private String TAG = ServiceDeskMailFilterPopup.class.getSimpleName();

    private Context mContext;
    private String content;


    public static ServiceDeskMailFilterPopup newInstance() {
        ServiceDeskMailFilterPopup dialog = new ServiceDeskMailFilterPopup();
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

        View v = inflater.inflate(R.layout.dialog_service_desk_mail_filter_popup, container);

        ImageView iv = (ImageView)v.findViewById(R.id.btn_close);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        WebView wb = (WebView)v.findViewById(R.id.popup_web_view);
        wb.loadData(content, "text/html;charset=UTF-8", null);
        wb.getSettings().setJavaScriptEnabled(true);
        return v;
    }

    public void setData(String content){
        this.content = content;
    }
}
