package com.sludge.client;

import com.sludge.sample.SludgeService;

class ClientManagerTest {

    public static void main(String[] args) {
        SludgeService service = ClientManager.getService(SludgeService.class);
        String caonima = service.sayHello("caonima");
        System.out.println("----result---" + caonima);
    }
}