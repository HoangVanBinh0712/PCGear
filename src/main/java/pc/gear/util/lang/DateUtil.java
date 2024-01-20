package pc.gear.util.lang;

import org.apache.commons.lang3.StringUtils;
import pc.gear.util.Constants;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static final String YYYY_MM_DD_HYPHEN = "yyyy-MM-dd";

    public static final String YYYY_MM_DD_SLASH = "yyyy/MM/dd";

    public static final String BASIC_ISO_DATE = "yyyyMMdd";

    public static final String YYYY_MM_DD_HH_MM_SS_ZZZ = "yyyy-MM-dd HH:mm:ss.zzz";
    public static final String YYYY_MM_DD_HH_MM_SS_ZZZZZZ = "yyyy-MM-dd HH:mm:ss.zzzzzz";

    public static LocalDateTime defaultLocalDatetime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return LocalDateTime.MIN;
        }
        return dateTime;
    }

    public static String localDatetimeToPattern(LocalDateTime dateTime, String pattern) {

        if (dateTime == null) {
            return Constants.EMPTY;
        }
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateToPattern(LocalDate date, String pattern) {

        if (date == null) {
            return Constants.EMPTY;
        }
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String getBasicIsoDate() {

        return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public static LocalDateTime currentDate() {
        return LocalDateTime.now();
    }

    public static LocalDateTime toLocalDatetime(String dateTimeStr, String pattern) {

        if (StringUtils.isNotBlank(dateTimeStr) && StringUtils.isNotBlank(pattern)) {
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    public static LocalDate toLocalDate(String dateStr, String pattern) {

        if (StringUtils.isNotBlank(dateStr) && StringUtils.isNotBlank(pattern)) {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        }
        return null;
    }

    public int compareLocalDateFromString(String dateStr1, String pattern1, String dateStr2, String pattern2) {

        LocalDate date1 = toLocalDate(dateStr1, pattern1);
        LocalDate date2 = toLocalDate(dateStr2, pattern2);
        return compareLocalDate(date1, date2);
    }

    public int compareLocalDateTimeFromString(String dateStr1, String pattern1, String dateStr2, String pattern2) {

        LocalDateTime date1 = toLocalDatetime(dateStr1, pattern1);
        LocalDateTime date2 = toLocalDatetime(dateStr2, pattern2);
        return compareLocalDateTime(date1, date2);
    }

    public static void main(String[] args) {
        testCompareLocalDate();
        testCompareLocalDateTime();
        testCompareTimeStamp();
    }

    private static void testCompareLocalDate() {
        System.out.println("-------------------Start-test-compare-localdate-------------------");
        LocalDate d1 = LocalDate.now();
        LocalDate d2 = LocalDate.parse("2023-12-12", DateTimeFormatter.ISO_DATE);
        LocalDate d3 = LocalDate.parse("2024-12-12", DateTimeFormatter.ISO_DATE);
        System.out.println(compareLocalDate(d1, d2));
        System.out.println(compareLocalDate(d1, d3));
        System.out.println(compareLocalDate(d1, d1));
        System.out.println(compareLocalDate(d1, null));
        System.out.println(compareLocalDate(null, null));
        System.out.println(compareLocalDate(null, d2));
        System.out.println("-------------------End-test-compare-localdate-------------------");
    }


    private static void testCompareLocalDateTime() {
        System.out.println("-------------------Start-test-compare-localdatetime-------------------");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime d1 = LocalDateTime.now();
        LocalDateTime d2 = LocalDateTime.parse("2023-12-12 12:12:12", dateTimeFormatter);
        LocalDateTime d3 = LocalDateTime.parse("2024-12-12 12:12:12", dateTimeFormatter);
        System.out.println(compareLocalDateTime(d1, d2));
        System.out.println(compareLocalDateTime(d1, d3));
        System.out.println(compareLocalDateTime(d1, d1));
        System.out.println(compareLocalDateTime(d1, null));
        System.out.println(compareLocalDateTime(null, null));
        System.out.println(compareLocalDateTime(null, d2));
        System.out.println("-------------------End-test-compare-localdatetime-------------------");
    }
    private static void testCompareTimeStamp() {
        System.out.println("-------------------Start-test-compare-timestamp-------------------");

        Timestamp d1 = new Timestamp(System.currentTimeMillis());
        Timestamp d2 = new Timestamp(0,11,31,23,59,59,0);
        Timestamp d3 = new Timestamp(2024,11,31,23,59,59,0);
        System.out.println(compareTimeStamp(d1, d2));
        System.out.println(compareTimeStamp(d1, d3));
        System.out.println(compareTimeStamp(d1, d1));
        System.out.println(compareTimeStamp(d1, null));
        System.out.println(compareTimeStamp(null, null));
        System.out.println(compareTimeStamp(null, d2));
        System.out.println("-------------------End-test-compare-timestamp-------------------");
    }

    /**
     *
     * If d1 = d2 =>  0
     * If d1 > d2 =>  1
     * If d1 < d2 => -1
     * Null is always the smallest
     *
     * @param d1
     * @param d2
     * @return int
     */
    public static int compareLocalDate(LocalDate d1, LocalDate d2) {
        if (d1 != null && d2 == null) {
            return 1;
        }
        if (d1 == null && d2 != null) {
            return -1;
        }
        if (d1 == null) d1 = LocalDate.MIN;
        if (d2 == null) d2 = LocalDate.MIN;
        int result = 0;
        if (d1.isAfter(d2)) result = 1;
        if (d1.isBefore(d2)) result = -1;
        return result;
    }

    /**
     * If d1 = d2 =>  0
     * If d1 > d2 =>  1
     * If d1 < d2 => -1
     * Null is always the smallest
     *
     * @param d1
     * @param d2
     * @return int
     */
    public static int compareLocalDateTime(LocalDateTime d1, LocalDateTime d2) {
        if (d1 != null && d2 == null) {
            return 1;
        }
        if (d1 == null && d2 != null) {
            return -1;
        }
        if (d1 == null) d1 = LocalDateTime.MIN;
        if (d2 == null) d2 = LocalDateTime.MIN;
        int result = 0;
        if (d1.isAfter(d2)) result = 1;
        if (d1.isBefore(d2)) result = -1;
        return result;
    }

    /**
     * If d1 = d2 =>  0
     * If d1 > d2 =>  1
     * If d1 < d2 => -1
     * Null is always the smallest
     *
     * @param d1
     * @param d2
     * @return int
     */
    public static int compareTimeStamp(Timestamp d1, Timestamp d2) {
        if (d1 != null && d2 == null) {
            return 1;
        }
        if (d1 == null && d2 != null) {
            return -1;
        }
        // If null set to 0l
        if (d1 == null) d1 = new Timestamp(0);
        if (d2 == null) d2 = new Timestamp(0);
        int result = 0;
        if (d1.after(d2)) result = 1;
        if (d1.before(d2)) result = -1;
        return result;
    }
}
