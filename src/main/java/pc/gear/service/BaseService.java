package pc.gear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import pc.gear.config.exception.PcGearException;

@Component
public abstract class BaseService {

    @Autowired
    private MessageSource messageSource;



    public void throwError(String messageCode, Object... args) {
        throw new PcGearException(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()));
    }

}
