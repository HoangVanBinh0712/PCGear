package pc.gear.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pc.gear.security.CustomUserDetail;
import pc.gear.util.type.Role;

public class JwtUtil {

    public static String getCurrentUsername() {
        CustomUserDetail userDetail = getCustomUserDetail();

        return userDetail.getUsername();
    }

    public static Long getCurrentUserId() {
        CustomUserDetail userDetail = getCustomUserDetail();

        return userDetail.getUserId();
    }

    public static Role getCurrentUserRole() {
        CustomUserDetail userDetail = getCustomUserDetail();

        return userDetail.getRole();
    }

    private static CustomUserDetail getCustomUserDetail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetail) authentication.getPrincipal();
    }
}
