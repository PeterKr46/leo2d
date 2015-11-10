package net.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by Peter on 25.05.2015.
 */
public class ByteConverter {

    public static byte[] string2Bytes(String string) {
        return string.getBytes();
    }

    public static String bytes2String(byte[] bytes) {
        String tmp;
        String content = "";
        try {
            tmp = new String(bytes, "UTF-8");
            for(int i = 0; i < tmp.length(); i+= 2) {
                content += tmp.charAt(i);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }
}
