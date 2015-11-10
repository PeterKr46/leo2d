package leo2d.util;

import java.lang.reflect.Method;

/**
 * Created by Peter on 10.11.2015.
 */
public class Callback {
    public Object target;
    private Method method;
    private Object[] values;
    public Callback(Object target, String method, Class<?>[] paramTypes, Object... values) {
        try {
             this.method = target.getClass().getMethod(method, paramTypes);
            this.values = values;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void invoke() {
        try {
            method.invoke(target, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
