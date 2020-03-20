package com.kolon.sign2.approval;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.vo.Res_AP_IF_020_VO;

import java.util.ArrayList;


public class ApprovalSearchAdapter extends RecyclerView.Adapter<ApprovalSearchAdapter.AdapterViewHolder> {

    private ArrayList<Res_AP_IF_020_VO.result.apprList> data;

    private OnTabClickListener mInterface;
    public interface OnTabClickListener {
        void selectPosition(int position);
    }
    public void setInterface(OnTabClickListener mInterface){
        this.mInterface = mInterface;
    }

    public ApprovalSearchAdapter(ArrayList<Res_AP_IF_020_VO.result.apprList> data){
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View rowView = new ApprovalSearchRowView(viewGroup.getContext());
        AdapterViewHolder holder = new AdapterViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.setData(data.get(position), position);
        adapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterface != null) mInterface.selectPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data == null){
            return 0;
        }
        return data.size();
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private ApprovalSearchRowView mRowView;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            mRowView = (ApprovalSearchRowView) itemView;
        }
        public void setData(Res_AP_IF_020_VO.result.apprList data, int position){
            mRowView.setData(data, position);
        }
    }
}
