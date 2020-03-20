package com.kolon.sign2.approval;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.vo.Res_AP_IF_201_VO;
import com.kolon.sign2.vo.Res_AP_IF_202_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;

import java.util.ArrayList;


public class ApprovalLindAddAdapter extends RecyclerView.Adapter<ApprovalLindAddAdapter.AdapterViewHolder> {

    private ListTabClickListener mInterface;

    public interface ListTabClickListener {
        void selectPosition(int position, String IkenId);
    }

    private ArrayList<Res_AP_IF_201_VO.result.apprCompList> data0;//회사
    private ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList> data1;//부서
    private ArrayList<Res_AP_IF_203_VO.result.apprSpList> data2;//사람
    private ArrayList<ApprovalTotalVO> totalVO;

    private int size;
    private int type;

    private int COMPANY = 0;

    public ApprovalLindAddAdapter(String data, int type) {
        Gson g = new Gson();
        this.type = type;
        if (type == 0) {
            data0 = g.fromJson(data, new TypeToken<ArrayList<Res_AP_IF_201_VO.result.apprCompList>>() {
            }.getType());
            if (data0 == null) return;
            size = data0.size();
        } else if (type == 1) {
            data1 = g.fromJson(data, new TypeToken<ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList>>() {
            }.getType());
            if (data1 == null) return;
            size = data1.size();
        } else if (type == 2) {
            // type 2
            data2 = g.fromJson(data, new TypeToken<ArrayList<Res_AP_IF_203_VO.result.apprSpList>>() {
            }.getType());
            if (data2 == null) return;
            size = data2.size();
        }
    }

    public ApprovalLindAddAdapter(ArrayList<ApprovalTotalVO> totalVO, int type) {
        this.type = type;
        this.totalVO = totalVO;
        size = totalVO.size();
    }

    public void setInterface(ListTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        ApprovalLineAddRow rowView = new ApprovalLineAddRow(parent.getContext());
        AdapterViewHolder holder = new AdapterViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder adapterViewHolder, int position) {
        adapterViewHolder.setData(position);
        adapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterface != null) {
                    if (totalVO == null || totalVO.size() == 0) {
                        mInterface.selectPosition(position, "");
                    } else {
                        mInterface.selectPosition(position, totalVO.get(position).getIkenId());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return size;
    }


    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private ApprovalLineAddRow mRowView;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            mRowView = (ApprovalLineAddRow) itemView;
        }

        public void setData(int position) {
            if (type == 0) {
                mRowView.setData1(data0.get(position));
            } else if (type == 1) {
//                mRowView.setData2(data1.get(position), position);
                mRowView.setData(totalVO.get(position));
            } else if (type == 2) {
                mRowView.setData3(data2.get(position));
            } else {
                mRowView.setData(totalVO.get(position));
            }
        }
    }
}