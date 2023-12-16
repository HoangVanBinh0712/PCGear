package pc.gear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import pc.gear.config.exception.PcGearException;
import pc.gear.config.exception.PcGearNotFoundException;

@Component
public abstract class BaseService {

    @Autowired
    private MessageSource messageSource;

    protected void throwError(String messageCode, Object... args) {
        throw new PcGearException(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()));
    }

    protected String getMessage(String messageCode, Object... args) {
        return messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale());
    }

    protected void throwErrorNotFound(String messageCode, Object... args) {
        throw new PcGearNotFoundException(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()));
    }
}
