package Learn.web.client.exception;

/**
 * Thrown when the remote UNSC XML endpoint cannot be reached or returns an error response.
 */
public class XmlFetchException extends RuntimeException {

    public XmlFetchException(String message) {
        super(message);
    }

    public XmlFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}