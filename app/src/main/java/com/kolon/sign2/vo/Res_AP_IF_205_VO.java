package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 최근조회검색
 */
public class Res_AP_IF_205_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprLatelyList> apprLatelyList;

        public class apprLatelyList {

            String userAccount;//	IKEN ID
            String companyCd;//회사코드
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

        public ArrayList<Res_AP_IF_205_VO.result.apprLatelyList> getApprLatelyList() {
            return apprLatelyList;
        }

        public void setApprLatelyList(ArrayList<Res_AP_IF_205_VO.result.apprLatelyList> apprLatelyList) {
            this.apprLatelyList = apprLatelyList;
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