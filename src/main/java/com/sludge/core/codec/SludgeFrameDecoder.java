package com.sludge.core.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class SludgeFrameDecoder extends LengthFieldBasedFrameDecoder {

    public SludgeFrameDecoder() {
        this(1024, 14, 4);
    }


    public SludgeFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

}
