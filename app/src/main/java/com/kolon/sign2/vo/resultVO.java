package com.kolon.sign2.vo;

import java.io.Serializable;

/**
 * result 공통 부분
 * */
public class resultVO implements Serializable {

    String errorCd;
    String errorMsg;

    public String getErrorCd() {
        return errorCd;
    }

    public void setErrorCd(String errorCd) {
        this.errorCd = errorCd;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
