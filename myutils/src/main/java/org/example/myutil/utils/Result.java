package org.example.myutil.utils;

import java.util.Optional;
import java.util.PrimitiveIterator;

/**
 * @author hzhq1255
 * @version 1.0
 * @since 2022-11-28 12:46 am
 *
 */
public class Result<T> {

    private Boolean success;
    private Integer errCode;
    private String errMsg;
    private String errDetail;
    private T data;

    public static <T> Result<T> success(){
        return new Result<>(true, 0, "", "", null);
    }

    public static <T> Result<T> success(T data){
        return new Result<>(true, 0, "", "", data);
    }

    public static <T> Result<T> failed(ErrorCode errorCode){
        return new Result<>(true, errorCode.errCode, errorCode.errMsg, "", null);
    }

    public static <T> Result<T> failed(ErrorCode errorCode, Throwable e){
        String errDetail = Optional.ofNullable(e).map(Throwable::toString).orElse("");
        return new Result<>(true, errorCode.errCode, errorCode.errMsg, errDetail, null);
    }

    public static <T> Result<T> failed(Integer errCode, String errMsg, String errDetail){
        return new Result<>(true, errCode, errMsg, errDetail, null);
    }

    public static <T> Result<T> failed(Integer errCode, String errMsg, String errDetail, Throwable e){
        StringBuilder errorBuilder = new StringBuilder(errDetail);
        errorBuilder.append(Optional.ofNullable(e).map(Throwable::toString).orElse(""));
        return new Result<>(true, errCode, errMsg, errorBuilder.toString(), null);
    }

    public enum ErrorCode{
        COMMON_SUCCESS(0, "success"),
        COMMON_FAILED(1, "failed"),
        ;
        final Integer errCode;

        final String errMsg;

        ErrorCode(Integer errCode, String errMsg) {
            this.errCode = errCode;
            this.errMsg = errMsg;
        }
    }



    public Result(Boolean success, Integer errCode, String errMsg, String errDetail, T data) {
        this.success = success;
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.errDetail = errDetail;
        this.data = data;
    }

    public Result() {
        this.success = false;
        this.errCode = 0;
        this.errMsg = "";
        this.errDetail = "";
        this.data = null;
    }
}
