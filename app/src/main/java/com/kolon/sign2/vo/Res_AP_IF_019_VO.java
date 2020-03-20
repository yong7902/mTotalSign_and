package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 첨부 파일
 */
public class Res_AP_IF_019_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprAttFileList> apprAttFileList;
        public ArrayList<Res_AP_IF_019_VO.result.apprAttFileList> getApprAttFileList() {
            return apprAttFileList;
        }

        public void setApprAttFileList(ArrayList<Res_AP_IF_019_VO.result.apprAttFileList> apprAttFileList) {
            this.apprAttFileList = apprAttFileList;
        }

        public class apprAttFileList {

            String wfInstanceId;//	WFInstanceID
            String originFilePath;//	첨부파일 경로
            String fileName;//	첨부파일 명
            String norder;//첨부파일 순서

            public String getWfInstanceId() {
                return wfInstanceId;
            }

            public void setWfInstanceId(String wfInstanceId) {
                this.wfInstanceId = wfInstanceId;
            }

            public String getOriginFilePath() {
                return originFilePath;
            }

            public void setOriginFilePath(String originFilePath) {
                this.originFilePath = originFilePath;
            }

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getNorder() {
                return norder;
            }

            public void setNorder(String norder) {
                this.norder = norder;
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