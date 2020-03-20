package com.kolon.sign2.vo;

import java.util.ArrayList;

public class Res_AP_IF_032_VO extends statusVO {

    public class result extends resultVO {

        private dlpDetailClass dlpDetail;

        public class dlpDetailClass {

            String userId;//IKEN_ID	String	IKEN_ID
            String appIdx;//신청서 번호	String	신청서 번호
            String chkIdx;//	결재서 번호	String	결재서 번호
            String chkStatus;//	결재서 상태	String	결재서 상태
            String appDate;//	신청서 작성 일시	String	신청서 작성 일시
            String appStatus;//신청서 상태	String	신청서 상태
            String appId;//신청자 IKEN_ID	String	신청자 IKEN_ID
            String appNm;//	신청자 이름	String	신청자 이름
            String appDept;//	신청자 부서	String	신청자 부서
            String appJbtt;//신청자 직급	String	신청자 직급
            String appTitle;//신청 제목		신청 제목
            String appContent;//	신청 사유	String	신청 사유
            String usesDate;//사용 시작일시	String	사용 시작일시
            String useeDate;//사용 종료일시	String	사용 종료일시
            String proReceiver;//제공처	String	제공처
            String proMeans;//제공방식	String	제공방식

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAppIdx() {
                return appIdx;
            }

            public void setAppIdx(String appIdx) {
                this.appIdx = appIdx;
            }

            public String getChkIdx() {
                return chkIdx;
            }

            public void setChkIdx(String chkIdx) {
                this.chkIdx = chkIdx;
            }

            public String getChkStatus() {
                return chkStatus;
            }

            public void setChkStatus(String chkStatus) {
                this.chkStatus = chkStatus;
            }

            public String getAppDate() {
                return appDate;
            }

            public void setAppDate(String appDate) {
                this.appDate = appDate;
            }

            public String getAppStatus() {
                return appStatus;
            }

            public void setAppStatus(String appStatus) {
                this.appStatus = appStatus;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getAppNm() {
                return appNm;
            }

            public void setAppNm(String appNm) {
                this.appNm = appNm;
            }

            public String getAppDept() {
                return appDept;
            }

            public void setAppDept(String appDept) {
                this.appDept = appDept;
            }

            public String getAppJbtt() {
                return appJbtt;
            }

            public void setAppJbtt(String appJbtt) {
                this.appJbtt = appJbtt;
            }

            public String getAppTitle() {
                return appTitle;
            }

            public void setAppTitle(String appTitle) {
                this.appTitle = appTitle;
            }

            public String getAppContent() {
                return appContent;
            }

            public void setAppContent(String appContent) {
                this.appContent = appContent;
            }

            public String getUsesDate() {
                return usesDate;
            }

            public void setUsesDate(String usesDate) {
                this.usesDate = usesDate;
            }

            public String getUseeDate() {
                return useeDate;
            }

            public void setUseeDate(String useeDate) {
                this.useeDate = useeDate;
            }

            public String getProReceiver() {
                return proReceiver;
            }

            public void setProReceiver(String proReceiver) {
                this.proReceiver = proReceiver;
            }

            public String getProMeans() {
                return proMeans;
            }

            public void setProMeans(String proMeans) {
                this.proMeans = proMeans;
            }
        }
        public dlpDetailClass getDlpDetail() {
            return dlpDetail;
        }

        public void setDlpDetail(dlpDetailClass dlpDetail) {
            this.dlpDetail = dlpDetail;
        }


        public ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> getDlpDetailFile() {
            return dlpDetailFile;
        }

        public void setDlpDetailFile(ArrayList<Res_AP_IF_032_VO.result.dlpDetailFile> dlpDetailFile) {
            this.dlpDetailFile = dlpDetailFile;
        }

        private ArrayList<dlpDetailFile> dlpDetailFile;

        public class dlpDetailFile {

            String fileName;//	파일이름	String	파일이름
            String filePath;//파일경로	String	파일경로
            String detectCnt1;//	주민등록번호 검출건수	String	주민등록번호 검출건수
            String detectCnt2;//	통합전화번호 검출건수	String	통합전화번호 검출건수
            String detectCnt3;//	이메일주소 검출건수	String	이메일주소 검출건수
            String detectCnt4;//	운전면허번호 검출건수	String	운전면허번호 검출건수
            String detectCnt5;//	여권번호 검출건수	String	여권번호 검출건수
            String detectCnt6;//	외국인등록번호 검출건수	String	외국인등록번호 검출건수
            String detectCnt7;//	은행계좌번호 검출건수	String	은행계좌번호 검출건수
            String detectCnt8;//	신용카드번호 검출건수	String	신용카드번호 검출건수
            String detectKeyword;//키워드 검출 여부	String	키워드 검출 여부


