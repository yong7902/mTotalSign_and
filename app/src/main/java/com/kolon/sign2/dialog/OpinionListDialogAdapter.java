package com.kolon.sign2.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.approval.ApprovalProgressRowView;
import com.kolon.sign2.utils.DpiUtil;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

/**
 * 의견 리스트 보기 어댑터
 */
public class OpinionListDialogAdapter extends RecyclerView.Adapter<OpinionListDialogAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Res_AP_IF_016_VO.result.apprLineList> data;
    private int type;

    public OpinionListDialogAdapter(ArrayList<Res_AP_IF_016_VO.result.apprLineList> listdata) {
        setData(listdata, 0);
    }

    public OpinionListDialogAdapter(ArrayList<Res_AP_IF_016_VO.result.apprLineList> listdata, int type) {
        setData(listdata, type);
    }

    private void setData(ArrayList<Res_AP_IF_016_VO.result.apprLineList> listdata, int type) {
        this.type = type;
        if (type == 0) {
            data = new ArrayList<>();
            for (int i = 0; i < listdata.size(); i++) {
                if (!TextUtils.isEmpty(listdata.get(i).getComment())) {
                    data.add(listdata.get(i));
                }
            }
        } else {
            data = listdata;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (type == 0) {
            //popup용
            view = inflater.inflate(R.layout.row_dialog_opinion, parent, false);
        } else {
            //activity용
            view = new ApprovalProgressRowView(parent.getContext());
        }

        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Res_AP_IF_016_VO.result.apprLineList read = data.get(position);
        String btn_state = read.getActionType();
        String tv_name = read.getName() + " " + read.getPosition();
        String tv_state = read.getActivity();

        String tv_team = read.getDepartment();
        String tv_time = read.getActionTime();
        String tv_comment = read.getComment();

        if (type == 0) {
            //4자이상일때 - 예)부서협조 병렬
            if (btn_state != null && btn_state.length() > 4) {
                holder.btn_state.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) DpiUtil.convertDpToPixel(context, 17)));
            }

            holder.btn_state.setText(btn_state);
            holder.tv_name.setText(tv_name);
            holder.tv_state.setText(tv_state);
            holder.tv_team.setText(tv_team);
            holder.tv_time.setText(tv_time);
            holder.tv_comment.setText(tv_comment);
        } else {
            holder.setData(read, position);
        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(mInterface != null){
//                    mInterface.selectPosition(position);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_state;
        TextView tv_name;
        TextView tv_state;
        TextView tv_team;
        TextView tv_time;
        TextView tv_comment;

        private ApprovalProgressRowView view;

        ViewHolder(View itemView) {
            super(itemView);
            if (type == 0) {
                btn_state = (Button) itemView.findViewById(R.id.btn_state);
                tv_name = itemView.findViewById(R.id.tv_name);
                tv_state = itemView.findViewById(R.id.tv_state);
                tv_team = itemView.findViewById(R.id.tv_team);
                tv_time = itemView.findViewById(R.id.tv_time);
                tv_comment = itemView.findViewById(R.id.tv_comment);
            } else {
                view = (ApprovalProgressRowView) itemView;
            }

        }

        public void setData(Res_AP_IF_016_VO.result.apprLineList data, int position) {
            view.setData(data, position);
        }
    }
}