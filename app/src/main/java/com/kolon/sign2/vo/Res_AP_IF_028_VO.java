package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 서비스데스크 상세 - 일반 보안 it
 */
public class Res_AP_IF_028_VO extends statusVO {

    public class result extends resultVO {

        public ArrayList<Res_AP_IF_028_VO.result.approvalDetail> getApprovalDetail() {
            return approvalDetail;
        }

        public void setApprovalDetail(ArrayList<Res_AP_IF_028_VO.result.approvalDetail> approvalDetail) {
            this.approvalDetail = approvalDetail;
        }

        private ArrayList<approvalDetail> approvalDetail;

        public class approvalDetail {
            String procId;//요청번호	String	요청번호

            public String getProcess() {
                return process;
            }

            public void setProcess(String process) {
                this.process = process;
            }

            public String getReqgubun() {
                return reqgubun;
            }

            public void setReqgubun(String reqgubun) {
                this.reqgubun = reqgubun;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getCustomerTitle() {
                return customerTitle;
            }

            public void setCustomerTitle(String customerTitle) {
                this.customerTitle = customerTitle;
            }

            String process;//	요청/변경 구분 프로세스	String	요청/변경 구분 프로세스
            String reqgubun;//	요청/변경 구분 프로세스 명	String	요청/변경 구분 프로세스
            String category;//요청구분	String	요청구분
            String phaseStatus;//단계/상태	String	단계/상태
            String customer;//요청자	String	요청자
            String customerId;//	요청자 IKENID
            String customerTitle;//	요청자 직급

            String customerCompany;//요청자 회사	String	요청자 회사
            String customerDepart;//	요청자 부서	String	요청자 부서
            String customerMobile;//	요청자 모바일	String	요청자 모바일
            String company;//대상회사	String	대상회사
            String emergencyType;//긴급유형	String	긴급유형
            String offeringName;//오퍼링	String	오퍼링
            String assigngroup;//처리그룹		처리그룹
            String assignee;//	처리자	String	처리자
            String expectDate;//희망완료일	String	희망완료일
            String title;//요청제목	String	요청제목
            String content;//요청내역	String	요청내역
            String solutionTypeL;//처리분류(대)	String	처리분류(대)
            String solutionTypeM;//처리분류(중)	String	처리분류(중)
            String resolveContent;//처리내역	String	처리내역
            String regDate;//	등록일시	String	등록일시
            String acceptDate;//	접수일시	String	접수일시

            //            String procId;//번호	String	번호
//            String phaseStatus;//단계/상태	String	단계/상태
//            String customer;//요청자	String	요청자
//            String customerCompany;//요청자 회사	String	요청자 회사
//            String customerDepart;//요청자 부서	String	요청자 부서
//            String customerMobile;//요청자 모바일	String	요청자 모바일
//            String company;//대상회사	String	대상회사
//            String offeringName;//	오퍼링	String	오퍼링
//            String assigngroup;//처리그룹	String	처리그룹
//            String assignee;//처리자	String	처리자
            String coType;//	변경유형	String	변경유형
            String coCategory;//	업무구분	String	업무구분
            String coClass;//변경분류	String	변경분류
            String coClassSub;//분류상세	String	분류상세
            String coGrade;//변경등급	String	변경등급
            //            String title;//요청제목	String	요청제목
//            String content;//	요청내역	String	요청내역
//            String regDate;//등록일시	String	등록일시
            String relPlanDate;//연관티켓납기일	String	연관티켓납기일
            String coPlanDate;//변경작업납기일	String	변경작업납기일
            String planEstDate;//	예상작업일	String	예상작업일
            String planDesc;//세부내역	String	세부내역
            String planDowntimeFlag;//	다운타임발생	String	다운타임발생
            String planDowntimeDate;//다운타임시간	String	다운타임시간
            String planTester;//테스트담당자	String	테스트담당자
            String planTestDate;//	테스트계획일	String	테스트계획일
            String planCustTestFlag;//사용자테스트	String	사용자테스트
            String planTestGoal;//	테스트목적	String	테스트목적
            String planTestScenario;//테스트시나리오	String	테스트시나리오
            String planImpact;//변경영향도	String	변경영향도
            String planCustImpactDesc;//고객영향내역	String	고객영향내역
            String planSysImpactDesc;//	시스템영향내역	String	시스템영향내역
            String planRollbackUser;//복구담당자	String	복구담당자
            String planRollbackDesc;//복구내용	String	복구내용
            String planApproveOpinion;//	검토의견	String	검토의견

            String seqNo;//SEQ	String	SEQ
            String reqName;//	신청자 (이름 / 부서)	String	신청자 (이름 / 부서)
            String reqDate;//요청일	String	요청일
            String detail;//요청내역 상세	String	요청내역 상세
            String reqGubun;//	요청구분 (VPN, PC반출 등)	String	요청구분 (VPN, PC반출 등)
            String appGubun;//승인구분 (팀장, 보안관리자 등)	String	승인구분 (팀장, 보안관리자 등)
            String appStatus;//승인상태 (승인요청, 처리완료 등)	String	승인상태 (승인요청, 처리완료 등)
            String onehrYn;//onehr건인지 구분필드	String	onehr건인지 구분필드
            String usedate1Code;//사용일자1_코드	String	사용일자1_코드
            String usedate1Name;//	사용일자1_이름	String	사용일자1_이름
            String usedate1From;//	사용일자1_시작	String	사용일자1_시작
            String usedate1To;//사용일자1_종료	String	사용일자1_종료
            String usedate2Code;//사용일자2_코드	String	사용일자2_코드
            String usedate2Name;//사용일자2_이름	String	사용일자2_이름
            String usedate2From;//사용일자2_시작	String	사용일자2_시작
            String usedate2To;//사용일자2_종료	String	사용일자2_종료

            //            String procId;//요청번호	String	요청번호
//            String category;//요청구분	String	요청구분
//            String phaseStatus;//	단계/상태	String	단계/상태
//            String customerCompany;//	요청자 회사	String	요청자 회사
//            String customerDepart;//요청자 부서	String	요청자 부서
//            String customerMobile;//	요청자 모바일	String	요청자 모바일
//            String company;//대상회사	String	대상회사
//            String emergencyType;//긴급유형	String	긴급유형
//            String offeringName;//오퍼링	String	오퍼링
//            String assigngroup;//처리그룹	String	처리그룹
//            String assignee;//	처리자	String	처리자
//            String expectDate;//희망완료일	String	희망완료일
//            String title;//요청제목	String	요청제목
//            String content;//요청내역	String	요청내역
//            String solutionTypeL;//처리분류(대)	String	처리분류(대)
//            String solutionTypeM;//처리분류(중)	String	처리분류(중)
//            String resolveContent;//처리내역	String	처리내역
//            String regDate;//등록일시	String	등록일시
//            String acceptDate;//접수일시	String	접수일시
            String wantedDate;//납기변경희망일자	String	납기변경희망일자
            String wantedComment;//납기변경사유	String	납기변경사유

            //            String procId;//요청번호	String	요청번호
//            String category;//	요청구분	String	요청구분
//            String phaseStatus;//단계/상태	String	단계/상태
//            String customer;//요청자	String	요청자
//            String customerCompany;//요청자 회사	String	요청자 회사
//            String customerDepart;//	요청자 부서	String	요청자 부서
//            String customerMobile;//요청자 모바일	String	요청자 모바일
//            String company;//대상회사	String	대상회사
//            String emergencyType;//	긴급유형	String	긴급유형
//            String offeringName;//오퍼링	String	오퍼링
//            String assigngroup;//처리그룹	String	처리그룹
//            String assignee;//처리자	String	처리자
//            String expectDate;//희망완료일	String	희망완료일
//            String title;//요청제목	String	요청제목
//            String content;//요청내역	String	요청내역
//            String solutionTypeL;//처리분류(대)	String	처리분류(대)
//            String solutionTypeM;//	처리분류(중)	String	처리분류(중)
//            String resolveContent;//	처리내역	String	처리내역
//            String regDate;//등록일시	String	등록일시
//            String acceptDate;//	접수일시	String	접수일시
            String requestDttm;//취소요청일시	String	취소요청일시
            String requestDesc;//	취소요청사유	String	취소요청사유

            //            String procId;//번호	String	번호
            String reqType;//	요청구분	String	요청구분
            //            String phaseStatus;//	단계/상태	String	단계/상태
//            String customer;//	요청자	String	요청자
//            String customerCompany;//요청자 회사	String	요청자 회사
//            String customerDepart;//	요청자 부서	String	요청자 부서
//            String customerMobile;//	요청자 모바일	String	요청자 모바일
//            String company;//	대상회사	String	대상회사
//            String assignee;//담당자(정)	String	담당자(정)
//            String title;//제목	String	제목
//            String content;//	내용	String	내용
            String hopeTime;//	희망완료일시	String	희망완료일시
//            String regDate;//	등록일시	String	등록일시

            public String getProcId() {
                return procId;
            }

            public void setProcId(String procId) {
                this.procId = procId;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public String getPhaseStatus() {
                return phaseStatus;
            }

            public void setPhaseStatus(String phaseStatus) {
                this.phaseStatus = phaseStatus;
            }

            public String getCustomer() {
                return customer;
            }

            public void setCustomer(String customer) {
                this.customer = customer;
            }

            public String getCustomerCompany() {
                return customerCompany;
            }

            public void setCustomerCompany(String customerCompany) {
                this.customerCompany = customerCompany;
            }

            public String getCustomerDepart() {
                return customerDepart;
            }

            public void setCustomerDepart(String customerDepart) {
                this.customerDepart = customerDepart;
            }

            public String getCustomerMobile() {
                return customerMobile;
            }

            public void setCustomerMobile(String customerMobile) {
                this.customerMobile = customerMobile;
            }

            public String getCompany() {
                return company;
            }

            public void setCompany(String company) {
                this.company = company;
            }

            public String getEmergencyType() {
                return emergencyType;
            }

            public void setEmergencyType(String emergencyType) {
                this.emergencyType = emergencyType;
            }

            public String getOfferingName() {
                return offeringName;
            }

            public void setOfferingName(String offeringName) {
                this.offeringName = offeringName;
            }

            public String getAssigngroup() {
                return assigngroup;
            }

            public void setAssigngroup(String assigngroup) {
                this.assigngroup = assigngroup;
            }

            public String getAssignee() {
                return assignee;
            }

            public void setAssignee(String assignee) {
                this.assignee = assignee;
            }

            public String getExpectDate() {
                return expectDate;
            }

            public void setExpectDate(String expectDate) {
                this.expectDate = expectDate;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getSolutionTypeL() {
                return solutionTypeL;
            }

            public void setSolutionTypeL(String solutionTypeL) {
                this.solutionTypeL = solutionTypeL;
            }

            public String getSolutionTypeM() {
                return solutionTypeM;
            }

            public void setSolutionTypeM(String solutionTypeM) {
                this.solutionTypeM = solutionTypeM;
            }

            public String getResolveContent() {
                return resolveContent;
            }

            public void setResolveContent(String resolveContent) {
                this.resolveContent = resolveContent;
            }

            public String getRegDate() {
                return regDate;
            }

            public void setRegDate(String regDate) {
                this.regDate = regDate;
            }

            public String getAcceptDate() {
                return acceptDate;
            }

            public void setAcceptDate(String acceptDate) {
                this.acceptDate = acceptDate;
            }

            public String getCoType() {
                return coType;
            }

            public void setCoType(String coType) {
                this.coType = coType;
            }

            public String getCoCategory() {
                return coCategory;
            }

            public void setCoCategory(String coCategory) {
                this.coCategory = coCategory;
            }

            public String getCoClass() {
                return coClass;
            }

            public void setCoClass(String coClass) {
                this.coClass = coClass;
            }

            public String getCoClassSub() {
                return coClassSub;
            }

            public void setCoClassSub(String coClassSub) {
                this.coClassSub = coClassSub;
            }

            public String getCoGrade() {
                return coGrade;
            }

            public void setCoGrade(String coGrade) {
                this.coGrade = coGrade;
            }

            public String getRelPlanDate() {
                return relPlanDate;
            }

            public void setRelPlanDate(String relPlanDate) {
                this.relPlanDate = relPlanDate;
            }

            public String getCoPlanDate() {
                return coPlanDate;
            }

            public void setCoPlanDate(String coPlanDate) {
                this.coPlanDate = coPlanDate;
            }

            public String getPlanEstDate() {
                return planEstDate;
            }

            public void setPlanEstDate(String planEstDate) {
                this.planEstDate = planEstDate;
            }

            public String getPlanDesc() {
                return planDesc;
            }

            public void setPlanDesc(String planDesc) {
                this.planDesc = planDesc;
            }

            public String getPlanDowntimeFlag() {
                return planDowntimeFlag;
            }

            public void setPlanDowntimeFlag(String planDowntimeFlag) {
                this.planDowntimeFlag = planDowntimeFlag;
            }

            public String getPlanDowntimeDate() {
                return planDowntimeDate;
            }

            public void setPlanDowntimeDate(String planDowntimeDate) {
                this.planDowntimeDate = planDowntimeDate;
            }

            public String getPlanTester() {
                return planTester;
            }

            public void setPlanTester(String planTester) {
                this.planTester = planTester;
            }

            public String getPlanTestDate() {
                return planTestDate;
            }

            public void setPlanTestDate(String planTestDate) {
                this.planTestDate = planTestDate;
            }

            public String getPlanCustTestFlag() {
                return planCustTestFlag;
            }

            public void setPlanCustTestFlag(String planCustTestFlag) {
                this.planCustTestFlag = planCustTestFlag;
            }

            public String getPlanTestGoal() {
                return planTestGoal;
            }

            public void setPlanTestGoal(String planTestGoal) {
                this.planTestGoal = planTestGoal;
            }

            public String getPlanTestScenario() {
                return planTestScenario;
            }

            public void setPlanTestScenario(String planTestScenario) {
                this.planTestScenario = planTestScenario;
            }

            public String getPlanImpact() {
                return planImpact;
            }

            public void setPlanImpact(String planImpact) {
                this.planImpact = planImpact;
            }

            public String getPlanCustImpactDesc() {
                return planCustImpactDesc;
            }

            public void setPlanCustImpactDesc(String planCustImpactDesc) {
                this.planCustImpactDesc = planCustImpactDesc;
            }

            public String getPlanSysImpactDesc() {
                return planSysImpactDesc;
            }

            public void setPlanSysImpactDesc(String planSysImpactDesc) {
                this.planSysImpactDesc = planSysImpactDesc;
            }

            public String getPlanRollbackUser() {
                return planRollbackUser;
            }

            public void setPlanRollbackUser(String planRollbackUser) {
                this.planRollbackUser = planRollbackUser;
            }

            public String getPlanRollbackDesc() {
                return planRollbackDesc;
            }

            public void setPlanRollbackDesc(String planRollbackDesc) {
                this.planRollbackDesc = planRollbackDesc;
            }

            public String getPlanApproveOpinion() {
                return planApproveOpinion;
            }

            public void setPlanApproveOpinion(String planApproveOpinion) {
                this.planApproveOpinion = planApproveOpinion;
            }

            public String getSeqNo() {
                return seqNo;
            }

            public void setSeqNo(String seqNo) {
                this.seqNo = seqNo;
            }

            public String getReqName() {
                return reqName;
            }

            public void setReqName(String reqName) {
                this.reqName = reqName;
            }

            public String getReqDate() {
                return reqDate;
            }

            public void setReqDate(String reqDate) {
                this.reqDate = reqDate;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public String getReqGubun() {
                return reqGubun;
            }

            public void setReqGubun(String reqGubun) {
                this.reqGubun = reqGubun;
            }

            public String getAppGubun() {
                return appGubun;
            }

            public void setAppGubun(String appGubun) {
                this.appGubun = appGubun;
            }

            public String getAppStatus() {
                return appStatus;
            }

            public void setAppStatus(String appStatus) {
                this.appStatus = appStatus;
            }

            public String getOnehrYn() {
                return onehrYn;
            }

            public void setOnehrYn(String onehrYn) {
                this.onehrYn = onehrYn;
            }

            public String getUsedate1Code() {
                return usedate1Code;
            }

            public void setUsedate1Code(String usedate1Code) {
                this.usedate1Code = usedate1Code;
            }

            public String getUsedate1Name() {
                return usedate1Name;
            }

            public void setUsedate1Name(String usedate1Name) {
                this.usedate1Name = usedate1Name;
            }

            public String getUsedate1From() {
                return usedate1From;
            }

            public void setUsedate1From(String usedate1From) {
                this.usedate1From = usedate1From;
            }

            public String getUsedate1To() {
                return usedate1To;
            }

            public void setUsedate1To(String usedate1To) {
                this.usedate1To = usedate1To;
            }

            public String getUsedate2Code() {
                return usedate2Code;
            }

            public void setUsedate2Code(String usedate2Code) {
                this.usedate2Code = usedate2Code;
            }

            public String getUsedate2Name() {
                return usedate2Name;
            }

            public void setUsedate2Name(String usedate2Name) {
                this.usedate2Name = usedate2Name;
            }

            public String getUsedate2From() {
                return usedate2From;
            }

            public void setUsedate2From(String usedate2From) {
                this.usedate2From = usedate2From;
            }

            public String getUsedate2To() {
                return usedate2To;
            }

            public void setUsedate2To(String usedate2To) {
                this.usedate2To = usedate2To;
            }

            public String getWantedDate() {
                return wantedDate;
            }

            public void setWantedDate(String wantedDate) {
                this.wantedDate = wantedDate;
            }

            public String getWantedComment() {
                return wantedComment;
            }

            public void setWantedComment(String wantedComment) {
                this.wantedComment = wantedComment;
            }

            public String getRequestDttm() {
                return requestDttm;
            }

            public void setRequestDttm(String requestDttm) {
                this.requestDttm = requestDttm;
            }

            public String getRequestDesc() {
                return requestDesc;
            }

            public void setRequestDesc(String requestDesc) {
                this.requestDesc = requestDesc;
            }

            public String getReqType() {
                return reqType;
            }

            public void setReqType(String reqType) {
                this.reqType = reqType;
            }


            public String getHopeTime() {
                return hopeTime;
            }

            public void setHopeTime(String hopeTime) {
                this.hopeTime = hopeTime;
            }


        }

        public ArrayList<Res_AP_IF_028_VO.result.approvalAttachList> getApprovalAttachList() {
            return approvalAttachList;
        }

        public void setApprovalAttachList(ArrayList<Res_AP_IF_028_VO.result.approvalAttachList> approvalAttachList) {
            this.approvalAttachList = approvalAttachList;
        }

        private ArrayList<approvalAttachList> approvalAttachList;

        public class approvalAttachList {

            String fileName;//	file 실제 이름	String	file 실제 이름
            String fileUrl;//	file 다운로드 경로	String	file 다운로드 경로

            public String getFileName() {
                return fileName;
            }

            public void setFileName(String fileName) {
                this.fileName = fileName;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }
        }

        public ArrayList<Res_AP_IF_028_VO.result.listappHistory> getListappHistory() {
            return listappHistory;
        }

        public void setListappHistory(ArrayList<Res_AP_IF_028_VO.result.listappHistory> listappHistory) {
            this.listappHistory = listappHistory;
        }

        private ArrayList<listappHistory> listappHistory;

        //승인이력정보
        public class listappHistory {
            public String getSeq() {
                return seq;
            }

            public void setSeq(String seq) {
                this.seq = seq;
            }

            public String getAppUser() {
                return appUser;
            }

            public void setAppUser(String appUser) {
                this.appUser = appUser;
            }

            public String getAppComment() {
                return appComment;
            }

            public void setAppComment(String appComment) {
                this.appComment = appComment;
            }

            public String getAppStatus() {
                return appStatus;
            }

            public void setAppStatus(String appStatus) {
                this.appStatus = appStatus;
            }

            public String getAppDate() {
                return appDate;
            }

            public void setAppDate(String appDate) {
                this.appDate = appDate;
            }

            public String getAppRole() {
                return appRole;
            }

            public void setAppRole(String appRole) {
                this.appRole = appRole;
            }

            String seq;//	결재순서	String	결재순서
            String appUser;//	결재자	String	결재자
            String appComment;//	결재자의견	String	결재자의견
            String appStatus;//	결재상태	String	결재상태
            String appDate;//결재일	String	결재일
            String appRole;//	결재자 직책	String	결재자 직책

            public String getAppUserDept() {
                return appUserDept;
            }

            public void setAppUserDept(String appUserDept) {
                this.appUserDept = appUserDept;
            }

            String appUserDept;//	결재자 부서

        }
    }


    private result result;

    public result getResult() {
        return result;
    }

    public void setResult(result result) {
        this.result = result;
    }

    /*
            *** data info ***

            String procId;//요청번호	String	요청번호
            String category;//요청구분	String	요청구분
            String phaseStatus;//단계/상태	String	단계/상태
            String customer;//요청자	String	요청자
            String customerCompany;//요청자 회사	String	요청자 회사
            String customerDepart;//	요청자 부서	String	요청자 부서
            String customerMobile;//	요청자 모바일	String	요청자 모바일
            String company;//대상회사	String	대상회사
            String emergencyType;//긴급유형	String	긴급유형
            String offeringName;//오퍼링	String	오퍼링
            String assigngroup;//처리그룹		처리그룹
            String assignee;//	처리자	String	처리자
            String expectDate;//희망완료일	String	희망완료일
            String title;//요청제목	String	요청제목
            String content;//요청내역	String	요청내역
            String solutionTypeL;//처리분류(대)	String	처리분류(대)
            String solutionTypeM;//처리분류(중)	String	처리분류(중)
            String resolveContent;//처리내역	String	처리내역
            String regDate;//	등록일시	String	등록일시
            String acceptDate;//	접수일시	String	접수일시

            String procId;//번호	String	번호
            String phaseStatus;//단계/상태	String	단계/상태
            String customer;//요청자	String	요청자
            String customerCompany;//요청자 회사	String	요청자 회사
            String customerDepart;//요청자 부서	String	요청자 부서
            String customerMobile;//요청자 모바일	String	요청자 모바일
            String company;//대상회사	String	대상회사
            String offeringName;//	오퍼링	String	오퍼링
            String assigngroup;//처리그룹	String	처리그룹
            String assignee;//처리자	String	처리자
            String coType;//	변경유형	String	변경유형
            String coCategory;//	업무구분	String	업무구분
            String coClass;//변경분류	String	변경분류
            String coClassSub;//분류상세	String	분류상세
            String coGrade;//변경등급	String	변경등급
            String title;//요청제목	String	요청제목
            String content;//	요청내역	String	요청내역
            String regDate;//등록일시	String	등록일시
            String relPlanDate;//연관티켓납기일	String	연관티켓납기일
            String coPlanDate;//변경작업납기일	String	변경작업납기일
            String planEstDate;//	예상작업일	String	예상작업일
            String planDesc;//세부내역	String	세부내역
            String planDowntimeFlag;//	다운타임발생	String	다운타임발생
            String planDowntimeDate;//다운타임시간	String	다운타임시간
            String planTester;//테스트담당자	String	테스트담당자
            String planTestDate;//	테스트계획일	String	테스트계획일
            String planCustTestFlag;//사용자테스트	String	사용자테스트
            String planTestGoal;//	테스트목적	String	테스트목적
            String planTestScenario;//테스트시나리오	String	테스트시나리오
            String planImpact;//변경영향도	String	변경영향도
            String planCustImpactDesc;//고객영향내역	String	고객영향내역
            String planSysImpactDesc;//	시스템영향내역	String	시스템영향내역
            String planRollbackUser;//복구담당자	String	복구담당자
            String planRollbackDesc;//복구내용	String	복구내용
            String planApproveOpinion;//	검토의견	String	검토의견

            String seqNo;//SEQ	String	SEQ
            String reqName;//	신청자 (이름 / 부서)	String	신청자 (이름 / 부서)
            String reqDate;//요청일	String	요청일
            String detail;//요청내역 상세	String	요청내역 상세
            String reqGubun;//	요청구분 (VPN, PC반출 등)	String	요청구분 (VPN, PC반출 등)
            String appGubun;//승인구분 (팀장, 보안관리자 등)	String	승인구분 (팀장, 보안관리자 등)
            String appStatus;//승인상태 (승인요청, 처리완료 등)	String	승인상태 (승인요청, 처리완료 등)
            String onehrYn;//onehr건인지 구분필드	String	onehr건인지 구분필드
            String usedate1Code;//사용일자1_코드	String	사용일자1_코드
            String usedate1Name;//	사용일자1_이름	String	사용일자1_이름
            String usedate1From;//	사용일자1_시작	String	사용일자1_시작
            String usedate1To;//사용일자1_종료	String	사용일자1_종료
            String usedate2Code;//사용일자2_코드	String	사용일자2_코드
            String usedate2Name;//사용일자2_이름	String	사용일자2_이름
            String usedate2From;//사용일자2_시작	String	사용일자2_시작
            String usedate2To;//사용일자2_종료	String	사용일자2_종료

            String procId;//요청번호	String	요청번호
            String category;//요청구분	String	요청구분
            String phaseStatus;//	단계/상태	String	단계/상태
            String customerCompany;//	요청자 회사	String	요청자 회사
            String customerDepart;//요청자 부서	String	요청자 부서
            String customerMobile;//	요청자 모바일	String	요청자 모바일
            String company;//대상회사	String	대상회사
            String emergencyType;//긴급유형	String	긴급유형
            String offeringName;//오퍼링	String	오퍼링
            String assigngroup;//처리그룹	String	처리그룹
            String assignee;//	처리자	String	처리자
            String expectDate;//희망완료일	String	희망완료일
            String title;//요청제목	String	요청제목
            String content;//요청내역	String	요청내역
            String solutionTypeL;//처리분류(대)	String	처리분류(대)
            String solutionTypeM;//처리분류(중)	String	처리분류(중)
            String resolveContent;//처리내역	String	처리내역
            String regDate;//등록일시	String	등록일시
            String acceptDate;//접수일시	String	접수일시
            String wantedDate;//납기변경희망일자	String	납기변경희망일자
            String wantedComment;//납기변경사유	String	납기변경사유

            String procId;//요청번호	String	요청번호
            String category;//	요청구분	String	요청구분
            String phaseStatus;//단계/상태	String	단계/상태
            String customer;//요청자	String	요청자
            String customerCompany;//요청자 회사	String	요청자 회사
            String customerDepart;//	요청자 부서	String	요청자 부서
            String customerMobile;//요청자 모바일	String	요청자 모바일
            String company;//대상회사	String	대상회사
            String emergencyType;//	긴급유형	String	긴급유형
            String offeringName;//오퍼링	String	오퍼링
            String assigngroup;//처리그룹	String	처리그룹
            String assignee;//처리자	String	처리자
            String expectDate;//희망완료일	String	희망완료일
            String title;//요청제목	String	요청제목
            String content;//요청내역	String	요청내역
            String solutionTypeL;//처리분류(대)	String	처리분류(대)
            String solutionTypeM;//	처리분류(중)	String	처리분류(중)
            String resolveContent;//	처리내역	String	처리내역
            String regDate;//등록일시	String	등록일시
            String acceptDate;//	접수일시	String	접수일시
            String requestDttm;//취소요청일시	String	취소요청일시
            String requestDesc;//	취소요청사유	String	취소요청사유

            String procId;//번호	String	번호
            String reqType;//	요청구분	String	요청구분
            String phaseStatus;//	단계/상태	String	단계/상태
            String customer;//	요청자	String	요청자
            String customerCompany;//요청자 회사	String	요청자 회사
            String customerDepart;//	요청자 부서	String	요청자 부서
            String customerMobile;//	요청자 모바일	String	요청자 모바일
            String company;//	대상회사	String	대상회사
            String assignee;//담당자(정)	String	담당자(정)
            String title;//제목	String	제목
            String content;//	내용	String	내용
            String hopeTime;//	희망완료일시	String	희망완료일시
            String regDate;//	등록일시	String	등록일시

     */
}