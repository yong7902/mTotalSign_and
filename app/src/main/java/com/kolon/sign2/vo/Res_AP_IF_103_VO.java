package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Res_AP_IF_103_VO implements Serializable {

    public Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result implements Serializable {
        private String errorCd;      // 결과코드
        private String errorMsg;       // 결과코드 메세지
        private ArrayList<dynamicListList> dynamicListList;   // 앱 리스트

        public ArrayList<dynamicListList> getDynamicListList() {
            return dynamicListList;
        }

        public void setDynamicListList(ArrayList<dynamicListList> list) {
            this.dynamicListList = list;
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

    public static class dynamicListList implements Serializable  {

        private String approvalId = "";
        private String approvalCase = "";
        private String title = "";
        private String status = "";
        private String requester = "";
        private String reqDatetime = "";
        private String hasattachYN = "";
        private String hasopinionYN = "";
        private String isPublic = "";
        private String inLine = "";
        private String webUrl = "";
        private String docId = "";
        private String param01 = "";
        private String param02 = "";
        private String param03 = "";
        private String param04 = "";
        private String param05 = "";

        public String getApprovalId() {
            return approvalId;
        }

        public void setApprovalId(String approvalId) {
            this.approvalId = approvalId;
        }

        public String getApprovalCase() {
            return approvalCase;
        }

        public void setApprovalCase(String approvalCase) {
            this.approvalCase = approvalCase;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRequester() {
            return requester;
        }

        public void setRequester(String requester) {
            this.requester = requester;
        }

        public String getReqDatetime() {
            return reqDatetime;
        }

        public void setReqDatetime(String reqDatetime) {
            this.reqDatetime = reqDatetime;
        }

        public String getHasattachYN() {
            return hasattachYN;
        }

        public void setHasattachYN(String hasattachYN) {
            this.hasattachYN = hasattachYN;
        }

        public String getHasopinionYN() {
            return hasopinionYN;
        }

        public void setHasopinionYN(String hasopinionYN) {
            this.hasopinionYN = hasopinionYN;
        }

        public String getIsPublic() {
            return isPublic;
        }

        public void setIsPublic(String isPublic) {
            this.isPublic = isPublic;
        }

        public String getInLine() {
            return inLine;
        }

        public void setInLine(String inLine) {
            this.inLine = inLine;
        }

        public String getWebUrl() {
            return webUrl;
        }

        public void setWebUrl(String webUrl) {
            this.webUrl = webUrl;
        }

        public String getDocId() {
            return docId;
        }

        public void setDocId(String docId) {
            this.docId = docId;
        }

        public String getParam01() {
            return param01;
        }

        public void setParam01(String param01) {
            this.param01 = param01;
        }

        public String getParam02() {
            return param02;
        }

        public void setParam02(String param02) {
            this.param02 = param02;
        }

        public String getParam03() {
            return param03;
        }

        public void setParam03(String param03) {
            this.param03 = param03;
        }

        public String getParam04() {
            return param04;
        }

        public void setParam04(String param04) {
            this.param04 = param04;
        }

        public String getParam05() {
            return param05;
        }

        public void setParam05(String param05) {
            this.param05 = param05;
        }
    }

}
