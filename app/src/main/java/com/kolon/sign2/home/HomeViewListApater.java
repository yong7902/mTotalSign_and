package com.kolon.sign2.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;

import java.util.ArrayList;

public class HomeViewListApater extends RecyclerView.Adapter<HomeViewListApater.HomeAdapterViewHolder> {


    private ArrayList<Res_AP_IF_002_VO.result.APPROVAL_LIST> data;
    private int rowType = 0;
    private HomeViewListTabClickListener mInterface;

    public interface HomeViewListTabClickListener {
        void selectPosition(int type, int position);
    }

    public HomeViewListApater(ArrayList<Res_AP_IF_002_VO.result.APPROVAL_LIST> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        this.data = data;
    }

    public void setInterface(HomeViewListTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public HomeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = null;
        switch (viewType) {
            case HomeFragment.HOME_TOP_TOTAL://최상단 전체 미승인 건수
                rowView = new HomeTopTotalRowView(parent.getContext());
                break;
            case HomeFragment.HOME_CATEGORY://각 항목에 관한 전체건수
                rowView = new HomeCategoryRowView(parent.getContext());
                break;
            case HomeFragment.HOME_DATA:// 데이터 로우
                rowView = new HomeDataRowView(parent.getContext());
                break;
            case HomeFragment.HOME_MORE_VIEW://더보기
                rowView = new HomeMoreView(parent.getContext());
                break;
            case HomeFragment.HOME_HIDE_VIEW://숨김
                rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
                break;
        }
        HomeAdapterViewHolder holder = new HomeAdapterViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapterViewHolder holder, int position) {
        if (data == null || data.size() == 0) {
            return;
        }

        holder.setData(data.get(position), position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterface != null) {
                    mInterface.selectPosition(data.get(position).type, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        } else {
            return data.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).isView){
            rowType = data.get(position).type;
        }else{
            rowType = HomeFragment.HOME_HIDE_VIEW;
        }
        return rowType;
    }

    class HomeAdapterViewHolder extends RecyclerView.ViewHolder {
        private View mRowView;

        public HomeAdapterViewHolder(View itemView) {
            super(itemView);
            switch (rowType) {
                case HomeFragment.HOME_TOP_TOTAL://최상단 전체 미승인 건수
                    mRowView = (HomeTopTotalRowView) itemView;
                    break;
                case HomeFragment.HOME_CATEGORY://각 항목에 관한 전체건수
                    mRowView = (HomeCategoryRowView) itemView;
                    break;
                case HomeFragment.HOME_DATA:// 전자결재 하위 로우
                    mRowView = (HomeDataRowView) itemView;
                    break;
                case HomeFragment.HOME_MORE_VIEW:
                    mRowView = (HomeMoreView) itemView;
                    break;
            }
        }

        public void setData(Res_AP_IF_002_VO.result.APPROVAL_LIST data, int position) {
            switch (rowType) {
                case HomeFragment.HOME_TOP_TOTAL://최상단 전체 미승인 건수
                    ((HomeTopTotalRowView) mRowView).setData(data.totalCnt);
                    break;
                case HomeFragment.HOME_CATEGORY://각 항목에 관한 전체건수
                    ((HomeCategoryRowView) mRowView).setData(data);
                    break;
                case HomeFragment.HOME_DATA://각 항목에 관한 전체건수
                    ((HomeDataRowView) mRowView).setData(data);
                    break;
            }
        }
    }
}
