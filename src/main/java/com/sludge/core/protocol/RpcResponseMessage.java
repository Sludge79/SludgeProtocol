package com.sludge.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponseMessage extends SludgeMessage {

    private int sequenceId;

    private Object returnValue;

    private Exception exceptionValue;

    @Override
    public int getMessageType() {
        return RpcResponseMessageType;
    }

}
