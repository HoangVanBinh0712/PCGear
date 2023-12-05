package pc.gear.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";

    public static final String BASIC_ISO_DATE = "yyyyMMdd";

    public static final String YYYY_MM_DD_HH_MM_SS_ZZZ = "yyyy-MM-dd HH:mm:ss.zzz";

    public String localDatetimeToPattern(LocalDateTime dateTime, String pattern) {

        if (dateTime == null) {
            return Constants.EMPTY;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public String localDateToPattern(LocalDate date, String pattern) {

        if (date == null) {
            return Constants.EMPTY;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getBasicIsoDate() {

        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public LocalDateTime toLocalDatetime(String dateTimeStr, String pattern) {

        if (StringUtils.isNotBlank(dateTimeStr) && StringUtils.isNotBlank(pattern)) {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    public LocalDate toLocalDate(String dateStr, String pattern) {

        if (StringUtils.isNotBlank(dateStr) && StringUtils.isNotBlank(pattern)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    public int compareLocalDate(LocalDate date1, LocalDate date2) {

        if (date1 != null) {
            return date1.compareTo(date2);
        }
        return date2 == null ? 0 : -1;
    }

    public int compareLocalDate(LocalDateTime date1, LocalDateTime date2) {

        if (date1 != null) {
            return date1.compareTo(date2);
        }
        return date2 == null ? 0 : -1;
    }

    public int compareLocalDateFromString(String dateStr1, String pattern1, String dateStr2, String pattern2) {

        LocalDate date1 = toLocalDate(dateStr1, pattern1);
        LocalDate date2 = toLocalDate(dateStr2, pattern2);
        return compareLocalDate(date1, date2);
    }

    public int compareLocalDateTimeFromString(String dateStr1, String pattern1, String dateStr2, String pattern2) {

        LocalDateTime date1 = toLocalDatetime(dateStr1, pattern1);
        LocalDateTime date2 = toLocalDatetime(dateStr2, pattern2);
        return compareLocalDate(date1, date2);
    }
}
