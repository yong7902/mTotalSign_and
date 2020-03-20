package com.kolon.sign2.approval;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_013_VO.result.apprList;

import java.util.ArrayList;

public class ApprovalDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<apprList> data;
    private ListTabClickListener mInterface;
    private boolean headerCheck;

    public interface ListTabClickListener {
        void selectPosition(int position);

        void clickMenu();
    }

    public ApprovalDataAdapter(ArrayList<apprList> inputData) {
        if(inputData == null) return;
        data = inputData;
        headerCheck = false;
    }

    public void setInterface(ListTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //일반 row
            ApprovalPersonalRowView rowView = new ApprovalPersonalRowView(parent.getContext());
            AdapterViewHolder holder = new AdapterViewHolder(rowView);
            return holder;
        } else {
            //header row (메뉴선택이 있는 row)
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_header_approval_depart_select_menu, parent, false);
            return new AdapterViewHeaderHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        if (data == null || data.size() == 0) return;

        if (viewHolder instanceof AdapterViewHolder) {
            AdapterViewHolder addHolder = (AdapterViewHolder) viewHolder;
            addHolder.setData(data.get(position), position);
            addHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mInterface != null) {
                        if (headerCheck) {
                            mInterface.selectPosition(position - 1);
                        } else {
                            mInterface.selectPosition(position);
                        }
                    }
                }
            });
        } else {
            AdapterViewHeaderHolder addHolder = (AdapterViewHeaderHolder) viewHolder;
            addHolder.txt_depart_tab.setText(data.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).isHeader) {
            headerCheck = true;
            return 1;
        } else {
            return 0;
        }
    }

    public void setData(ArrayList<apprList> data){
        this.data = data;
        notifyDataSetChanged();
    }

    public ArrayList<apprList> getAdapterData() {
        return data;
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private ApprovalPersonalRowView mRowView;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            mRowView = (ApprovalPersonalRowView) itemView;
        }

        public void setData(apprList data, int position) {
            mRowView.setData(data, position);
        }
    }


    //제일 상단 메뉴선택이 있는 row
    public class AdapterViewHeaderHolder extends RecyclerView.ViewHolder {

        RelativeLayout lay_depart_tab;
        TextView txt_depart_tab;
        Button btn_depart_tab_select_menu;

        public AdapterViewHeaderHolder(@NonNull View itemView) {
            super(itemView);
            lay_depart_tab = (RelativeLayout) itemView.findViewById(R.id.lay_depart_tab);
            txt_depart_tab = (TextView) itemView.findViewById(R.id.txt_depart_tab);
            btn_depart_tab_select_menu = (Button) itemView.findViewById(R.id.btn_depart_tab_select_menu);
            btn_depart_tab_select_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInterface != null) mInterface.clickMenu();
                }
            });
        }
    }
}