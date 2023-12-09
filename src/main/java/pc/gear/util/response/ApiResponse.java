package pc.gear.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private List<ApiError> errors;

    private T data;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(List<ApiError> errors) {
        this.errors = errors;
    }
}
