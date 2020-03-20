package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 메인 리스트 조회(Home 리스트 조회)
 * */
public class Res_AP_IF_002_VO extends statusVO {

    public class result extends resultVO implements Serializable {

        private ArrayList<APPROVAL_LIST> approvalList;

        public class APPROVAL_LIST {

            String approvalId;//	문서 번호 (사용자 기준 식별자)
            String approvalCase;//	문서 종류
            String docId;//	문서 번호 (시스템 기준 식별자)			O
            String title;//결재 제목			O
            String status;//진행상태
            String requester;//	요청/기안 자			O
            String reqDatetime;//	요청/기안 일자			O
            String hasattachYN;//첨부파일 유무				전자결재만 사용
            String hasopinionYN;//	의견 유무				전자결재만 사용
            String isPublic;//	일반문서여부				전자결재만 사용
            String inLine;//열람여부				전자결재만 사용
            String webUrl;//	상세 웹 URL(PC사용)				향후 PC 통합결재 구축 시 상세화면 링크로써 사용
            String param01;//	화면간 연동을 위한 hidden 파라미터 01
            String param02;//	화면간 연동을 위한 hidden 파라미터 02
            String param03;//	화면간 연동을 위한 hidden 파라미터 03
            String param04;//화면간 연동을 위한 hidden 파라미터 04
            String param05;//	화면간 연동을 위한 hidden 파라미터 05
            String sysId;
            String menuId;


            public int type;// 0:타이틀(ex:전자결재 6건등 타이틀의 표시) 1:리스트 - 본내용 2:더보기 버튼(3개이상일 경우 보임)
            public boolean isView = false;
            public String totalCnt;//최상단 총건수


            public String getSysId() {
                return sysId;
            }

            public void setSysId(String sysId) {
                this.sysId = sysId;
            }

            public String getMenuId() {
                return menuId;
            }

            public void setMenuId(String menuId) {
                this.menuId = menuId;
            }

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

            public String getDocId() {
                return docId;
            }

            public void setDocId(String docId) {
                this.docId = docId;
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

        public ArrayList<APPROVAL_LIST> getApprovalList() {
            return approvalList;
        }

        public void setApprovalList(ArrayList<APPROVAL_LIST> approvalList) {
            this.approvalList = approvalList;
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