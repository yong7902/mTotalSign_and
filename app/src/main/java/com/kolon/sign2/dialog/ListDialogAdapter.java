package com.kolon.sign2.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kolon.sign2.R;


import java.util.ArrayList;

public class ListDialogAdapter extends RecyclerView.Adapter<ListDialogAdapter.ViewHolder> {

    private ArrayList<String> data = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public ListDialogAdapter(ArrayList<String> list) {
        data = list ;
    }

    private ListTabClickListener mInterface;

    public interface ListTabClickListener {
        void selectPosition(int position);
    }
    public void setInterface(ListTabClickListener mInterface) {
        this.mInterface = mInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.row_dialog_text_list, parent, false) ;
        ViewHolder vh = new ViewHolder(view) ;

        return vh ;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String text = data.get(position) ;
        holder.textView1.setText(text) ;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterface != null){
                    mInterface.selectPosition(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1 ;
        ViewHolder(View itemView) {
            super(itemView) ;
            textView1 = itemView.findViewById(R.id.tv_dialog_text_row) ;
        }
    }
}