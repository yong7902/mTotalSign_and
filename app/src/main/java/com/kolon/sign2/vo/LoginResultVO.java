package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * Created by sunho_km on 2019. 02. 07..
 */
public class LoginResultVO {

    public session session = new session();
    public role role = new role();
    public ArrayList<rulesOfUser> rulesOfUser = new ArrayList<>();

    public String account = "";                 // account
    public String name = "";                    // 사용자이름
    public String groupName = "";              //
    public String mobileNo = "";               // 이동전화번호
    public String telNo = "";                  // 사내전화번호
    public String faxNo = "";                  // 팩스번호
    public String email = "";                  // 회사이름
    public String companyCode = "";           // 회사코드
    public String companyName = "";           // 팀명
    public String departmentCode = "";       // 부서코드
    public String departmentName = "";       // 부서명
    public String jobTitle = "";              // 직급
    public String mKolon = "";                //
    public String roleName = "";              // 직책
    public String task = "";                  // 담당업무
    public String statusMessage = "";        //
    public String profileUrl = "";            // 프로필URL
    public String fmcNumber = "";             //
    public String createdAt = "";             //
    public String updatedAt = "";             //


    public LoginResultVO.session getSession() {
        return session;
    }

    public void setSession(LoginResultVO.session session) {
        this.session = session;
    }

    public LoginResultVO.role getRole() {
        return role;
    }

    public void setRole(LoginResultVO.role role) {
        this.role = role;
    }

    public ArrayList<LoginResultVO.rulesOfUser> getRulesOfUser() {
        return rulesOfUser;
    }

    public void setRulesOfUser(ArrayList<LoginResultVO.rulesOfUser> rulesOfUser) {
        this.rulesOfUser = rulesOfUser;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getFaxNo() {
        return faxNo;
    }

    public void setFaxNo(String faxNo) {
        this.faxNo = faxNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getmKolon() {
        return mKolon;
    }

    public void setmKolon(String mKolon) {
        this.mKolon = mKolon;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getFmcNumber() {
        return fmcNumber;
    }

    public void setFmcNumber(String fmcNumber) {
        this.fmcNumber = fmcNumber;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class session {
        public String key = "";                 // //Session Key - login이후 후속 요청에 사용.
        public String expiresAt = "";          // ISO8601 timestamp - 인증 파기 시점(유효기간)
        public String deviceUUID = "";         //
        public String appName = "";            //

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getExpiresAt() {
            return expiresAt;
        }

        public void setExpiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
        }
    }

    public static class role {
        public String groupWideAdmin = "";                 // 그루 Addmin 유무

        public String getGroupWideAdmin() {
            return groupWideAdmin;
        }

        public void setGroupWideAdmin(String groupWideAdmin) {
            this.groupWideAdmin = groupWideAdmin;
        }
    }

    public static class rulesOfUser {
        public String appName = "";                 // Application 이름
        public String usageRule = "";               // 사용 권한 (application의 사용 권한)

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getUsageRule() {
            return usageRule;
        }

        public void setUsageRule(String usageRule) {
            this.usageRule = usageRule;
        }
    }

}
