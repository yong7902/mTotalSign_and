package com.kolon.sign2.vo;

import java.util.ArrayList;

/**
 * 서비스데스크 - 승인상세 메일필터
 */
public class Res_AP_IF_035_VO extends statusVO {

    public class result extends resultVO {

        public Res_AP_IF_035_VO.result.mailFilterDetailClass getMailFilterDetail() {
            return mailFilterDetail;
        }

        public void setMailFilterDetail(Res_AP_IF_035_VO.result.mailFilterDetailClass mailFilterDetail) {
            this.mailFilterDetail = mailFilterDetail;
        }

        private mailFilterDetailClass mailFilterDetail;


        public class mailFilterDetailClass {

            String subject;//	제목	String	제목		“테스트입니다..”	“테스트입니다..”
            String date;//		메일 발송 날짜	String	메일 발송 날짜		42530.77505	42530.77505
            String sender;//		발신자 이메일	String	발신자 이메일		hanawhy@jiran.com	hanawhy@jiran.com
            String senderName;//		발신자 이름	String	발신자 이름		박하나	박하나
            String senderPos;//	발신자 직급	String	발신자 직급		대리	대리
            String senderDept;//	발신자 소속	String	발신자 소속		개발1팀	개발1팀
            String receiver;//	수신자 이메일	String	수신자 이메일		s@jiran.com	s@jiran.com 첨부파일명	String	첨부파일명		“설계서.xlsx”	“설계서.xlsx”
            String contents;//	메일 본문 내용	String	메일 본문 내용		“This is mail body……..”	“This is mail body……..”
            String viewMode;//	"메일 내용 출력 모드 text일 경우 plain하게 출력하며, html일 경우 태그가 적용되어 보임."		“text” 또는 “html”	“text” 또는 “html”
            String finfo;//		정책명	String	정책명		"보안 파일 감시"	"보안 파일 감시"
            String prvInfo;//		개인 정보 정책 상세	String	개인 정보 정책 상세		주민 등록 번호 1회 : 본문, 휴대폰 번호 1회 : 본문, 이메일 주소 1회 : test.txt	주민 등록 번호 1회 : 본문, 휴대폰 번호 1회 : 본문, 이메일 주소 1회 : test.txt

            public String getSubject() {
                return subject;
            }

            public void setSubject(String subject) {
                this.subject = subject;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getSender() {
                return sender;
            }

            public void setSender(String sender) {
                this.sender = sender;
            }

            public String getSenderName() {
                return senderName;
            }

            public void setSenderName(String senderName) {
                this.senderName = senderName;
            }

            public String getSenderPos() {
                return senderPos;
            }

            public void setSenderPos(String senderPos) {
                this.senderPos = senderPos;
            }

            public String getSenderDept() {
                return senderDept;
            }

            public void setSenderDept(String senderDept) {
                this.senderDept = senderDept;
            }

            public String getReceiver() {
                return receiver;
            }

            public void setReceiver(String receiver) {
                this.receiver = receiver;
            }

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public String getViewMode() {
                return viewMode;
            }

            public void setViewMode(String viewMode) {
                this.viewMode = viewMode;
            }

            public String getFinfo() {
                return finfo;
            }

            public void setFinfo(String finfo) {
                this.finfo = finfo;
            }

            public String getPrvInfo() {
                return prvInfo;
            }

            public void setPrvInfo(String prvInfo) {
                this.prvInfo = prvInfo;
            }

        }

        public class properties {

            String code;//	성공/실패 코드	String	성공/실패 코드		0 (성공) / 1 (실패)	0 (성공) / 1 (실패)
            String msg;//	리턴 메시지	String	리턴 메시지		ex. “인증에 실패하였습니다.”	ex. “인증에 실패하였습니다.”
            String permission;//	권한	String	권한		1 (개인사용자) / 2 (결재권자)	1 (개인사용자) / 2 (결재권자)

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getMsg() {
                return msg;
            }

            public void setMsg(String msg) {
                this.msg = msg;
            }

            public String getPermission() {
                return permission;
            }

            public void setPermission(String permission) {
                this.permission = permission;
            }
        }


        public ArrayList<Res_AP_IF_035_VO.result.files> getFiles() {
            return files;
        }

        public void setFiles(ArrayList<Res_AP_IF_035_VO.result.files> files) {
            this.files = files;
        }

        private ArrayList<files> files;

        public class files {

            String link;//	첨부파일 다운로드 링크	String	첨부파일 다운로드 링크
            String name;
            String size;

            public boolean isChecked = false;

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
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