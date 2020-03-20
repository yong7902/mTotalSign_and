package com.kolon.sign2.setting.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kolon.sign2.R;
import com.kolon.sign2.vo.LoginParamVO;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class MenuChangeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        DragHelper.ActionCompletionContract {
    private ItemTouchHelper mTouchHelper;
    private Context mContext;
    private ArrayList<LoginParamVO> mMenuArray;
    private Vibrator vibrator;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.setting_menu_change_item, parent, false);
        return new MenuListViewHolder(view);

    }

    public MenuChangeListAdapter(Context context, ArrayList<LoginParamVO> menuArray) {
        this.mContext = context;
        this.mMenuArray = menuArray;
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MenuChangeListAdapter.MenuListViewHolder) {
            MenuListViewHolder viewHolder = (MenuListViewHolder) holder;
            viewHolder.mResourceName.setText(mMenuArray.get(position).getAccount());

            Glide.with(mContext).load(mMenuArray.get(position).iconUrl).into(viewHolder.mIcon);

            viewHolder.mFavoriteListLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    vibrator.vibrate(100);
                    mTouchHelper.startDrag(holder);
                    return false;
                }
            });
//            viewHolder.mFavoriteDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mMenuArray.remove(position);
//                    notifyDataSetChanged();
//                }
//            });
        }

    }

    @Override
    public int getItemCount() {
        return mMenuArray == null ? 0 : mMenuArray.size();
    }

    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        LoginParamVO targetService = mMenuArray.get(oldPosition);
        LoginParamVO tempService = new LoginParamVO(targetService.getAccount(), targetService.getPassword());
        tempService.sysId = targetService.sysId;
        tempService.iconUrl = targetService.iconUrl;
        mMenuArray.remove(oldPosition);
        mMenuArray.add(newPosition, tempService);
        notifyItemMoved(oldPosition, newPosition);
    }

    @Override
    public void onViewSwiped(int position) {
//        mServiceList.remove(position);
//        notifyItemRemoved(position);
    }

    @Override
    public void onVIewDragFinish() {

    }

    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.mTouchHelper = touchHelper;
    }

    public class MenuListViewHolder extends RecyclerView.ViewHolder {

        TextView mResourceName;
        ImageView mFavoriteDelete;
        ConstraintLayout mFavoriteListLayout;
        ImageView mIcon;

        public MenuListViewHolder(View itemView) {
            super(itemView);
            mFavoriteListLayout = itemView.findViewById(R.id.cl_reservation_favorite_layout);
            mResourceName = itemView.findViewById(R.id.tv_reservation_favorite_listname);
            mFavoriteDelete = itemView.findViewById(R.id.iv_reservation_favorite_delete);
            mIcon = itemView.findViewById(R.id.iv_menu_change_icon);
        }
    }

}
