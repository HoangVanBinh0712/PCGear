package pc.gear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.response.GetAllUserResponse;
import pc.gear.response.GetUserInfoResponse;
import pc.gear.service.UserService;
import pc.gear.util.UriConstants;
import pc.gear.util.response.ApiResponse;

@RestController
@RequestMapping(value = UriConstants.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = UriConstants.INFO)
    public ApiResponse<GetUserInfoResponse> getUserInfo() {
        return new ApiResponse<>(new GetUserInfoResponse("BinhHV", 19, "MALE"));
    }

    @GetMapping(value = UriConstants.ALL)
    public ApiResponse<GetAllUserResponse> getAllUser() {
        return new ApiResponse<>(userService.getAllUser());
    }
}
