package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 전자결재 목록
 * */
public class Res_AP_IF_013_VO extends statusVO{

    private result result;

    public Res_AP_IF_013_VO.result getResult() {
        return result;
    }

    public void setResult(Res_AP_IF_013_VO.result result) {
        this.result = result;
    }

    public class result extends resultVO{
        String totalCnt;
        ArrayList<apprList> apprList;

        public ArrayList<Res_AP_IF_013_VO.result.apprList> getApprList() {
            return apprList;
        }

        public void setApprList(ArrayList<Res_AP_IF_013_VO.result.apprList> apprList) {
            this.apprList = apprList;
        }

        public String getTotalCnt() {
            return totalCnt;
        }

        public void setTotalCnt(String totalCnt) {
            this.totalCnt = totalCnt;
        }

        public class apprList implements Serializable {

            String title;
            String author;
            String guid;
            String pubDate;
            String status;
            String hasattachYn;
            String hasopinionYn;
            String isPublic;
            String inLine;
            String category;

            public boolean isHeader = false;

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

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }
    }
}
