package pc.gear.util;

import java.util.List;

public class CollectionUtil {

    public static <T> T getFirstFormCollection(List<T> list) {
        return list.stream().findFirst().orElse(null);
    }
}
