package com.kolon.sign2.approval;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.kolon.sign2.R;
import com.kolon.sign2.dialog.CommentDialog;
import com.kolon.sign2.dialog.TextDialog;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;

/**
 * 전자결재 상세 하단 메뉴
 */
public class ApprovalBottomMenuView extends LinearLayout implements View.OnClickListener {
    private String TAG = "ApprovalBottomMenuView";
    private Context mContext;
    private LinearLayout lay_bottom_menu, lay_bottom_txt;
    private RelativeLayout btn_bottom_menu_1, btn_bottom_menu_2, btn_bottom_menu_3, btn_bottom_menu_4, btn_bottom_menu_5;
    private View iv_bottom_menu_div_1, iv_bottom_menu_div_2, iv_bottom_menu_div_3, iv_bottom_menu_div_4, iv_bottom_menu_div_5;
    private ImageView iv_bottom_menu_icon_1, iv_bottom_menu_icon_2, iv_bottom_menu_icon_3, iv_bottom_menu_icon_4, iv_bottom_menu_icon_5;
    private TextView tv_bottom_menu_title_1, tv_bottom_menu_title_2, tv_bottom_menu_title_3, tv_bottom_menu_title_4, tv_bottom_menu_title_5;
    private boolean isApprovalLineOk;//결재선 확인

    public static final String APPROVAL_MIGYUL = "P";//미결함
    public static final String APPROVAL_GIGYUL = "D";//기결함
    public static final String APPROVAL_GIAN = "R";//기안함
    public static final String APPROVAL_WANRYO = "C";//완료함
    public static final String APPROVAL_HOOGYUL = "Z";//후결함
    public static final String APPROVAL_MICHULI = "004";//미처리문서
    public static final String APPROVAL_JINHAENG = "010";//진행문서
    public static final String APPROVAL_GYULJAE = "100";//결재문서
    public static final String APPROVAL_BALSIN = "610";//발신문서
    public static final String APPROVAL_SUSIN = "500";//수신문서
    public static final String APPROVAL_HYUPJO = "600";//협조문서
    public static final String APPROVAL_BOOGYUL = "400";//부결문서


    private onBottomMenuClickListener mInterface;

    public interface onBottomMenuClickListener {
        void onClickApprovalLine();

        void onSendMessage(String title, String actionCode, String comment);

        void setApprovalLineView(boolean isView);//우측 결재선 표시 유무
    }

    public void setInterface(onBottomMenuClickListener mInterface) {
        this.mInterface = mInterface;
    }

    public ApprovalBottomMenuView(Context context) {
        super(context);
        initView(context);
    }

