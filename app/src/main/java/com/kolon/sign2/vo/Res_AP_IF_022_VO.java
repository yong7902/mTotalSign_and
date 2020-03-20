package com.kolon.sign2.vo;

/**
 * 결재선 저장
 * */
public class Res_AP_IF_022_VO extends statusVO{
    private result result;

    public class result extends resultVO{
    }

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }
}
