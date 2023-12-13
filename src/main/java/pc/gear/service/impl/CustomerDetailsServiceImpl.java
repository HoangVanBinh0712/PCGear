package pc.gear.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pc.gear.entity.Customer;
import pc.gear.repository.CustomerRepository;
import pc.gear.security.CustomUserDetail;
import pc.gear.util.MessageConstants;
import pc.gear.util.type.Role;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findTop1ByUserName(username);
        if (customer != null) {
            CustomUserDetail userDetail = new CustomUserDetail();
            userDetail.setUsername(username);
            userDetail.setUserId(customer.getCustomerId());
            userDetail.setRole(Role.CUSTOMER);
            return userDetail;
        }
        throw new UsernameNotFoundException(messageSource.getMessage(MessageConstants.USER_NOT_FOUND_MESSAGE, new String[] { username }, LocaleContextHolder.getLocale()));
    }
}
