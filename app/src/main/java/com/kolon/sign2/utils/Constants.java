package com.kolon.sign2.utils;

import java.util.HashMap;

/**
 * Created by sunho_kim on 2019-12-02.
 */

public class Constants {
    public static final String ROOT_KEY = "";
    public static final String INTERFACE_SUCCESS = "success";
    public static final String LIST_DEFAULT_PAGE ="1";
    public static final String LIST_DEFAULT_COUNT ="1000";
    public static final String APP_OS ="1";
    public static final int APP_NOTIFICATION_ID = 1123;
    public static final String PLATFORMCD = "2000012";
    public static final String PLATFORMCD_TABLET = "2000022";
    public static final String ENCRYPTION_KEY = "NTE3YjA0YjUtZGJkZS00NDQ5LTg4NzgtMzFiNWFjMjg1YmZi";


    //SharedPreference
    public static final String PREF_NAME = "kolon_sign2";
    public static final String PREF_USER_ID = "pref_user_id";
    public static final String PREF_USER_IF_ID = "pref_user_if_id";
    public static final String PREF_USER_NAME = "pref_user_name";
    public static final String PREF_USER_PW = "pref_user_pw";
    public static final String PREF_AUTO_LOGIN = "pref_auto_login";
    public static final String PREF_SAVE_ID = "pref_save_id";
    public static final String PREF_DEVICE_ID = "pref_device_id";
    public static final String PREF_SETTING_PUSH_SILENT = "pref_push_silent";
    public static final String PREF_TOKEN_KEY = "pref_token_key";
    public static final String PREF_POLICY_CHECK = "pref_policy_check";
    public static final String PREF_TUTORIAL_CHECK = "pref_tutorial_check";
    public static final String PREF_LOGIN_INFO = "pref_login_info";
    public static final String PREF_LOGIN_IF_INFO = "pref_login_if_info";
    public static final String PREF_USER_AUTH_INFO = "pref_user_auth_info";

    public static final String PREF_DEPT_ID = "pref_dept_id";//부서 아이디 - 주용도 전자결재 조회
    public static final String PREF_DEPT_CD = "pref_dept_cd";//부서 코드 - 주용도 전자결재 조회
    public static final String PREF_COMPANY_CD = "pref_company_cd";//회사코드 - 주용도 전자결재 조회


    //Activity Extra
    public static final String APP_ROOTKEY_EXTRA = "rootkey";
    public static final String APP_LOGOUT_EXTRA = "logout";


    //recently search id
    public static final String PREF_RECENTLY_SEARCH_DATA = "recently_search_data";

    //text size setValue
    public static final String PREF_TEXTSIZE_VALUE = "textsize_value";

    //scheme data
    public static final String PREF_SCHEME_DATA = "pref_scheme_data";

    //change account data
    public static final String PREF_CHANGED_ACCOUNT_DATA = "pref_changed_account_data";
    public static final String PREF_MOD_ACCOUNT_DATA = "pref_mod_account_data";


    public static boolean isLogin = false;//로그인 상태파악
    public static HashMap<String, String> schemeMap = new HashMap<>();//scheme로 들어온 데이터
    public static final int PAGE_CNT = 3;//페이징 처리:서버에서 가져오는 데이터 갯수


    public static final String BROADCAST_MESSAGE = "com.kolon.sign2.approvalapp.pushbr";
}
