package com.kolon.sign2.approval;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_020_VO;

import java.util.ArrayList;
import java.util.HashMap;

public class ApprovalSearchPopup extends Dialog implements View.OnClickListener {
    private String TAG = ApprovalSearchPopup.class.getSimpleName();

    private Context mContext;
    private EditText ed;
    private TextView txt_subj, txt_num;

    private LinearLayout layout_no_search;
    private RecyclerView rv_search;

    private ProgressBar progress_bar;
    private HashMap hm;
    private String searchType;//검색 타입(제목:1, 기안자:2)

    private SearchPopupInterface mInterface;
    public interface SearchPopupInterface{
        void onClick(ArrayList<Res_AP_IF_020_VO.result.apprList> list, int position);
    }
    public void setInterface(SearchPopupInterface mInterface){
        this.mInterface = mInterface;
    }

    public ApprovalSearchPopup(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();

        lpWindow.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//        lpWindow.dimAmount = 0.8f;
        lpWindow.windowAnimations = R.style.AnimationPopupStyle;
        getWindow().setAttributes(lpWindow);

        getWindow().setContentView(R.layout.dialog_approval_search);

        TextView closeTxt = (TextView)findViewById(R.id.txt_close);
        closeTxt.setOnClickListener(this);
        LinearLayout ll = (LinearLayout)findViewById(R.id.layout_subject);
        ll.setOnClickListener(this);

        txt_subj = (TextView)findViewById(R.id.txt_subj);

        ImageView del = (ImageView)findViewById(R.id.delete_search);
        del.setVisibility(View.INVISIBLE);
        del.setOnClickListener(this);

        ed = (EditText)findViewById(R.id.edit_search);
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    del.setVisibility(View.INVISIBLE);
                } else {
                    del.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ed.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if(TextUtils.isEmpty(ed.getText())){
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_approval_search_txt1), Toast.LENGTH_SHORT).show();
                    }else{
                        sendMessage();
                    }
                    handled = true;
                }
                return handled;
            }
        });
        ed.requestFocus();
        showKeypad();

        progress_bar = (ProgressBar)findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        txt_num = (TextView)findViewById(R.id.txt_num);
        layout_no_search = (LinearLayout)findViewById(R.id.layout_no_search);
        rv_search = (RecyclerView)findViewById(R.id.rv_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_search.setLayoutManager(linearLayoutManager);

        searchType = "1"; //default 제목

        TextView tv_search_result1 = (TextView)findViewById(R.id.tv_search_result1);
        TextView tv_search_result2 = (TextView)findViewById(R.id.tv_search_result2);

        CommonUtils.textSizeSetting(mContext, txt_num);
        CommonUtils.textSizeSetting(mContext, tv_search_result1);
        CommonUtils.textSizeSetting(mContext, tv_search_result2);
    }

    public void setData(String category, String userId, String deptId, String containerId, String subQuery){
        hm = new HashMap();
        hm.put("category", category);
        hm.put("userId", userId);
        hm.put("deptId", deptId);
        hm.put("containerId", containerId);
        hm.put("subQuery", subQuery);
    }

    private void sendMessage(){
        String keyword = ed.getText().toString().trim();
        hm.put("searchType", searchType);//검색 타입(제목:1, 기안자:2)
        hm.put("searchKeyword", keyword);
        Log.d(TAG, "approval process input:"+hm);
        progress_bar.setVisibility(View.VISIBLE);
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getApprovalSearch(hm, new NetworkPresenter.getApprovalSearchResult(){

            @Override
            public void onResponse(Res_AP_IF_020_VO result) {
                String errMsg ="";
                progress_bar.setVisibility(View.GONE);
                Log.d(TAG, "approval process re:"+new Gson().toJson(result));

                if (result != null) {
                    Log.d(TAG, "approval search re:"+new Gson().toJson(result));
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                            if (result.getResult().getApprList() == null || result.getResult().getApprList().size() == 0) {
                                rv_search.setVisibility(View.GONE);
                                layout_no_search.setVisibility(View.VISIBLE);
                                txt_num.setText("0");
                            } else {
                                rv_search.setVisibility(View.VISIBLE);
                                layout_no_search.setVisibility(View.GONE);
                                txt_num.setText(String.valueOf(result.getResult().getApprList().size()));
                                ApprovalSearchAdapter adapter = new ApprovalSearchAdapter(result.getResult().getApprList());
                                adapter.setInterface(new ApprovalSearchAdapter.OnTabClickListener() {
                                    @Override
                                    public void selectPosition(int position) {
                                        if(mInterface != null){
                                            mInterface.onClick(result.getResult().getApprList(), position);
                                        }
                                    }
                                });
                                rv_search.setAdapter(adapter);

                                //keypad down
                                hideKeypad();
                            }
                            return;
                        } else {
                            errMsg = result.getResult().getErrorMsg();
                        }
                    } else {
                        errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
                    }

                } else {
                    errMsg = mContext.getResources().getString(R.string.txt_network_error);
                }

                TextDialog dialog = TextDialog.newInstance("", errMsg, mContext.getResources().getString(R.string.txt_alert_confirm));
                dialog.setCancelable(false);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(((MainActivity) mContext).getSupportFragmentManager());
            }
        });
    }

    private void showKeypad(){
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
    }
    private void hideKeypad(){
        InputMethodManager mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_close:
                //close
                dismiss();
                break;
            case R.id.layout_subject:
                //change sub
                if (mContext.getResources().getString(R.string.txt_approval_search_txt2).equals(txt_subj.getText().toString())) {
                    //기안자
                    txt_subj.setText(mContext.getResources().getString(R.string.txt_approval_search_txt3));
                    searchType = "2";
                } else {
                    //제목
                    txt_subj.setText(mContext.getResources().getString(R.string.txt_approval_search_txt2));
                    searchType = "1";
                }
                break;
            case R.id.delete_search:
                ed.setText("");
                break;
        }
    }
}
