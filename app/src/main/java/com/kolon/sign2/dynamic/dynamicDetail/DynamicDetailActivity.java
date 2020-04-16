package com.kolon.sign2.dynamic.dynamicDetail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.kolon.sign2.R;
import com.kolon.sign2.dynamic.DynamicListFragment;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.view.ViewPagerFixed;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;


public class DynamicDetailActivity extends AppCompatActivity {

    final String TAG = getClass().getName();

    private ViewPagerFixed pager;
    private DynamicDetailPagerAdapter adapter;
    private RelativeLayout mTitleBack;
    private TextView mTitleText;

    private ImageView btn_textsize;
    private TextSizeAdjView view_text_size_adj;

    private String userId = "";
    private String sysId = "";
    private String sysNm = "";
    private String menuId = "";
    private String docId = "";
    private String param01 = "";
    private String param02 = "";
    private String param03 = "";
    private String param04 = "";
    private String param05 = "";
    private int position = 0;
    ArrayList<Res_AP_IF_103_VO.dynamicListList> dynamicListList;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_datail);
        mTitleBack = findViewById(R.id.rl_dynamic_detail_title_back);
        mTitleText = findViewById(R.id.tv_dynamic_detail_title);
        pager = findViewById(R.id.view_pager);

        btn_textsize = findViewById(R.id.btn_textsize);
        btn_textsize.setVisibility(View.VISIBLE);
        btn_textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_text_size_adj.show();
            }
        });
        view_text_size_adj = (TextSizeAdjView)findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                adapter.textSizeChange();
            }
        });

        if (null != getIntent()) {
            if (null != getIntent().getExtras()) {
                position = getIntent().getIntExtra("position", 0);
                userId = getIntent().getStringExtra("userId");
                sysId = getIntent().getStringExtra("sysId");
                sysNm = getIntent().getStringExtra("sysNm");
                menuId = getIntent().getStringExtra("menuId");
                docId = getIntent().getStringExtra("docId");
                param01 = getIntent().getStringExtra("param01");
                param02 = getIntent().getStringExtra("param02");
                param03 = getIntent().getStringExtra("param03");
                param04 = getIntent().getStringExtra("param04");
                param05 = getIntent().getStringExtra("param05");
                dynamicListList = (ArrayList<Res_AP_IF_103_VO.dynamicListList>) getIntent().getSerializableExtra("object");
                adapter = new DynamicDetailPagerAdapter(this, dynamicListList, userId, sysId, menuId, docId, param01, param02, param03, param04, param05);
                mTitleText.setText(sysNm);

                pager.setAdapter(adapter);
                pager.addOnPageChangeListener(new ViewPagerFixed.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                pager.setCurrentItem(position);
            }
        }

        mTitleBack.setOnClickListener(v -> {
            finish();
        });
    }

    public void nextApprovalLoading(int nowPosition) {
        //승인한 아이템 삭제
        adapter.getItemData().remove(nowPosition);
        adapter.notifyDataSetChanged();

        //페이지 이동 처리
        pager.setCurrentItem(nowPosition);
    }

    public void finishDetailActivity() {
        finish();
    }

    public void finishDetailActivityParentRefresh() {
        setResult(RESULT_OK);
        finish();
    }

}
