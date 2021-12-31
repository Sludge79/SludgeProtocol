package com.sludge.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequestMessage extends SludgeMessage {

    private int sequenceId;
    private String interfaceName;
    private String method;
    private Class<?>[] parameterTypes;
    private Object[] parameterValues;
    private Class<?> returnType;


    @Override
    public int getMessageType() {
        return RpcRequestMessageType;
    }
}
