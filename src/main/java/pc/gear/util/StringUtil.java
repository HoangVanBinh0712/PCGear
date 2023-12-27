package pc.gear.util;

public class StringUtil {

    public static String addQueryContains(String text) {
        if (text == null) {
            text = Constants.EMPTY;
        }
        return Constants.PERCENT + text + Constants.PERCENT;
    }
}
