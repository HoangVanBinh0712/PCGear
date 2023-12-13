package pc.gear.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pc.gear.entity.Admin;
import pc.gear.entity.Customer;
import pc.gear.util.Constants;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long userID;

    private String name;

    private String userName;

    private String email;

    private String departmentCd;

    public User(Customer customer) {
        this.userID = customer.getCustomerId();
        this.name = customer.getName();
        this.userName = customer.getUserName();
        this.email = customer.getEmail();
        this.departmentCd = customer.getDepartment().getDepartmentCd();
    }

    public User(Admin admin) {
        this.userID = admin.getId();
        this.name = admin.getName();
        this.userName = admin.getUserName();
        this.email = admin.getEmail();
        this.departmentCd = admin.getDepartment().getDepartmentCd();
    }

    public User(Claims claims) {
        this.userID = claims.get(Constants.CLAIMS_USER_ID, Long.class);
        this.name = claims.get(Constants.CLAIMS_NAME, String.class);
        this.userName = claims.get(Constants.CLAIMS_USER_NAME, String.class);
        this.email = claims.get(Constants.CLAIMS_EMAIL, String.class);
        this.departmentCd = claims.get(Constants.CLAIMS_DEPARTMENT, String.class);
    }
}
