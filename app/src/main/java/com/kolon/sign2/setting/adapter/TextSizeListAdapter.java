package com.kolon.sign2.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kolon.sign2.R;
import com.kolon.sign2.vo.TextsizeVO;

import java.util.ArrayList;


/**
 * Created by sunho_kim on 2019-12-30.
 */

public class TextSizeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private TextSizeListAdapter mInstancd;
    private ArrayList<TextsizeVO> mTextsizeArrayList;
    private Context mContext;

    public TextSizeListAdapter(Context context, ArrayList<TextsizeVO> list) {
        mContext = context;
        mTextsizeArrayList = list;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public TextSizeListAdapter getInstance(Context context, ArrayList<TextsizeVO> list) {
        if (mInstancd == null) {
            mInstancd = new TextSizeListAdapter(context, list);
        }
        return mInstancd;
    }

    public int getCount() {
        return mTextsizeArrayList.size();
    }

    public Object getItem(int position) {
        return mTextsizeArrayList.get(position);
    }

    public long getItemId(int arg0) {
        return 0;
    }

    static class ViewHolder {
        TextView mTextValueTextView;
        TextView mTextSampleTextView;
        CheckBox mTextCheckBox;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vHolder = null;//= new ViewHolder();
        if (convertView == null) {
            vHolder = new ViewHolder();
            v = mInflater.inflate(R.layout.textsize_list_item, parent, false);
            vHolder.mTextValueTextView = v.findViewById(R.id.tv_textsize_value);
            vHolder.mTextSampleTextView = v.findViewById(R.id.tv_textsize_sample);
            vHolder.mTextCheckBox = v.findViewById(R.id.cb_textsize_selected);
            v.setTag(vHolder);
        } else {
            vHolder = (ViewHolder)v.getTag();
        }
        vHolder.mTextValueTextView.setText(mTextsizeArrayList.get(position).getTitle());
        vHolder.mTextSampleTextView.setText(mTextsizeArrayList.get(position).getSampleText());
        vHolder.mTextSampleTextView.setTextSize(mTextsizeArrayList.get(position).getTextsize());
        if (mTextsizeArrayList.get(position).isChecked()) {
            vHolder.mTextCheckBox.setChecked(true);
        } else {
            vHolder.mTextCheckBox.setChecked(false);
        }


        return v;
    }

}
