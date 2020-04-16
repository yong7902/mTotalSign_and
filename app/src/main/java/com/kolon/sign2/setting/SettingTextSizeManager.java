package com.kolon.sign2.setting;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kolon.sign2.R;
import com.kolon.sign2.setting.adapter.TextSizeListAdapter;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.TextsizeVO;

import java.util.ArrayList;

/**
 * 글자크기
 * */
public class SettingTextSizeManager extends Activity implements View.OnClickListener {
    private LinearLayout mTitleBack;
    private ListView mTextsizeListView;
    private ArrayList<TextsizeVO> mTextsizeArray;
    private TextSizeListAdapter mTextsizeListAdapter;
    private SharedPreferenceManager mPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_textsize);
        mPref = SharedPreferenceManager.getInstance(getBaseContext());

        initView();
    }

    private void initView() {
        mTitleBack = findViewById(R.id.ll_setting_title_back);
        mTitleBack.setOnClickListener(this);

        mTextsizeListView = findViewById(R.id.lv_setting_textsize_list);
        mTextsizeListView.setEmptyView(findViewById(android.R.id.empty));
        testData();
    }

    private void testData() {
        int readPosition = 1;
        switch ((int) mPref.getFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0)){
            case -1:
                readPosition = 0;
                break;
            case 0:
                readPosition = 1;
                break;
            case 1:
                readPosition = 2;
                break;
            case 2:
                readPosition = 3;
                break;
        }

        Resources res = getResources();
        TypedArray textsize = res.obtainTypedArray(R.array.textsize_value);
        TypedArray textValue = res.obtainTypedArray(R.array.textsize_title);
        mTextsizeArray = new ArrayList<>();
        for (int index = 0; index < textValue.length(); index++) {
            TextsizeVO vo = new TextsizeVO();
            vo.setTitle(textValue.getString(index));
            vo.setSampleText(getString(R.string.txt_config_textsize_sample_text));
           float size = DpiUtil.convertPixelsToDp(getBaseContext(), textsize.getDimensionPixelSize(index, 49));
            vo.setTextsize(size);
            if(readPosition == index){
                vo.setChecked(true);
            }else{
                vo.setChecked(false);
            }
            mTextsizeArray.add(vo);
        }
        setAppListView();
    }

    private void setAppListView() {
        if (null != mTextsizeArray && mTextsizeArray.size() > 0) {
            mTextsizeListAdapter = new TextSizeListAdapter(this, mTextsizeArray);
            mTextsizeListView.setAdapter(mTextsizeListAdapter);
            mTextsizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (int index = 0; index < mTextsizeArray.size(); index++) {
                        mTextsizeArray.get(index).setChecked(false);
                    }
                    switch (position){
                        case 0:
                            mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, -1);
                            break;
                        case 1:
                            mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 0);
                            break;
                        case 2:
                            mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 1);
                            break;
                        case 3:
                            mPref.setFloatPreference(Constants.PREF_TEXTSIZE_VALUE, 2);
                            break;
                    }

                    mTextsizeArray.get(position).setChecked(true);
                    mTextsizeListAdapter.notifyDataSetChanged();

                    mPref.setStringPreference(Constants.PREF_CHANGED_TEXT_SIZE, "Y");
                }
            });
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_setting_title_back:
                finish();
                break;
        }
    }
}
