package com.kolon.sign2.dynamic.dynamicDetail;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kolon.sign2.approval.ApprovalDetailRowView;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;

/**
 * 동적 상세 리스트 페이지 어댑
 */
public class DynamicDetailPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Res_AP_IF_103_VO.dynamicListList> data;
    private String userId;
    private String sysId;
    private String menuId;
    private String docId;
    private String param01;
    private String param02;
    private String param03;
    private String param04;
    private String param05;
    private int nowPosition;
    private boolean textSizeAdj;

    public DynamicDetailPagerAdapter(Context mContext, ArrayList<Res_AP_IF_103_VO.dynamicListList> data,
                                     String userId, String sysId, String menuId, String docId, String param01, String param02, String param03, String param04, String param05) {
        this.mContext = mContext;
        this.data = data;
        this.userId = userId;
        this.sysId = sysId;
        this.menuId = menuId;
        this.docId = docId;
        this.param01 = param01;
        this.param02 = param02;
        this.param03 = param03;
        this.param04 = param04;
        this.param05 = param05;
    }

    public ArrayList<Res_AP_IF_103_VO.dynamicListList> getItemData() {
        return data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        DynamiclDetailView view = null;

        if (mContext != null) {
            view = new DynamiclDetailView(mContext);

            if(textSizeAdj){
      //          view.textSizeAdj();
            }else{
                //마지막페이지 여부
                boolean isFinal = data.size() == position+1 ? true : false;
                view.getDynamicDetailList(userId, sysId, menuId, data.get(position).getDocId(), data.get(position).getParam01(), data.get(position).getParam02(), data.get(position).getParam03(), data.get(position).getParam04(), data.get(position).getParam05(), isFinal, position);
            }
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return (view == (View) obj);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if(textSizeAdj){
            ((DynamiclDetailView)object).textSizeAdj();
            return super.getItemPosition(object);
        }
        return POSITION_NONE;
    }

    public void textSizeChange(){
        textSizeAdj = true;
        notifyDataSetChanged();
    }
}
