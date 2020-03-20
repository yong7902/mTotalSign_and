package com.kolon.sign2.approval;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ApprovalLineSearchAdapter extends RecyclerView.Adapter<ApprovalLineSearchAdapter.AdapterViewHolder> {
    private Context mContext;
    private ArrayList<ApprovalLineSearchPopup.dataClass> data;
    private OnTabClickListener mInterface;

    public interface OnTabClickListener {
        void selectPosition(int position);
    }

    public void setInterface(OnTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public ApprovalLineSearchAdapter(ArrayList<ApprovalLineSearchPopup.dataClass> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_dialog_approval_line_search, parent, false);
        AdapterViewHolder vh = new AdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.tv_name.setText(data.get(position).getName());
        holder.tv_job.setText(data.get(position).getJob());
        holder.tv_company.setText(data.get(position).getCompany());
        holder.tv_team.setText(data.get(position).getTeam());

        String time = data.get(position).getTime();
        if (TextUtils.isEmpty(time)) {
            holder.tv_time.setVisibility(View.GONE);
        } else {
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(data.get(position).getTime());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface != null) mInterface.selectPosition(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_job;
        TextView tv_company;
        TextView tv_team;
        TextView tv_time;

        public AdapterViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_job = (TextView) itemView.findViewById(R.id.tv_job);
            tv_company = (TextView) itemView.findViewById(R.id.tv_company);
            tv_team = (TextView) itemView.findViewById(R.id.tv_team);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            CommonUtils.textSizeSetting(mContext, tv_name);
            CommonUtils.textSizeSetting(mContext, tv_job);
            CommonUtils.textSizeSetting(mContext, tv_company);
            CommonUtils.textSizeSetting(mContext, tv_team);
            CommonUtils.textSizeSetting(mContext, tv_time);
        }
    }
}
