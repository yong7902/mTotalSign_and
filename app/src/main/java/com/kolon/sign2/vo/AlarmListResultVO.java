package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * Created by sunho_km on 2020. 02. 20..
 */

public class AlarmListResultVO {
    public Result result;
    public status status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public AlarmListResultVO.status getStatus() {
        return status;
    }

    public void setStatus(AlarmListResultVO.status status) {
        this.status = status;
    }

    public static class Result {
        public String errorCd;
        public String errorMsg;
        private ArrayList<List> list;   // 앱 리스트

        public String getErrorCd() {
            return errorCd;
        }

        public void setErrorCd(String errorCd) {
            this.errorCd = errorCd;
        }

        public String getErrorMsg() {
            return errorMsg;
        }

        public void setErrorMsg(String errorMsg) {
            this.errorMsg = errorMsg;
        }

        public ArrayList<List> getList() {
            return list;
        }

        public void setList(ArrayList<List> list) {
            this.list = list;
        }
    }

    public static class status {
        public String statusCd;
        public String statusMssage;

        public String getStatusCd() {
            return statusCd;
        }

        public void setStatusCd(String statusCd) {
            this.statusCd = statusCd;
        }

        public String getStatusMssage() {
            return statusMssage;
        }

        public void setStatusMssage(String statusMssage) {
            this.statusMssage = statusMssage;
        }
    }

    public static class List {
        private String sysId;                  // 시스템 아이디
        private String sysNm;                  // 시스템 명
        private String pushYn;                // Push 수신여부 Y/N
        private String iconUrl;               // 아이콘 URL

        public String getSysId() {
            return sysId;
        }

        public void setSysId(String sysId) {
            this.sysId = sysId;
        }

        public String getSysNm() {
            return sysNm;
        }

        public void setSysNm(String sysNm) {
            this.sysNm = sysNm;
        }

        public String getPushYn() {
            return pushYn;
        }

        public void setPushYn(String pushYn) {
            this.pushYn = pushYn;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }
    }
}
