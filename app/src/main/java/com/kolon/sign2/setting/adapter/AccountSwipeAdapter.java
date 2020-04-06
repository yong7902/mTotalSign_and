package com.kolon.sign2.setting.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.TextsizeVO;

import java.util.ArrayList;

import retrofit2.Retrofit;

/**
 * Created by sunho_kim on 2019-12-31.
 */

public class AccountSwipeAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private final int TYPE_LIST_ITEM = 0;
    private final int TYPE_BOTTOM_ADD = 1;

    private Context mContext;
    //private ArrayList<TextsizeVO> mList;
    private ListEditInterface mCallback;
    private Activity mActivity;
    private Retrofit mRetrofit;
    private ArrayList<Res_AP_IF_004_VO.result.multiuserList> mList;


    public AccountSwipeAdapter(Context context, ArrayList<Res_AP_IF_004_VO.result.multiuserList> data, ListEditInterface callback) {
        this.mContext = context;
        this.mList = data;
        this.mCallback = callback;
    }

    public void changeList(ArrayList<Res_AP_IF_004_VO.result.multiuserList> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_BOTTOM_ADD) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_account_add, parent, false);
            return new BottomAddHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_account_item, parent, false);
            return new RowViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof BottomAddHolder) {
            BottomAddHolder addHolder = (BottomAddHolder) viewHolder;
            addHolder.btn_account_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) mCallback.addAccount();
                }
            });
        } else {

            RowViewHolder holder = (RowViewHolder) viewHolder;
            //   final TextsizeVO item = mList.get(position);
            SwipeLayout.SwipeListener swipeListener = new SwipeLayout.SwipeListener() {
                @Override
                public void onStartOpen(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    //when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                }

                @Override
                public void onClose(SwipeLayout layout) {
                    //when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    //you are swiping.
                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                }
            };

            String multi = "";//겸직표시
            if("N".equalsIgnoreCase(mList.get(position).getDelYn())){
                multi = mContext.getResources().getString(R.string.txt_account_multi_text);
            }

            //   holder.mCompanyNm.setText(item.getTitle());
            holder.mDeptNm.setText(mList.get(position).getDeptName());

            String job = mList.get(position).getTitleName();
            if (!TextUtils.isEmpty(mList.get(position).getRoleName())) {
                job = mList.get(position).getRoleName() + "/" + mList.get(position).getTitleName();
            }
            holder.mName.setText(mList.get(position).getUserName() + " " + job+" "+multi);
            holder.mCompanyNm.setText(mList.get(position).getCompanyName());

            holder.mSwipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            holder.mSwipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.mSwipeLayout.findViewById(R.id.ll_swipe_righttoleft));
            holder.mSwipeLayout.setClickToClose(true);
            holder.mSwipeLayout.addSwipeListener(swipeListener);

            //삭제 불가능 계정 처리
            if ("N".equalsIgnoreCase(mList.get(position).getDelYn())) {
                holder.mSwipeLayout.findViewById(R.id.ll_swipe_righttoleft).setVisibility(View.GONE);
            }

            holder.mSwipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            holder.mListDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteDialog(position);
                }
            });

            holder.mCheckBoxParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mList && !mList.isEmpty()) {
                        for (int index = 0; index < mList.size(); index++) {
                            mList.get(index).isChecked = false;
                        }
                    }
                    mList.get(position).isChecked = true;
                    mCallback.selectPosition(position);

                    notifyDataSetChanged();
                }
            });

            if (mList.get(position).isChecked) {
                holder.mDefaultAccountCheckBox.setChecked(true);
            } else {
                holder.mDefaultAccountCheckBox.setChecked(false);
            }
            mItemManger.bindView(viewHolder.itemView, position);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mList == null || mList.size() == 0|| position == mList.size()){
            return TYPE_BOTTOM_ADD;
        }
        return TYPE_LIST_ITEM;
    }

    @Override
    public int getItemCount() {
        if (null != mList) {
            return mList.size() + 1;
        } else {
            return 1;
        }
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_main_layout;
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        public SwipeLayout mSwipeLayout;
        public TextView mCompanyNm;
        public TextView mDeptNm;
        public TextView mName;

        public ImageView mListDelete;
        public CheckBox mDefaultAccountCheckBox;
        public LinearLayout mCheckBoxParent;

        public RowViewHolder(View itemView) {
            super(itemView);

            mSwipeLayout = itemView.findViewById(R.id.swipe_main_layout);
            mCompanyNm = itemView.findViewById(R.id.tv_account_company_nm);

            mDeptNm = itemView.findViewById(R.id.tv_account_dept_nm);
            mName = itemView.findViewById(R.id.tv_name);

            mListDelete = itemView.findViewById(R.id.iv_swipe_delete);
            mCheckBoxParent = itemView.findViewById(R.id.ll_main_account);
            mDefaultAccountCheckBox = itemView.findViewById(R.id.chk_main_account);
        }
    }

    //계정 추가 row
    public class BottomAddHolder extends RecyclerView.ViewHolder {
        LinearLayout btn_account_add;

        public BottomAddHolder(@NonNull View itemView) {
            super(itemView);
            btn_account_add = itemView.findViewById(R.id.btn_account_add);
        }
    }

    private void showDeleteDialog(final int position) {
        if (mList.get(position).isChecked) {
            //현재 선택되어있는 계정은 삭제 불가
            mCallback.errMessage(mContext.getResources().getString(R.string.txt_account_delete_alert3));
        } else {
            if ("Y".equalsIgnoreCase(mList.get(position).getDelYn())) {
                //계정 삭제
                mItemManger.closeAllItems();
                mCallback.deleteList(position);
            } else {
                //겸직 계정이므로 삭제 불가
                mCallback.errMessage(mContext.getResources().getString(R.string.txt_account_delete_alert2));
            }
        }
    }
}
