package org.example.vailddemo.model;

import org.example.vailddemo.enums.ErrorCodeMessage;

/**
 * @author hzhq1255
 * @date 2022/2/1 1:21
 *
 * 通用返回类
 */
public class Result<T> {



    private static final int BUSINESS_SUCCESS_CODE = 0;
    private static final int BUSINESS_UNKNOWN_CODE = 1;


    /**
     * 业务状态码
     * 0 为成功
     * 1 为未知错误
     * 不为 0 则不成功
     */
    private int code = 0;
    /**
     * http请求是否成功
     */
    private boolean success = true;
    /**
     * 报错信息
     */
    private String errorMessage;
    /**
     * 报错详细信息
     */
    private String errorDetailMessage;
    /**
     * 返回数据
     */
    private T data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetailMessage() {
        return errorDetailMessage;
    }

    public void setErrorDetailMessage(String errorDetailMessage) {
        this.errorDetailMessage = errorDetailMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Result() {
    }

    public Result(int code, boolean success, String errorMessage, String errorDetailMessage, T data) {
        this.code = code;
        this.success = success;
        this.errorMessage = errorMessage;
        this.errorDetailMessage = errorDetailMessage;
        this.data = data;
    }

    /**
     * 全参数构造
     * @param code 业务码
     * @param success 成功状态
     * @param errorMessage 报错信息
     * @param errorDetailMessage 报错详细信息
     * @param data 响应数据
     * @param <T> 泛型
     * @return result
     */
    public static <T> Result<T> build(int code, boolean success, String errorMessage, String errorDetailMessage, T data){
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setSuccess(success);
        result.setErrorMessage(errorMessage);
        result.setErrorDetailMessage(errorDetailMessage);
        result.setData(data);
        return result;
    }

    /**
     * 无参 success
     * @param <T> 泛型
     * @return 返回 result
     */
    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.setCode(BUSINESS_SUCCESS_CODE);
        result.setSuccess(true);
        return result;
    }


    /**
     * 带有响应数据的 result
     * @param data 响应数据
     * @param <T> 泛型
     * @return result
     */
    public static <T> Result<T> success(T data){
        Result<T> result = success();
        result.setData(data);
        return result;
    }

    /**
     * 默认 error 未知错误 状态码 1
     * success false
     * @param <T> 返回
     * @return result
     */
    public static <T> Result<T> error(){
        Result<T> result = new Result<>();
        result.setCode(BUSINESS_UNKNOWN_CODE);
        result.setSuccess(false);
        return result;
    }


    /**
     * 错误枚举类方法
     * @param errorCodeMessage 错误信息枚举
     * @param <T> 泛型
     * @return result
     */
    public static <T> Result<T> error(ErrorCodeMessage errorCodeMessage){
        Result<T> result = new Result<>();
        result.setCode(errorCodeMessage.getCode());
        result.setSuccess(false);
        result.setErrorMessage(errorCodeMessage.getChReasonMsg());
        return result;
    }


    /**
     * 带有详细信息的错误返回
     * @param errorCodeMessage 错误信息枚举
     * @param errorDetailMessage 错误详细信息
     * @param <T> 泛型
     * @return result
     */
    public static <T> Result<T> error(ErrorCodeMessage errorCodeMessage, String errorDetailMessage){
        Result<T> result = error(errorCodeMessage);
        result.setErrorDetailMessage(errorDetailMessage);
        return result;
    }
}
