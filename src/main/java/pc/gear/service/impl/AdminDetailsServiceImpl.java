package pc.gear.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pc.gear.entity.Admin;
import pc.gear.repository.AdminRepository;
import pc.gear.security.CustomUserDetail;
import pc.gear.util.MessageConstants;
import pc.gear.util.type.Role;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MessageSource messageSource;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Admin admin = adminRepository.findTop1ByUserName(username);
        if (admin != null) {
            CustomUserDetail userDetail = new CustomUserDetail();
            userDetail.setUsername(username);
            userDetail.setUserId(admin.getId());
            userDetail.setRole(Role.ADMIN);
            return userDetail;
        }
        throw new UsernameNotFoundException(messageSource.getMessage(MessageConstants.USER_NOT_FOUND_MESSAGE, new String[] { username }, LocaleContextHolder.getLocale()));
    }
}
