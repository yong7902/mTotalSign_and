package com.kolon.sign2.vo;

/**
 * 동적 리스트 상세 승인 반려 액션
 */
public class Res_AP_DynamicAction_VO {

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
}
