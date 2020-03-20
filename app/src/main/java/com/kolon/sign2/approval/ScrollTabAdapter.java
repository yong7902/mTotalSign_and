package com.kolon.sign2.approval;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;

public class ScrollTabAdapter extends RecyclerView.Adapter<ScrollTabAdapter.AdapterViewHolder> {


    private ArrayList<Res_AP_IF_102_VO.result.menuArray> data;
    private OnTabClickListener mInterface;

    public interface OnTabClickListener {
        void selectPosition(int position);
    }

    /**
     * @param data 데이터
     */
    public ScrollTabAdapter(ArrayList<Res_AP_IF_102_VO.result.menuArray> data) {
        this.data = data;
    }

    public void setInterface(OnTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TabRowView rowView = new TabRowView(parent.getContext());
        AdapterViewHolder holder = new AdapterViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.setData(data.get(position), position);
        adapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterface != null){
                    mInterface.selectPosition(position);

                    for(int i=0;i<data.size();i++){
                        data.get(i).isSelected = false;
                    }
                    data.get(position).isSelected = true;
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {

        private TabRowView rowView;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = (TabRowView) itemView;
        }

        public void setData(Res_AP_IF_102_VO.result.menuArray data, int position) {
            rowView.setData(data, position);
        }
    }
}
