package com.sens.lush.vo;

/**
 * 공통 API 응답 래퍼
 * success/total/message/data를 함께 내려줄 때 사용
 *
 * 사용 예:
 *   ApiResponse<Board> res = new ApiResponse<>(true, list.size(), "OK", list);
 *   return new ObjectMapper().writeValueAsString(res);
 *
 * 또는 @RestController에서 그대로 리턴하면 Jackson이 자동 직렬화한다.
 */
public class ApiResponse<T> {

    private boolean success;
    private int total;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, int total, String message, T data) {
        this.success = success;
        this.total = total;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 축약 생성자
    public static <T> ApiResponse<T> ok(int total, T data) {
        return new ApiResponse<>(true, total, "OK", data);
    }

    // 실패 응답 축약 생성자
    public static <T> ApiResponse<T> fail(String message) {
        return new ApiResponse<>(false, 0, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
