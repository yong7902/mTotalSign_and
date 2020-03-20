package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 서비스데스크 권한목록
 */
public class Res_AP_IF_037_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<aprList> aprList;

        public class aprList {

            String cnt;//	미결건수	미결건수
            String docNo;//		결재 문서 고유번호	결재 문서 고유번호
            String title;//		[신청유형]시스템 요청권한 사용자(부서) 사유	[신청유형]시스템 요청권한 사용자(부서) 사유
            String reqInfo;//		요청일 시스템 요청자(부서)	요청일 시스템 요청자(부서)

            public boolean isChecked = false;

            public String getCnt() {
                return cnt;
            }

            public void setCnt(String cnt) {
                this.cnt = cnt;
            }

            public String getDocNo() {
                return docNo;
            }

            public void setDocNo(String docNo) {
                this.docNo = docNo;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getReqInfo() {
                return reqInfo;
            }

            public void setReqInfo(String reqInfo) {
                this.reqInfo = reqInfo;
            }
        }

        public ArrayList<Res_AP_IF_037_VO.result.aprList> getAprList() {
            return aprList;
        }

        public void setAprList(ArrayList<Res_AP_IF_037_VO.result.aprList> aprList) {
            this.aprList = aprList;
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