package com.kolon.sign2.dynamic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.dynamic.dynamicDetail.DynamicDetailActivity;
import com.kolon.sign2.servicedesk.ServiceDeskDetailActivity;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;

public class DynamicListAdapter extends RecyclerView.Adapter<DynamicListAdapter.ViewHolder> {

    private static ArrayList<Res_AP_IF_103_VO.dynamicListList> list;
    private String AUTO_DETAIL_FLAG;
    private static String mUserId;
    private static String mSysId;
    private static String mMeunId;
    private static String mSysNm;
    private DynamicListAdapter.ViewHolder holder;
    private static Context mContext;

    public DynamicListAdapter(Context context, ArrayList<Res_AP_IF_103_VO.dynamicListList> list) {
        this.list = list;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_list, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position), AUTO_DETAIL_FLAG, list, mMeunId, position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(ArrayList<Res_AP_IF_103_VO.dynamicListList> list, String autoDetailYN) {
        updateList(list, autoDetailYN, "", "", "", "");
    }

    public void updateList(ArrayList<Res_AP_IF_103_VO.dynamicListList> list, String autoDetailYN, String userId, String sysId, String menuId, String sysNm) {
        this.list = list;
        this.AUTO_DETAIL_FLAG = autoDetailYN;
        this.mUserId = userId;
        this.mSysId = sysId;
        this.mMeunId = menuId;
        this.mSysNm = sysNm;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView approvalIdTv;
        TextView statusTv;
        TextView titleTv;
        TextView requesterTv;
        TextView reqDateTimeTv;
//        TextView docIdTv;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            approvalIdTv = itemView.findViewById(R.id.item_detail_list_approvalId_tv);
            statusTv = itemView.findViewById(R.id.item_detail_list_status_tv);
            titleTv = itemView.findViewById(R.id.item_detail_list_title_tv);
            requesterTv = itemView.findViewById(R.id.item_detail_list_requester_tv);
            reqDateTimeTv = itemView.findViewById(R.id.item_detail_list_reqDatetime_tv);

            CommonUtils.textSizeSetting(itemView.getContext(), approvalIdTv);
            CommonUtils.textSizeSetting(itemView.getContext(), statusTv);
            CommonUtils.textSizeSetting(itemView.getContext(), titleTv);
            CommonUtils.textSizeSetting(itemView.getContext(), requesterTv);
            CommonUtils.textSizeSetting(itemView.getContext(), reqDateTimeTv);

        }

        public void bind(Res_AP_IF_103_VO.dynamicListList item, String autoDetailYN, ArrayList<Res_AP_IF_103_VO.dynamicListList> list, String menuId, int position) {

            CommonUtils.changeTextSize(itemView.getContext(), approvalIdTv);
            CommonUtils.changeTextSize(itemView.getContext(), statusTv);
            CommonUtils.changeTextSize(itemView.getContext(), titleTv);
            CommonUtils.changeTextSize(itemView.getContext(), requesterTv);
            CommonUtils.changeTextSize(itemView.getContext(), reqDateTimeTv);

            if (item.getStatus().isEmpty()) {
                statusTv.setVisibility(View.INVISIBLE);
            } else {
                statusTv.setVisibility(View.VISIBLE);
            }

            if (item.getApprovalId().isEmpty()) {
                approvalIdTv.setText(item.getDocId());
                titleTv.setText(item.getTitle());
                requesterTv.setText(item.getRequester());
                reqDateTimeTv.setText(item.getReqDatetime());

            } else {
                approvalIdTv.setText(item.getApprovalId());
                statusTv.setText(item.getStatus());
                titleTv.setText(item.getTitle());
                requesterTv.setText(item.getRequester());
                reqDateTimeTv.setText(item.getReqDatetime());
            }

            //TODO : Y일때 DynamicDetailActivity intent로 넘겨주어야 함
            //      "" 일때는 기본 웹 뷰? 보여주는 창으로 intent 로 넘겨줘야함
            // @args : userId, sysId, menuId, docId 넘겨줘야함 <ㅡ 104 IF 참조
            if (autoDetailYN.equalsIgnoreCase("Y")) {
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), DynamicDetailActivity.class);

                    intent.putExtra("position", getAdapterPosition());
                    intent.putExtra("userId", mUserId);
                    intent.putExtra("sysId", mSysId);
                    intent.putExtra("sysNm", mSysNm);
                    intent.putExtra("menuId", mMeunId);
                    intent.putExtra("docId", item.getDocId());
                    intent.putExtra("param01", item.getParam01());
                    intent.putExtra("param02", item.getParam02());
                    intent.putExtra("param03", item.getParam03());
                    intent.putExtra("param04", item.getParam04());
                    intent.putExtra("param05", item.getParam05());
                    intent.putExtra("listcount", list.size());
                    intent.putExtra("object", list);
       //             itemView.getContext().startActivity(intent);
                    if (mContext instanceof Activity) {

                        if (((MainActivity) mContext).getActivityMoveOkCheck()) {
                            return;
                        }
                        ((MainActivity) mContext).setActivityMoveOkCheck(true);
                        ((Activity) mContext).startActivityForResult(intent, 100);
                    }
                });

            } else {
                itemView.setOnClickListener(v -> {
                    //TODO : 기본 webView를 보여주는 창으로 intent
                    Intent intent = new Intent(itemView.getContext(), ServiceDeskDetailActivity.class);
                    intent.putExtra("data", list);
                    intent.putExtra("menuId", menuId);
                    intent.putExtra("position", position);
                    intent.putExtra("userId", mUserId);
               //     itemView.getContext().startActivity(intent);

                    if (mContext instanceof Activity) {
                        if (((MainActivity) mContext).getActivityMoveOkCheck()) {
                            return;
                        }
                        ((MainActivity) mContext).setActivityMoveOkCheck(true);
                        ((Activity) mContext).startActivityForResult(intent, 100);
                    }
                });
            }


        }

    }
}
