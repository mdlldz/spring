package com.example.entity;

/**
 * 统一API响应结果封装类
 * 功能：规范所有控制器返回的响应格式，便于前端统一处理
 * @param <T> 响应数据的泛型类型
 */
public class ApiResponse<T> {
    private Integer code; // 响应状态码：200成功，500服务器错误，400参数错误，401未授权
    private String message; // 响应信息
    private T data; // 响应数据

    // 成功响应（带数据）
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }

    // 成功响应（无数据）
    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    // 失败响应
    public static <T> ApiResponse<T> fail(Integer code, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    // Getter和Setter
    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}