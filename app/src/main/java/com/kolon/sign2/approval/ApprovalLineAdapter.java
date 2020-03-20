package com.kolon.sign2.approval;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;
import com.kolon.sign2.dialog.ListDialog;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;
import java.util.Collections;

public class ApprovalLineAdapter extends RecyclerView.Adapter<ApprovalLineAdapter.AdapterViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private Vibrator vibrator;
    private final StartDragListener mStartDragListener;
    private Context mContext;

    private onClick mInterface;
    public interface onClick{
        void onEdit(int position);
    }
    public void setInterface(onClick mInterface){
        this.mInterface = mInterface;
    }

    private ArrayList<Res_AP_IF_016_VO.result.apprLineList> data;

    public ApprovalLineAdapter(Context mContext, StartDragListener startDragListener, ArrayList<Res_AP_IF_016_VO.result.apprLineList> data) {
        mStartDragListener = startDragListener;
        if(data == null) return;
        this.data = data;
        for (int i = 0; i < data.size(); i++) {
            data.get(i).dragId = "row:" + i;
        }
        this.mContext = mContext;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);

        Log.d("ApprovalLineAdapter", "===================================");
        for (int i = 0; i < data.size(); i++) {
            Log.d("ApprovalLineAdapter", "#### " + data.get(i).getSn() + " " + data.get(i).getActionType() + "  " + data.get(i).getName() + " " + data.get(i).getAprtype());
        }
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = new ApprovalLineRowView(parent.getContext());
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder adapterViewHolder, int position) {
        if (position != 0) {
            binderHelper.bind(adapterViewHolder.swipeLayout, data.get(position).dragId);

            if(!data.get(position).isOpend){
                adapterViewHolder.swipeLayout.close(false);
            }else{
                adapterViewHolder.swipeLayout.open(false);
            }
        }
        adapterViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        private ApprovalLineRowView rowView;
        private LinearLayout front_layout, delete_layout;
        private SwipeRevealLayout swipeLayout;
        private Button btn_edit, btn_delete;
        long then = 0;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);

            front_layout = (LinearLayout) itemView.findViewById(R.id.front_layout);
            delete_layout = (LinearLayout) itemView.findViewById(R.id.delete_layout);
            delete_layout.setVisibility(View.GONE);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);

            rowView = (ApprovalLineRowView) itemView;
        }

        public void setData(int position) {
            swipeLayout.viewTagId = data.get(position).dragId;
            rowView.setData(data.get(position), position);

            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mInterface != null) mInterface.onEdit(position);
                }
            });
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0;i<data.size();i++){
                        data.get(i).isOpend = false;
                    }
                    data.remove(position);
                    notifyDataSetChanged();
                }
            });

            if (position == 0) {
                front_layout.setEnabled(false);
                delete_layout.setVisibility(View.GONE);
                btn_edit.setVisibility(View.GONE);
                btn_delete.setVisibility(View.GONE);
            } else {
                front_layout.setEnabled(true);
                delete_layout.setVisibility(View.VISIBLE);
                btn_edit.setVisibility(View.VISIBLE);
                btn_delete.setVisibility(View.VISIBLE);
            }

            front_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    dragView(view, position);
                    return false;
                }
            });

            swipeLayout.setSwipeListener(new SwipeRevealLayout.SwipeListener(){

                @Override
                public void onClosed(SwipeRevealLayout view) {
                }

                @Override
                public void onOpened(SwipeRevealLayout view) {

                    for(int i=0;i<data.size();i++){
                        if(data.get(i).dragId.equals(view.viewTagId)){
                            data.get(i).isOpend = true;
                        }else{
                            data.get(i).isOpend = false;
                        }

                    }
                    notifyDataSetChanged();
                }

                @Override
                public void onSlide(SwipeRevealLayout view, float slideOffset) {
                }
            });
        }

        private void dragView(View v, int position) {
            if (position != 0) {
                vibrator.vibrate(100);
                mStartDragListener.requestDrag(this);
            }
        }
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(AdapterViewHolder holder) {
        holder.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onRowClear(AdapterViewHolder holder) {
        holder.itemView.setBackgroundColor(0xffffffff);
    }
}
