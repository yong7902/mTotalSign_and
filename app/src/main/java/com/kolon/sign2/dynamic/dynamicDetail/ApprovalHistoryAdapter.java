package com.kolon.sign2.dynamic.dynamicDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_104_VO;

import java.util.ArrayList;

public class ApprovalHistoryAdapter extends RecyclerView.Adapter<ApprovalHistoryAdapter.ViewHolder> {

    ArrayList<Res_AP_IF_104_VO.dynamicDetailList> list;


    public ApprovalHistoryAdapter(ArrayList<Res_AP_IF_104_VO.dynamicDetailList> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder parent, int position) {
        parent.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderTv;
        TextView orderManagerTv;
        TextView statusInfoTv;
        TextView contentsTv;
        TextView dateTv;
        TextView detailContentsTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTv = (TextView) itemView.findViewById(R.id.item_approval_history_order_tv);
            orderManagerTv = (TextView) itemView.findViewById(R.id.item_approval_history_order_manager_tv);
            statusInfoTv = (TextView) itemView.findViewById(R.id.item_approval_history_status_info_tv);
            contentsTv = (TextView) itemView.findViewById(R.id.item_approval_history_contents_tv);
            dateTv = (TextView) itemView.findViewById(R.id.item_approval_history_date_tv);
            detailContentsTv = (TextView) itemView.findViewById(R.id.item_approval_history_detail_contents_tv);

            CommonUtils.textSizeSetting(itemView.getContext(), orderManagerTv);
            CommonUtils.textSizeSetting(itemView.getContext(), statusInfoTv);
            CommonUtils.textSizeSetting(itemView.getContext(), contentsTv);
            CommonUtils.textSizeSetting(itemView.getContext(), dateTv);
            CommonUtils.textSizeSetting(itemView.getContext(), detailContentsTv);
        }

        public void bind(Res_AP_IF_104_VO.dynamicDetailList item) {
            orderTv.setText(item.getAttr01());
            orderManagerTv.setText(item.getAttr02());
            statusInfoTv.setText(item.getAttr03());
            contentsTv.setText(item.getAttr04());
            dateTv.setText(item.getAttr05());

            if (!item.getAttr06().isEmpty()) {
                detailContentsTv.setVisibility(View.VISIBLE);
                detailContentsTv.setText(item.getAttr06());
            }

            CommonUtils.changeTextSize(itemView.getContext(), orderManagerTv);
            CommonUtils.changeTextSize(itemView.getContext(), statusInfoTv);
            CommonUtils.changeTextSize(itemView.getContext(), contentsTv);
            CommonUtils.changeTextSize(itemView.getContext(), dateTv);
            CommonUtils.changeTextSize(itemView.getContext(), detailContentsTv);
        }
    }
}
