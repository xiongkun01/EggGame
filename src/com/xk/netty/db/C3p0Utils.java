package com.xk.netty.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class C3p0Utils {

    static Logger logger = Logger.getLogger(C3p0Utils.class.getName());

    private static ComboPooledDataSource dataSource = new ComboPooledDataSource("mysql");

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.info("数据库连接出错！");
        }
        return null;
    }

    public static void close(Connection conn, PreparedStatement pst, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.info(e.toString());
            }
        }
        if (pst != null) {
            try {
                pst.close();
            } catch (SQLException e) {
                logger.info(e.toString());
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.info(e.toString());
            }

        }
    }

}
