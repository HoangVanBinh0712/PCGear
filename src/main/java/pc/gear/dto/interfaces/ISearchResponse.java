package pc.gear.dto.interfaces;

import java.util.List;

public interface ISearchResponse<T> {
    List<T> getContents();

    Integer getTotalRow();
}
