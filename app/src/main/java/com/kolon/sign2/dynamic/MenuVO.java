package com.kolon.sign2.dynamic;

import com.kolon.sign2.vo.Res_AP_IF_102_VO;

import java.util.ArrayList;

public class MenuVO {
    private String sysId;
    private String menuId;
    private String menuName;
    private String countNum;
    private String autoDetailYN;
    private String autoListYN;

    private ArrayList<Res_AP_IF_102_VO.result.menuArray> childList;

    public MenuVO(String sysId, String menuId, String menuName, String countNum, String autoDetailYN, String autoListYN) {
        this.sysId = sysId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.countNum = countNum;
        this.autoDetailYN = autoDetailYN;
        this.autoListYN = autoListYN;
        this.childList = new ArrayList<>();
    }

    public String getCountNum() {
        return countNum;
    }

    public void setCountNum(String countNum) {
        this.countNum = countNum;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public ArrayList<Res_AP_IF_102_VO.result.menuArray> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<Res_AP_IF_102_VO.result.menuArray> childList) {
        this.childList = childList;
    }

    public boolean isExistChild() {
        if (childList.isEmpty()) return false;
        else return true;
    }

    public String checkChildViewType() {
        if (isExistChild()) return childList.get(0).getFilterTabOption();
        else return "";
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getAutoDetailYN() {
        return autoDetailYN;
    }

    public void setAutoDetailYN(String autoDetailYN) {
        this.autoDetailYN = autoDetailYN;
    }

    public String getAutoListYN() {
        return autoListYN;
    }

    public void setAutoListYN(String autoListYN) {
        this.autoListYN = autoListYN;
    }
}
