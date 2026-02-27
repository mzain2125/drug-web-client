package Learn.web.client.exception;

/**
 * Thrown when the fetched XML cannot be parsed into the expected structure.
 */
public class XmlParseException extends RuntimeException {

    public XmlParseException(String message) {
        super(message);
    }

    public XmlParseException(String message, Throwable cause) {
        super(message, cause);
    }
}