package com.kolon.sign2.approval;

import com.kolon.sign2.vo.Res_AP_IF_202_VO;
import com.kolon.sign2.vo.Res_AP_IF_203_VO;

/**
 * 결재선 추가 - 조직원 + 조직도
 */
public class ApprovalTotalVO {
    public boolean header;


    //AP_IF_202
    String ikenId;//	IKEN ID
    String name;//	임직원 이름
    String mobile;//	휴대폰 번호
    String email;//이메일ID
    String deptCode;//	부서코드
    String orgUnit;//부서명
    String jobTitle;//	직위
    String roleName;//	직책

    //AP_IF_203
    String deptCd;//	부서코드
    String deptName;//	부서명
    String parentCd;//	상위부서코드
    String childDept;//	하위 부서 여부

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
