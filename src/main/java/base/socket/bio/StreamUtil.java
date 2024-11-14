package main.java.base.socket.bio;

import java.io.Closeable;

public class StreamUtil {

    public static void close(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (Exception e) {
            System.out.println("Error closing stream " + stream.getClass().getName());
        }
    }
}
