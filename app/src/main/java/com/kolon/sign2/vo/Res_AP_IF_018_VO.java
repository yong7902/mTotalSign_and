package com.kolon.sign2.vo;

/**
 * 전자결재 본문보기
 * */
public class Res_AP_IF_018_VO extends statusVO{

    public class result {

        public Res_AP_IF_018_VO.result.apprBody getApprBody() {
            return apprBody;
        }

        public void setApprBody(Res_AP_IF_018_VO.result.apprBody apprBody) {
            this.apprBody = apprBody;
        }

        private apprBody apprBody;

        public class apprBody {
            private String bodyimage;

            public String getBodyimage() {
                return bodyimage;
            }

            public void setBodyimage(String bodyimage) {
                this.bodyimage = bodyimage;
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