package com.kolon.sign2.servicedesk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_032_VO;

import java.util.ArrayList;

public class ServiceDeskDLPListAdapter extends RecyclerView.Adapter<ServiceDeskDLPListAdapter.AdapterViewHolder> {

    private ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList;

    private OnDocClickListener mInterface;

    public void setInterface(OnDocClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public ServiceDeskDLPListAdapter(ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> detectList) {
        this.detectList = detectList;
    }


    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceDeskDetailDocRowView view = new ServiceDeskDetailDocRowView(parent.getContext());
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if (detectList == null || detectList.size() == 0) return;
        AdapterViewHolder addHolder = (AdapterViewHolder) holder;
        addHolder.setData(detectList.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (detectList == null) return 0;
        return detectList.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        ServiceDeskDetailDocRowView rowView;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = (ServiceDeskDetailDocRowView)itemView;
        }

        public void setData(Res_AP_IF_032_VO.result.dlpDetailFile data, int position){
            rowView.setData(data, position);
            rowView.setInterface(new OnDocClickListener() {
                @Override
                public void onFileClick(int position, String url) {
                    if(mInterface != null) mInterface.onFileClick(position, url);
                    detectList.get(position).isChecked = true;
                    notifyDataSetChanged();
                }

                @Override
                public void onChecked(int position, boolean isChecked) {
                    if(mInterface != null) mInterface.onChecked(position, isChecked);
                    detectList.get(position).isChecked = isChecked;
                    notifyDataSetChanged();
                }
            });
        }
    }
}