    public ApprovalBottomMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ApprovalBottomMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.view_approval_bottom_menu, this, false);


        lay_bottom_menu = (LinearLayout) v.findViewById(R.id.lay_bottom_menu);
        lay_bottom_menu.setVisibility(View.GONE);
        lay_bottom_txt = (LinearLayout) v.findViewById(R.id.lay_bottom_txt);
        lay_bottom_txt.setVisibility(View.GONE);

        setVisibility(View.GONE);

        //하단 메뉴
        btn_bottom_menu_1 = (RelativeLayout) v.findViewById(R.id.btn_bottom_menu_1);
        btn_bottom_menu_1.setOnClickListener(this);
        btn_bottom_menu_2 = (RelativeLayout) v.findViewById(R.id.btn_bottom_menu_2);
        btn_bottom_menu_2.setOnClickListener(this);
        btn_bottom_menu_3 = (RelativeLayout) v.findViewById(R.id.btn_bottom_menu_3);
        btn_bottom_menu_3.setOnClickListener(this);
        btn_bottom_menu_4 = (RelativeLayout) v.findViewById(R.id.btn_bottom_menu_4);
        btn_bottom_menu_4.setOnClickListener(this);
        btn_bottom_menu_5 = (RelativeLayout) v.findViewById(R.id.btn_bottom_menu_5);
        btn_bottom_menu_5.setOnClickListener(this);

        iv_bottom_menu_div_1 = (View) v.findViewById(R.id.iv_bottom_menu_div_1);
        iv_bottom_menu_div_2 = (View) v.findViewById(R.id.iv_bottom_menu_div_2);
        iv_bottom_menu_div_3 = (View) v.findViewById(R.id.iv_bottom_menu_div_3);
        iv_bottom_menu_div_4 = (View) v.findViewById(R.id.iv_bottom_menu_div_4);
        iv_bottom_menu_div_5 = (View) v.findViewById(R.id.iv_bottom_menu_div_5);

        iv_bottom_menu_icon_1 = (ImageView) v.findViewById(R.id.iv_bottom_menu_icon_1);
        iv_bottom_menu_icon_2 = (ImageView) v.findViewById(R.id.iv_bottom_menu_icon_2);
        iv_bottom_menu_icon_3 = (ImageView) v.findViewById(R.id.iv_bottom_menu_icon_3);
        iv_bottom_menu_icon_4 = (ImageView) v.findViewById(R.id.iv_bottom_menu_icon_4);
        iv_bottom_menu_icon_5 = (ImageView) v.findViewById(R.id.iv_bottom_menu_icon_5);

        tv_bottom_menu_title_1 = (TextView) v.findViewById(R.id.tv_bottom_menu_title_1);
        tv_bottom_menu_title_2 = (TextView) v.findViewById(R.id.tv_bottom_menu_title_2);
        tv_bottom_menu_title_3 = (TextView) v.findViewById(R.id.tv_bottom_menu_title_3);
        tv_bottom_menu_title_4 = (TextView) v.findViewById(R.id.tv_bottom_menu_title_4);
        tv_bottom_menu_title_5 = (TextView) v.findViewById(R.id.tv_bottom_menu_title_5);

        isApprovalLineOk = false;

        addView(v);
    }

    public void setData(Res_AP_IF_016_VO.result readData, String userId) {
        //처리버튼 노출 처리
        if("N".equalsIgnoreCase(readData.getApprDetailItem().getApporvalPlag())){
            setVisibility(View.GONE);
            return;
        }else {
            setVisibility(View.VISIBLE);

            //하단메뉴 상태 misform == 1 인 경우 버튼 hidden하고 ‘DB연동 양식 결재는 PC에서만 결재가 가능합니다.” 메시지 노출
            if ("1".equals(readData.getApprDetailItem().getMisform())) {
                setVisibility(View.VISIBLE);
                lay_bottom_txt.setVisibility(View.VISIBLE);
            } else {
                /**
                 * -버튼노출 가능 조건 : 상위리스트 메뉴 종류>
                 *   1. 개인결재함 & 미결함(P)
                 *    1) aprstate == ‘A04002’ and activity == ‘진행’
                 *   2. 개인결재함 & 후결함(?)
                 *   3. 부서결재함 & 미처리함(앞의 3자리가 004)
                 *    1) status == ‘미접수’
                 *    2) status == ‘보류’ and processorid == 사용자id
                 *
                 * - 버튼 종류
                 *   1. 개인결재함 & 미결함(P)
                 *    1) misform == 0 > 가결,부결,반송
                 *    2) misform == 9 > 가결,부결
                 *   2. 개인결재함 & 후결함(?) > 가결
                 *   3. 부서결재함 & 미처리함(앞의 3자리가 004)
                 *    1) state == ‘A02005’ > 가결,부결,반송,보류,접수자전결
                 *    2) state == ‘A02012’ > 가결,부결,반송,보류,접수자전결
                 *    3) state == ‘A02011’ > 결재상신,공문반송,접수자전결
                 */
                /**
                 * 우상단 결재선 버튼 노출
                 * 버튼종류가 3.3)일 경우 노출
                 * 3. 부서결재함 & 미처리함(앞의 3자리가 004)
                 * 3) state == ‘A02011’ > 결재상신,공문반송,접수자전결
                 * */
                String category = readData.getApprDetailItem().getCategory();

                if (APPROVAL_MIGYUL.equalsIgnoreCase(category)) { //&& //"A04002".equals(readData.getApprDetailItem().getAprstate()) &&
                    //mContext.getResources().getString(R.string.txt_approval_status_progress).equals(readData.getApprDetailItem().getStatus())) {
                    // 1. 개인결재함 & 미결함(P) 1) aprstate == ‘A04002’ and activity == ‘진행’
                    lay_bottom_menu.setVisibility(View.VISIBLE);
                    setVisibility(View.VISIBLE);
                    if ("0".equals(readData.getApprDetailItem().getMisform())) {
                        //1) misform == 0 > 가결,부결,반송
                        btn_bottom_menu_1.setVisibility(View.VISIBLE);
                        btn_bottom_menu_2.setVisibility(View.VISIBLE);
                        btn_bottom_menu_3.setVisibility(View.VISIBLE);
                        btn_bottom_menu_4.setVisibility(View.GONE);
                        btn_bottom_menu_5.setVisibility(View.GONE);
                        //가결
                        iv_bottom_menu_div_1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccept));
                        iv_bottom_menu_icon_1.setBackgroundResource(R.drawable.menu_icon_01);
                        tv_bottom_menu_title_1.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_1));
                        //부결
                        iv_bottom_menu_div_2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_pink));
                        iv_bottom_menu_icon_2.setBackgroundResource(R.drawable.menu_icon_02);
                        tv_bottom_menu_title_2.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_2));
                        //반송
                        iv_bottom_menu_div_3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower));
                        iv_bottom_menu_icon_3.setBackgroundResource(R.drawable.menu_icon_03);
                        tv_bottom_menu_title_3.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_3));
                    } else if ("9".equals(readData.getApprDetailItem().getMisform())) {
                        setVisibility(View.VISIBLE);
                        //2) misform == 9 > 가결,부결
                        btn_bottom_menu_1.setVisibility(View.VISIBLE);
                        btn_bottom_menu_2.setVisibility(View.VISIBLE);
                        btn_bottom_menu_3.setVisibility(View.GONE);
                        btn_bottom_menu_4.setVisibility(View.GONE);
                        btn_bottom_menu_5.setVisibility(View.GONE);
                        //가결
                        iv_bottom_menu_div_1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower_two));
                        iv_bottom_menu_icon_1.setBackgroundResource(R.drawable.menu_icon_01);
                        tv_bottom_menu_title_1.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_1));
                        //부결
                        iv_bottom_menu_div_2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_pink));
                        iv_bottom_menu_icon_2.setBackgroundResource(R.drawable.menu_icon_02);
                        tv_bottom_menu_title_2.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_2));

                    }
                } else if (APPROVAL_HOOGYUL.equalsIgnoreCase(category)) {
                    //2. 개인결재함 & 후결함(?) > 후결승인
                    setVisibility(View.VISIBLE);
                    lay_bottom_menu.setVisibility(View.VISIBLE);
                    btn_bottom_menu_1.setVisibility(View.VISIBLE);
                    btn_bottom_menu_2.setVisibility(View.GONE);
                    btn_bottom_menu_3.setVisibility(View.GONE);
                    btn_bottom_menu_4.setVisibility(View.GONE);
                    btn_bottom_menu_5.setVisibility(View.GONE);
                    //가결
                    iv_bottom_menu_div_1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower_two));
                    iv_bottom_menu_icon_1.setBackgroundResource(R.drawable.menu_icon_01);
                    tv_bottom_menu_title_1.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_8));
                } else if (APPROVAL_MICHULI.equalsIgnoreCase(category)) {
                    //3. 부서결재함 & 미처리함(앞의 3자리가 004)
                    boolean isMenuView = false;
//                if (mContext.getResources().getString(R.string.txt_approval_status_no_accept).equals(readData.getApprDetailItem().getStatus())) {
//                    //1) status == ‘미접수’
//                    lay_bottom_menu.setVisibility(View.VISIBLE);
//                    isMenuView = true;
//                } else if (mContext.getResources().getString(R.string.txt_approval_status_hold_off).equals(readData.getApprDetailItem().getStatus())
//                        && userId.equals(readData.getApprDetailItem().getProcessorid())) {
//                    // 2) status == ‘보류’ and processorid == 사용자id
//                    lay_bottom_menu.setVisibility(View.VISIBLE);
//                    isMenuView = true;
//                }
                    isMenuView = true;
                    if (isMenuView) {
                        lay_bottom_menu.setVisibility(View.VISIBLE);
                        setVisibility(View.VISIBLE);
                        String state = readData.getApprDetailItem().getState();
                        if ("A02005".equals(state) || ("A02012".equals(state))) {
                            //1) state == ‘A02005’ > 가결,부결,반송,보류,접수자전결
                            //2) state == ‘A02012’ > 가결,부결,반송,보류,접수자전결
                            btn_bottom_menu_1.setVisibility(View.VISIBLE);
                            btn_bottom_menu_2.setVisibility(View.VISIBLE);
                            btn_bottom_menu_3.setVisibility(View.VISIBLE);
                            btn_bottom_menu_4.setVisibility(View.VISIBLE);
                            btn_bottom_menu_5.setVisibility(View.VISIBLE);
                            //가결
                            iv_bottom_menu_div_1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower_two));
                            iv_bottom_menu_icon_1.setBackgroundResource(R.drawable.menu_icon_01);
                            tv_bottom_menu_title_1.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_1));
                            //부결
                            iv_bottom_menu_div_2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red_pink));
                            iv_bottom_menu_icon_2.setBackgroundResource(R.drawable.menu_icon_02);
                            tv_bottom_menu_title_2.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_2));
                            //반송
                            iv_bottom_menu_div_3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower));
                            iv_bottom_menu_icon_3.setBackgroundResource(R.drawable.menu_icon_03);
                            tv_bottom_menu_title_3.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_3));
                            //보류
                            iv_bottom_menu_div_4.setBackgroundColor(ContextCompat.getColor(mContext, R.color.teal));
                            iv_bottom_menu_icon_4.setBackgroundResource(R.drawable.menu_icon_04);
                            tv_bottom_menu_title_4.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_4));
                            //접수자전결
                            iv_bottom_menu_div_5.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                            iv_bottom_menu_icon_5.setBackgroundResource(R.drawable.menu_icon_05);
                            tv_bottom_menu_title_5.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_5));

                        } else if ("A02011".equals(state)) {
                            //3) state == ‘A02011’ > 결재상신,공문반송,접수자전결
                            btn_bottom_menu_1.setVisibility(View.VISIBLE);
                            btn_bottom_menu_2.setVisibility(View.VISIBLE);
                            btn_bottom_menu_3.setVisibility(View.VISIBLE);
                            btn_bottom_menu_4.setVisibility(View.GONE);
                            btn_bottom_menu_5.setVisibility(View.GONE);
                            //결재상신
                            iv_bottom_menu_div_1.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bright_orange));
                            iv_bottom_menu_icon_1.setBackgroundResource(R.drawable.menu_icon_06);
                            tv_bottom_menu_title_1.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_6));
                            //공문반송
                            iv_bottom_menu_div_2.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cornflower));
                            iv_bottom_menu_icon_2.setBackgroundResource(R.drawable.menu_icon_03);
                            tv_bottom_menu_title_2.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_7));
                            //접수자전결
                            iv_bottom_menu_div_3.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));
                            iv_bottom_menu_icon_3.setBackgroundResource(R.drawable.menu_icon_05);
                            tv_bottom_menu_title_3.setText(mContext.getResources().getString(R.string.txt_approval_bottom_menu_5));
                        }
                    }

