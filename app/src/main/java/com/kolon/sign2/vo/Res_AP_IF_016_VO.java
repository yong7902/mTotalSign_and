package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 전자 결재 상세 - 서버결과
 */
public class Res_AP_IF_016_VO extends statusVO {

    public class result extends resultVO implements Serializable {

        private apprDetailItem apprDetailItem;
        private ArrayList<apprAttFileList> apprAttFileList;
        private ArrayList<apprLineList> apprLineList;

        public ArrayList<result.apprLineList> getApprLineList() {
            return apprLineList;
        }

        public void setApprLineList(ArrayList<result.apprLineList> apprLineList) {
            this.apprLineList = apprLineList;
        }

        public result.apprDetailItem getApprDetailItem() {
            return apprDetailItem;
        }

        public void setApprDetailItem(result.apprDetailItem apprDetailItem) {
            this.apprDetailItem = apprDetailItem;
        }

        public ArrayList<result.apprAttFileList> getApprAttFileList() {
            return apprAttFileList;
        }

        public void setApprAttFileList(ArrayList<result.apprAttFileList> apprAttFileList) {
            this.apprAttFileList = apprAttFileList;
        }


        public class apprLineList implements Serializable {

            String actionTime;//	Action Time
            String actionType;//	Action Type
            String activity;//	Activity
            String company;//회사
            String department;//	부서
            String name;//	성명
            String position;//	직급
            String sn;//순번
            String comment;//	의견
            String IsProposerYn;//	변경불가유무
            String userId;//사용자ID
            String deptId;//	부서ID
            String aprtype;//	Action Type 코드

            public String dragId;
            public boolean isOpend = false;

            public String getActionTime() {
                return actionTime;
            }

            public void setActionTime(String actionTime) {
                this.actionTime = actionTime;
            }

            public String getActionType() {
                return actionType;
            }

            public void setActionType(String actionType) {
                this.actionType = actionType;
            }

            public String getActivity() {
                return activity;
            }

            public void setActivity(String activity) {
                this.activity = activity;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getDepartment() {
                return department;
            }

            public void setDepartment(String department) {
                this.department = department;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPosition() {
                return position;
            }

            public void setPosition(String position) {
                this.position = position;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getIsProposerYn() {
                return IsProposerYn;
            }

            public void setIsProposerYn(String isProposerYn) {
                IsProposerYn = isProposerYn;
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

            public String getAprtype() {
                return aprtype;
            }

            public void setAprtype(String aprtype) {
                this.aprtype = aprtype;
            }
        }

        public class apprAttFileList implements Serializable {
            String length;//	첨부문서 length(사이즈)
            String uid;//	첨부문서 문서명
            String url;//	첨부문서 url(경로)
            String value;//	첨부ID

            public String getLength() {
                return length;
            }

            public void setLength(String length) {
                this.length = length;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public class apprDetailItem implements Serializable {

            String title;//	결재문서 타이틀
            String author;//	기안자
            String category;//	카테고리
            String guid;//guid
            String pubDate;//	기안일자
            String status;//결재진행상태
            String misform;//
            String misformmsg;//
            String signCount;//결재칸수
            String hapyuiCount;//	개인협조칸수
            String dhapyuiCount;//	부서협조칸수
            String state;//결재타입
            String aprstate;//결재상태
            String processorid;//보류자ID
            String writerdeptname;//기안자부서명
            String apporvalPlag;//처리버튼 노출유무 Y인경우 노출
            String writerId;//id

            public String getWriterId() {
                return writerId;
            }

            public void setWriterId(String writerId) {
                this.writerId = writerId;
            }

            public String getApporvalPlag() {
                return apporvalPlag;
            }

            public void setApporvalPlag(String apporvalPlag) {
                this.apporvalPlag = apporvalPlag;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getGuid() {
                return guid;
            }

            public void setGuid(String guid) {
                this.guid = guid;
            }

            public String getPubDate() {
                return pubDate;
            }

            public void setPubDate(String pubDate) {
                this.pubDate = pubDate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getMisform() {
                return misform;
            }

            public void setMisform(String misform) {
                this.misform = misform;
            }

            public String getMisformmsg() {
                return misformmsg;
            }

            public void setMisformmsg(String misformmsg) {
                this.misformmsg = misformmsg;
            }

            public String getSignCount() {
                return signCount;
            }

            public void setSignCount(String signCount) {
                this.signCount = signCount;
            }

            public String getHapyuiCount() {
                return hapyuiCount;
            }

            public void setHapyuiCount(String hapyuiCount) {
                this.hapyuiCount = hapyuiCount;
            }

            public String getDhapyuiCount() {
                return dhapyuiCount;
            }

            public void setDhapyuiCount(String dhapyuiCount) {
                this.dhapyuiCount = dhapyuiCount;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getAprstate() {
                return aprstate;
            }

            public void setAprstate(String aprstate) {
                this.aprstate = aprstate;
            }

            public String getProcessorid() {
                return processorid;
            }

            public void setProcessorid(String processorid) {
                this.processorid = processorid;
            }

            public String getWriterdeptname() {
                return writerdeptname;
            }

            public void setWriterdeptname(String writerdeptname) {
                this.writerdeptname = writerdeptname;
            }
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
