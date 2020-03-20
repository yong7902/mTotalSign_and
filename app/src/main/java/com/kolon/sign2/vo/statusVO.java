package com.kolon.sign2.vo;

import java.io.Serializable;

/**
 * status 공통부분
 * */
public class statusVO implements Serializable {

    private status status;

    public status getStatus() {
        return status;
    }

    public void setStatus(status status) {
        this.status = status;
    }


    public class status implements Serializable{

        String statusCd;
        String statusMssage;

        public String getStatusCd() {
            return statusCd;
        }

        public void setStatusCd(String statusCd) {
            this.statusCd = statusCd;
        }

        public String getStatusMssage() {
            return statusMssage;
        }

        public void setStatusMssage(String statusMssage) {
            this.statusMssage = statusMssage;
        }
    }
}
