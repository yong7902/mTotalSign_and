package com.kolon.sign2.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kolon.sign2.R;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;

import java.util.ArrayList;

public class TopMenuViewAdapter extends RecyclerView.Adapter<TopMenuViewAdapter.AdapterViewHolder>{

    private Context mContext;
    private ArrayList<Res_AP_IF_101_VO.result.sysArray> data;

    private OnSelectMenu mInterface;
    public void setInterface(OnSelectMenu mInterface){
        this.mInterface = mInterface;
    }
    public interface OnSelectMenu{
        void onSelection(int position, String sysId, String name);
    }
    public TopMenuViewAdapter(ArrayList<Res_AP_IF_101_VO.result.sysArray> data){
        if(data == null) return;
        this.data = data;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_top_menu_view, parent, false);
        AdapterViewHolder vh = new AdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if(position == 0){
            holder.icon.setImageResource(R.drawable.more_icon_01);
            holder.title.setText(mContext.getResources().getString(R.string.txt_top_menu_title_1));
        }else{
            Glide.with(mContext).load(data.get(position).getSysIcon()).into(holder.icon);
            holder.title.setText(data.get(position).getSysName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mInterface != null){
                    mInterface.onSelection(position, data.get(position).getSysId(), data.get(position).getSysName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(data == null) return 0;
        return data.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;
        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = (ImageView)itemView.findViewById(R.id.iv_top);
            title = (TextView)itemView.findViewById(R.id.tv_top);
        }
    }
}
