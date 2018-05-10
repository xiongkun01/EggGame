package com.xk.netty.struct;

/**
 * 消息类型（1个字节）
 */
public enum MessageType {

    SERVICE_REQ((byte) 0), SERVICE_RESP((byte) 1), ONE_WAY((byte) 2),
    LOGIN_REQ((byte) 3), LOGIN_RESP((byte) 4),
    HEARTBEAT_REQ((byte) 5), HEARTBEAT_RESP((byte) 6),
    REGISTER_REQ((byte) 7), REGISTER_RESP((byte) 8);

    private byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return this.value;
    }


}
