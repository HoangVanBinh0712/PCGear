package pc.gear.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.repository.UserRepository;
import pc.gear.response.GetAllUserResponse;
import pc.gear.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public GetAllUserResponse getAllUser() {
        return GetAllUserResponse.builder()
                .totalRows(userRepository.count())
                .users(userRepository.findAll().stream().map(user -> GetAllUserResponse.UserInfo.builder()
                        .name(user.getName())
                        .age(user.getAge())
                        .gender(user.getGender().toString())
                        .build()).toList()).build();
    }
}
