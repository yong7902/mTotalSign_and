package com.kolon.sign2.approval;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.dialog.OpinionListDialogAdapter;
import com.kolon.sign2.view.TextSizeAdjView;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

/**
 * 결재 진행 상태 , 의견보기
 * */
public class ApprovalProgressActivity extends AppCompatActivity {

    private TextSizeAdjView view_text_size_adj;
    private OpinionListDialogAdapter adapter;
    private LinearLayout layout_no_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_progress);

        TextView tvTitle = (TextView)findViewById(R.id.title);
        layout_no_data = (LinearLayout)findViewById(R.id.layout_no_data);

        RecyclerView lv_approval_progress = (RecyclerView)findViewById(R.id.lv_approval_progress);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_approval_progress.setLayoutManager(linearLayoutManager);

        Button backBtn = (Button)findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent it = getIntent();
        if (it == null) return;

        String title = it.getStringExtra("title");
        ArrayList<Res_AP_IF_016_VO.result.apprLineList> data = (ArrayList<Res_AP_IF_016_VO.result.apprLineList> )it.getSerializableExtra("apprLineList");

        if (data == null || data.size() == 0) {
            layout_no_data.setVisibility(View.VISIBLE);
        } else {
            layout_no_data.setVisibility(View.GONE);
        }

        adapter = new OpinionListDialogAdapter(data, 1);
        lv_approval_progress.setAdapter(adapter);

        tvTitle.setText(title);

        view_text_size_adj = (TextSizeAdjView)findViewById(R.id.view_text_size_adj);
        view_text_size_adj.setTopDivView(false);
        view_text_size_adj.setInterface(new TextSizeAdjView.OnSizeClickListener() {
            @Override
            public void onSize(float size) {
                //변경안했을경우 제외
                adapter.notifyDataSetChanged();
                setResult(10);
            }
        });

        ImageView btn_textsize = (ImageView)findViewById(R.id.btn_textsize);
        btn_textsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_text_size_adj.show();
            }
        });
    }
}
