package cn.zcw.entity;

import java.io.Serializable;

/**
 * 分页结果集
 */
public class Result implements Serializable {

    // 成功或者失败
    private boolean success;
    // 提示消息
    private String message;
    // 总记录数
    private Long total;
    // 表示数据
    private Object data;

    public Result() {}

    // 返回执行成功/失败
    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // 例如：通过主键查询
    public Result(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // 分页查询返回结果
    public Result(boolean success, String message, Long total, Object data) {
        this.success = success;
        this.message = message;
        this.total = total;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}