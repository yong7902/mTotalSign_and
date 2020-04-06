package com.kolon.sign2.approval;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.Req_AP_IF_205_VO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;
import com.kolon.sign2.vo.Res_AP_IF_204_VO;
import com.kolon.sign2.vo.Res_AP_IF_205_VO;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 결재선 추가 검색
 */
public class ApprovalLineSearchPopup extends DialogFragment {

    private String TAG = ApprovalLineSearchPopup.class.getSimpleName();
    private Context mContext;

    private EditText ed;
    private ProgressBar progress_bar;
    private LinearLayout layout_no_search;
    private RecyclerView rv_search;
    private ArrayList<Res_AP_IF_016_VO.result.apprLineList> existLine;
    private ArrayList<dataClass> data;

    private TextView txt_subj;
    private String searchType;

    private OnTabClickListener mInterface;

    public interface OnTabClickListener {
        void selectPosition(int position, String userId, Res_AP_IF_204_VO.result.apprSpSearchList data);

        void selectRecentlyPosition(String userId, Res_AP_IF_203_VO.result.apprSpList data);
    }

    public void setInterface(OnTabClickListener mInterface) {
        this.mInterface = mInterface;
    }


    public static ApprovalLineSearchPopup newInstance() {
        ApprovalLineSearchPopup dialog = new ApprovalLineSearchPopup();
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

        View v = inflater.inflate(R.layout.dialog_approval_line_search, container);

        TextView closetv = (TextView) v.findViewById(R.id.txt_close);
        closetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ImageView del = (ImageView) v.findViewById(R.id.delete_search);
        del.setVisibility(View.INVISIBLE);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed.setText("");
            }
        });
        ed = (EditText) v.findViewById(R.id.edit_search);
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
                    if (TextUtils.isEmpty(ed.getText())) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.txt_approval_search_txt1), Toast.LENGTH_SHORT).show();
                    } else {
                        searchData();
                    }
                    handled = true;
                }
                return handled;
            }
        });
        ed.requestFocus();
        showKeypad();

        LinearLayout btn_team = (LinearLayout)v.findViewById(R.id.btn_team);
        btn_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        layout_no_search = (LinearLayout) v.findViewById(R.id.layout_no_search);
        rv_search = (RecyclerView) v.findViewById(R.id.rv_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_search.setLayoutManager(linearLayoutManager);

        txt_subj = (TextView) v.findViewById(R.id.txt_subj);
        txt_subj.setText(mContext.getResources().getString(R.string.txt_approval_line_search_txt1));
        searchType = "1";
        LinearLayout layout_subject = (LinearLayout) v.findViewById(R.id.layout_subject);
        layout_subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext.getResources().getString(R.string.txt_approval_line_search_txt1).equals(txt_subj.getText().toString())) {
                    //부서
                    txt_subj.setText(mContext.getResources().getString(R.string.txt_approval_line_search_txt2));
                    searchType = "2";
                } else {
                    //이름
                    txt_subj.setText(mContext.getResources().getString(R.string.txt_approval_line_search_txt1));
                    searchType = "1";
                }
            }
        });

        searchRecently();
        return v;
    }

    private void searchRecently() {

        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(mContext);
        String json = mPref.getStringPreference(Constants.PREF_RECENTLY_SEARCH_DATA);

        if (TextUtils.isEmpty(json)) {
            return;
        }

        Gson g = new Gson();

        ArrayList<Res_AP_IF_203_VO.result.apprSpList> readData = g.fromJson(json, new TypeToken<ArrayList<Res_AP_IF_203_VO.result.apprSpList>>() {
        }.getType());
        if (readData == null || readData.size() == 0) {
            return;
        }

        Req_AP_IF_205_VO input = new Req_AP_IF_205_VO();
        ArrayList<Req_AP_IF_205_VO.apprLatelyList> apprLatelyList = new ArrayList<>();
        for(int i=0;i<readData.size();i++){
            Req_AP_IF_205_VO.apprLatelyList info = new Req_AP_IF_205_VO().new apprLatelyList();
            info.setDeptCd(readData.get(i).getDeptCode());
            info.setUserAccount(readData.get(i).getIkenId());
            info.setCompanyCd(readData.get(i).companyCd);
            apprLatelyList.add(info);
        }
        input.setApprLatelyList(apprLatelyList);
        progress_bar.setVisibility(View.VISIBLE);
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getApprovalLatelyLineinfo(input, new NetworkPresenter.getApprovalLatelyLineinfoListener() {

            @Override
            public void onResponse(Res_AP_IF_205_VO result) {
                progress_bar.setVisibility(View.GONE);
                data = new ArrayList<>();
                if (result != null) {
                    ArrayList<Res_AP_IF_205_VO.result.apprLatelyList> read = result.getResult().getApprLatelyList();
                    if (read != null && read.size() != 0) {
                        //입력값과 동일한 값은 오류가 있으니 삭제후 저장
                        for (int i = 0; i < read.size(); i++) {
                            String userid = read.get(i).getUserAccount();//res...
                            for(int j=0;j<readData.size();j++){
                                if(userid.equals(readData.get(j).getIkenId())){
                                    readData.remove(j);
                                    break;
                                }
                            }
                        }
                    }

                    for(int i=0;i<readData.size();i++){
                        dataClass info = new dataClass();
                        info.setName(readData.get(i).getName());
                        if (TextUtils.isEmpty(readData.get(i).getRoleName())) {
                            info.setJob(readData.get(i).getJobTitle());
                        } else {
                            info.setJob(readData.get(i).getRoleName() + "/" + readData.get(i).getJobTitle());
                        }
                        info.setCompany(readData.get(i).orgName);
                        info.setTeam(readData.get(i).getOrgUnit());

                        info.setTime("");
                        data.add(info);
                    }

                    ApprovalLineSearchAdapter adapter = new ApprovalLineSearchAdapter(data);
                    adapter.setInterface(new ApprovalLineSearchAdapter.OnTabClickListener() {
                        @Override
                        public void selectPosition(int position) {
                            dismiss();
                            if (mInterface != null) mInterface.selectRecentlyPosition(readData.get(position).getIkenId(), readData.get(position));
                        }
                    });

                    rv_search.setAdapter(adapter);

                    //결과 저장
                    String saveData = g.toJson(readData);
                    mPref.setStringPreference(Constants.PREF_RECENTLY_SEARCH_DATA, saveData);

                }
            }
        });
    }

    private void searchData() {

        HashMap hm = new HashMap();
        String keyword = ed.getText().toString().trim();
        hm.put("searchRange", searchType);//        searchRange			검색범위				0:전체, 1:이름, 2:부서, 3:직책, 4:담당업무
        hm.put("keyword", keyword);//        keyword			검색어				1:이름인 경우 자음(초성) 가능, 그 외는 2자 이상의 단어

        Log.d(TAG, "approval line search input:" + hm);
        progress_bar.setVisibility(View.VISIBLE);
        NetworkPresenter presenter = new NetworkPresenter();
        presenter.getApprovalSpsearchLineinfo(hm, new NetworkPresenter.getApprovalSpsearchLineinfoListener() {

            @Override
            public void onResponse(Res_AP_IF_204_VO result) {
                progress_bar.setVisibility(View.GONE);
                Log.d(TAG, "#### approval line search:" + new Gson().toJson(result));
                String errMsg ="";
                if (result != null) {
                    if ("200".equals(result.getStatus().getStatusCd())) {
                        if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {

                            if (result.getResult().getApprSpSearchList() == null || result.getResult().getApprSpSearchList().size() == 0) {
                                layout_no_search.setVisibility(View.VISIBLE);
                                rv_search.setVisibility(View.GONE);
                            } else {
                                layout_no_search.setVisibility(View.GONE);
                                rv_search.setVisibility(View.VISIBLE);

                                data = new ArrayList<>();
                                ArrayList<Res_AP_IF_204_VO.result.apprSpSearchList> read = result.getResult().getApprSpSearchList();
                                for (int i = 0; i < read.size(); i++) {

                                    dataClass info = new dataClass();
                                    info.setName(read.get(i).getUserName());
                                    if (TextUtils.isEmpty(read.get(i).getRoleName())) {
                                        info.setJob(read.get(i).getTitleName());
                                    } else {
                                        info.setJob(read.get(i).getRoleName() + "/" + read.get(i).getTitleName());
                                    }
                                    info.setCompany(read.get(i).getCompanyName());
                                    info.setTeam(read.get(i).getDeptName());
                                    info.setTime("");
                                    data.add(info);
                                }

                                ApprovalLineSearchAdapter adapter = new ApprovalLineSearchAdapter(data);
                                adapter.setInterface(new ApprovalLineSearchAdapter.OnTabClickListener() {
                                    @Override
                                    public void selectPosition(int position) {
                                        if (existLine == null || existLine.size() == 0) {
                                            //선택포지션 추가
                                            dismiss();
                                            if (mInterface != null)
                                                mInterface.selectPosition(position, read.get(position).getUserAccount(), result.getResult().getApprSpSearchList().get(position));
                                        } else {
                                            boolean duplicateId = false;
                                            for (int i = 0; i < existLine.size(); i++) {
                                                if (existLine.get(i).getUserId().equals(read.get(position).getUserAccount())) {
                                                    duplicateId = true;
                                                    break;
                                                }
                                            }
                                            if (duplicateId) {
                                                TextDialog dialog = TextDialog.newInstance("", getResources().getString(R.string.txt_approval_line_txt6), "", getResources().getString(R.string.txt_confirm));
                                                dialog.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show(getActivity().getSupportFragmentManager());
                                            } else {
                                                //선택포지션 추가
                                                dismiss();
                                                if (mInterface != null)
                                                    mInterface.selectPosition(position, read.get(position).getUserAccount(), result.getResult().getApprSpSearchList().get(position));
                                            }
                                        }
                                    }
                                });
                                rv_search.setAdapter(adapter);
                            }
                            return;
                        }else{
                            errMsg = result.getResult().getErrorMsg();
                        }
                    } else {
                        errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
                    }
                } else {
                    errMsg = getResources().getString(R.string.txt_network_error);
                }

                //popup
                TextDialog dialog = TextDialog.newInstance("", errMsg, getResources().getString(R.string.txt_alert_confirm));
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show(((ApprovalLineAddActivity) mContext).getSupportFragmentManager());
            }
        });
    }


    public void setData(ArrayList<Res_AP_IF_016_VO.result.apprLineList> existLine) {
        this.existLine = existLine;
    }

    private void showKeypad(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(ed, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);
    }

    private void hideKeypad() {
        InputMethodManager mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        int dialogWidth = ActionBar.LayoutParams.MATCH_PARENT;//getResources().getDimensionPixelSize(R.dimen.dp_344);
        int dialogHeight = ActionBar.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }



    public class dataClass {

        String name;
        String team;
        String job;
        String time;
        String company;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTeam() {
            return team;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }
    }
}
