package com.sludge.sample;

public class SludgeImpl implements SludgeService{
    @Override
    public String sayHello(String name) {
        return "holla: "+name;
    }
}
