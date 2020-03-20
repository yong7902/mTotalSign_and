package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 멀티유저조회(로그인)
 */
public class Res_AP_IF_004_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<multiuserList> multiuserList;

        public class multiuserList implements Serializable {

            String userId;//	로그인아이디(주계정)
            String subUserId;//	멀티계정아이디
            String updateDt;//	등록일자
            String companyCd;//	회사코드
            String userName;//사용자명
            String deptCode;//부서코드
            String deptName;//	부서명
            String titleName;//	직책
            String companyName;//회사
            String roleName;//직급
            String delYn;//삭제 가능 여부

            public boolean isChecked = false;


            public String getDelYn() {
                return delYn;
            }

            public void setDelYn(String delYn) {
                this.delYn = delYn;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getSubUserId() {
                return subUserId;
            }

            public void setSubUserId(String subUserId) {
                this.subUserId = subUserId;
            }

            public String getUpdateDt() {
                return updateDt;
            }

            public void setUpdateDt(String updateDt) {
                this.updateDt = updateDt;
            }

            public String getCompanyCd() {
                return companyCd;
            }

            public void setCompanyCd(String companyCd) {
                this.companyCd = companyCd;
            }


            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
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

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getRoleName() {
                return roleName;
            }

            public void setRoleName(String roleName) {
                this.roleName = roleName;
            }
        }

        public ArrayList<Res_AP_IF_004_VO.result.multiuserList> getMultiuserList() {
            return multiuserList;
        }

        public void setMultiuserList(ArrayList<Res_AP_IF_004_VO.result.multiuserList> multiuserList) {
            this.multiuserList = multiuserList;
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