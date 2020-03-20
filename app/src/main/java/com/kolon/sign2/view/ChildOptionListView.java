package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.kolon.sign2.R;
import com.kolon.sign2.dynamic.MenuVO;
import com.kolon.sign2.utils.DpiUtil;

import java.util.ArrayList;
import java.util.Iterator;

public class ChildOptionListView extends LinearLayout {

    private int INIT_INDEX = 0;

    ArrayList<MenuVO> menuItems;
    TextView statusTv;
    TextView countTv;
    LinearLayout listContainer;

    ArrayList<TextView> tvArr;
    private String SELECTED_MENU_KEY = "";

    public ChildOptionListView(Context context) {
        super(context);
        initView();
    }

    public ChildOptionListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChildOptionListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_child_option_list, this, false);

        statusTv = (TextView) view.findViewById(R.id.child_option_list_status_tv);
        countTv = (TextView) view.findViewById(R.id.child_option_list_count_tv);
        listContainer = (LinearLayout) view.findViewById(R.id.child_option_list_container);

        addView(view);
    }

    public void setMenuItems(ArrayList<MenuVO> menuItems) {
        this.menuItems = menuItems;
    }

    public void setTextColorLoop(View selectedView) {
        for (TextView tv : tvArr) {
            if (tv.equals(selectedView)) {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.lightish_blue));
                statusTv.setText(tv.getText());
            } else {
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.greyish_brown));
            }
        }
    }

    public void setCountNumText(String key) {
        MenuVO item = null;
        try {
            item = findFocusItem(key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (null != item) {
            countTv.setText(item.getCountNum());
        }
    }

    private MenuVO findFocusItem(String key) throws ClassNotFoundException {
        for (MenuVO item : menuItems) {
            if (item.getMenuName().equals(key)) {
                return item;
            }
        }
        throw new ClassNotFoundException("menuItems Key not Found in Items!!");
    }

    public void updateCount(String menuId) {
        for (MenuVO item : menuItems) {
            if (item.getMenuId().equals(menuId)) {
                countTv.setText(item.getCountNum());
            }
        }
    }


    public void drawView(OnClickListener listener) {
        if (menuItems == null) {
            throw new ExceptionInInitializerError("Must Be call setMenuItems() First!");
        }

        Iterator<MenuVO> itr = menuItems.iterator();
        MenuVO item;
        tvArr = new ArrayList<>();

        for (int i = 0; i < menuItems.size(); i++) {

            item = itr.next();

            LinearLayout.LayoutParams textParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

            TextView tv = new TextView(getContext());
            tv.setText(item.getMenuName());
            tv.setOnClickListener(listener);
            tv.setTextColor(ContextCompat.getColor(getContext(), R.color.greyish_brown));
//            tv.setTextColor(R.color.colorNotSelected);
            tv.setGravity(Gravity.CENTER_VERTICAL);
            tv.setLayoutParams(textParams);
            //TODO layoutGravity 설정하기, CENTER_VERTICAL


            tv.setTextSize(14);
            tvArr.add(tv);
            listContainer.addView(tv);

            if (i == INIT_INDEX) {
                tv.callOnClick();
            }

            if (i != LAST_INDEX()) {
                createSplitLine();
            }

        }

    }

    private void createSplitLine() {
        LinearLayout splitLine = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LayoutParams((int) DpiUtil.convertDpToPixel(getContext(), 2)
                , ViewGroup.LayoutParams.MATCH_PARENT);

        params.setMargins((int) DpiUtil.convertDpToPixel(getContext(), 8),
                (int) DpiUtil.convertDpToPixel(getContext(), 15),
                (int) DpiUtil.convertDpToPixel(getContext(), 12),
                (int) DpiUtil.convertDpToPixel(getContext(), 15));

        splitLine.setLayoutParams(params);
        splitLine.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_two));

        listContainer.addView(splitLine);
    }

    private int LAST_INDEX() {
        return menuItems.size() - 1;
    }

    public String getSELECTED_MENU_KEY() {
        return SELECTED_MENU_KEY;
    }

    public void setSELECTED_MENU_KEY(String SELECTED_MENU_KEY) {
        this.SELECTED_MENU_KEY = SELECTED_MENU_KEY;
    }

    public MenuVO getVoInfo(String key) {
        MenuVO item = null;
        try {
            item = findFocusItem(key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (item != null) {
            return item;
        } else {
            return null;
        }
    }

    public void setINIT_INDEX(int index) {
        INIT_INDEX = index;
    }
}