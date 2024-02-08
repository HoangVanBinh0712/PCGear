package pc.gear.util.lang;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
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

    public static <T> List<List<T>> splitList(List<T> originalList, int chunkSize) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < originalList.size(); i += chunkSize) {
            result.add(originalList.subList(i, Math.min(i + chunkSize, originalList.size())));
        }
        return result;
    }

}
