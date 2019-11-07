package tv.engine.exceptions
import org.grails.core.exceptions.GrailsException;

/**
 * Extend off here
 *
 * @author Tobias Hagenbeek
 */
public class BaseException extends GrailsException {
    protected static final int code = 400
    private static final long serialVersionUID = 1L;

    public BaseException(msg) {
        super(msg);
    }

    public String getMessage() {
        return super.getMessage();
    }

    public Integer getCode(){
        return this.code
    }
}
