package com.kolon.sign2.approval;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.RequestManager;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

/**
 * 전자결재 상세 뷰페이저 어댑터
 */
public class ApprovalPagerAdapter extends PagerAdapter {

    private Context mContext;
    private RequestManager mGlideRequestManager;
    private ArrayList<ApprovalDetailActivity.inputData> data;
    private String category;
    private String userId;
    private String deptId;
    private String companyCd;
    private boolean textSizeAdj;

    public ApprovalPagerAdapter(Context mContext, RequestManager mGlideRequestManager, ArrayList<ApprovalDetailActivity.inputData> data, String category, String userId, String deptId, String companyCd) {
        this.mContext = mContext;
        this.mGlideRequestManager = mGlideRequestManager;
        this.data = data;
        this.category = category;
        this.userId = userId;
        this.deptId = deptId;
        this.companyCd = companyCd;
    }

    public ArrayList<ApprovalDetailActivity.inputData> getItemData() {
        return data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ((ApprovalDetailActivity) mContext).shimmerStart();

        ApprovalDetailRowView view = null;

        if (mContext != null) {
            view = new ApprovalDetailRowView(mContext);
            view.setInterface(new ApprovalDetailRowView.OnRowViewState() {
                @Override
                public void setApprovalLineView(boolean isView) {
                    ((ApprovalDetailActivity) mContext).setApprovalLineView(position, isView);
                }

                @Override
                public void getServerResponse(Res_AP_IF_016_VO result) {
                    ((ApprovalDetailActivity) mContext).getServerResponse(result, position);
                    if (result != null)
                        data.get(position).rowData = result.getResult();
                }

                @Override
                public void onTextSizeChange() {
                    textSizeAdj = false;
                }
            });

            if(textSizeAdj){
                view.textSizeAdj();
            }else{
                view.setData(mGlideRequestManager, data, position, category, userId, deptId, companyCd);
            }
        }

        container.addView(view);

        ((ApprovalDetailActivity) mContext).shimmerStop();

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
            ((ApprovalDetailRowView)object).textSizeAdj();
            return super.getItemPosition(object);
        }
        return POSITION_NONE;
    }

    public void textSizeChange(){
        textSizeAdj = true;
        notifyDataSetChanged();
    }
}
