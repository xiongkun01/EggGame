package com.xk.netty.db;

import java.sql.SQLException;

public class TestDB {

    public static void main(String[] args) {
        //dbBo对象已经封装了一个数据库连接以及要执行的操作
//        DBUtilBO dbBo = new DBUtilBO();
//        String sql = "select * from eg_user";
//        dbBo.conn = C3p0Utils.getConnection();
//        try {
//            if (dbBo.conn != null) {
//                dbBo.pst = dbBo.conn.prepareStatement(sql);
//
//                //通过数据库操作类来执行这个操作封装类，结果封装回这个操作封装类
//                DBUtils.executeQuery(dbBo);
//
//                if (dbBo.rs != null) {
//                    //从dbBo类中提取操作结果
//                    while (dbBo.rs.next()) {
//                        System.out.println("[userName : " + dbBo.rs.getString("userName") +
//                                ", password : " + dbBo.rs.getString("password") + "]");
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            DBUtils.releaseSource(dbBo);
//        }
        try {
            test3();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("main");
    }


    public static void test() throws Exception {
        throw new Exception("参数越界");
        //System.out.println("异常后"); //编译错误，无法访问的语句
    }

    public static void test2() {
        try {
            throw new Exception("参数越界");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("异常后");//可以执行
    }

    public static void test3() throws Exception {
        if (true) {
            throw new Exception("参数越界");
        }
        System.out.println("异常后"); //抛出异常，不会执行
    }
}
