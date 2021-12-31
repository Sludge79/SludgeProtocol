package com.sludge.core.protocol;


import lombok.Data;

import java.io.Serializable;

@Data
public abstract class SludgeMessage implements Serializable {

    private int sequenceId;

    private int messageType;

    private String username;


    public static final int RpcRequestMessageType = 101;
    public static final int RpcResponseMessageType = 102;

    public static final int LoginMessageType = 1;
    public static final int RequestMessageType = 2;
    public static final int ResponseMessageType = 3;

    public abstract int getMessageType();
}