//                /**
//                 * 우상단 결재선 버튼 노출
//                 * 버튼종류가 3.3)일 경우 노출
//                 * 3. 부서결재함 & 미처리함(앞의 3자리가 004)
//                 * 3) state == ‘A02011’ > 결재상신,공문반송,접수자전결
//                 * */
//                //apporvalPlag
//
//                if ("A02011".equals(readData.getApprDetailItem().getState())) {
//                    if (mInterface != null) {
//                        mInterface.setApprovalLineView(true);
//                    }
//                }
                    //결재 버튼 노출 = getApporvalPlag가 Y일때
                    if (mInterface != null) {
                        //if ("Y".equalsIgnoreCase(readData.getApprDetailItem().getApporvalPlag()) && "A02011".equals(readData.getApprDetailItem().getState())) {
                        if (APPROVAL_MICHULI.equalsIgnoreCase(category) && "A02011".equals(readData.getApprDetailItem().getState())) {
                            mInterface.setApprovalLineView(true);
                        } else {
                            mInterface.setApprovalLineView(false);
                        }
                    }
                    //   Log.d(TAG, "#### state:" + readData.getApprDetailItem().getState() + "  getStatus:" + readData.getApprDetailItem().getStatus() + "   getProcessorid:" + readData.getApprDetailItem().getProcessorid());
                }
            }
        }
    }

    public void setApprovalLineOk(boolean lineOk) {
        isApprovalLineOk = lineOk;
    }


    private void approvalProcess(String title) {
        boolean isCommentMust = true;//의견 필수, 선택
        String temp = ""; //가결,결재상신 = 1, 반송,공문반송 =2, 부결 =3, 참조 = 4(미사용!!, detail에서 bChamjo 보내는걸로 변경)
        if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_1))) {
            //가결
            isCommentMust = false;
            temp = "1";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_2))) {
            //부결
            temp = "3";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_3))) {
            //반송
            temp = "2";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_4))) {
            //보류
            temp = "5";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_5))) {
            //접수자전결
            isCommentMust = false;
            temp = "6";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_6))) {
            //결재상신
            isCommentMust = false;
            temp = "1";
            //결재선 상신시
            if (!isApprovalLineOk) {
                TextDialog dialog = TextDialog.newInstance("", mContext.getResources().getString(R.string.txt_approval_line_check), mContext.getResources().getString(R.string.txt_alert_confirm));
                dialog.setCancelable(false);
                dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        //결재선 확인으로 이동
                        if (mInterface != null) mInterface.onClickApprovalLine();
                    }
                });
                dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
                return;
            }
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_7))) {
            //공문반송
            temp = "2";
        } else if (title.equals(mContext.getResources().getString(R.string.txt_approval_bottom_menu_8))) {
            //후결승인
            isCommentMust = false;
            temp = "1"; //??  //test 임시로 가결로 넣었음. 추후 후결승인에 맞는 actionCode 필요
        }
        /**
         * 의견 필수: 부결/반송/보류/결재상신/공문반송
         * 의견 옵션: 가결, 접수자전결
         * */
        String actionCode = temp;
        CommentDialog dialog = CommentDialog.newInstance();
        dialog.setData(title, title, isCommentMust);
        dialog.setInterface(new CommentDialog.CommentDialogListener() {

            @Override
            public void getMessage(String comment) {
                //send message..
                //interface send...
                if (mInterface != null) {
                    mInterface.onSendMessage(title, actionCode, comment);
                }
            }
        });
        dialog.show(((ApprovalDetailActivity) mContext).getSupportFragmentManager());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bottom_menu_1:
                approvalProcess(tv_bottom_menu_title_1.getText().toString());
                break;
            case R.id.btn_bottom_menu_2:
                approvalProcess(tv_bottom_menu_title_2.getText().toString());
                break;
            case R.id.btn_bottom_menu_3:
                approvalProcess(tv_bottom_menu_title_3.getText().toString());
                break;
            case R.id.btn_bottom_menu_4:
                approvalProcess(tv_bottom_menu_title_4.getText().toString());
                break;
            case R.id.btn_bottom_menu_5:
                approvalProcess(tv_bottom_menu_title_5.getText().toString());
                break;
        }
    }
}
