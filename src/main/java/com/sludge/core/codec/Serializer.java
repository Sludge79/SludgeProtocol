package com.sludge.core.codec;

import java.io.*;

public interface Serializer {

    <T> byte[] serialize(T object);


    <T> T deserialize(Class<T> clazz, byte[] bytes);


    enum Algorithm implements Serializer {
        Java {
            @Override
            public <T> byte[] serialize(T object) {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    return bos.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("could not serialize", e);
                }
            }

            @Override
            public <T> T deserialize(Class<T> clazz, byte[] bytes) {
                try {
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    return clazz.cast(ois.readObject());
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("could not deserialize", e);
                }
            }
        }
    }
}