            public boolean isChecked;

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }

            public String getDetectCnt1() {
                return detectCnt1;
            }

            public void setDetectCnt1(String detectCnt1) {
                this.detectCnt1 = detectCnt1;
            }

            public String getDetectCnt2() {
                return detectCnt2;
            }

            public void setDetectCnt2(String detectCnt2) {
                this.detectCnt2 = detectCnt2;
            }

            public String getDetectCnt3() {
                return detectCnt3;
            }

            public void setDetectCnt3(String detectCnt3) {
                this.detectCnt3 = detectCnt3;
            }

            public String getDetectCnt4() {
                return detectCnt4;
            }

            public void setDetectCnt4(String detectCnt4) {
                this.detectCnt4 = detectCnt4;
            }

            public String getDetectCnt5() {
                return detectCnt5;
            }

            public void setDetectCnt5(String detectCnt5) {
                this.detectCnt5 = detectCnt5;
            }

            public String getDetectCnt6() {
                return detectCnt6;
            }

            public void setDetectCnt6(String detectCnt6) {
                this.detectCnt6 = detectCnt6;
            }

            public String getDetectCnt7() {
                return detectCnt7;
            }

            public void setDetectCnt7(String detectCnt7) {
                this.detectCnt7 = detectCnt7;
            }

            public String getDetectCnt8() {
                return detectCnt8;
            }

            public void setDetectCnt8(String detectCnt8) {
                this.detectCnt8 = detectCnt8;
            }

            public String getDetectKeyword() {
                return detectKeyword;
            }

            public void setDetectKeyword(String detectKeyword) {
                this.detectKeyword = detectKeyword;
            }
        }

        public ArrayList<Res_AP_IF_032_VO.result.dlpDetailOrder> getDlpDetailOrder() {
            return dlpDetailOrder;
        }

        public void setDlpDetailOrder(ArrayList<Res_AP_IF_032_VO.result.dlpDetailOrder> dlpDetailOrder) {
            this.dlpDetailOrder = dlpDetailOrder;
        }

        private ArrayList<dlpDetailOrder> dlpDetailOrder;

        public class dlpDetailOrder {

            String orderNo;//	결재자 순번	String	결재자 순번
            String orderId;//	결재자 IKEN_ID	String	결재자 IKEN_ID
            String orderNm;//	결재자 이름	String	결재자 이름
            String orderCompany;//결재자 회사	String	결재자 회사
            String orderDept;//결재자 부서	String	결재자 부서
            String orderJbtt;//결재자 직급	String	결재자 직급
            String orderDuty;//결재자 직책	String	결재자 직책
            String orderStatus;//	결재 상태	String	결재 상태
            String orderDate;//결재 일시	String	결재 일시
            String orderReason;//	결재 사유	String	결재 사유

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getOrderNm() {
                return orderNm;
            }

            public void setOrderNm(String orderNm) {
                this.orderNm = orderNm;
            }

            public String getOrderCompany() {
                return orderCompany;
            }

            public void setOrderCompany(String orderCompany) {
                this.orderCompany = orderCompany;
            }

            public String getOrderDept() {
                return orderDept;
            }

            public void setOrderDept(String orderDept) {
                this.orderDept = orderDept;
            }

            public String getOrderJbtt() {
                return orderJbtt;
            }

            public void setOrderJbtt(String orderJbtt) {
                this.orderJbtt = orderJbtt;
            }

            public String getOrderDuty() {
                return orderDuty;
            }

            public void setOrderDuty(String orderDuty) {
                this.orderDuty = orderDuty;
            }

            public String getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(String orderStatus) {
                this.orderStatus = orderStatus;
            }

            public String getOrderDate() {
                return orderDate;
            }

            public void setOrderDate(String orderDate) {
                this.orderDate = orderDate;
            }

            public String getOrderReason() {
                return orderReason;
            }

            public void setOrderReason(String orderReason) {
                this.orderReason = orderReason;
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