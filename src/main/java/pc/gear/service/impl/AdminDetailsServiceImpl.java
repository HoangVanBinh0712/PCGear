package pc.gear.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pc.gear.entity.Admin;
import pc.gear.repository.AdminRepository;
import pc.gear.security.AdminUserDetail;
import pc.gear.util.type.Role;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByUserName(username).stream().findFirst().orElse(null);
        if (admin != null) {
            AdminUserDetail userDetail = new AdminUserDetail();
            userDetail.setUsername(username);
            userDetail.setUserId(admin.getId());
            userDetail.setRole(Role.ADMIN);
        }
        return null;
    }
}
