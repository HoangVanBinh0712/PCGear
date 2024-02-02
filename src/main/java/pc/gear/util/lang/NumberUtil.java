package pc.gear.util.lang;

import org.apache.commons.lang3.math.NumberUtils;
import pc.gear.util.Constants;

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

    public static String getNumberFormat(int i, int f) {
        StringBuilder patternBuilder = new StringBuilder();
        patternBuilder.append(Constants.HASH.repeat(Math.max(0, i)));
        if (f > 0) {
            patternBuilder.append(Constants.DOT);
            patternBuilder.append(Constants.HASH.repeat(f));
        }
        return patternBuilder.toString();
    }

    public static Integer add2Integer(Integer a, Integer b) {
        if (a == null || b == null) {
            return null;
        }
        return a + b;
    }

    public static boolean isValidNumber(Object item, int integerPart, int fractionPart) {
        if (NumberUtils.isCreatable(item.toString())) {
            String s = new BigDecimal(item.toString()).toPlainString();
            String[] parts = s.split("\\.");
            if (parts.length == 1 && fractionPart == 0) {
                return parts[0].length() <= integerPart;
            }
            if (parts.length == 2) {
                return parts[0].length() <= integerPart && parts[1].length() <= fractionPart;
            }
        }
        return false;
    }

    public static boolean validateFormatNumber(String number, int integer, int fraction) {
        boolean isValid = true;
        if (!NumberUtils.isCreatable(number)) {
            isValid = false;
        }
        String[] splitter = number.split("\\.");
        if (splitter.length > 1) {
            if (number.length() > (integer + fraction + 1)) {
                isValid = false;
            }
            // Before Decimal Count
            if (splitter[0].length() > integer) {
                isValid = false;
            }
            // After Decimal Count
            if (splitter[1].length() > fraction) {
                isValid = false;
            }
        } else {
            if (number.length() > integer) {
                isValid = false;
            }
        }
        return isValid;
    }

    public static Object convertToType(String value, Class<?> targetType) {
        try {
            if (targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == Long.class) {
                return Long.parseLong(value);
            } else if (targetType == Double.class) {
                return Double.parseDouble(value);
            } else if (targetType == BigDecimal.class) {
                return new BigDecimal(value);
            } else if (targetType == String.class) {
                return value;
            }

        } catch (NumberFormatException e) {

        }
        return null;
    }

    public static String generateMessageDigit(int integer, int fraction) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#".repeat(integer));
        if (fraction > 0) {
            stringBuilder.append(".");
            stringBuilder.append("#".repeat(fraction));
        }
        return stringBuilder.toString();
    }

    public static boolean validateIntegerNumber(Number number, int max) {
        if (number == null) return true;
        String val = String.valueOf(number);
        BigDecimal b = new BigDecimal(val);
        String str = b.abs().toPlainString();
        return !str.contains(".") && str.length() <= max;
    }

    public static boolean validateIntegerNumber(String number, int max) {
        if (number == null) return true;
        BigDecimal b = new BigDecimal(number);
        String str = b.abs().toPlainString();
        return !str.contains(".") && str.length() <= max;
    }

    public static void main(String[] args) {

        System.out.println(scaleDouble(divideDoubleInteger(5.377, 3, null), 2, RoundingMode.HALF_UP));
        System.out.println(divideDoubleInteger(5.377, 3, null));
    }
}
