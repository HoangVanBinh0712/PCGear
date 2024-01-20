package pc.gear.util.lang;

import org.apache.commons.lang3.StringUtils;
import pc.gear.util.Constants;

public class StringUtil {

    public static String addQueryContains(String text) {
        if (text == null) {
            text = Constants.EMPTY;
        }
        return Constants.PERCENT + text + Constants.PERCENT;
    }

    public static String removeIndexFromString(String inputString) {
        // Construct the regular expression pattern to match [2] or any other index
        String regex = "\\[\\d+\\]";

        // Replace all occurrences of the matched pattern with an empty string
        return inputString.replaceAll(regex, Constants.EMPTY);
    }

    public static String generateMessageDigits(int integer, int fraction) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < integer; i++) {
            s.append("#");
        }
        if (fraction != 0) {
            s.append(".");
            for (int i = 0; i < fraction; i++) {
                s.append("#");
            }
        }
        return s.toString();
    }

    public static boolean checkSize(Object obj, int minLength, int maxLength) {
        if (obj instanceof String value) {
            return !(value == null || value.length() < minLength || value.length() > maxLength);
        }
        return false;
    }

    public static String getFileExtension(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return Constants.EMPTY;
        } else {
            String[] s = fileName.split("\\.");
            return s[s.length - 1];
        }
    }

    /**
     * Compare 2 strings
     *
     * @param s1
     * @param s2
     * @return boolean
     */
    public static boolean equal(String s1, String s2) {
        if ((s1 != null && s2 == null) || (s1 == null && s2 != null)) {
            return false;
        }
        if (s1 == null) s1 = Constants.EMPTY;
        if (s2 == null) s2 = Constants.EMPTY;
        return s1.equals(s2);
    }

    /**
     * Default String if null
     *
     * @param s1
     * @param defaultStr
     * @return String
     */
    public static String coalesce(String s1, String defaultStr) {
        if (s1 == null) {
            return defaultStr;
        }
        return s1;
    }

    /**
     *
     * Concat String with spliter
     *
     * @param spliter
     * @param values
     * @return
     */
    public static String concat(String spliter, String... values) {
        StringBuilder stringBuilder = new StringBuilder();
        if (spliter == null)
            spliter = "";
        for (String value : values) {
            stringBuilder.append(value);
            stringBuilder.append(spliter);
        }
        return stringBuilder.substring(0, stringBuilder.length() - spliter.length());
    }

    public static boolean validateTextArea(String value, int min, int max) {
        if (StringUtils.isEmpty(value)) return true;
        value = value.replace("\r\n", " ");
        return validateLengthForPoint(value, min, max);
    }

    public static boolean validateLengthForPoint(String value, int min, int max) {
        if (StringUtils.isEmpty(value)) return true;
        int len = value.codePointCount(0, value.length());
        return len >= min && len <= max;
    }

}
