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
}
