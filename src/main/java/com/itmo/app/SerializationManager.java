package com.itmo.app;

import java.io.*;

public class SerializationManager {
    public static <T extends Serializable> T readObject(byte[] data) throws IOException, ClassNotFoundException, ClassCastException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream obs = new ObjectInputStream(byteStream);
        return (T) obs.readObject();
    }

    public static <T extends Serializable> byte[] writeObject(T object) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(object);
        return outputStream.toByteArray();
    }
}