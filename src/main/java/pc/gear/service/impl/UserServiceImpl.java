package pc.gear.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pc.gear.dto.User;
import pc.gear.entity.Admin;
import pc.gear.entity.Customer;
import pc.gear.entity.Department;
import pc.gear.repository.AdminRepository;
import pc.gear.repository.CustomerRepository;
import pc.gear.repository.DepartmentRepository;
import pc.gear.request.auth.LoginRequest;
import pc.gear.request.auth.RegisterRequest;
import pc.gear.response.auth.LoginResponse;
import pc.gear.service.BaseService;
import pc.gear.service.JwtService;
import pc.gear.service.UserService;
import pc.gear.util.MessageConstants;
import pc.gear.util.type.DepartmentType;
import pc.gear.validator.AuthValidation;

@Service
public class UserServiceImpl extends BaseService implements UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthValidation authValidation;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public LoginResponse customerLogin(LoginRequest request) {
        Customer customer = customerRepository.findTop1ByUserName(request.getUsername());
        if (customer != null) {
            if (passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
                User user = new User(customer);
                String accessToken = jwtService.generateCustomerAccessToken(user);
                String refreshToken = jwtService.generateCustomerRefreshToken(user);
                return LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        this.throwError(MessageConstants.WRONG_USERNAME_PASSWORD_MESSAGE_CODE);
        return null;
    }

    @Override
    public LoginResponse adminLoginLogin(LoginRequest request) {
        Admin admin = adminRepository.findTop1ByUserName(request.getUsername());
        if (admin != null) {
            if (passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                User user = new User(admin);
                String accessToken = jwtService.generateCustomerAccessToken(user);
                String refreshToken = jwtService.generateCustomerRefreshToken(user);
                return LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        }
        this.throwError(MessageConstants.WRONG_USERNAME_PASSWORD_MESSAGE_CODE);
        return null;
    }

    @Override
    public void customerRegister(RegisterRequest request) {
        authValidation.validateRegisterCustomer(request);
        Customer customer = new Customer();//modelMapper.map(request, Customer.class);
        customer.setUserName(request.getUserName());
        customer.setName(request.getName());
        customer.setPassword(request.getPassword());
        customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
        Department department = departmentRepository.findByDepartmentCd(DepartmentType.CUSTOMER.getKey());
        customer.setDepartment(department);
        customerRepository.save(customer);
    }
}
