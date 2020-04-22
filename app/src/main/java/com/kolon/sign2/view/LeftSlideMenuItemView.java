package com.kolon.sign2.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;

/**
 * 좌측 메뉴 cell view
 */
public class LeftSlideMenuItemView extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private ImageView iv_icon;
    private View iv_div;
    private TextView tv_title;
    private LinearLayout btn_expand_menu;

    ArrayList<Res_AP_IF_102_VO.result.menuArray> tempArray;
    private LinearLayout lv_expand_menu_layer;

    private OnTabClick mInterface;

    public interface OnTabClick {
        void selectItem(Res_AP_IF_102_VO.result.menuArray item);
    }

    public void setInterface(OnTabClick mInterface) {
        this.mInterface = mInterface;
    }

    public LeftSlideMenuItemView(Context context) {
        super(context);
        initView(context);
    }

    public LeftSlideMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LeftSlideMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_left_menu_approval, this, false);

        iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
        iv_div = (View) v.findViewById(R.id.iv_div);
        tv_title = (TextView) v.findViewById(R.id.tv_title);

        lv_expand_menu_layer = (LinearLayout) v.findViewById(R.id.lv_expand_menu_layer);

        btn_expand_menu = (LinearLayout) v.findViewById(R.id.btn_expand_menu);
        btn_expand_menu.setOnClickListener(this);

        //기본은 닫은 상태
        btn_expand_menu.setSelected(false);
        iv_div.setVisibility(View.GONE);
        lv_expand_menu_layer.setVisibility(View.GONE);

        addView(v);
    }

    public void setData(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray, Res_AP_IF_101_VO.result.sysArray sysArray) {

        tv_title.setText(sysArray.getSysName());
        Glide.with(this).load(sysArray.getSysIcon()).into(iv_icon);

        //그룹타이틀 추가
        tempArray = new ArrayList<>();
        String oldName = "";
        for (int i = 0; i < menuArray.size(); i++) {
            if (menuArray.get(i).getSysId().equals(sysArray.getSysId())) {
                String name = menuArray.get(i).getGroupName();
                if (!TextUtils.isEmpty(name)) {
                    //group이름이 붙은 최상단 타이틀
                    if (!name.equals(oldName)) {
                        Res_AP_IF_102_VO.result.menuArray title = new Res_AP_IF_102_VO.result.menuArray();
                        title.isGroupTitle = true;
                        title.setSysId(menuArray.get(i).getSysId());
                        title.setGroupName(menuArray.get(i).getGroupName());
                        tempArray.add(title);
                    }
                    oldName = name;
                }
                tempArray.add(menuArray.get(i));
            }
        }

        for (int i = 0; i < tempArray.size(); i++) {
            Res_AP_IF_102_VO.result.menuArray read = tempArray.get(i);
            if (sysArray.getSysId().equals(read.getSysId())) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View cell = inflater.inflate(R.layout.row_left_menu_view, this, false);
                cell.setOnClickListener(this);
                cell.setTag(i);
                LinearLayout lay_menu_item_depth1, lay_menu_item_depth2, lay_menu_item_depth3;
                TextView tv_menu_title_depth1;
                TextView tv_menu_item, tv_menu_item_num;
                TextView tv_sub_menu_item, tv_sub_menu_item_num;

                lay_menu_item_depth1 = (LinearLayout) cell.findViewById(R.id.lay_menu_item_depth1);
                lay_menu_item_depth2 = (LinearLayout) cell.findViewById(R.id.lay_menu_item_depth2);
                lay_menu_item_depth3 = (LinearLayout) cell.findViewById(R.id.lay_menu_item_depth3);

                tv_menu_title_depth1 = (TextView) cell.findViewById(R.id.tv_menu_title_depth1);

                tv_menu_item = (TextView) cell.findViewById(R.id.tv_menu_item);
                tv_menu_item_num = (TextView) cell.findViewById(R.id.tv_menu_item_num);

                tv_sub_menu_item = (TextView) cell.findViewById(R.id.tv_sub_menu_item);
                tv_sub_menu_item_num = (TextView) cell.findViewById(R.id.tv_sub_menu_item_num);

                tv_menu_item_num.setTag("0");
                tv_sub_menu_item_num.setTag("0");

                //그룹이름이 붙은 최상단
                if (tempArray.get(i).isGroupTitle) {
                    lay_menu_item_depth1.setVisibility(View.VISIBLE);
                    lay_menu_item_depth2.setVisibility(View.GONE);
                    lay_menu_item_depth3.setVisibility(View.GONE);
                    tv_menu_title_depth1.setText(read.getGroupName());
                    cell.setTag(-1);
                } else {
                    // 하위 메뉴 표시 여부
                    if (TextUtils.isEmpty(read.getParentMenuId())) {
                        //하위메뉴 없음
                        lay_menu_item_depth1.setVisibility(View.GONE);
                        lay_menu_item_depth2.setVisibility(View.VISIBLE);
                        lay_menu_item_depth3.setVisibility(View.GONE);

                        tv_menu_item.setText(read.getMenuName());

                        if (!"Y".equals(read.getCountYn())) { //(TextUtils.isEmpty(read.getCountNum())){// || "0".equals(read.getCountNum())) {
                            tv_menu_item_num.setVisibility(View.GONE);
                        } else {
                            tv_menu_item_num.setVisibility(View.VISIBLE);
                            tv_menu_item_num.setText(read.getCountNum());
                        }

                        tv_menu_item_num.setTag(read.getMenuId());

                    } else {
                        //하위 메뉴가 있음
                        lay_menu_item_depth1.setVisibility(View.GONE);
                        lay_menu_item_depth2.setVisibility(View.GONE);
                        lay_menu_item_depth3.setVisibility(View.VISIBLE);

                        tv_sub_menu_item.setText(read.getMenuName());

                        if (!"Y".equals(read.getCountYn())) { //(TextUtils.isEmpty(read.getCountNum())){// || "0".equals(read.getCountNum())) {
                            tv_sub_menu_item_num.setVisibility(View.GONE);
                        } else {
                            tv_sub_menu_item_num.setVisibility(View.VISIBLE);
                            tv_sub_menu_item_num.setText(read.getCountNum());
                        }
                        tv_sub_menu_item_num.setTag(read.getMenuId());
                    }
                }
                lv_expand_menu_layer.addView(cell);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_expand_menu:
                if (btn_expand_menu.isSelected()) {
                    btn_expand_menu.setSelected(false);
                    iv_div.setVisibility(View.GONE);
                    lv_expand_menu_layer.setVisibility(View.GONE);
                } else {
                    btn_expand_menu.setSelected(true);
                    iv_div.setVisibility(View.VISIBLE);
                    lv_expand_menu_layer.setVisibility(View.VISIBLE);
                }
                break;
            default:
                if (mInterface != null) {
                    int pos = -1;
                    try {
                        pos = (int) v.getTag();
                    } catch (Exception e) {

                    }
                    if(pos != -1){
                        mInterface.selectItem(tempArray.get(pos));
                    }
                }
        }
    }
}
