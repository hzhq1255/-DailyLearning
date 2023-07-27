package org.hzhq.myutil.exception;

/**
 * @author guokang
 * @date 2022/11/15 15:37
 */
public class HelmException extends RuntimeException {

    private Code code;


    public HelmException() {
    }

    public HelmException(String message) {
        super(message);
        this.code = handleErrorMessage(message);
    }

    public HelmException(String message, Throwable cause) {
        super(message, cause);
    }

    public HelmException(Throwable cause) {
        super(cause);
    }

    public HelmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean isNotFound(){
        return this.code == Code.NOT_FOUND;
    }

    public boolean isAlreadyExists(){
        return this.code == Code.ALREADY_EXISTS;
    }

    public boolean isAccessDenied(){
        return this.code == Code.ACCESS_DENIED;
    }

    public boolean isTimeout(){
        return this.code == Code.TIMEOUT;
    }


    private Code handleErrorMessage(String message){
        message = message == null? "": message.toLowerCase();
        if (message.contains("not found") || message.contains("notfound")){
            return Code.NOT_FOUND;
        } else if (message.contains("exist")){
            return Code.ALREADY_EXISTS;
        } else if (message.contains("access denied")){
            return Code.ACCESS_DENIED;
        } else if (message.contains("timeout") || message.contains("time out")){
            return Code.TIMEOUT;
        }
        return Code.UNKNOWN;
    }

    public enum Code{
        NOT_FOUND,
        ALREADY_EXISTS,
        ACCESS_DENIED,
        TIMEOUT,
        UNKNOWN,
    }

}
