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

public class LoginHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            login(ctx, message);
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private void login(ChannelHandlerContext ctx, NettyMessage message) {
        Map<String, String> body = (Map<String, String>) message.getBody();
        String tel = body.get("tel");
        String password = body.get("password");
        boolean res = false;
        DBUtilBO dbBo = new DBUtilBO();
        Connection conn = C3p0Utils.getConnection();
        dbBo.conn = conn;
        if (conn != null) {
            String sql = "SELECT password FROM eg_user WHERE tel = ?";
            try {
                dbBo.pst = dbBo.conn.prepareStatement(sql);
                dbBo.pst.setString(1, tel);
                DBUtils.executeQuery(dbBo);

                if (dbBo.rs.next() && password.equals(dbBo.rs.getString("password"))) {
                    res = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (!res) {
            Map<String, String> map = new HashMap<>();
            map.put(NettyConstant.STATUS_CODE, String.valueOf(StatusCode.FAIL));
            map.put(NettyConstant.ERROR, "登录失败");
            ctx.writeAndFlush(buildResp(map));
        } else {
            Map<String, String> map = new HashMap<>();
            map.put(NettyConstant.STATUS_CODE, String.valueOf(StatusCode.SUCCESS));
            map.put(NettyConstant.ERROR, "登录成功");
            ctx.writeAndFlush(buildResp(map));
        }
    }

    private NettyMessage buildResp(Object body) {
        NettyMessage msg = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        msg.setHeader(header);
        msg.setBody(body);
        return msg;
    }
}
