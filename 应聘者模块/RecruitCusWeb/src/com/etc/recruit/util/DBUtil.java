package com.etc.recruit.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtil {
	//����һ����̬����Դ����
		private static DataSource dataSource ;
		
		static {
			//��ʼ������Դ����   //��ȡc3p0-config.xml�е�����
			 dataSource = new ComboPooledDataSource();
		}
		
		
		private DBUtil() {
			// TODO Auto-generated constructor stub
		}

		/**
		 * ��ȡ���Ӷ���Connection
		 * 
		 * @return java.sql.Connection
		 */
		public static Connection getConn() {
			Connection conn = null;
			try {			
				conn =dataSource.getConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return conn;
		}

		/**
		 * ͨ�õ����ӣ�ɾ�����޸ĵĴ���
		 * 
		 * @param sql    Ҫִ�е�sql���
		 * @param params ռλ����ֵ�����������������ݻ���ֱ�Ӹ�ֵ
		 * @return int ��Ӱ�����
		 */
		public static int exUpdate(String sql, Object... params) {

			Connection conn = null;
			PreparedStatement pstmt = null;
			int result = 0;
			try {
				conn = getConn();

				// 3����һ��PreparedStatement =>���� ɾ�� �޸� ��ȫһ�£�����sql��䲻ͬ
				pstmt = conn.prepareStatement(sql);
				// �������ò����ķ���
				setPstmt(pstmt, params);
				// 5 ִ��sql
				result = pstmt.executeUpdate();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// �ͷ���Դ
				closeAll(pstmt, conn, null);
			}

			return result;

		}

		/**
		 * ���ò���,����ռλ��
		 * 
		 * @param pstmt  PreparedStatement
		 * @param params ռλ��������ֵ�б�(����)
		 */
		private static void setPstmt(PreparedStatement pstmt, Object... params) {
			// ����ռλ��
			if (params != null) {
				for (int i = 0; i < params.length; i++) {
					try {
						pstmt.setObject(i + 1, params[i]);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			System.out.println(pstmt);
		}

		/**
		 * �ͷ���Դ��ķ���
		 * 
		 * @param pstmt PreparedStatement ����
		 * @param conn  Connection ���Ӷ���
		 * @param rs    ResultSet ���������
		 */
		public static void closeAll(PreparedStatement pstmt, Connection conn, ResultSet rs) {
			// 6 �ͷ���Դ
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * ͨ�õĲ�ѯ����
		 * @param sql  ��ѯsql���
		 * @param cls  Class���Ͷ���
		 * @param params  ��ѯ��ռλ����ֵ
		 * @return   List ��ѯ���ض��󼯺�
		 */
		public static List exQuery(String sql, Class cls, Object... params) {
			List list = new ArrayList();
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn = getConn();
				// 3����һ��PreparedStatement =>���� ɾ�� �޸� ��ȫһ�£�����sql��䲻ͬ
				pstmt = conn.prepareStatement(sql);
				// �������ò����ķ���
				setPstmt(pstmt, params);
				// 5 ִ��sql
				rs = pstmt.executeQuery();
				// ��Ҫ��resultset�еĽ����������
				while (rs.next()) {
					// ��rs�е�ָ���һ�������е��е�ֵȡ�����������ݷ�װ��һ��java�еĶ���->�ŵ�һ����������
					Object obj = convert(rs, cls);
					// ��Obj����ӵ�List����.
					list.add(obj);
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				// �ͷ���Դ
				closeAll(pstmt, conn, rs);
			}

			return list;
		}

		/**
		 * ��rs���е�һ�м�¼ת��ΪClass->���Ͷ���,����ֵ
		 * 
		 * @param rs  �����
		 * @param cls Class����
		 * @return Object
		 */
		public static Object convert(ResultSet rs, Class cls) {

			// ��rs�е�ָ���һ�������е��е�ֵȡ�����������ݷ�װ��һ��java�еĶ���->�ŵ�һ����������
			Object obj = null;
			try {
				obj = cls.newInstance();
				// ��ȡ��ѯ������е�����
				ResultSetMetaData rsmd = rs.getMetaData();
//				System.out.println("rsmd: " + rsmd.getColumnCount());
				// �����ResultSet�е�����ȡ����
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					// ��ѯ�������ʾ������->
					String name = rsmd.getColumnLabel(i);
					// �õ��Ĳ�ѯ���е�ֵ
					Object value = rs.getObject(i);
					//������value��"��ʵ����"��ʲô ->ʱ�� ->java.sql.Date
					//System.out.println(value.getClass().getName());
					// ��java.sql.Date ->ʵ���� java.time.LocalDate
					if(value instanceof java.sql.Date) {
						value = ((java.sql.Date)value).toLocalDate();
					}
					// obj=> obj.setxx����
					// ��һ������ ������,"������"�����Ե�ֵ
					BeanUtils.setProperty(obj, name, value);
				}

			} catch (InstantiationException | IllegalAccessException | SQLException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return obj;
		}
}
