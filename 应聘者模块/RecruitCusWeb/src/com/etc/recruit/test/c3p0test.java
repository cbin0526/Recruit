package com.etc.recruit.test;

import java.sql.Connection;

import com.etc.recruit.util.DBUtil;

public class c3p0test {

	public static void main(String[] args) {
		
		DBUtil.exUpdate("UPDATE manager SET manager_pwd = 111 WHERE manager_id = 1");
	}
}
