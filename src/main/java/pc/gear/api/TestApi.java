package pc.gear.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pc.gear.request.TestRequest;

@RestController
@RequestMapping(value = "test")
@Log4j2
public class TestApi {
    @Autowired
    private MessageSource messageSource;

    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public String greeting(@RequestBody @Valid TestRequest request) {
        log.info(request);
        return messageSource.getMessage("greeting.message", null, LocaleContextHolder.getLocale());
    }
}
