package com.xk.netty.struct;

import java.io.Serializable;

public final class NettyMessage implements Serializable {

    private static final long serialVersionUID = -8594510270086468814L;
    private Header header;
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage [header=" + header + "]";
    }
}
