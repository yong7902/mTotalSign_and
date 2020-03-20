package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 결재함 검색
 */
public class Res_AP_IF_020_VO extends statusVO {

    public class result extends resultVO {

        private String totalCnt;//전체 건수
        private ArrayList<apprList> apprList;
        public class apprList {

            String title;//	결재문서 타이틀
            String author;//		기안자
            String category;//		카테고리				P = 미결함, D = 기결함, R = 기안함, C = 완료함
            String guid;//		guid				"MainDocID|DocID|chamjo|0 두번째 DocID 가 Key 입니다.세번째 chamjo 가 1 이면 참조문서 입니다."
            String pubDate;//		기안일자
            String status;//		결재진행상태				예)완료/진행
            String hasattachYn;//		첨부파일 유무
            String hasopinionYn;//	의견 유무
            String isPublic;//		일반문서여부				Y:일반문서 N:기밀문서
            String inLine;//	열람여부				Y:열람가능 N:열람불가능

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getGuid() {
                return guid;
            }

            public void setGuid(String guid) {
                this.guid = guid;
            }

            public String getPubDate() {
                return pubDate;
            }

            public void setPubDate(String pubDate) {
                this.pubDate = pubDate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getHasattachYn() {
                return hasattachYn;
            }

            public void setHasattachYn(String hasattachYn) {
                this.hasattachYn = hasattachYn;
            }

            public String getHasopinionYn() {
                return hasopinionYn;
            }

            public void setHasopinionYn(String hasopinionYn) {
                this.hasopinionYn = hasopinionYn;
            }

            public String getIsPublic() {
                return isPublic;
            }

            public void setIsPublic(String isPublic) {
                this.isPublic = isPublic;
            }

            public String getInLine() {
                return inLine;
            }

            public void setInLine(String inLine) {
                this.inLine = inLine;
            }
        }

        public String getTotalCnt() {
            return totalCnt;
        }

        public void setTotalCnt(String totalCnt) {
            this.totalCnt = totalCnt;
        }

        public ArrayList<Res_AP_IF_020_VO.result.apprList> getApprList() {
            return apprList;
        }

        public void setApprList(ArrayList<Res_AP_IF_020_VO.result.apprList> apprList) {
            this.apprList = apprList;
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
