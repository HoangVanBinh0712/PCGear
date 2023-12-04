package pc.gear.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllUserResponse {

    private List<UserInfo> users;

    private Long totalRows;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserInfo {

        private String name;
        private Integer age;
        private String gender;
    }


}
