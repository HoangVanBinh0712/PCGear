package pc.gear.util.lang;

import org.springframework.web.multipart.MultipartFile;
import pc.gear.util.Constants;

import java.io.IOException;

public class MultipartUtil {

    public static boolean checkSize(MultipartFile file, int size) {
        try {
            if (file.getBytes().length <= size * Constants.MEGA_BYTE) {
                return true;
            }
        } catch (IOException ignored) {
        }
        return false;
    }
}
