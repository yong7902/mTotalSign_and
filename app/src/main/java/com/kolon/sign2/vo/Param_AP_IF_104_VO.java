package com.kolon.sign2.vo;

/**
 * Created by kiyeon_kim on 2019. 12. 09
 */
public class Param_AP_IF_104_VO {
    private String userId; //사용자 아이디
    private String sysId; // 시스템 아이디
    private String menuId; //메뉴 아이디
    private String id; //결제 아이디


    public Param_AP_IF_104_VO(String userId, String sysId, String menuId, String id) {
        this.userId = userId;
        this.sysId = sysId;
        this.menuId = menuId;
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public String getSysId() {
        return sysId;
    }

    public String getMenuId() {
        return menuId;
    }

    public String getId() {
        return id;
    }
}
