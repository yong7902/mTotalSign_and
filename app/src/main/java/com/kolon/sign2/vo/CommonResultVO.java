package com.kolon.sign2.vo;

/**
 * Created by sunho_km on 2020. 02. 20..
 */
public class CommonResultVO {

    public Result result;
    public status status;

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
    public static class Result {
        public String errorCd;
        public String errorMsg;

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

}
