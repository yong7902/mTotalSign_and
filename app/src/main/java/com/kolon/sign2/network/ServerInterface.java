package com.kolon.sign2.network;


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
import com.kolon.sign2.vo.LoginParamVO;
import com.kolon.sign2.vo.LoginResultVO;
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
import com.kolon.sign2.vo.Res_AP_IF_103_VO;
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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sunho_kim on 2019-12-02.
 */

public interface ServerInterface {

    // IF-ATT-001  BaseServer 로그인
    @Headers("Content-Type:application/json")
//    @POST("v1/user/a_MTP/g_kolon/login")
    @POST("g_kolon/login")
    Call<LoginResultVO> userLogin(
            @Header("Authorization") String autho,
            /*@Header("Timestamp") String timestamp,
            @Query("account") String account,
            @Query("password") String password*/
            @Body LoginParamVO loginParamVO);

    //디바이스 정보 갱신
    @Headers("Content-Type:application/json")
    @POST("/mTotalSignPush/devices/infos")
    Call<CommonResultVO> AP_IF_007(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                   @Body HashMap<String, String> req);
    //Push수신여부 저장
    @Headers("Content-Type:application/json")
    @POST("/mTotalSignPush/devices/pushes")
    Call<CommonResultVO> AP_IF_008(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                      @Body HashMap<String, String> req);

    //Push수신여부 조회
    @Headers("Content-Type:application/json")
    @GET("/mTotalSignPush/devices/pushes?")
    Call<AlarmListResultVO> AP_IF_009(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                      @Query("dvcId") String account);

    //메인 리스트 조회
    /*@Headers("Content-Type:application/json")
    @GET("/mkolonApps/app/main")
    Call<MainListResultVO> main(@Query("userId") String userId, @Query("platformCd") String platformCd,
                                @Query("deviceId") String deviceId, @Query("packageNm") String packageNm,
                                @Query("targetUrl") String targetUrl);*/


