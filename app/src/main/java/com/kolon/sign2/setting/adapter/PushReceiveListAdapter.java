package com.kolon.sign2.setting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.R;
import com.kolon.sign2.network.NetworkPresenter;
import com.kolon.sign2.setting.SettingPushAlarm;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.AlarmListResultVO;
import com.kolon.sign2.vo.CommonResultVO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sunho_kim on 2020-02-20.
 */

public class PushReceiveListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private PushReceiveListAdapter mInstancd;
    private ArrayList<AlarmListResultVO.List> mPushReceiveArrayList;
    private Context mContext;
    private SharedPreferenceManager mPref;
    private Res_AP_IF_101_VO.result userAuthInfo;

    public PushReceiveListAdapter(Context context, ArrayList<AlarmListResultVO.List> list, SharedPreferenceManager pref) {
        mContext = context;
        mPushReceiveArrayList = list;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPref = pref;

        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());
    }

    public PushReceiveListAdapter getInstance(Context context, ArrayList<AlarmListResultVO.List> list, SharedPreferenceManager pref) {
        if (mInstancd == null) {
            mInstancd = new PushReceiveListAdapter(context, list, pref);
        }
        return mInstancd;
    }

    public int getCount() {
        return userAuthInfo.getSysArray().size();
    }

    public Object getItem(int position) {
        return userAuthInfo.getSysArray().get(position);
    }

    public long getItemId(int arg0) {
        return 0;
    }

    static class ViewHolder {
        TextView mAppNameTextView;
        ToggleButton mPushReceiveSwitch;
        ImageView mIconImg;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vHolder = null;//= new ViewHolder();
        if (convertView == null) {
            vHolder = new ViewHolder();
            v = mInflater.inflate(R.layout.push_receive_list_item, parent, false);
            vHolder.mAppNameTextView = (TextView)v.findViewById(R.id.tv_push_receive_app_name);
            vHolder.mPushReceiveSwitch = (ToggleButton) v.findViewById(R.id.tbtn_push_receive);
            vHolder.mIconImg = (ImageView) v.findViewById(R.id.iv_push_icon);

            CommonUtils.textSizeSetting(mContext, vHolder.mAppNameTextView);

            v.setTag(vHolder);
        } else {
            vHolder = (ViewHolder)v.getTag();
        }
        String appName = userAuthInfo.getSysArray().get(position).getSysName();
        vHolder.mAppNameTextView.setText(appName);
        Glide.with(mContext).load(userAuthInfo.getSysArray().get(position).getSysIcon()).into(vHolder.mIconImg);

        vHolder.mPushReceiveSwitch.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  String isPushYN = "";

                  if ("Y".equalsIgnoreCase(userAuthInfo.getSysArray().get(position).getSysPushYn())) {
                      isPushYN = "N";
                  } else {
                      isPushYN = "Y";
                  }

                  updatePushYN(userAuthInfo.getSysArray().get(position).getSysId(), isPushYN, position);
              }
          });
        if ("Y".equalsIgnoreCase(userAuthInfo.getSysArray().get(position).getSysPushYn())) {
            vHolder.mPushReceiveSwitch.setChecked(true);
        } else {
            vHolder.mPushReceiveSwitch.setChecked(false);
        }

        return v;
    }

    private void updatePushYN(String sysId, final String isPushYN, final int position){
        ((SettingPushAlarm)mContext).showProgressBar();
        NetworkPresenter presenter = new NetworkPresenter();
        HashMap hm = new HashMap();
        hm.put("dvcId", mPref.getStringPreference(Constants.PREF_DEVICE_ID));
        hm.put("pushYn", isPushYN);
        hm.put("userId", mPref.getStringPreference(Constants.PREF_USER_ID));
        hm.put("sysId", sysId);
        String timeStamp = CommonUtils.getTimeStamp();
        try {
            String auth = CipherUtils.encrypt(timeStamp, BuildConfig.r_key);
            presenter.registerPushSetting(auth, timeStamp, hm, new NetworkPresenter.registerPushSettingListener() {
                @Override
                public void onResponse(CommonResultVO result) {
                    ((SettingPushAlarm)mContext).hideProgressBar();
                    String errMsg = "";
                    if (result != null) {
                        if ("200".equals(result.getStatus().getStatusCd())) {
                            if ("S".equalsIgnoreCase(result.getResult().getErrorCd())) {
                                userAuthInfo.getSysArray().get(position).setSysPushYn(isPushYN);
                                notifyDataSetChanged();
                                return;
                            } else {
                                errMsg = result.getResult().getErrorMsg();
                            }
                        } else {
                            errMsg = result.getStatus().getStatusCd() + "\n" + result.getStatus().getStatusMssage();
                        }
                    } else {
                        errMsg = mContext.getResources().getString(R.string.txt_network_error);
                    }
                    notifyDataSetChanged();
                    Toast.makeText(mContext, errMsg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            ((SettingPushAlarm)mContext).hideProgressBar();
            notifyDataSetChanged();
        }
    }

}
