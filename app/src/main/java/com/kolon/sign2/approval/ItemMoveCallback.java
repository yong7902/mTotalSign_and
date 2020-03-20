package com.kolon.sign2.approval;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperContract mAdapter;

    public ItemMoveCallback(ItemTouchHelperContract adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }



    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {

        if(target.getAdapterPosition() == 0){
            return false;
        }
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ApprovalLineAdapter.AdapterViewHolder) {
                ApprovalLineAdapter.AdapterViewHolder holder=
                        (ApprovalLineAdapter.AdapterViewHolder) viewHolder;
                mAdapter.onRowSelected(holder);
            }

        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ApprovalLineAdapter.AdapterViewHolder) {
            ApprovalLineAdapter.AdapterViewHolder holder=
                    (ApprovalLineAdapter.AdapterViewHolder) viewHolder;
            mAdapter.onRowClear(holder);
        }
    }

    public interface ItemTouchHelperContract {

        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(ApprovalLineAdapter.AdapterViewHolder holder);
        void onRowClear(ApprovalLineAdapter.AdapterViewHolder holder);

    }

}
