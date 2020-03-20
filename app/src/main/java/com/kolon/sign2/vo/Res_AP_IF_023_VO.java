package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 - 사위조직도조회
 */
public class Res_AP_IF_023_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprApParentList> apprApParentList;

        public class apprApParentList {

            String deptCd;//	부서코드				CompanyCD 와 DEPTCD 가 같으면 최상위 조직
            String deptName;//	부서명
            String parentCd;    //상위부서코드
            String path;//
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

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getChildDept() {
                return childDept;
            }

            public void setChildDept(String childDept) {
                this.childDept = childDept;
            }
        }

        public ArrayList<Res_AP_IF_023_VO.result.apprApParentList> getApprApParentList() {
            return apprApParentList;
        }

        public void setApprApParentList(ArrayList<Res_AP_IF_023_VO.result.apprApParentList> apprApParentList) {
            this.apprApParentList = apprApParentList;
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