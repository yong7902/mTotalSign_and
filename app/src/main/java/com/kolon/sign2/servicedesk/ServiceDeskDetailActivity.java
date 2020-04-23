package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.view.ViewPagerFixed;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;

public class ServiceDeskDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout progress_bar;
    private TextSizeAdjView view_text_size_adj;
    private ViewPagerFixed pager;
    private ServiceDeskPagerAdapter adapter;

    private ShimmerFrameLayout mShimmerLayout;
    private ShimmerFrameLayout mShimmerLayout_sd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_desk_detail);

        Button btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);

        progress_bar = (RelativeLayout) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        //yong79. shimmer
        mShimmerLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_layout);
        mShimmerLayout_sd2 = (ShimmerFrameLayout) findViewById(R.id.shimmer_layout_sd2);

        ImageView btn_textsize = (ImageView) findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(this);

        view_text_size_adj = (TextSizeAdjView) findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                adapter.textSizeChange();
            }
        });

        Intent it = getIntent();
        if (it == null) return;
        ArrayList<Res_AP_IF_103_VO.dynamicListList> list = (ArrayList<Res_AP_IF_103_VO.dynamicListList>)it.getSerializableExtra("data");

        String menuId = it.getStringExtra("menuId");
        int position = it.getIntExtra("position", 1);
        String userid = it.getStringExtra("userId");

        adapter = new ServiceDeskPagerAdapter(this, list, menuId, userid);

        pager = (ViewPagerFixed) findViewById(R.id.view_pager);
        pager.setAdapter(adapter);

        pager.setCurrentItem(position);
    }

    public void showProgressBar(){
        progress_bar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progress_bar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_textsize:
                view_text_size_adj.show();
                break;
        }
    }

    public void shimmerStart() {
        //Toast.makeText(mContext, "shimerStart", Toast.LENGTH_LONG).show();
        //lv_home.setVisibility(View.GONE);
        //no_data.setVisibility(View.GONE);
        mShimmerLayout.setVisibility(View.VISIBLE);
        mShimmerLayout.startShimmer();
    }

    public void shimmerStop() {
        //Toast.makeText(mContext, "shimerStop", Toast.LENGTH_LONG).show();
        mShimmerLayout.setVisibility(View.GONE);
        mShimmerLayout.stopShimmer();
    }

    //남은 결재건수를 체크_yong79
    public void checkRemainApproval(String title, boolean isFinal, int position) {
        //boolean isFinal = true;
        //결재 1건이상 처리로 목록 복귀 리프레시 set
        setResult(RESULT_OK);

        if (isFinal) { //목록으로
            String msg = String.format(getResources().getString(R.string.txt_approval_success), title);
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

            onBackPressed();
        } else { //다음 결재
            String msg = String.format(getResources().getString(R.string.txt_approval_success), title) + "\n" + this.getResources().getString(R.string.txt_approval_next_process);
            TextDialog dialog = TextDialog.newInstance("", msg, this.getResources().getString(R.string.txt_approval_next_process_2),
                    this.getResources().getString(R.string.txt_approval_next_process_3));

            dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.btn_left:
                            //목록으로
                            dialog.dismiss();
                            onBackPressed();
                            break;
                        case R.id.btn_right:
                            //다음 결재건
                            dialog.dismiss();
                            showProgressBar();
                            nextApprovalLoading(position);
                            break;
                    }
                }
            });
            dialog.show(getSupportFragmentManager());
        }
    }

    private void nextApprovalLoading(int nowPosition) {
        //승인한 아이템 삭제
        adapter.getData().remove(nowPosition);
        adapter.notifyDataSetChanged();

        //페이지 이동 처리
        pager.setCurrentItem(nowPosition);
    }
}
