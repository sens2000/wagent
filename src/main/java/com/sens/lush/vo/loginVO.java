//@Getter @Setter
//@ToString

package com.sens.lush.vo;

import java.sql.Date;

public class loginVO {
	private String USERID;
    private String GROUPCD;
    private String GROUPNAME;
    private String COMCD;
    private String COMNAME;
	private String USERNAME;
    private Integer GRADE;
    private String GUBUN;

    public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getGROUPCD() {
		return GROUPCD;
	}
	public void setGROUPCD(String gROUPCD) {
		GROUPCD = gROUPCD;
	}
	public String getGROUPNAME() {
		return GROUPNAME;
	}
	public void setGROUPNAME(String gROUPNAME) {
		GROUPNAME = gROUPNAME;
	}
	public String getCOMCD() {
		return COMCD;
	}
	public void setCOMCD(String cOMCD) {
		COMCD = cOMCD;
	}
	public String getCOMNAME() {
		return COMNAME;
	}
	public void setCOMNAME(String cOMNAME) {
		COMNAME = cOMNAME;
	}
	public String getUSERNAME() {
		return USERNAME;
	}
	public void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}
	public Integer getGRADE() {
		return GRADE;
	}
	public void setGRADE(Integer gRADE) {
		GRADE = gRADE;
	}
	public String getGUBUN() {
		return GUBUN;
	}
	public void setGUBUN(String gUBUN) {
		GUBUN = gUBUN;
	}
    
}
