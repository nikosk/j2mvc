package gr.dsigned.jmvc.exceptions;

/**
 *
 * @author Nikosk <nikosk@dsigned.gr>
 */
public class CustomHttpException extends Exception {
    public enum HttpErrors {
        E404, E500, CUSTOM
    }
    private HttpErrors errorCode;
    
    public CustomHttpException(String message, HttpErrors er) {
        super(message);
        this.errorCode = er;
    }

    public HttpErrors getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpErrors errorCode) {
        this.errorCode = errorCode;
    }

   
}
