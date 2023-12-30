package pc.gear.util;

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
}
