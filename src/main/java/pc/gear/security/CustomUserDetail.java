package pc.gear.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pc.gear.util.type.Role;

import java.util.Collection;

@Getter
@Setter
public class CustomUserDetail implements UserDetails {

    private Long userId;
    private String username;
    private Role role;
    protected Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetail() {
        super();
    }

    public CustomUserDetail(Long userId, String username, Role role,
                            Collection<? extends GrantedAuthority> authorities) {
        super();
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
