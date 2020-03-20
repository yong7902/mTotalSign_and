package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 임직원검색
 */
public class Res_AP_IF_204_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprSpSearchList> apprSpSearchList;

        public class apprSpSearchList {

            String userAccount;//IKEN ID
            String userName;//	임직원 이름
            String mobileNum;//휴대폰 번호
            String email;//이메일ID
            String companyName;//회사명
            String deptCode;//	부서코드
            String deptName;//부서명
            String titleName;//	직위
            String roleName;//직책
            String companyCd;//

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getMobileNum() {
                return mobileNum;
            }

            public void setMobileNum(String mobileNum) {
                this.mobileNum = mobileNum;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getDeptCode() {
                return deptCode;
            }

            public void setDeptCode(String deptCode) {
                this.deptCode = deptCode;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getTitleName() {
                return titleName;
            }

            public void setTitleName(String titleName) {
                this.titleName = titleName;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }

            public String getCompanyCd() {
                return companyCd;
            }

            public void setCompanyCd(String companyCd) {
                this.companyCd = companyCd;
            }

        }

        public ArrayList<Res_AP_IF_204_VO.result.apprSpSearchList> getApprSpSearchList() {
            return apprSpSearchList;
        }

        public void setApprSpSearchList(ArrayList<Res_AP_IF_204_VO.result.apprSpSearchList> apprSpSearchList) {
            this.apprSpSearchList = apprSpSearchList;
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