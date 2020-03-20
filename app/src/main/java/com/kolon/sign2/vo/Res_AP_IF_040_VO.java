package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 위임자목록
 */
public class Res_AP_IF_040_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<dgtnList> dgtnList;

        public class dgtnList {
            String userId;
            String userNm;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserNm() {
                return userNm;
            }

            public void setUserNm(String userNm) {
                this.userNm = userNm;
            }
        }

        public ArrayList<Res_AP_IF_040_VO.result.dgtnList> getDgtnList() {
            return dgtnList;
        }

        public void setDgtnList(ArrayList<Res_AP_IF_040_VO.result.dgtnList> dgtnList) {
            this.dgtnList = dgtnList;
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