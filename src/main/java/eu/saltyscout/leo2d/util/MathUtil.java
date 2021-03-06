package eu.saltyscout.leo2d.util;

/**
 * Created by Peter on 09.11.2015.
 */
public class MathUtil {
    public static double lerp(double percentage, double origin, double target) {
        return origin + (target - origin) * Math.min(1, Math.max(0, percentage));
    }
}
