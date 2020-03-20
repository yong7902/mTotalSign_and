package com.kolon.sign2.servicedesk;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_037_VO;

import java.util.ArrayList;

public class ServiceDeskAuthListAdapter extends RecyclerView.Adapter<ServiceDeskAuthListAdapter.AdapterViewHolder> {

    private ArrayList<Res_AP_IF_037_VO.result.aprList> data;
    private Context context;

    private OnSelectItem mInterface;

    public interface OnSelectItem {
        void checkPosition(int position);

        void selectPosition(int position);
    }

    public void setInterface(OnSelectItem mInterface) {
        this.mInterface = mInterface;
    }

    public ServiceDeskAuthListAdapter(ArrayList<Res_AP_IF_037_VO.result.aprList> data) {
        if(data == null) return;
        this.data = data;
    }

    public void setData(ArrayList<Res_AP_IF_037_VO.result.aprList> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_service_desk_auth, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        holder.setData(position);
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

        private TextView tv_title, tv_team, tv_name, tv_time, tv_type;
        private RelativeLayout iv_check;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_team = (TextView) itemView.findViewById(R.id.tv_team);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            iv_check = (RelativeLayout) itemView.findViewById(R.id.iv_check);

            CommonUtils.textSizeSetting(context, tv_title);
            CommonUtils.textSizeSetting(context, tv_team);
            CommonUtils.textSizeSetting(context, tv_name);
            CommonUtils.textSizeSetting(context, tv_time);
            CommonUtils.textSizeSetting(context, tv_type);


        }

        public void setData(int position) {
            /**
             * reqInfo에서 잘라서 붙인다. 시간 - 팀 - 이름 순
             * title의 내용에서는 [ ] 안의 내용만 잘라서 권한신청등으로 사용후 나머지 이하는 title에 그대로 붙인다
             * */
            //"reqInfo": "2016-09-19 19:22:14 RMS 유제호" >>> 중간의 부서이름이 다양할수 있으므로.. 제일 앞 2개(날짜, 시간) 그리고 맨뒤 이름을 가져옴.
            //앞, 제일뒤를 가져온후 나머지를 부서명으로 ...
            String time = "";
            String team = "";
            String name = "";
            String type = "";
            String title = "";

            String str = data.get(position).getReqInfo();
            if (!TextUtils.isEmpty(str)) {
                String temp[] = str.split(" ");
                if (temp.length > 3) {
                    time = temp[0] + " " + temp[1];
                    name = temp[temp.length - 1];

                    for (int i = 2; i < temp.length - 1; i++) {
                        team = team + temp[i] + " ";
                    }
                    team = team.trim();
                }
            }

            tv_time.setText(time);
            tv_team.setText(team);
            tv_name.setText(name);

            String str2 = data.get(position).getTitle();
            if (!TextUtils.isEmpty(str) && str2.length() > 1) {
                str2 = data.get(position).getTitle().substring(1, data.get(position).getTitle().length());
                String temp2[] = str2.split("] ");

                if (temp2.length > 1) {
                    type = temp2[0];
                    title = temp2[1];
                }
            }

            tv_type.setText(type);
            tv_title.setText(title);

            if (data.get(position).isChecked) {
                iv_check.setSelected(true);
            } else {
                iv_check.setSelected(false);
            }
            iv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInterface != null) mInterface.checkPosition(position);
                }
            });


            CommonUtils.changeTextSize(context, tv_title);
            CommonUtils.changeTextSize(context, tv_team);
            CommonUtils.changeTextSize(context, tv_name);
            CommonUtils.changeTextSize(context, tv_time);
            CommonUtils.changeTextSize(context, tv_type);
        }
    }
}
