package com.kolon.sign2.network;

import android.util.Log;

import com.google.gson.Gson;
import com.kolon.sign2.BuildConfig;
import com.kolon.sign2.utils.CipherUtils;
import com.kolon.sign2.utils.CommonUtils;
import com.kolon.sign2.vo.AlarmListResultVO;
import com.kolon.sign2.vo.CommonResultVO;
import com.kolon.sign2.vo.Req_AP_IF_022_VO;
import com.kolon.sign2.vo.Req_AP_IF_039_VO;
import com.kolon.sign2.vo.Req_AP_IF_108_VO;
import com.kolon.sign2.vo.Req_AP_IF_205_VO;
import com.kolon.sign2.vo.Res_AP_DynamicAction_VO;
import com.kolon.sign2.vo.Res_AP_Empty_VO;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;
import com.kolon.sign2.vo.Res_AP_IF_004_VO;
import com.kolon.sign2.vo.Res_AP_IF_018_VO;
import com.kolon.sign2.vo.Res_AP_IF_019_VO;
import com.kolon.sign2.vo.Res_AP_IF_020_VO;
import com.kolon.sign2.vo.Res_AP_IF_021_VO;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_014_VO;
import com.kolon.sign2.vo.Res_AP_IF_016_VO;
import com.kolon.sign2.vo.Res_AP_IF_022_VO;
import com.kolon.sign2.vo.Res_AP_IF_028_VO;
import com.kolon.sign2.vo.Res_AP_IF_032_VO;
import com.kolon.sign2.vo.Res_AP_IF_033_VO;
import com.kolon.sign2.vo.Res_AP_IF_035_VO;
import com.kolon.sign2.vo.Res_AP_IF_036_VO;
import com.kolon.sign2.vo.Res_AP_IF_037_VO;
import com.kolon.sign2.vo.Res_AP_IF_040_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_104_VO;
import com.kolon.sign2.vo.Res_AP_IF_107_VO;
import com.kolon.sign2.vo.Res_AP_IF_201_VO;
import com.kolon.sign2.vo.Res_AP_IF_202_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;
import com.kolon.sign2.vo.Res_AP_IF_204_VO;
import com.kolon.sign2.vo.Res_AP_IF_205_VO;
import com.kolon.sign2.vo.Res_AP_IF_023_VO;
import com.kolon.sign2.vo.Res_AP_IF_102_VO;
import com.kolon.sign2.vo.Res_Doc_Viewer;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 서버 데이터 요청
 */
public class NetworkPresenter {

    private final String TAG = "NetworkPresenter";

    public interface getCommonListener {
        void onResponse(Res_AP_Empty_VO result);
    }


    /**
     * 디바이스 저장
     */
    public interface getRegisterDeviceInfoListener {
        void onResponse(CommonResultVO result);
    }

