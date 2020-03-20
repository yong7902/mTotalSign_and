package com.kolon.sign2.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.AttachFileListVO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AttatchFileListAdapter extends RecyclerView.Adapter<AttatchFileListAdapter.ViewHolder> {

    private ArrayList<AttachFileListVO> data;
    private ListTabClickListener mInterface;
    private boolean isCheckProcess;

    public interface ListTabClickListener {
        void selectPosition(int position);

        void checkPosition(int position, boolean isChecked);
    }

    public void setInterface(ListTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public AttatchFileListAdapter(ArrayList<AttachFileListVO> data) {
        this.data = data;
        this.isCheckProcess = false;
    }

    public AttatchFileListAdapter(ArrayList<AttachFileListVO> data, boolean isCheckProcess) {
        this.data = data;
        this.isCheckProcess = isCheckProcess;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.row_attach_file, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String filename = data.get(position).getName();// data.get(position).getUid();
        int pos = filename.lastIndexOf(".");
        String ext = filename.substring(pos + 1);

        String txt = "기타";
        if (ext.equalsIgnoreCase("pptx")) {
            txt = "PPT";
        } else if (ext.equalsIgnoreCase("docx")) {
            txt = "Word";
        } else if (ext.equalsIgnoreCase("xlsx")) {
            txt = "EXCEL";
        } else if (ext.equalsIgnoreCase("jpg")) {
            txt = "JEPG";
        } else if (ext.equalsIgnoreCase("png")) {
            txt = "PNG";
        } else if (ext.equalsIgnoreCase("zip")) {
            txt = "ZIP";
        } else if (ext.equalsIgnoreCase("pdf")) {
            txt = "PDF";
        } else if (ext.equalsIgnoreCase("hwp")) {
            txt = "한글";
        }

        holder.btn_state.setText(txt);
        holder.tv_name.setText(filename);
        if (data.get(position).isChecked()) {
            holder.iv_attach_check.setBackgroundResource(R.drawable.checkbox_checked);//checkbox_checked
        } else {
            holder.iv_attach_check.setBackgroundResource(R.drawable.checkbox_unchecked);
        }

        holder.layout_filename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterface != null) {
                    mInterface.selectPosition(position);
                }
            }
        });
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mInterface != null) {
//                    mInterface.selectPosition(position);
//                }
//            }
//        });
        holder.layout_attach_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.get(position).isChecked()) {
                    data.get(position).setChecked(false);
                    if (mInterface != null) mInterface.checkPosition(position, false);
                } else {
                    data.get(position).setChecked(true);
                    if (mInterface != null) mInterface.checkPosition(position, true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btn_state;
        TextView tv_name;
        LinearLayout layout_filename, layout_attach_info;
        ImageView iv_attach_check;

        ViewHolder(View itemView) {
            super(itemView);
            btn_state = (Button) itemView.findViewById(R.id.btn_state);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            layout_filename = (LinearLayout) itemView.findViewById(R.id.layout_filename);
            layout_attach_info = (LinearLayout) itemView.findViewById(R.id.layout_attach_info);
            iv_attach_check = (ImageView) itemView.findViewById(R.id.iv_attach_check);
            if (isCheckProcess) {
                layout_attach_info.setVisibility(View.VISIBLE);
            } else {
                layout_attach_info.setVisibility(View.GONE);
            }
        }
    }
}
