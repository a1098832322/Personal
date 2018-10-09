package com.zl.daoimpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.zl.utils.PropUtil;

public class DBConnection {
	private static Properties prop = PropUtil.getInstance();
	private static final String MySQLDriver = prop.getProperty("DB_DRIVER");
	private static final String MySQLURL = prop.getProperty("DB_URL");
	private static final String adminID = prop.getProperty("DB_USERNAME");
	private static final String adminPasswd = prop.getProperty("DB_PASSWORD");

	// 初始化Connection
	private static Connection conn = null;

	/**
	 * 数据库连接方法，返回一个Connection
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() {
		try {
			Class.forName(MySQLDriver);
			conn = DriverManager.getConnection(MySQLURL, adminID, adminPasswd);// 用数据库管理员账户登录
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}

	public static void free(ResultSet rs, Statement state, Connection conn) { // 释放连接资源
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				state.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void free(ResultSet rs, PreparedStatement ps, Connection conn) { // 释放连接资源
		try {
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void free(PreparedStatement ps, Connection conn) { // 释放连接资源
		try {
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void free(Statement state, Connection conn) {// 释放连接资源
		try {
			state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