    public void registerDeviceInfo(String auth, String timeStamp, HashMap<String, String> hm, getRegisterDeviceInfoListener listener) {
        Call<CommonResultVO> c = getPushServerInterface().AP_IF_007(auth, timeStamp, hm);
        c.enqueue(new Callback<CommonResultVO>() {
            @Override
            public void onResponse(Call<CommonResultVO> call, Response<CommonResultVO> response) {
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<CommonResultVO> call, Throwable t) {
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * Push 수신저장
     */
    public interface registerPushSettingListener {
        void onResponse(CommonResultVO result);
    }

    public void registerPushSetting(String auth, String timeStamp, HashMap<String, String> hm, registerPushSettingListener listener) {
        Call<CommonResultVO> c = getPushServerInterface().AP_IF_008(auth, timeStamp, hm);
        c.enqueue(new Callback<CommonResultVO>() {
            @Override
            public void onResponse(Call<CommonResultVO> call, Response<CommonResultVO> response) {
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<CommonResultVO> call, Throwable t) {
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * Push수신여부 조회
     */
    public interface getPushSettingListener {
        void onResponse(AlarmListResultVO result);
    }

    public void getPushSettingList(String auth, String timeStamp, String dvcId, getPushSettingListener listener) {
        Log.d(TAG, "#### getPushSettingList in auth:" + auth+" timeStamp:"+timeStamp+" dvcId:"+dvcId);
        Call<AlarmListResultVO> c = getPushServerInterface().AP_IF_009(auth, timeStamp, dvcId);
        c.enqueue(new Callback<AlarmListResultVO>() {
            @Override
            public void onResponse(Call<AlarmListResultVO> call, Response<AlarmListResultVO> response) {
                Log.d(TAG, "#### getPushSettingList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<AlarmListResultVO> call, Throwable t) {
                Log.d(TAG, "#### getPushSettingList err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 메인리스트 조회(홈 화면)
     */
    public interface getMainListResult {
        void onResponse(Res_AP_IF_002_VO result);
    }

    public void getMainList(HashMap<String, String> hm, getMainListResult listener) {
        Log.d(TAG, "#### getMainList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_002_VO> c = getServerInterface().AP_IF_002(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_002_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_002_VO> call, Response<Res_AP_IF_002_VO> response) {
                Log.d(TAG, "#### getMainList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_002_VO> call, Throwable t) {
                Log.d(TAG, "#### getMainList err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 멀티유저조회
     */
    public interface getMultiUserResult {
        void onResponse(Res_AP_IF_004_VO result);
    }

    public void getMultiUserList(HashMap<String, String> hm, getMultiUserResult listener) {
        Log.d(TAG, "#### getMultiUserList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_004_VO> c = getServerInterface().AP_IF_004(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_004_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_004_VO> call, Response<Res_AP_IF_004_VO> response) {
                Log.d(TAG, "#### getMultiUserList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_004_VO> call, Throwable t) {
                Log.d(TAG, "#### getMultiUserList err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 멀티유저등록
     */
    public void getMultiUserInsert(HashMap<String, String> hm, getCommonListener listener) {
        Log.d(TAG, "#### getMultiUserInsert in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_005(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getMultiUserInsert res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getMultiUserInsert err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 멀티유저삭제
     */
    public void getMultiUserDelete(HashMap<String, String> hm, getCommonListener listener) {
        Log.d(TAG, "#### getMultiUserDelete in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_006(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getMultiUserDelete res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getMultiUserDelete err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 통합결재 사용자권한 MDM
     */
    public interface getUserAuthSearchResult {
        void onResponse(Res_AP_IF_101_VO result);
    }

    public void getUserAuthSearch(HashMap<String, String> hm, getUserAuthSearchResult listener) {
        Log.d(TAG, "#### getUserAuthSearch in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;
        /*
        try {
            CipherUtils.encrypt(timeStamp, BuildConfig.r_key);
        } catch (Exception e) {e.printStackTrace();}
        */
        Call<Res_AP_IF_101_VO> c = getServerInterface().AP_IF_101(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_101_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_101_VO> call, Response<Res_AP_IF_101_VO> response) {
                Log.d(TAG, "#### getUserAuthSearch res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_101_VO> call, Throwable t) {
                Log.d(TAG, "#### getUserAuthSearch err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 통합결재 시스템메뉴조회
     */
    public interface getSystemMenuResult {
        void onResponse(Res_AP_IF_102_VO result);
    }

    public void getSystemMenu(HashMap<String, String> hm, getSystemMenuResult listener) {
        Log.d(TAG, "#### getSystemMenu in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_102_VO> c = getServerInterface().AP_IF_102(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_102_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_102_VO> call, Response<Res_AP_IF_102_VO> response) {
                Log.d(TAG, "#### getSystemMenu res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_102_VO> call, Throwable t) {
                Log.d(TAG, "#### getSystemMenu err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 회사 코드 조회
     * */
    public interface getUserCompSearchResult {
        void onResponse(Res_AP_IF_107_VO result);
    }
    public void getUserCompSearch(HashMap<String, String> hm, getUserCompSearchResult listener) {
        Log.d(TAG, "#### getUserCompSearch in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_107_VO> c = getServerInterface().AP_IF_107(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_107_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_107_VO> call, Response<Res_AP_IF_107_VO> response) {
                Log.d(TAG, "#### getUserCompSearch res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_107_VO> call, Throwable t) {
                Log.d(TAG, "#### getUserCompSearch res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 메뉴순서전송
     * */
    public void getUpdateSysOrderMgt(Req_AP_IF_108_VO req, getCommonListener listener){
        Log.d(TAG, "#### getUpdateSysOrderMgt in:" + new Gson().toJson(req));

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_108(auth, timeStamp, req);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getUpdateSysOrderMgt res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getUpdateSysOrderMgt err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }



    /**
     * 전자결재 부서문서함목록조회
     */
//    public interface getApprovalDeptListResult {
//        void onResponse(Res_AP_IF_012_VO result);
//    }
//
//    public void getApprovalDeptList(HashMap<String, String> hm, getApprovalDeptListResult listener) {
//        Log.d(TAG, "#### getApprovalDeptList in:" + hm);
//        Call<Res_AP_IF_012_VO> c = getServerInterface().AP_IF_012(hm);
//        c.enqueue(new Callback<Res_AP_IF_012_VO>() {
//            @Override
//            public void onResponse(Call<Res_AP_IF_012_VO> call, Response<Res_AP_IF_012_VO> response) {
//                Log.d(TAG, "#### getApprovalDeptList res:" + new Gson().toJson(response.body()));
//                if (listener != null) listener.onResponse(response.body());
//            }
//
//            @Override
//            public void onFailure(Call<Res_AP_IF_012_VO> call, Throwable t) {
//                Log.d(TAG, "#### getApprovalDeptList err:" + t.getMessage());
//                if (listener != null) listener.onResponse(null);
//            }
//        });
//    }

    /**
     * 전자결재 목록요청
     */
    public interface getApprovalListResult {
        void onResponse(Res_AP_IF_013_VO result);
    }

    public void getApprovalList(HashMap<String, String> hm, getApprovalListResult listener) {
        Log.d(TAG, "#### getApprovalList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_013_VO> c = getServerInterface().AP_IF_013(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_013_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_013_VO> call, Response<Res_AP_IF_013_VO> response) {
                Log.d(TAG, "#### getApprovalList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_013_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalList err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 상세요청
     */
    public interface getApprovalDetailResult {
        void onResponse(Res_AP_IF_016_VO result);
    }

    public void getApprovalDetail(HashMap<String, String> hm, getApprovalDetailResult listener) {
        Log.d(TAG, "#### getApprovalDetail in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_016_VO> c = getServerInterface().AP_IF_016(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_016_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_016_VO> call, Response<Res_AP_IF_016_VO> response) {
                Log.d(TAG, "#### getApprovalDetail res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_016_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalDetail err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 전자결재 결재함검색
     */
    public interface getApprovalSearchResult {
        void onResponse(Res_AP_IF_020_VO result);
    }

    public void getApprovalSearch(HashMap<String, String> hm, getApprovalSearchResult listener) {
        Log.d(TAG, "#### getApprovalSearch in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_020_VO> c = getServerInterface().AP_IF_020(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_020_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_020_VO> call, Response<Res_AP_IF_020_VO> response) {
                Log.d(TAG, "#### getApprovalSearch res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_020_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalSearch err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 결재처리
     */
    public interface getApprovalProcessingResult {
        void onResponse(Res_AP_IF_014_VO result);
    }

    public void getApprovalProceesing(HashMap<String, String> hm, getApprovalProcessingResult listener) {
        Log.d(TAG, "#### getApprovalProceesing in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_014_VO> c = getServerInterface().AP_IF_014(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_014_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_014_VO> call, Response<Res_AP_IF_014_VO> response) {
                Log.d(TAG, "#### getApprovalProceesing res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_014_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalProceesing err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 본문보기
     */
    public interface getApprovalFormUrlListener {
        void onResponse(Res_AP_IF_018_VO result);
    }

    public void getApprovalFormUrl(HashMap hm, getApprovalFormUrlListener listener) {
        Log.d(TAG, "#### getApprovalFormUrl in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_018_VO> c = getServerInterface().AP_IF_018(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_018_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_018_VO> call, Response<Res_AP_IF_018_VO> response) {
                Log.d(TAG, "#### getApprovalFormUrl res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_018_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalFormUrl err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 첨부파일조회
     */
    public interface getApprovalAttachsListener {
        void onResponse(Res_AP_IF_019_VO result);
    }

    public void getApprovalAttachs(HashMap hm, getApprovalAttachsListener listener) {
        Log.d(TAG, "#### getApprovalAttachs in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_019_VO> c = getServerInterface().AP_IF_019(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_019_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_019_VO> call, Response<Res_AP_IF_019_VO> response) {
                Log.d(TAG, "#### getApprovalAttachs res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_019_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalAttachs err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 전자결재 개인,부서함 미결건수
     */
    public interface getApprovalCountAllListener {
        void onResponse(Res_AP_IF_021_VO result);
    }

    public void getApprovalCountAll(HashMap<String, String> hm, getApprovalCountAllListener listener) {
        Log.d(TAG, "#### getApprovalCountAll in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_021_VO> c = getServerInterface().AP_IF_021(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_021_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_021_VO> call, Response<Res_AP_IF_021_VO> response) {
                Log.d(TAG, "#### getApprovalCountAll res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_021_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalCountAll err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 전자결재 결재선저장
     */
    public interface getApprovalLineSaveListener {
        void onResponse(Res_AP_IF_022_VO result);
    }

    public void getApprovalLineSave(Req_AP_IF_022_VO req, getApprovalLineSaveListener listener) {
        Log.d(TAG, "#### getApprovalLineSave in:" + new Gson().toJson(req));

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_022_VO> c = getServerInterface().AP_IF_022(auth, timeStamp, req);
        c.enqueue(new Callback<Res_AP_IF_022_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_022_VO> call, Response<Res_AP_IF_022_VO> response) {
                Log.d(TAG, "#### getApprovalLineSave res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_022_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalLineSave err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 상위조직도조회
     */
    public interface getApprovalSpparentsListener {
        void onResponse(Res_AP_IF_023_VO result);
    }

    public void getApprovalSpparents(HashMap<String, String> hm, getApprovalSpparentsListener listener) {
        Log.d(TAG, "#### getApprovalSpparents in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_023_VO> c = getServerInterface().AP_IF_023(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_023_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_023_VO> call, Response<Res_AP_IF_023_VO> response) {
                Log.d(TAG, "#### getApprovalSpparents res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_023_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalSpparents err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 회사코드전체조회
     */
    public interface getApprovalListcompsListener {
        void onResponse(Res_AP_IF_201_VO result);
    }

    public void getApprovalListcomps(HashMap<String, String> hm, getApprovalListcompsListener listener) {
        Log.d(TAG, "#### getApprovalListcomps in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_201_VO> c = getServerInterface().AP_IF_023_1(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_201_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_201_VO> call, Response<Res_AP_IF_201_VO> response) {
                Log.d(TAG, "#### getApprovalListcomps res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_201_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalListcomps err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 조직도조회
     */
    public interface getApprovalSporganizationApprovalListener {
        void onResponse(Res_AP_IF_202_VO result);
    }

    public void getApprovalSporganizationApproval(HashMap<String, String> hm, getApprovalSporganizationApprovalListener listener) {
        Log.d(TAG, "#### getApprovalSporganizationApproval in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_202_VO> c = getServerInterface().AP_IF_023_2(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_202_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_202_VO> call, Response<Res_AP_IF_202_VO> response) {
                Log.d(TAG, "#### getApprovalSporganizationApproval res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_202_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalSporganizationApproval err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 조직원목록
     */
    public interface getApprovalSplistApprovalListener {
        void onResponse(Res_AP_IF_203_VO result);
    }

    public void getApprovalSplistApproval(HashMap<String, String> hm, getApprovalSplistApprovalListener listener) {
        Log.d(TAG, "#### getApprovalSplistApproval in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_203_VO> c = getServerInterface().AP_IF_023_3(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_203_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_203_VO> call, Response<Res_AP_IF_203_VO> response) {
                Log.d(TAG, "#### getApprovalSplistApproval res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_203_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalSplistApproval err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 임직원검색
     */
    public interface getApprovalSpsearchLineinfoListener {
        void onResponse(Res_AP_IF_204_VO result);
    }

    public void getApprovalSpsearchLineinfo(HashMap<String, String> hm, getApprovalSpsearchLineinfoListener listener) {
        Log.d(TAG, "#### getApprovalSpsearchLineinfo in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_204_VO> c = getServerInterface().AP_IF_023_4(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_204_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_204_VO> call, Response<Res_AP_IF_204_VO> response) {
                Log.d(TAG, "#### getApprovalSpsearchLineinfo res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_204_VO> call, Throwable t) {
                Log.d(TAG, "##### getApprovalSpsearchLineinfo err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 전자결재 최근조회검색
     */
    public interface getApprovalLatelyLineinfoListener {
        void onResponse(Res_AP_IF_205_VO result);
    }

    public void getApprovalLatelyLineinfo(Req_AP_IF_205_VO input, getApprovalLatelyLineinfoListener listener) {
        Log.d(TAG, "#### getApprovalLatelyLineinfo in:" + new Gson().toJson(input));

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_205_VO> c = getServerInterface().AP_IF_023_5(auth, timeStamp, input);
        c.enqueue(new Callback<Res_AP_IF_205_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_205_VO> call, Response<Res_AP_IF_205_VO> response) {
                Log.d(TAG, "#### getApprovalLatelyLineinfo res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_205_VO> call, Throwable t) {
                Log.d(TAG, "#### getApprovalLatelyLineinfo err:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 -승인상세(일반 보안 IT)
     */
    public interface getServiceDeskDetailListener {
        void onResponse(Res_AP_IF_028_VO result);
    }

    public void getServiceDeskDetail(HashMap<String, String> hm, getServiceDeskDetailListener listener) {
        Log.d(TAG, "#### getServiceDeskDetail in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_028_VO> c = getServerInterface().AP_IF_028(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_028_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_028_VO> call, Response<Res_AP_IF_028_VO> response) {
                Log.d(TAG, "#### getServiceDeskDetail res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_028_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDetail res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 서비스데스크 -승인확인(일반)
     */
    public void getServiceDeskApprove(HashMap<String, String> hm, getCommonListener listener) {
        Log.d(TAG, "#### getServiceDeskApprove in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_029(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getServiceDeskApprove res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskApprove res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 서비스데스크 -승인확인(일반 보안 IT)
     */
    public void getServiceDeskBoanApprove(HashMap<String, String> hm, getCommonListener listener) {
        Log.d(TAG, "#### getServiceDeskBoanApprove in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_030(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getServiceDeskBoanApprove res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskBoanApprove res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 서비스데스크 - 승인상세(DLP 문서반출)
     */
    public interface getServiceDeskDlpDetailListener {
        void onResponse(Res_AP_IF_032_VO result);
    }

    public void getServiceDeskDlpDetail(HashMap<String, String> hm, getServiceDeskDlpDetailListener listener) {
        Log.d(TAG, "#### getServiceDeskDlpDetail in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_032_VO> c = getServerInterface().AP_IF_032(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_032_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_032_VO> call, Response<Res_AP_IF_032_VO> response) {
                Log.d(TAG, "#### getServiceDeskDlpDetail res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_032_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDlpDetail res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 승인확인(DLP 문서반출)
     */
    public interface getServiceDeskDlpApproveListener {
        void onResponse(Res_AP_IF_033_VO result);
    }

    public void getServiceDeskDlpApprove(HashMap<String, String> hm, getServiceDeskDlpApproveListener listener) {
        Log.d(TAG, "#### getServiceDeskDlpApprove in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_033_VO> c = getServerInterface().AP_IF_033(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_033_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_033_VO> call, Response<Res_AP_IF_033_VO> response) {
                Log.d(TAG, "#### getServiceDeskDlpApprove res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_033_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDlpApprove res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 승인상세(메일필터)
     */
    public interface getServiceDeskMainFilterDetailListener {
        void onResponse(Res_AP_IF_035_VO result);
    }

    public void getServiceDeskMailFilterDetail(HashMap<String, String> hm, getServiceDeskMainFilterDetailListener listener) {
        Log.d(TAG, "#### getServiceDeskMailFilterDetail in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_035_VO> c = getServerInterface().AP_IF_035(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_035_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_035_VO> call, Response<Res_AP_IF_035_VO> response) {
                Log.d(TAG, "#### getServiceDeskMailFilterDetail res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_035_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskMailFilterDetail res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 승인확인(메일필터)
     */
    public interface getServiceDeskMainFilterApproveListener {
        void onResponse(Res_AP_IF_036_VO result);
    }

    public void getServiceDeskMailFilterApprove(HashMap<String, String> hm, getServiceDeskMainFilterApproveListener listener) {
        Log.d(TAG, "#### getServiceDeskMailFilterApprove in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_036_VO> c = getServerInterface().AP_IF_036(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_036_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_036_VO> call, Response<Res_AP_IF_036_VO> response) {
                Log.d(TAG, "#### getServiceDeskMailFilterApprove res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_036_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskMailFilterApprove res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 권한목록
     */
    public interface getServiceDeskAprListListener {
        void onResponse(Res_AP_IF_037_VO result);
    }

    public void getServiceDeskAprList(HashMap<String, String> hm, getServiceDeskAprListListener listener) {
        Log.d(TAG, "#### getServiceDeskAprList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_037_VO> c = getServerInterface().AP_IF_037(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_037_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_037_VO> call, Response<Res_AP_IF_037_VO> response) {
                Log.d(TAG, "#### getServiceDeskAprList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_037_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskAprList res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 승인확인(시스템권한)
     */
    public void getServiceDeskAprApprove(Req_AP_IF_039_VO input, getCommonListener listener) {
        Log.d(TAG, "#### getServiceDeskDgtnApprove in:" + new Gson().toJson(input));

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_Empty_VO> c = getServerInterface().AP_IF_039(auth, timeStamp, input);
        c.enqueue(new Callback<Res_AP_Empty_VO>() {
            @Override
            public void onResponse(Call<Res_AP_Empty_VO> call, Response<Res_AP_Empty_VO> response) {
                Log.d(TAG, "#### getServiceDeskDgtnApprove res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_Empty_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDgtnApprove res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 서비스데스크 - 위임자조회
     */
    public interface getServiceDeskDgtnListener {
        void onResponse(Res_AP_IF_040_VO result);
    }

    public void getServiceDeskDgtnList(HashMap<String, String> hm, getServiceDeskDgtnListener listener) {
        Log.d(TAG, "#### getServiceDeskDgtnList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_040_VO> c = getServerInterface().AP_IF_040(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_040_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_040_VO> call, Response<Res_AP_IF_040_VO> response) {
                Log.d(TAG, "#### getServiceDeskDgtnList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_040_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDgtnList res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    /**
     * 동적 리스트 상세
     */
    public interface getDynamicDetailListListener {
        void onResponse(Res_AP_IF_104_VO result);
    }

    public void getDynamicDetailList(HashMap<String, String> hm, getDynamicDetailListListener listener) {
        Log.d(TAG, "#### getDynamicDetailList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_IF_104_VO> c = getServerInterface().AP_IF_104(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_IF_104_VO>() {
            @Override
            public void onResponse(Call<Res_AP_IF_104_VO> call, Response<Res_AP_IF_104_VO> response) {
                Log.d(TAG, "#### getDynamicDetailList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_IF_104_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDgtnList res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 동적 리스트 상세 버튼 액션
     */
    public interface getDynamicDetailActionListener {
        void onResponse(Res_AP_DynamicAction_VO result);
    }

    public void getDynamicDetailAction(HashMap<String, String> hm, getDynamicDetailActionListener listener) {
        Log.d(TAG, "#### getDynamicDetailList in:" + hm);

        String timeStamp = CommonUtils.getIfTimeStamp();
        String auth = BuildConfig.r_key;

        Call<Res_AP_DynamicAction_VO> c = getServerInterface().AP_IF_DynamicAction(auth, timeStamp, hm);
        c.enqueue(new Callback<Res_AP_DynamicAction_VO>() {
            @Override
            public void onResponse(Call<Res_AP_DynamicAction_VO> call, Response<Res_AP_DynamicAction_VO> response) {
                Log.d(TAG, "#### getDynamicDetailList res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_AP_DynamicAction_VO> call, Throwable t) {
                Log.d(TAG, "#### getServiceDeskDgtnList res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }

    /**
     * 문서 보기 키 조회
     */
    public interface getDocViewerKeyLisetner {
        void onResponse(Res_Doc_Viewer result);
    }

    public void getDocViewerKey(String fileUrl, String id, getDocViewerKeyLisetner listener) {
        Log.d(TAG, "#### getDocViewerKey fileUrl:" + fileUrl + " id:" + id);
        Retrofit retrofit = new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(BuildConfig.DocViewerURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerInterface service = retrofit.create(ServerInterface.class);
        Call<Res_Doc_Viewer> cl = service.getDocViewerKey("URL", fileUrl, id);
        cl.enqueue(new Callback<Res_Doc_Viewer>() {
            @Override
            public void onResponse(Call<Res_Doc_Viewer> call, Response<Res_Doc_Viewer> response) {
                Log.d(TAG, "#### getDocViewerKey res:" + new Gson().toJson(response.body()));
                if (listener != null) listener.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Res_Doc_Viewer> call, Throwable t) {
                Log.d(TAG, "#### getDocViewerKey res:" + t.getMessage());
                if (listener != null) listener.onResponse(null);
            }
        });
    }


    private ServerInterface getServerInterface() {
        String url = BuildConfig.MainURL;//"http://203.225.255.241";// "http://203.225.59.72:18080";
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerInterface service = mRetrofit.create(ServerInterface.class);

        return service;
    }

    private ServerInterface getPushServerInterface() {
        String url = BuildConfig.pushURL;
        Retrofit mRetrofit = new Retrofit.Builder()
                .client(NetworkConstants.getRequestHeader())
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerInterface service = mRetrofit.create(ServerInterface.class);

        return service;
    }
}
