package com.kolon.sign2.dynamic.viewmodel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.activity.MainActivity;
import com.kolon.sign2.network.NetworkConstants;
import com.kolon.sign2.network.ServerInterface;
import com.kolon.sign2.utils.BaseViewModel;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DynamicListViewModel extends BaseViewModel {
    private MutableLiveData<ArrayList<Res_AP_IF_103_VO.dynamicListList>> rvLiveData = new MutableLiveData<>();

    public MutableLiveData<ArrayList<Res_AP_IF_103_VO.dynamicListList>> getRvLiveData() {
        if (rvLiveData == null) {
            rvLiveData = new MutableLiveData<>();
        }
        return rvLiveData;
    }


    //TODO : Server interface 연결시 인터페이스로 확인해볼 것
    public void call_IF_103(Context context, String userId, String sysId, String menuId) {
        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("sysId", sysId);
        hm.put("menuId", menuId);
        //((MainActivity) context).showProgressBar();

        String timeStamp = CommonUtils.getTimeStamp();
        String auth = "";
        try {
            CipherUtils.encrypt(timeStamp, BuildConfig.r_key);
        } catch (Exception e) {e.printStackTrace();}

        Call<Res_AP_IF_103_VO> c = getServerInterface().AP_IF_103(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_103_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_103_VO> call, Response<Res_AP_IF_103_VO> response) {
                //((MainActivity) context).hideProgressBar();
                Log.d(this.getClass().getSimpleName(), "#### Res_AP_IF_103_VO res:" + new Gson().toJson(response.body()));
                if (null != response && null != response.body()) {
                    if (null != response.body().getResult()) {
                        if (response.body().getResult().getErrorCd().equalsIgnoreCase("S")) {
                            ArrayList<Res_AP_IF_103_VO.dynamicListList> ll = response.body().getResult().getDynamicListList();
                            rvLiveData.setValue(ll);
                        } else {
                            ArrayList<Res_AP_IF_103_VO.dynamicListList> ll = response.body().getResult().getDynamicListList();
                            rvLiveData.setValue(ll);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Res_AP_IF_103_VO> call, Throwable t) {
                //((MainActivity) context).hideProgressBar();
            }
        });
    }

    private ServerInterface getServerInterface() {
        String url = BuildConfig.MainURL;
        Retrofit retrofit = new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerInterface service = retrofit.create(ServerInterface.class);

        return service;
    }
}
