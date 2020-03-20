package com.kolon.sign2.vo;

public class Res_AP_IF_107_VO extends statusVO {

    public class result extends resultVO {

        public String getCompanyCd() {
            return companyCd;
        }

        public void setCompanyCd(String companyCd) {
            this.companyCd = companyCd;
        }

        public String companyCd;
    }


    private result result;

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }
}