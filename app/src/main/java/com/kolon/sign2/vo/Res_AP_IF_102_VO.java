package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 응답 - 시스템 메뉴 조회
 */
public class Res_AP_IF_102_VO extends statusVO {

    public static class result extends resultVO {

        public ArrayList<Res_AP_IF_102_VO.result.menuArray> getMenuArray() {
            return menuArray;
        }

        public void setMenuArray(ArrayList<Res_AP_IF_102_VO.result.menuArray> menuArray) {
            this.menuArray = menuArray;
        }

        private ArrayList<menuArray> menuArray;

        public static class menuArray implements Serializable {
            public String getSysId() {
                return sysId;
            }

            public void setSysId(String sysId) {
                this.sysId = sysId;
            }

            public String getMenuId() {
                return menuId;
            }

            public void setMenuId(String menuId) {
                this.menuId = menuId;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public String getMenuOrder() {
                return menuOrder;
            }

            public void setMenuOrder(String menuOrder) {
                this.menuOrder = menuOrder;
            }

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getParentMenuId() {
                return parentMenuId;
            }

            public void setParentMenuId(String parentMenuId) {
                this.parentMenuId = parentMenuId;
            }


            public String getCountNum() {
                return countNum;
            }

            public void setCountNum(String countNum) {
                this.countNum = countNum;
            }

            public String getAutoListYn() {
                return autoListYn;
            }

            public void setAutoListYn(String autoListYn) {
                this.autoListYn = autoListYn;
            }

            public String getAutoDetailYn() {
                return autoDetailYn;
            }

            public void setAutoDetailYn(String autoDetailYn) {
                this.autoDetailYn = autoDetailYn;
            }

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public boolean isGroupTitle() {
                return isGroupTitle;
            }

            public void setGroupTitle(boolean groupTitle) {
                isGroupTitle = groupTitle;
            }

            public String getFilterTabOption() {
                return filterTabOption;
            }

            public void setFilterTabOption(String filterTabOption) {
                this.filterTabOption = filterTabOption;
            }

            public String getAttr01() {
                return attr01;
            }

            public void setAttr01(String attr01) {
                this.attr01 = attr01;
            }

            public String getAttr02() {
                return attr02;
            }

            public void setAttr02(String attr02) {
                this.attr02 = attr02;
            }

            public String getCountYn() {
                return countYn;
            }

            public void setCountYn(String countYn) {
                this.countYn = countYn;
            }

            public String getBadgeYn() {
                return badgeYn;
            }

            public void setBadgeYn(String badgeYn) {
                this.badgeYn = badgeYn;
            }

            public String getApprovalIFId() {
                return approvalIFId;
            }

            public void setApprovalIFId(String approvalIFId) {
                this.approvalIFId = approvalIFId;
            }

            public String getRejectIFId() {
                return rejectIFId;
            }

            public void setRejectIFId(String rejectIFId) {
                this.rejectIFId = rejectIFId;
            }

            String sysId = "";//	시스템 ID
            String menuId = "";//	메뉴 ID
            String menuName = "";//	메뉴 명
            String menuOrder = "";//	메뉴 순서
            String groupName = "";//	그룹 이름 (ex: 전자결재-개인결재함-미결함일 경우 > 개인결재함)
            String parentMenuId = "";//		상위 메뉴 ID, 최상단(시스템 바로 아래) 일 경우 공백
            String countYn = "";//	메뉴트리에서 건수 표시 유무
            String badgeYn = "";//  리스트 탭에서 배지 표시 유무
            String countNum = "";//	건수, countYN=Y 일 경우 필수
            String autoListYn = "";//		동적화면구성 리스트화면 유무
            String autoDetailYn = "";//		동적화면구성 상세화면 유무
            String approvalIFId = "";//		승인 IF명, autoDetailYN=Y 일 경우 필수
            String rejectIFId = "";//	반려 IF명, autoDetailYN=Y 일 경우 필수
            String filterTabOption = "";//	"탭 구성 시 필터 형식일 경우 종류 기술  (menu : 팝업 선택 방식, list : 우측 선택)"
            String attr01;//	전자결재 부서결재함(containerId)
            String attr02;//	전자결재 부서결재함(subQuery)


            public boolean isSelected = false;//list click tag
            public boolean isGroupTitle = false;//좌측메뉴용
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