package com.sludge.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SludgeRequestMessage extends SludgeMessage {

    private int sequenceId;

    private String from;

    private String to;

    private String content;

    @Override
    public int getMessageType() {
        return RequestMessageType;
    }
}
