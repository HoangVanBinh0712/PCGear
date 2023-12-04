package pc.gear.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pc.gear.util.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String code;
    private T data;

    public ApiResponse(T data) {
        this.data = data;
        this.code = Constants.SUCCESS_CODE;
    }
}
