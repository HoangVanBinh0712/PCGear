package pc.gear.config.exception;

public class PcGearNotFoundException extends RuntimeException {
    public PcGearNotFoundException(String message) {
        super(message);
    }
    public PcGearNotFoundException(String message, Exception e) {
        super(message, e);
    }
}
