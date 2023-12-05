package pc.gear.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {

    public static Double divideDoubleInteger(Double d, Integer i, Double defaultValue) {

        if (d != null && i != null && i != 0) {
            return d / i;
        }
        return defaultValue;
    }

    public static Double divideDoubleLong(Double d, Long i, Double defaultValue) {

        if (d != null && i != null && i != 0) {
            return d / i;
        }
        return defaultValue;
    }

    public static Double multiplyDoubleLong(Double d, Long i, Double defaultValue) {

        if (d != null && i != null) {
            return d * i;
        }
        return defaultValue;
    }

    public static Double multiplyDoubleInteger(Double d, Integer i, Double defaultValue) {

        if (d != null && i != null) {
            return d * i;
        }
        return defaultValue;
    }

    public static Long multiplyIntegerInteger(Integer d, Integer i, Integer defaultValue) {

        if (d != null && i != null) {
            return d.longValue() * i.longValue();
        }
        return defaultValue != null ? defaultValue.longValue() : null;
    }

    public static Long multiplyLongLong(Long d, Long i, Long defaultValue) {

        if (d != null && i != null) {
            return d * i;
        }
        return defaultValue;
    }

    public static Double scaleDouble(Double d, int fraction, RoundingMode roundingMode) {

        BigDecimal bigDecimal = new BigDecimal(d);
        return bigDecimal.setScale(fraction, roundingMode).doubleValue();
    }


    public static void main(String[] args) {

        System.out.println(scaleDouble(divideDoubleInteger(5.377, 3, null), 2, RoundingMode.HALF_UP));
        System.out.println(divideDoubleInteger(5.377, 3, null));
    }
}
