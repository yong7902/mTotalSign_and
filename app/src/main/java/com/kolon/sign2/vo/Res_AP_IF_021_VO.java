package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 개인,부서함 미결건수
 * */
public class Res_AP_IF_021_VO extends statusVO{
    private result result;
    public Res_AP_IF_021_VO.result getResult() {
        return result;
    }

    public void setResult(Res_AP_IF_021_VO.result result) {
        this.result = result;
    }

    public class result extends resultVO{

        public ArrayList<Res_AP_IF_021_VO.result.apprCntList> getApprCntList() {
            return apprCntList;
        }

        public void setApprCntList(ArrayList<Res_AP_IF_021_VO.result.apprCntList> apprCntList) {
            this.apprCntList = apprCntList;
        }

        private ArrayList<apprCntList> apprCntList;



        public class apprCntList {
            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getContainerId() {
                return containerId;
            }

            public void setContainerId(String containerId) {
                this.containerId = containerId;
            }

            public String getSubQuery() {
                return subQuery;
            }

            public void setSubQuery(String subQuery) {
                this.subQuery = subQuery;
            }

            public String getCnt() {
                return cnt;
            }

            public void setCnt(String cnt) {
                this.cnt = cnt;
            }

            String category;
            String containerId;
            String subQuery;
            String cnt;
        }
    }

}
