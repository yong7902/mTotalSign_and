package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 메뉴 순서 저장
 */
public class Req_AP_IF_108_VO {

    private String userId;
    private ArrayList<menuOrder> menuOrder;

    public class menuOrder {

        String sysId;//	시스템ID
        String orderBy;//	순서

        public String getSysId() {
            return sysId;
        }

        public void setSysId(String sysId) {
            this.sysId = sysId;
        }

        public String getOrderBy() {
            return orderBy;
        }

        public void setOrderBy(String orderBy) {
            this.orderBy = orderBy;
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Req_AP_IF_108_VO.menuOrder> getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(ArrayList<Req_AP_IF_108_VO.menuOrder> menuOrder) {
        this.menuOrder = menuOrder;
    }

}
