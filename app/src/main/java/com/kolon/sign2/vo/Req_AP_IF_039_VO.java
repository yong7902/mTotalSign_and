package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 서비스데스크 권한 승인 요청
 * */
public class Req_AP_IF_039_VO {

    String userId;
    String apprGb;//A:승인, R:반려
    ArrayList<aprApproveList> aprApproveList;

    public class aprApproveList {

        String docNo;//결재 문서 고유번호, 결재 의견 객체화된 데이터
        String ctnt;

        public String getDocNo() {
            return docNo;
        }

        public void setDocNo(String docNo) {
            this.docNo = docNo;
        }

        public String getCtnt() {
            return ctnt;
        }

        public void setCtnt(String ctnt) {
            this.ctnt = ctnt;
        }
    }

    public ArrayList<Req_AP_IF_039_VO.aprApproveList> getAprApproveList() {
        return aprApproveList;
    }

    public void setAprApproveList(ArrayList<Req_AP_IF_039_VO.aprApproveList> aprApproveList) {
        this.aprApproveList = aprApproveList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApprGb() {
        return apprGb;
    }

    public void setApprGb(String apprGb) {
        this.apprGb = apprGb;
    }

}
