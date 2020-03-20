package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 전자결재 회사코드전체조회
 * */
public class Res_AP_IF_201_VO extends statusVO {

    public class result extends resultVO{

        private ArrayList<apprCompList> apprCompList;
        public class apprCompList{

            String name;//	회사명
            String companyCd;//	회사코드
            String status;//	상태
            String sort;//	순서

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCompanyCd() {
                return companyCd;
            }

            public void setCompanyCd(String companyCd) {
                this.companyCd = companyCd;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getSort() {
                return sort;
            }

            public void setSort(String sort) {
                this.sort = sort;
            }
        }

        public ArrayList<Res_AP_IF_201_VO.result.apprCompList> getApprCompList() {
            return apprCompList;
        }

        public void setApprCompList(ArrayList<Res_AP_IF_201_VO.result.apprCompList> apprCompList) {
            this.apprCompList = apprCompList;
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