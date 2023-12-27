package pc.gear.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pc.gear.dto.interfaces.ISearchRequest;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class SearchRequest implements ISearchRequest {
    Integer pageNumber;

    Integer pageSize;

    String sortFields;

    String sortDirections;

}
