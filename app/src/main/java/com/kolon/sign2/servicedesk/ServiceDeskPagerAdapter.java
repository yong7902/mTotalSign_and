package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.kolon.sign2.R;
import com.kolon.sign2.approval.ApprovalDetailActivity;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;

public class ServiceDeskPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Res_AP_IF_103_VO.dynamicListList> data;
    private String menuId;
    private String userId;
    private boolean textSizeAdj;

    public ServiceDeskPagerAdapter(Context mContext, ArrayList<Res_AP_IF_103_VO.dynamicListList> data, String menuId, String userId) {
        this.mContext = mContext;
        this.data = data;
        this.menuId = menuId;
        this.userId = userId;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = null;

        //마지막 페이지 여부
        boolean isFinal = false;
        if (data.size() == position+1) //마지막 페이지인지 여부
            isFinal = true;

        if (menuId.startsWith("S03")) {
            //보안 it
            view = new ServiceDeskSecurityITDetailView(mContext);
            //((ServiceDeskSecurityITDetailView) view).shimmerStart();
            if (textSizeAdj) {
                ((ServiceDeskSecurityITDetailView) view).textSizeAdj();
            } else {
                ((ServiceDeskSecurityITDetailView) view).setData(userId, data.get(position).getDocId(), menuId, position, isFinal);
            }
            //((ServiceDeskSecurityITDetailView) view).shimmerStop();
        } else if (menuId.startsWith("S04")) {
            //문서반출
            //approvalId > appidx, docid > chkidx
            view = new ServiceDeskDLPDetailView(mContext);
            //((ServiceDeskDLPDetailView) view).shimmerStart();
            if (textSizeAdj) {
                ((ServiceDeskDLPDetailView) view).textSizeAdj();
            } else {
                ((ServiceDeskDLPDetailView) view).setData(userId, data.get(position).getDocId(), data.get(position).getParam01(), position, isFinal);
            }
            //((ServiceDeskDLPDetailView) view).shimmerStop();
        } else if (menuId.startsWith("S05")) {
            //메일필터
            //docid > seq
            view = new ServiceDeskMailFilterView(mContext);
            //((ServiceDeskMailFilterView) view).shimmerStart();
            if (textSizeAdj) {
                ((ServiceDeskMailFilterView) view).textSizeAdj();
            } else {
                ((ServiceDeskMailFilterView) view).setData(userId, data.get(position).getDocId(), position, isFinal);
            }
            //((ServiceDeskMailFilterView) view).shimmerStop();
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
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    public void textSizeChange(){
        textSizeAdj = true;
        notifyDataSetChanged();
    }

    public ArrayList<Res_AP_IF_103_VO.dynamicListList> getData() {
        return data;
    }

}
