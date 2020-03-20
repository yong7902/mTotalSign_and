package com.kolon.sign2.vo;

/**
 * 서비스데스크 - 승인확인(메일필터)
 */
public class Res_AP_IF_036_VO extends statusVO {

    public class result extends resultVO {

        public class properties {

            String code;//성공/실패 코드	String	성공/실패 코드
            String msg;//리턴 메시지	String	리턴 메시지
            String permission;//권한	String	권한

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getPermission() {
                return permission;
            }

            public void setPermission(String permission) {
                this.permission = permission;
            }
        }

    }


    private result result;

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }
}