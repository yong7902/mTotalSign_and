package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_IF_028_VO;

import java.util.ArrayList;

public class ServiceDeskConfirmHistoryAdapter extends RecyclerView.Adapter<ServiceDeskConfirmHistoryAdapter.AdapterViewHolder> {

    private Context mContext;
    private ArrayList<Res_AP_IF_028_VO.result.listappHistory> data;

    public ServiceDeskConfirmHistoryAdapter(ArrayList<Res_AP_IF_028_VO.result.listappHistory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_dialog_opinion, parent, false);

        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.setData(position);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInterface != null) mInterface.selectPosition(position);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        Button btn_state;
        TextView tv_name, tv_state, tv_team, tv_time, tv_comment;
        LinearLayout lay_comment;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            btn_state.getLayoutParams().width = (int)DpiUtil.convertDpToPixel(mContext, 58);
            btn_state.setTextColor(ContextCompat.getColor(mContext, R.color.lightish_blue));
            btn_state.setBackgroundResource(R.drawable.drw_rad12_corner_blue);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            tv_state.setTextColor(ContextCompat.getColor(mContext, R.color.warm_grey_two));
            tv_team = (TextView) itemView.findViewById(R.id.tv_team);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            lay_comment = (LinearLayout) itemView.findViewById(R.id.lay_comment);
            tv_comment = (TextView) itemView.findViewById(R.id.tv_comment);


            CommonUtils.textSizeSetting(mContext, tv_name);
            CommonUtils.textSizeSetting(mContext, tv_state);
            CommonUtils.textSizeSetting(mContext, tv_team);
            CommonUtils.textSizeSetting(mContext, tv_time);
            CommonUtils.textSizeSetting(mContext, tv_comment);
        }

        public void setData(int position) {

            btn_state.setText(data.get(position).getAppRole());

            String name = data.get(position).getAppUser();
            String team = "";
            String[] temp = name.split("\\(");
            if(temp.length >1){
                name = temp[0];
                team = temp[1].substring(0, temp[1].length()-1);
            }

//            if (!TextUtils.isEmpty(data.get(position).getAppRole())) {
//                name = name + " / " + data.get(position).getAppRole();
//            }
            tv_name.setText(name);
            tv_state.setText(data.get(position).getAppStatus());

            tv_team.setText(team);
            tv_time.setText(data.get(position).getAppDate());

            if (TextUtils.isEmpty(data.get(position).getAppComment())) {
                lay_comment.setVisibility(View.GONE);
            } else {
                lay_comment.setVisibility(View.VISIBLE);
                tv_comment.setText(data.get(position).getAppComment());
            }


            CommonUtils.changeTextSize(mContext, tv_name);
            CommonUtils.changeTextSize(mContext, tv_state);
            CommonUtils.changeTextSize(mContext, tv_team);
            CommonUtils.changeTextSize(mContext, tv_time);
            CommonUtils.changeTextSize(mContext, tv_comment);

        }
    }
}
