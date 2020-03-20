package com.kolon.sign2.vo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 전자결재_부서함목록조회&미결건수조회
 */
public class Res_AP_IF_012_VO extends statusVO {

    public class result extends resultVO {

        private ArrayList<apprDeptList> apprDeptList;

        public class apprDeptList implements Serializable {

            String containerId;//	문서함 ID				"- 개인(P = 미결함, D = 기결함, R = 기안함, C = 완료함) - 부서(서버에서 내려주는 category)"
            String containerName;//	문서함 명				ContainerID가 존재하면 부서 문서함 건수 조회
            String category;//	문서함 타입				ContainerID가 존재하면 부서 문서함 건수 조회
            String depth;//Depth
            String subQuery;//	서브쿼리(전자결재 서버에서 사용)
            String cnt;//미결건수

            public String fixQuery ="";//서브쿼리형식에서 안쪽 숫자만 빼넣어서 비교가 편하게 할 용도. ex) FormID IN ('2016000387') 형에서 숫자인 2016000387만 집어넣어서 containerid등을 비교할용도

            public String getContainerId() {
                return containerId;
            }

            public void setContainerId(String containerId) {
                this.containerId = containerId;
            }

            public String getContainerName() {
                return containerName;
            }

            public void setContainerName(String containerName) {
                this.containerName = containerName;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getDepth() {
                return depth;
            }

            public void setDepth(String depth) {
                this.depth = depth;
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
        }

        public ArrayList<Res_AP_IF_012_VO.result.apprDeptList> getApprDeptList() {
            return apprDeptList;
        }

        public void setApprDeptList(ArrayList<Res_AP_IF_012_VO.result.apprDeptList> apprDeptList) {
            this.apprDeptList = apprDeptList;
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
