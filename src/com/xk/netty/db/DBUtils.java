package com.xk.netty.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class DBUtils {

    static Logger logger = Logger.getLogger(DBUtils.class.getName());

    private static void releaseSource(Connection conn, PreparedStatement pst, ResultSet rs) {
        C3p0Utils.close(conn, pst, rs);
    }

    public static void releaseSource(DBUtilBO vo) {
        if (vo != null) {
            releaseSource(vo.conn, vo.pst, vo.rs);
        }
    }

    public static void executeQuery(DBUtilBO vo) throws SQLException {
        try {
            vo.rs = vo.pst.executeQuery();
        } catch (SQLException e) {
            releaseSource(vo);
            logger.info("UUID: " + UUID.randomUUID() + ", SQL语法有误：" + e.getMessage());
            throw e; //将异常向上抛
        }
    }

    public static boolean executeUpdate(DBUtilBO vo) {
        Connection conn = vo.conn;
        PreparedStatement pst = vo.pst;
        try {
            int rowCount = pst.executeUpdate();
            if (rowCount > 0) {
                return true;
            }
        } catch (SQLException e) {
            releaseSource(conn, pst, null);
            logger.info("UUID: " + UUID.randomUUID() + ", SQL语法有误：" + e.getMessage());
        } finally {
            releaseSource(conn, pst, null);
        }
        return false;
    }
}
