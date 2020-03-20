package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 - 결재선저장 요청
 */
public class Req_AP_IF_022_VO {

    private String userId;//	사용자ID
    private String wfDocId;//	결재마스터ID
    private String companyCd;//	회사코드
    private ArrayList<approvalLine> approvalLine;

    public class approvalLine {

        String userNum;//	순번
        String userId;//Iken ID
        String deptId;//	부서 ID
        String aprType;//	결재타입				일반결재, 개인협조, 개인협조(병렬), 부서협조, 부서협조(병렬)) 등등.
        String aprState;//	결재상태				(진행, 대기) [접수자만 진행으로 주시면 됩니다.

        public String getUserNum() {
            return userNum;
        }

        public void setUserNum(String userNum) {
            this.userNum = userNum;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getAprType() {
            return aprType;
        }

        public void setAprType(String aprType) {
            this.aprType = aprType;
        }

        public String getAprState() {
            return aprState;
        }

        public void setAprState(String aprState) {
            this.aprState = aprState;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWfDocId() {
        return wfDocId;
    }

    public void setWfDocId(String wfDocId) {
        this.wfDocId = wfDocId;
    }

    public String getCompanyCd() {
        return companyCd;
    }

    public void setCompanyCd(String companyCd) {
        this.companyCd = companyCd;
    }

    public ArrayList<Req_AP_IF_022_VO.approvalLine> getApprovalLine() {
        return approvalLine;
    }

    public void setApprovalLine(ArrayList<Req_AP_IF_022_VO.approvalLine> approvalLine) {
        this.approvalLine = approvalLine;
    }

}
