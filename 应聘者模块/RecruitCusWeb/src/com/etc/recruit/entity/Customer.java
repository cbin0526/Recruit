package com.etc.recruit.entity;

public class Customer {
	//�û�ID ��������
	private int u_userid;
	//�û��� 
	private String u_username;
	//����
	private String u_userpwd;
	//�绰
	private String u_tel;
	//����
	private String u_email;
	//״̬��������Ա���ã�
	private int u_status;
	public int getU_userid() {
		return u_userid;
	}
	public void setU_userid(int u_userid) {
		this.u_userid = u_userid;
	}
	public String getU_username() {
		return u_username;
	}
	public void setU_username(String u_username) {
		this.u_username = u_username;
	}
	public String getU_userpwd() {
		return u_userpwd;
	}
	public void setU_userpwd(String u_userpwd) {
		this.u_userpwd = u_userpwd;
	}
	public String getU_tel() {
		return u_tel;
	}
	public void setU_tel(String u_tel) {
		this.u_tel = u_tel;
	}
	public String getU_email() {
		return u_email;
	}
	public void setU_email(String u_email) {
		this.u_email = u_email;
	}
	public int getU_status() {
		return u_status;
	}
	public void setU_status(int u_status) {
		this.u_status = u_status;
	}
	@Override
	public String toString() {
		return "Customer [u_userid=" + u_userid + ", u_username=" + u_username + ", u_userpwd=" + u_userpwd + ", u_tel="
				+ u_tel + ", u_email=" + u_email + ", u_status=" + u_status + "]";
	}
	
}
