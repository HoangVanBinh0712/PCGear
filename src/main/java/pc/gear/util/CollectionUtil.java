package pc.gear.util;

import java.util.List;
import java.util.Objects;

public class CollectionUtil {

    public static <T> T getFirstFormCollection(List<T> list) {
        return list.stream().findFirst().orElse(null);
    }

    public static <T> boolean allContains(List<T> sources, List<T> list) {
        for (T i : list) {
            if (sources.stream().noneMatch(s -> Objects.equals(s, i))) {
                return false;
            }
        }
        return true;
    }
}
