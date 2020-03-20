package com.kolon.sign2.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kolon.sign2.SplashActivity;
import com.kolon.sign2.approval.ApprovalDetailActivity;
import com.kolon.sign2.dynamic.dynamicDetail.DynamicDetailActivity;
import com.kolon.sign2.servicedesk.ServiceDeskDetailActivity;
import com.kolon.sign2.utils.Constants;
import com.kolon.sign2.utils.SharedPreferenceManager;
import com.kolon.sign2.vo.PushBodyVO;
import com.kolon.sign2.vo.Res_AP_IF_002_VO;
import com.kolon.sign2.vo.Res_AP_IF_013_VO;
import com.kolon.sign2.vo.Res_AP_IF_101_VO;
import com.kolon.sign2.vo.Res_AP_IF_103_VO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kolon.sign2.utils.Constants.PREF_SCHEME_DATA;

/**
 * push 또는 scheme 처리
 */
public class SchemeActivity extends Activity {

    private final String TAG = "SchemeActivity";

    private final String SCHEME_URL1 = "sign2dev://?sysId="; //신규
    private final String SCHEME_URL2 = "sign2dev://?applink=appSign:"; //기존

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //푸시 또는 웹등에서의 scheme호출의 구분 후 이동
        Intent it = getIntent();
        if (it == null) {
            gotoMain();
        } else {
        //    SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());

            if(TextUtils.isEmpty(it.getStringExtra("pushData"))){
                //push data가 없으므로 scheme로 들어옴

                String url = it.getDataString();
                Log.d(TAG, "#### scheme url:" + url);
                if (TextUtils.isEmpty(url)) {
                    gotoMain();
                    return;
                }

                String menuId = "";
                String docId = "";
                String sysId = "";
                String param01 = "";
                String param02 = "";
                String param03 = "";
                String param04 = "";
                String param05 = "";

                HashMap<String, String> getParamMap = getQueryMap(url);
                try {
                    if (url.startsWith(SCHEME_URL1)) {
                        //sign2dev://?sysId=sign&menuId=P&docId=00000000000000111307
                        menuId = getParamMap.get("menuId");
                        docId = getParamMap.get("docId");
                        sysId = getParamMap.get("sysId");
                        param01 = getParamMap.get("param01");
                        param02 = getParamMap.get("param02");
                        param03 = getParamMap.get("param03");
                        param04 = getParamMap.get("param04");
                        param05 = getParamMap.get("param05");

                    } else if (url.startsWith(SCHEME_URL2)) {
                        //sign2dev://?applink=appSign:00000000000000111307
                        /*
                         * url이 전자결재 전용으로 반드시 sign2dev://?applink=appSign: 이런 형으로만 들어와야함.
                         * 다른 형으로 들어오는게 있는지 url형식 문의 필요
                         * */
                        docId = url.replace(SCHEME_URL2, "");//
                        getParamMap.put("docId", docId);

                        getParamMap.put("param01", "0");
                    }
                    Log.d(TAG, "#### scheme param:" + getParamMap);

           //         String value = new Gson().toJson(getParamMap);
          //          mPref.setStringPreference(PREF_SCHEME_DATA, value);

                    schemeGotoActivity(getParamMap);
                } catch (Exception e) {
                    gotoMain();
                }

            }else {
                //push data가 있으므로 push로 들어옴
                String pushData = it.getStringExtra("pushData");
                Log.d(TAG, "#### pushData:"+pushData);
                if(TextUtils.isEmpty(pushData)){
                    gotoMain();
                }else{
                    try {
                        Gson gson = new Gson();
                        PushBodyVO pushBody = gson.fromJson(pushData, PushBodyVO.class);
                        HashMap<String, String> getParamMap = new HashMap<>();
                        getParamMap.put("docId", pushBody.getDocId());
                        getParamMap.put("menuId", pushBody.getMenuId());
                        getParamMap.put("sysId", pushBody.getSysId());
                        getParamMap.put("userId", pushBody.getUserId());
                        getParamMap.put("param01", pushBody.getParam01());
                        getParamMap.put("param02", pushBody.getParam02());
                        getParamMap.put("param03", pushBody.getParam03());
                        getParamMap.put("param04", pushBody.getParam04());
                        getParamMap.put("param05", pushBody.getParam05());

                        schemeGotoActivity(getParamMap);
                    }catch (Exception e){
                        gotoMain();
                    }
                }
            }
        }
    }


    /**
     * URL에서 파라미터를 파싱한다
     **/
    private HashMap<String, String> getQueryMap(String query) {
        if (query == null)
            return null;

        int pos1 = query.indexOf("?");
        if (pos1 >= 0) {
            query = query.substring(pos1 + 1);
        }

        String[] params = query.split("&");
        HashMap<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private void gotoMain() {
        Intent i = new Intent(this, SplashActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }

    private void schemeGotoActivity(HashMap<String, String> getParamMap) {
        Log.d(TAG, "#### app running:"+Constants.isLogin);
        //로그인을 한 상태 - (앱이 실행중이었던 상태) 디테일로 보내고 아니라면 로그인으로 보냄
        if (Constants.isLogin) {
            //바로 디테일로 보냄
            if(TextUtils.isEmpty(getParamMap.get("sysId"))){
                //sysId가 없으므로 구형
                getParamMap.put("sysId","sign");
            }
            Constants.schemeMap = getParamMap;
            sendBroadcast(new Intent(Constants.BROADCAST_MESSAGE));
            finish();
//            gotoDetail(getParamMap);
        } else {
            //앱이 실행되지 않은 상태
            Constants.schemeMap = getParamMap;
            gotoMain();
        }
    }

    //디테일 화면으로 이동 - 로그인시
    private void gotoDetail(Map<String, String> getParamMap) {
        SharedPreferenceManager mPref = SharedPreferenceManager.getInstance(getBaseContext());
        String str = mPref.getStringPreference(Constants.PREF_USER_AUTH_INFO);
        Res_AP_IF_101_VO.result userAuthInfo = new Gson().fromJson(str, new TypeToken<Res_AP_IF_101_VO.result>() {
        }.getType());


        String userId = mPref.getStringPreference(Constants.PREF_USER_IF_ID);
        String deptId = mPref.getStringPreference(Constants.PREF_DEPT_ID);
        String companyCd = mPref.getStringPreference(Constants.PREF_COMPANY_CD);
        String menuId = getParamMap.get("menuId");
        String docId = getParamMap.get("docId");
        String sysId = getParamMap.get("sysId");
        String param01 = getParamMap.get("param01");
        String param02 = getParamMap.get("param02");
        String param03 = getParamMap.get("param03");
        String param04 = getParamMap.get("param04");
        String param05 = getParamMap.get("param05");


       // String sysId = getParamMap.get("sysId");
        if ("sign".equals(sysId)) {
            //전자결재
            Intent it = new Intent(this, ApprovalDetailActivity.class);

            ArrayList<String> guidList = new ArrayList<>();

            guidList.add(docId + "|" + docId + "|" + param01);

            String containerId = "";
            String subQuery = "";
            String category = menuId;

            ArrayList<Res_AP_IF_013_VO.result.apprList> obj = new ArrayList<>();
            Res_AP_IF_013_VO.result.apprList info = new Res_AP_IF_013_VO().new result().new apprList();
            info.setTitle(getParamMap.get("title"));
            info.setAuthor(getParamMap.get("requester"));
            info.setGuid(docId);
            info.setPubDate(getParamMap.get("reqDatetime"));
            info.setStatus(getParamMap.get("status"));
            info.setHasattachYn(getParamMap.get("hasattachYN"));
            info.setHasopinionYn(getParamMap.get("hasopinionYN"));
            info.setInLine(getParamMap.get("inLine"));
            info.setIsPublic(getParamMap.get("isPublic"));
            info.setCategory(category);
            obj.add(info);

            it.putExtra("companyCd", companyCd);
            it.putExtra("guidList", guidList);
            it.putExtra("position", 0);
            it.putExtra("userId", userId);
            it.putExtra("deptId", deptId);
            it.putExtra("containerId", containerId);
            it.putExtra("subQuery", subQuery);
            it.putExtra("category", category);
            it.putExtra("object", obj);
            startActivity(it);
            finish();
        } else {
            String mSysNm = "";
            for (int i = 0; i < userAuthInfo.getSysArray().size(); i++) {
                if (userAuthInfo.getSysArray().get(i).getSysId().equals(sysId)) {
                    mSysNm = userAuthInfo.getSysArray().get(i).getSysName();
                    break;
                }
            }

            ArrayList<Res_AP_IF_103_VO.dynamicListList> dynamicListList = new ArrayList<>();
            Res_AP_IF_103_VO.dynamicListList info = new Res_AP_IF_103_VO.dynamicListList();
            info.setApprovalCase(getParamMap.get("approvalCase"));
            info.setApprovalId(getParamMap.get("approvalId"));
            info.setDocId(docId);
            info.setHasattachYN(getParamMap.get("hasattachYN"));
            info.setHasopinionYN(getParamMap.get("hasopinionYN"));
            info.setInLine(getParamMap.get("isPublic"));
            info.setIsPublic(getParamMap.get("isPublic"));
            info.setParam01(getParamMap.get("param01"));
            info.setParam02(getParamMap.get("param02"));
            info.setParam03(getParamMap.get("param03"));
            info.setParam04(getParamMap.get("param04"));
            info.setParam05(getParamMap.get("param05"));
            info.setReqDatetime(getParamMap.get("reqDatetime"));
            info.setRequester(getParamMap.get("requester"));
            info.setStatus(getParamMap.get("status"));
            info.setTitle(getParamMap.get("title"));
            dynamicListList.add(info);


            //서비스데스크 중 보안(), 권한 S03,04,05
            if (menuId.startsWith("S03") || menuId.startsWith("S04") || menuId.startsWith("S05")) {
                Intent intent = new Intent(this, ServiceDeskDetailActivity.class);
                intent.putExtra("data", dynamicListList);
                intent.putExtra("menuId", menuId);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            } else {
                //그외...
                Intent intent = new Intent(this, DynamicDetailActivity.class);
                intent.putExtra("position", 0);
                intent.putExtra("userId", userId);
                intent.putExtra("sysId", sysId);
                intent.putExtra("sysNm", mSysNm);
                intent.putExtra("menuId", menuId);
                intent.putExtra("docId", docId);
                intent.putExtra("param01", param01);
                intent.putExtra("param02", param02);
                intent.putExtra("param03", param03);
                intent.putExtra("param04", param04);
                intent.putExtra("param05", param05);
                intent.putExtra("listcount", 1);
                intent.putExtra("object", dynamicListList);
                startActivity(intent);
                finish();
            }
        }
    }
}
