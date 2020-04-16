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

    private int pageSize = 10;//한번에 보여주는 사이즈
    private int pageNum = 0; //0, 1 ,2 ,3 이 아니라 0 20 40 등 시작페이지이고 pageSize만큼 더해저야함.

    public MutableLiveData<ArrayList<Res_AP_IF_103_VO.dynamicListList>> getRvLiveData() {
        if (rvLiveData == null) {
            rvLiveData = new MutableLiveData<>();
        }
        return rvLiveData;
    }

    public boolean loadingMore = false;

    //TODO : Server interface 연결시 인터페이스로 확인해볼 것
    public void call_IF_103(Context context, String userId, String sysId, String menuId, String loadingType) {
        if (loadingType != "loadingMore")
            pageNum = 0;

        HashMap hm = new HashMap();
        hm.put("userId", userId);
        hm.put("sysId", sysId);
        hm.put("menuId", menuId);

        hm.put("pageNum", pageNum); //page num
        hm.put("pageCnt", pageSize);//page size

        if (loadingType == "loadingMore") {
            if (rvLiveData.getValue().size() < pageNum) { //페이지숫자보다 데이터가 적은경우 데이터를 모두 가져온것으로 판단하고 리턴
                return;
            }
            ((MainActivity) context).showProgressBar();
            pageNum = pageNum + pageSize;//처음0 그다음 pageSize만큼 증가 20 , 40, 60...
        }

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
                ((MainActivity) context).hideProgressBar();
                Log.d(this.getClass().getSimpleName(), "#### Res_AP_IF_103_VO res:" + new Gson().toJson(response.body()));
                if (null != response && null != response.body()) {
                    if (null != response.body().getResult()) {
                        if (response.body().getResult().getErrorCd().equalsIgnoreCase("S")) {
                            ArrayList<Res_AP_IF_103_VO.dynamicListList> ll = response.body().getResult().getDynamicListList();
                            if (loadingType == "loadingMore") {
                                ArrayList<Res_AP_IF_103_VO.dynamicListList> tmpList = rvLiveData.getValue();
                                tmpList.addAll(ll);
                                rvLiveData.postValue(tmpList);
                            } else
                                rvLiveData.setValue(ll);

                        } else {//
                            ArrayList<Res_AP_IF_103_VO.dynamicListList> ll = response.body().getResult().getDynamicListList();
                            rvLiveData.setValue(ll);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Res_AP_IF_103_VO> call, Throwable t) {
                ((MainActivity) context).hideProgressBar();
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
