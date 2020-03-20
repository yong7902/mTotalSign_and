package com.kolon.sign2.vo;


import java.util.ArrayList;

/**
 * Created by kiyeon_kim on 2019. 12. 09
 * modify by kiyeon_kim on 2019. 12. 26
 */
public class Res_AP_IF_104_VO {

    public Result result;
    public Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result {
        private String errorCd;      // 결과코드
        private String errorMsg;       // 결과코드 메세지
        private ArrayList<dynamicDetailList> dynamicDetailList;   // 앱 리스트

        public ArrayList<dynamicDetailList> getDynamicDetailList() {
            return dynamicDetailList;
        }

        public void setDynamicDetailList(ArrayList<dynamicDetailList> list) {
            this.dynamicDetailList = list;
        }

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

    }

    public static class Status {

        private String statusCd;
        private String statusMssage;

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

    public static class dynamicDetailList {
        private String itemClass;
        private String leftText;
        private String rightText;
        private String attr01;
        private String attr02;
        private String attr03;
        private String attr04;
        private String attr05;
        private String attr06;
        private String attr07;
        private String attr08;
        private String attr09;
        private String attr10;

        public String getItemClass() {
            return itemClass;
        }

        public void setItemClass(String itemClass) {
            this.itemClass = itemClass;
        }

        public String getLeftText() {
            return leftText;
        }

        public void setLeftText(String leftText) {
            this.leftText = leftText;
        }

        public String getRightText() {
            return rightText;
        }

        public void setRightText(String rightText) {
            this.rightText = rightText;
        }

        public String getAttr01() {
            return attr01;
        }

        public void setAttr01(String attr01) {
            this.attr01 = attr01;
        }

        public String getAttr02() {
            return attr02;
        }

        public void setAttr02(String attr02) {
            this.attr02 = attr02;
        }

        public String getAttr03() {
            return attr03;
        }

        public void setAttr03(String attr03) {
            this.attr03 = attr03;
        }

        public String getAttr04() {
            return attr04;
        }

        public void setAttr04(String attr04) {
            this.attr04 = attr04;
        }

        public String getAttr05() {
            return attr05;
        }

        public void setAttr05(String attr05) {
            this.attr05 = attr05;
        }

        public String getAttr06() {
            return attr06;
        }

        public void setAttr06(String attr06) {
            this.attr06 = attr06;
        }

        public String getAttr07() {
            return attr07;
        }

        public void setAttr07(String attr07) {
            this.attr07 = attr07;
        }

        public String getAttr08() {
            return attr08;
        }

        public void setAttr08(String attr08) {
            this.attr08 = attr08;
        }

        public String getAttr09() {
            return attr09;
        }

        public void setAttr09(String attr09) {
            this.attr09 = attr09;
        }

        public String getAttr10() {
            return attr10;
        }

        public void setAttr10(String attr10) {
            this.attr10 = attr10;
        }
    }


}
