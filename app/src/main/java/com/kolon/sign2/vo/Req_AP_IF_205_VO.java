package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 최근 검색 요청
 * */
public class Req_AP_IF_205_VO {

    ArrayList<apprLatelyList> apprLatelyList;

    public class apprLatelyList {

        String userAccount;//	IKENID
        String companyCd;//	회사코드
        String deptCd;//	부서코드

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getCompanyCd() {
            return companyCd;
        }

        public void setCompanyCd(String companyCd) {
            this.companyCd = companyCd;
        }

        public String getDeptCd() {
            return deptCd;
        }

        public void setDeptCd(String deptCd) {
            this.deptCd = deptCd;
        }
    }

    public ArrayList<Req_AP_IF_205_VO.apprLatelyList> getApprLatelyList() {
        return apprLatelyList;
    }

    public void setApprLatelyList(ArrayList<Req_AP_IF_205_VO.apprLatelyList> apprLatelyList) {
        this.apprLatelyList = apprLatelyList;
    }

}
