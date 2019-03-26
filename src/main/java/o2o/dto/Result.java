package o2o.dto;

/**
 * 对controller返回结果的封装
 * @param <T>
 */
public class Result<T> {

    private boolean success;
    private T data;

    private String errorMsg;

    private int errorCode;

    public Result() {
    }

    //成功构造器
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    //失败构造器
    public Result(boolean success,String errorMsg, int errorCode) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