    //메인리스트조회
    @POST("/mTotalSign/main/mainList")
    Call<Res_AP_IF_002_VO> AP_IF_002(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //멀티유저조회
    @POST("/mTotalSign/main/multiUserLogin")
    Call<Res_AP_IF_004_VO> AP_IF_004(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //멀티유저등록
    @POST("/mTotalSign/main/multiUserInsert")
    Call<Res_AP_Empty_VO> AP_IF_005(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body HashMap<String, String> req);

    //멀티유저삭제
    @POST("/mTotalSign/main/multiUserDelete")
    Call<Res_AP_Empty_VO> AP_IF_006(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body HashMap<String, String> req);

    //통합결재 사용자권한 MDM
    @POST("/mTotalSign/main/userAuthSearch")
    Call<Res_AP_IF_101_VO> AP_IF_101(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //시스템 메뉴 조회
    @Headers("Content-Type:application/json")
    @POST("/mTotalSign/main/sysMenuList")
    Call<Res_AP_IF_102_VO> AP_IF_102(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //회사 코드 조회
    @POST("/mTotalSign/main/userCompSearch")
    Call<Res_AP_IF_107_VO> AP_IF_107(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //메뉴 순서 저장
    @POST("/mTotalSign/main/updateSysOrderMgt")
    Call<Res_AP_Empty_VO> AP_IF_108(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body Req_AP_IF_108_VO req);

    //전자결재 목록조회
    @Headers("Content-Type:application/json")
    @POST("/mTotalSign/approval/list")
    Call<Res_AP_IF_013_VO> AP_IF_013(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재 결재처리
    @Headers("Content-Type:application/json")
    @POST("/mTotalSign/approval/process")
    Call<Res_AP_IF_014_VO> AP_IF_014(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재 상세조회
    @Headers("Content-Type:application/json")
    @POST("/mTotalSign/approval/detail")
    Call<Res_AP_IF_016_VO> AP_IF_016(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재 본문보기 AP_IF_018
    @POST("/mTotalSign/approval/bodyimage")
    Call<Res_AP_IF_018_VO> AP_IF_018(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재 첨부파일 조회
    @POST("/mTotalSign/approval/attachs")
    Call<Res_AP_IF_019_VO> AP_IF_019(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재-결재함검색
    @POST("/mTotalSign/approval/listSearch")
    Call<Res_AP_IF_020_VO> AP_IF_020(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재 미결건수
    @Headers("Content-Type:application/json")
    @POST("/mTotalSign/approval/countAll")
    Call<Res_AP_IF_021_VO> AP_IF_021(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재-결재선저장
    @POST("/mTotalSign/approval/lineInfoSave")
    Call<Res_AP_IF_022_VO> AP_IF_022(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body Req_AP_IF_022_VO req);

    //전자결재-상위조직도조회
    @POST("/mTotalSign/approval/spparents")
    Call<Res_AP_IF_023_VO> AP_IF_023(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //전자결재-회사코드전체조회
    @POST("/mTotalSign/approval/listcomps")
    Call<Res_AP_IF_201_VO> AP_IF_023_1(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                       @Body HashMap<String, String> req);

    //전자결재-조직도조회
    @POST("/mTotalSign/approval/sporganizationApproval")
    Call<Res_AP_IF_202_VO> AP_IF_023_2(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                       @Body HashMap<String, String> req);

    //전자결재-조직원목록
    @POST("/mTotalSign/approval/splistApproval")
    Call<Res_AP_IF_203_VO> AP_IF_023_3(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                       @Body HashMap<String, String> req);

    //전자결재-임직원검색
    @POST("/mTotalSign/approval/spsearchLineinfo")
    Call<Res_AP_IF_204_VO> AP_IF_023_4(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                       @Body HashMap<String, String> req);

    //전자결재-최근조회검색
    @POST("/mTotalSign/approval/latelyLineinfo")
    Call<Res_AP_IF_205_VO> AP_IF_023_5(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                       @Body Req_AP_IF_205_VO req);

    //서비스데스크 - 승인상세
    @POST("/mTotalSign/servicedesk/serviceDetail")
    Call<Res_AP_IF_028_VO> AP_IF_028(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);


    //서비스데스크 - 승인확인(일반-각 테스트, CAB,협업)
    @POST("/mTotalSign/servicedesk/serviceApprove")
    Call<Res_AP_Empty_VO> AP_IF_029(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body HashMap<String, String> req);


    //서비스데스크 - 승인확인(일반-보안&IT)
    @POST("/mTotalSign/servicedesk/serviceBoanApprove")
    Call<Res_AP_Empty_VO> AP_IF_030(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body HashMap<String, String> req);


    //서비스데스크 - 승인상세(DLP 문서반출)
    @POST("/mTotalSign/servicedesk/dlpDetail")
    Call<Res_AP_IF_032_VO> AP_IF_032(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //서비스데스크 - 승인확인(DLP 문서반출)
    @POST("/mTotalSign/servicedesk/dlpApprove")
    Call<Res_AP_IF_033_VO> AP_IF_033(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //서비스데스크 - 승인상세(메일필터)
    @POST("/mTotalSign/servicedesk/mailFilterDetail")
    Call<Res_AP_IF_035_VO> AP_IF_035(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);


    //서비스데스크 - 승인상세(메일필터)
    @POST("/mTotalSign/servicedesk/mailFilterApprove")
    Call<Res_AP_IF_036_VO> AP_IF_036(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //서비스데스크 - 권한목록
    @POST("/mTotalSign/servicedesk/aprList")
    Call<Res_AP_IF_037_VO> AP_IF_037(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);


    //서비스데스크 - 승인확인(시스템권한)
    @POST("/mTotalSign/servicedesk/aprApprove")
    Call<Res_AP_Empty_VO> AP_IF_039(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                    @Body Req_AP_IF_039_VO req);


    //서비스데스크 - 위임자목록
    @POST("/mTotalSign/servicedesk/dgtnList")
    Call<Res_AP_IF_040_VO> AP_IF_040(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);


    //URL로 get방식 데이터 가져오기 - 주용도:결재문서보기
    @GET("/SynapDocViewServer/job")
    public Call<Res_Doc_Viewer> getDocViewerKey(@Query("fileType") String fileType, @Query("filePath") String filePath, @Query("fid") String fid);


    //다이나믹
    @POST("/mTotalSign/dynamic/getDynamicList")
    Call<Res_AP_IF_103_VO> AP_IF_103(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);


    //다이나믹 상세
    @POST("/mTotalSign/dynamic/getDynamicDetail")
    Call<Res_AP_IF_104_VO> AP_IF_104(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                     @Body HashMap<String, String> req);

    //다이나믹 승인/반려
    @POST("/mTotalSign/dynamic/doDynamicAction")
    Call<Res_AP_DynamicAction_VO> AP_IF_DynamicAction(@Header("Authorization") String autho, @Header("Timestamp") String timestamp,
                                                      @Body HashMap<String, String> req);

}
