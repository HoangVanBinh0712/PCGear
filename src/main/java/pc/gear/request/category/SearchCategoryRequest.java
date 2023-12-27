package pc.gear.request.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pc.gear.dto.SearchRequest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchCategoryRequest extends SearchRequest {
    private String text;

}
