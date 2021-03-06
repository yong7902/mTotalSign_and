package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

public class Res_AP_IF_101_VO extends statusVO {

    public class result extends resultVO {

        public userInfoClass getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(userInfoClass userInfo) {
            this.userInfo = userInfo;
        }

        public ArrayList<Res_AP_IF_101_VO.result.sysArray> getSysArray() {
            return sysArray;
        }

        public void setSysArray(ArrayList<Res_AP_IF_101_VO.result.sysArray> sysArray) {
            this.sysArray = sysArray;
        }

        private userInfoClass userInfo;

        public class userInfoClass implements Serializable{
            public String getMdmYn() {
                return mdmYn;
            }

            public void setMdmYn(String mdmYN) {
                this.mdmYn = mdmYn;
            }

            String mdmYn;//mdm 적용여부
        }

        private ArrayList<sysArray> sysArray;

        public class sysArray implements Serializable {

            String sysId;//	시스템 아이디
            String sysName;//시스템 이름
            String sysIcon;//시스템 아이콘URL
            String sysOrder;//	시스템 정렬순서
            String sysPushYn;//	시스템 푸시 알림 여부
            String autoTabYn;//	업무 리스트 구성 시 TAB을 자동으로 그릴 지 여부
            String sysIconYn;//	시스템 아이콘 유무

            public String getSysId() {
                return sysId;
            }

            public void setSysId(String sysId) {
                this.sysId = sysId;
            }

            public String getSysName() {
                return sysName;
            }

            public void setSysName(String sysName) {
                this.sysName = sysName;
            }

            public String getSysIcon() {
                return sysIcon;
            }

            public void setSysIcon(String sysIcon) {
                this.sysIcon = sysIcon;
            }

            public String getSysOrder() {
                return sysOrder;
            }

            public void setSysOrder(String sysOrder) {
                this.sysOrder = sysOrder;
            }

            public String getSysPushYn() {
                return sysPushYn;
            }

            public void setSysPushYn(String sysPushYN) {
                this.sysPushYn = sysPushYn;
            }

            public String getAutoTabYn() {
                return autoTabYn;
            }

            public void setAutoTabYn(String autoTabYn) {
                this.autoTabYn = autoTabYn;
            }

            public String getSysIconYn() {
                return sysIconYn;
            }

            public void setSysIconYn(String sysIconYn) {
                this.sysIconYn = sysIconYn;
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