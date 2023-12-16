package pc.gear.config.exception;

public class PcGearException extends RuntimeException {
    public PcGearException(String message) {
        super(message);
    }
    public PcGearException(String message, Exception e) {
        super(message, e);
    }
}
