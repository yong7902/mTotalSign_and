package com.kolon.sign2.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.kolon.sign2.R;
import com.kolon.sign2.utils.CommonUtils;

public class DynamicDetailTxtBtnItemView extends LinearLayout {

    TextView mDynamicUserInfo;
    ImageView mDynamicKolonTalk;
    String mUserId = "";
    String mCompanyCode = "";
    private KolonTalkClickListener dynamicKolonTalkListener;
    public interface KolonTalkClickListener {
        void onTalkClickListener(String userId);
    }
    public DynamicDetailTxtBtnItemView(Context context) {
        super(context);
        initView();
    }

    public DynamicDetailTxtBtnItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DynamicDetailTxtBtnItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public DynamicDetailTxtBtnItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        View view = layoutInflater.inflate(R.layout.view_dynamic_txtbtn_item, this, false);

        mDynamicUserInfo = view.findViewById(R.id.tv_detail_txtbtn_item_userinfo);
        mDynamicKolonTalk = view.findViewById(R.id.iv_detail_txtbtn_item_image);
        mDynamicKolonTalk.setOnClickListener(v -> {

            //Todo 현재 UserID를 넘기지 않기 떄문에 무조건 톡 호출 하도록 함. USerID가 파라미터로 넘어가야 한다면 아래 주석 코드를 사용하고 userID가 없을 경우 다른 액션 필요
            /*
            * if (!TextUtils.isEmpty(mUserId)) {
                callKolonTalk();
            }
            * */
            dynamicKolonTalkListener.onTalkClickListener(mUserId);

        });

        CommonUtils.textSizeSetting(getContext(), mDynamicUserInfo);

        addView(view);
    }

    public void setViewData(String userInfo, String usedId, KolonTalkClickListener listener) {
        mDynamicUserInfo.setText(userInfo);
        mUserId = usedId;
        dynamicKolonTalkListener = listener;
    }

    public void textSizeAdj(){
        CommonUtils.changeTextSize(getContext(), mDynamicUserInfo);
    }
}
