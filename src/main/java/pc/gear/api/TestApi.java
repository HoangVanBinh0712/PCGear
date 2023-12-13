package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.TestRequest;
import pc.gear.security.CustomUserDetail;
import pc.gear.service.JwtService;

@RestController
@RequestMapping(value = "test1")
@Log4j2
public class TestApi {
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "")
    public Object greeting(@RequestBody @Valid TestRequest request, @AuthenticationPrincipal CustomUserDetail userDetail) {
//        SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        return userDetail;
    }
}
