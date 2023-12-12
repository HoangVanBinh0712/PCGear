package pc.gear.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pc.gear.entity.Customer;
import pc.gear.repository.CustomerRepository;
import pc.gear.security.CustomerUserDetail;
import pc.gear.util.type.Role;

@Service
public class CustomerDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findTop1ByUserName(username);
        if (customer != null) {

            CustomerUserDetail userDetail = new CustomerUserDetail();
            userDetail.setUsername(username);
            userDetail.setUserId(customer.getCustomerId());
            userDetail.setRole(Role.CUSTOMER);
            return userDetail;
        } else {
            throw new UsernameNotFoundException("User not found !");
        }
    }
}
