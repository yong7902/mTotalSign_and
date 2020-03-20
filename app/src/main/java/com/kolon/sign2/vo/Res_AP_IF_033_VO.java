package com.kolon.sign2.vo;

/**
 * 서비스데스크 - 싱인확인(DLP 문서반출)
 * */
public class Res_AP_IF_033_VO extends statusVO {

    public class result extends resultVO {

        public class dlpApprove{

            String appIdx;
            String chkIdx;
            String jobCode;
            String jobMessage;

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

            public String getJobCode() {
                return jobCode;
            }

            public void setJobCode(String jobCode) {
                this.jobCode = jobCode;
            }

            public String getJobMessage() {
                return jobMessage;
            }

            public void setJobMessage(String jobMessage) {
                this.jobMessage = jobMessage;
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