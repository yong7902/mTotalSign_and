package com.kolon.sign2.vo;

import java.util.ArrayList;

public class UserInfoResultVO {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public static class Result {
        private String errorCd;      // 결과코드
        private String errorMsg;       // 오류 메세지
        private String mdmYn;
        private ArrayList<List> list;


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

        public String getMdmYn() {
            return mdmYn;
        }

        public void setMdmYn(String mdmYN) {
            this.mdmYn = mdmYn;
        }

        public ArrayList<List> getList() {
            return list;
        }

        public void setList(ArrayList<List> list) {
            this.list = list;
        }
    }

    public static class List {
        private String sysId;
        private String sysName;
        private String sysIcon;
        private String sysOrder;
        private String sysPushYn;
        private String autoTabYn;

        public String getSysId() {
            return sysId;
        }

        public void setSysId(String sysId) {
            this.sysId = sysId;
        }

        public String getSysName() {
            return sysName;
        }

        public void setSysName(String sysName) {
            this.sysName = sysName;
        }

        public String getSysIcon() {
            return sysIcon;
        }

        public void setSysIcon(String sysIcon) {
            this.sysIcon = sysIcon;
        }

        public String getSysOrder() {
            return sysOrder;
        }

        public void setSysOrder(String sysOrder) {
            this.sysOrder = sysOrder;
        }

        public String getSysPushYn() {
            return sysPushYn;
        }

        public void setSysPushYn(String sysPushYn) {
            this.sysPushYn = sysPushYn;
        }

        public String getAutoTabYn() {
            return autoTabYn;
        }

        public void setAutoTabYn(String autoTabYN) {
            this.autoTabYn = autoTabYn;
        }
    }

}
