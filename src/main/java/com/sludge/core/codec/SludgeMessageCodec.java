package com.sludge.core.codec;

import com.sludge.core.protocol.SludgeMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
public class SludgeMessageCodec extends MessageToMessageCodec<ByteBuf, SludgeMessage> {

    private static final String PROTOCOL = "sludge";

    private static final int SERIALIZE_TYPE_JAVA = 0;

    @Override
    protected void encode(ChannelHandlerContext ctx, SludgeMessage msg, List<Object> outList) throws Exception {
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        //magicNum 6 bytes
        out.writeBytes(PROTOCOL.getBytes());
        //version 1 byte
        out.writeByte(1);
        //serialize type  0 as jdk  1 byte
        out.writeByte(SERIALIZE_TYPE_JAVA);
        //message type 1 byte
        out.writeByte(msg.getMessageType());
        //sequenceId 4 bytes
        out.writeInt(msg.getSequenceId());
        //ignored
        out.writeByte(0xff);
        //getBody
        byte[] bytes = Serializer.Algorithm.values()[SERIALIZE_TYPE_JAVA].serialize(msg);
        //body length 4 bytes
        out.writeInt(bytes.length);
        //write body
        out.writeBytes(bytes);
        log.info("flush nettyOutBound");
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf magicNum = in.readBytes(6);
        byte version = in.readByte();
        byte serializeType = in.readByte();
        byte messageType = in.readByte();
        int sequenceId = in.readInt();
        //ignored
        in.readByte();
        int contentLength = in.readInt();
        byte[] bytes = new byte[contentLength];
        in.readBytes(bytes, 0, contentLength);
        /**
         * Serializer.Algorithm.values() gets enumArray
         * jdk serializer
         */
        SludgeMessage message = Serializer.Algorithm.values()[serializeType].deserialize(SludgeMessage.class, bytes);
        log.info("-----from nettyInBound----------{},{},{},{},{},{}", magicNum, version, serializeType, messageType, sequenceId, contentLength);
        out.add(message);
    }
}
