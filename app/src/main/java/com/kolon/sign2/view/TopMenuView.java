package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;

/**
 * Top Menu
 */
public class TopMenuView extends LinearLayout implements View.OnClickListener {

    private Context mContext;

    private ArrayList<Res_AP_IF_101_VO.result.sysArray> sysArray;
    private RecyclerView rv;
    private TopMenuViewAdapter adapter;

    private TopMenuClickListener mInterface;

    public interface TopMenuClickListener {
        void onClickTopMenu(String sysId);
    }

    public TopMenuView(Context context) {
        super(context);
        initView(context);
    }

    public TopMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TopMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context mContext) {
        this.mContext = mContext;

        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_top_menu, this, false);

        rv = (RecyclerView) v.findViewById(R.id.rv_top_menu_layer);

        addView(v);
    }

    public void setInterface(TopMenuClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public void onClick(View view) {
    }

    public void setData(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray, Res_AP_IF_101_VO.result userAuthInfo) {

        sysArray = new ArrayList<>();
        //home row
        Res_AP_IF_101_VO.result.sysArray info = new Res_AP_IF_101_VO().new result().new sysArray();
        info.setSysName(mContext.getResources().getString(R.string.txt_top_menu_title_1));
        info.setSysIcon("");
        info.setSysId("0");
        sysArray.add(info);
        //나머지
        sysArray.addAll(userAuthInfo.getSysArray());

        adapter = new TopMenuViewAdapter(sysArray);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new GridLayoutManager(mContext, 4));
        adapter.setInterface(new TopMenuViewAdapter.OnSelectMenu() {
            @Override
            public void onSelection(int position, String sysId, String name) {
                if (position == 0) {
                    if (mInterface != null) {
                        mInterface.onClickTopMenu("home");
                    }
                } else {
                    if (mInterface != null) {
                        mInterface.onClickTopMenu(sysId);
                    }
                }
            }
        });
    }
}
