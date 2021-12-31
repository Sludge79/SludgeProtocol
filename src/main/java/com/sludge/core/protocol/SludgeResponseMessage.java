package com.sludge.core.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SludgeResponseMessage extends SludgeMessage {

    private int sequenceId;

    private String content;


    @Override
    public int getMessageType() {
        return ResponseMessageType;
    }
}
