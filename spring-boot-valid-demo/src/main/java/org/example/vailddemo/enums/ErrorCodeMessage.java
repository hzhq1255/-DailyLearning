package org.example.vailddemo.enums;

/**
 * @author hzhq1255
 * @date 2022/2/1 1:23
报错枚举类
 */
public enum ErrorCodeMessage {

    // 未知错误
    UNKNOWN_ERROR(1, "未知错误", " unknown error")
    ;

    private int code;
    private String chReasonMsg;
    private String enReasonMsg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getChReasonMsg() {
        return chReasonMsg;
    }

    public void setChReasonMsg(String chReasonMsg) {
        this.chReasonMsg = chReasonMsg;
    }

    public String getEnReasonMsg() {
        return enReasonMsg;
    }

    public void setEnReasonMsg(String enReasonMsg) {
        this.enReasonMsg = enReasonMsg;
    }

    ErrorCodeMessage() {
    }

    ErrorCodeMessage(int code, String chReasonMsg, String enReasonMsg) {
        this.code = code;
        this.chReasonMsg = chReasonMsg;
        this.enReasonMsg = enReasonMsg;
    }
}
