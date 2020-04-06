package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 전자결재 조직원목록
 */
public class Res_AP_IF_203_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprSpList> apprSpList;

        public class apprSpList implements Serializable {

            String ikenId;//	IKEN ID
            String name;//	임직원 이름
            String mobile;//	휴대폰 번호
            String email;//이메일ID
            String deptCode;//	부서코드
            String orgUnit;//부서명
            String jobTitle;//	직위
            String roleName;//	직책

            public String companyCd ="";//회사 코드 -최근검색 전용
            public String orgName ="";

            public String getIkenId() {
                return ikenId;
            }

            public void setIkenId(String ikenId) {
                this.ikenId = ikenId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getDeptCode() {
                return deptCode;
            }

            public void setDeptCode(String deptCode) {
                this.deptCode = deptCode;
            }

            public String getOrgUnit() {
                return orgUnit;
            }

            public void setOrgUnit(String orgUnit) {
                this.orgUnit = orgUnit;
            }

            public String getJobTitle() {
                return jobTitle;
            }

            public void setJobTitle(String jobTitle) {
                this.jobTitle = jobTitle;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }
        }

        public ArrayList<Res_AP_IF_203_VO.result.apprSpList> getApprSpList() {
            return apprSpList;
        }

        public void setApprSpList(ArrayList<Res_AP_IF_203_VO.result.apprSpList> apprSpList) {
            this.apprSpList = apprSpList;
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