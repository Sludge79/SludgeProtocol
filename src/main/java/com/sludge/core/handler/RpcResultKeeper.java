package com.sludge.core.handler;

import io.netty.util.concurrent.Promise;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcResultKeeper {

    public static final Map<Integer, Promise<Object>> RESULT_KEEPER = new ConcurrentHashMap<>();

}
