package com.xk.netty.handler;

import com.xk.netty.db.C3p0Utils;
import com.xk.netty.db.DBUtilBO;
import com.xk.netty.db.DBUtils;
import com.xk.netty.struct.*;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class RegisterHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.REGISTER_REQ.value()) {
            register(message, ctx);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void register(NettyMessage message, ChannelHandlerContext ctx) {
        //先判断手机号码是否重复
        DBUtilBO dbBo = new DBUtilBO();
        Connection conn = C3p0Utils.getConnection();
        dbBo.conn = conn;
        boolean res = true;
        Map<String, String> body = (Map<String, String>) message.getBody();
        if (conn != null) {
            String sql = "select * from eg_user where tel = ?";
            try {
                dbBo.pst = dbBo.conn.prepareStatement(sql);
                dbBo.pst.setString(1, body.get("tel"));
                DBUtils.executeQuery(dbBo);

                if (dbBo.rs.next()) {
                    res = false;
                } else {
                    res = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                res = false;
            } finally {
                DBUtils.releaseSource(dbBo);
            }
        }
        if (!res) {
            Map<String, String> map = new HashMap<>();
            map.put(NettyConstant.STATUS_CODE, String.valueOf(StatusCode.FAIL));
            map.put(NettyConstant.ERROR, "注册失败！");
            ctx.writeAndFlush(buildResp(map));
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(NettyConstant.STATUS_CODE, String.valueOf(StatusCode.SUCCESS));
            map.put(NettyConstant.ERROR, "注册成功！");
            ctx.writeAndFlush(buildResp(map));
        }
    }

    private NettyMessage buildResp(Object body) {
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.REGISTER_RESP.value());
        msg.setHeader(header);
        msg.setBody(body);
        return msg;
    }

}
