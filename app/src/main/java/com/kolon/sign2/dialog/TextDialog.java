package com.kolon.sign2.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kolon.sign2.R;

public class TextDialog extends DialogFragment {
    private String TAG = TextDialog.class.getSimpleName();

    private String title;
    private String content;
    private String leftBtnTxt, rightBtnTxt;
    private Button leftBtn, rightBtn;
    private View view_div;

    public static TextDialog newInstance(String title, String content, String rightBtnTxt) {
        TextDialog dialog = new TextDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("leftBtnTxt", "");
        args.putString("rightBtnTxt", rightBtnTxt);
        dialog.setArguments(args);
        return dialog;
    }
    public static TextDialog newInstance(String title, String content, String leftBtnTxt, String rightBtnTxt) {
        TextDialog dialog = new TextDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("leftBtnTxt", leftBtnTxt);
        args.putString("rightBtnTxt", rightBtnTxt);
        dialog.setArguments(args);
        return dialog;
    }

    private View.OnClickListener listener;
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
     //   mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(true);
        // 타이틀 영역 제거
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_text, container);

//        setCancelable(false);
        Bundle bundle = getArguments();

        title = "";
        content = "";
        if (bundle != null) {
            title = bundle.getString("title");
            content = bundle.getString("content");
            leftBtnTxt = bundle.getString("leftBtnTxt");
            rightBtnTxt = bundle.getString("rightBtnTxt");
        }

        TextView tvTitle = (TextView)v.findViewById(R.id.dialog_title);
        tvTitle.setText(title);

        RelativeLayout titleLayout = (RelativeLayout) v.findViewById(R.id.dialog_title_layout);
        if (TextUtils.isEmpty(title)) {
            titleLayout.setVisibility(View.GONE);
        } else {
            titleLayout.setVisibility(View.VISIBLE);
        }

        TextView tvContent = (TextView)v.findViewById(R.id.dialog_content);
        tvContent.setText(content);

        leftBtn = (Button)v.findViewById(R.id.btn_left);
        leftBtn.setText(leftBtnTxt);
        leftBtn.setOnClickListener(listener);
        rightBtn = (Button)v.findViewById(R.id.btn_right);
        rightBtn.setText(rightBtnTxt);
        rightBtn.setOnClickListener(listener);
        view_div = (View)v.findViewById(R.id.view_div);
        setButton(leftBtnTxt, rightBtnTxt);

        return v;
    }

    private void setButton(String btn1, String btn2) {
        view_div.setVisibility(View.GONE);
        if (TextUtils.isEmpty(btn1)) {
            leftBtn.setVisibility(View.GONE);
        } else {
            leftBtn.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(btn2)) {
            rightBtn.setVisibility(View.GONE);
        } else {
            rightBtn.setVisibility(View.VISIBLE);
        }

        if(leftBtn.getVisibility() == View.VISIBLE && leftBtn.getVisibility() == View.VISIBLE){
            view_div.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
            @Override
            public void onBackPressed() {
                onDismiss(getDialog());
            }
        };
    }

    public void show(FragmentManager fragmentManager) {
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(this, TAG);
            transaction.commitAllowingStateLoss();
        }
    }
}
