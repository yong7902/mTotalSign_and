package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 조직도조회
 */
public class Res_AP_IF_202_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprOrganizationList> apprOrganizationList;

        public class apprOrganizationList {

            String deptCd;//	부서코드
            String deptName;//	부서명
            String parentCd;//	상위부서코드
            String childDept;//	하위 부서 여부

            public String getDeptCd() {
                return deptCd;
            }

            public void setDeptCd(String deptCd) {
                this.deptCd = deptCd;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getParentCd() {
                return parentCd;
            }

            public void setParentCd(String parentCd) {
                this.parentCd = parentCd;
            }

            public String getChildDept() {
                return childDept;
            }

            public void setChildDept(String childDept) {
                this.childDept = childDept;
            }
        }

        public ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList> getApprOrganizationList() {
            return apprOrganizationList;
        }

        public void setApprOrganizationList(ArrayList<Res_AP_IF_202_VO.result.apprOrganizationList> apprOrganizationList) {
            this.apprOrganizationList = apprOrganizationList;
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