package com.kolon.sign2.approval;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;

public class ApprovalScrollTabView extends LinearLayout {

    private Context mContext;
    private LinearLayout mLayoutTabList;
    private RecyclerView rv;
    private ImageView iv_back, iv_next;
    private ArrayList<Res_AP_IF_102_VO.result.menuArray> data;

    private final int REMOVE_TIME = 2000;// 터치안했을시 좌우 화살표 사라지는시간 ms

    public ApprovalScrollTabView(Context context) {
        super(context);
        initView(context);
    }
    public ApprovalScrollTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public ApprovalScrollTabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private OnTabListener mInterface;
    public interface OnTabListener {
        void select(int position);
    }
    public void setInterface(OnTabListener mInterface){
        this.mInterface = mInterface;
    }

    private void initView(Context context){
        mContext = context;

        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_scroll_tab, this, false);

        iv_back = (ImageView) v.findViewById(R.id.iv_back);
        iv_next = (ImageView) v.findViewById(R.id.iv_next);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        rv = (RecyclerView)v.findViewById(R.id.lv_scroll_tab);
        rv.setLayoutManager(linearLayoutManager);

        rv.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (rv.canScrollHorizontally(-1) && rv.canScrollHorizontally(1)) {
                    iv_back.setVisibility(View.VISIBLE);
                    iv_next.setVisibility(View.VISIBLE);
                    startTimer();
                } else if (rv.canScrollHorizontally(-1)) {
                    iv_back.setVisibility(View.VISIBLE);
                    iv_next.setVisibility(View.GONE);
                    startTimer();
                } else if (rv.canScrollHorizontally(1)) {
                    iv_back.setVisibility(View.GONE);
                    iv_next.setVisibility(View.VISIBLE);
                    startTimer();
                }
            }
        });

        addView(v);
    }

    public void setData(ArrayList<Res_AP_IF_102_VO.result.menuArray> data){
        this.data = data;
        ScrollTabAdapter adapter = new ScrollTabAdapter(data);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setInterface(new ScrollTabAdapter.OnTabClickListener() {
            @Override
            public void selectPosition(int position) {
                if(mInterface != null){
                    mInterface.select(position);
                }
            }
        });
    }

    public void setMove(int position){
        rv.scrollToPosition(position);
    }

    private void startTimer(){
        h.removeMessages(0);
        h.sendEmptyMessageDelayed(0, REMOVE_TIME);
    }

    final Handler h = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            iv_back.setVisibility(View.GONE);
            iv_next.setVisibility(View.GONE);
            return false;
        }
    });
}
