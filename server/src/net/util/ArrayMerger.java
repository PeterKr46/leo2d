package net.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 25.05.2015.
 */
public class ArrayMerger {

    private List<Byte> internal;

    public ArrayMerger(byte[] start) {
        internal = new ArrayList<>();
        append(start);
    }


    public ArrayMerger append(byte... array) {
        for(byte b : array) {
            internal.add(b);
        }
        return this;
    }

    public byte[] array() {
        byte[] result = new byte[internal.size()];
        for(int i = 0; i < result.length; i++) {
            result[i] = internal.get(i);
        }
        return result;
    }

}
