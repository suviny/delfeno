package me.subin.delfeno.global.common.response;

import lombok.Getter;

/**
 * 클라이언트의 요청이 정상적으로 처리되었을 경우 반환될 성공 응답 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 * @param <T> 응답 본문에 포함될 응답 데이터의 타입
 */
@Getter
public class ApiResponse<T> extends BaseResponse {

    private final T data;

    private ApiResponse(int code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    /**
     * 성공 응답 객체를 생성한다.
     *
     * @param apiEnum   API 응답의 메타 정보를 정의한 열거형 상수
     * @param data      본문에 포함되어 반환될 응답 데이터
     * @return          생성된 성공 응답 객체 반환
     * @param <T>       응답 데이터의 타입
     */
    public static <T> ApiResponse<T> success(ApiEnum apiEnum, T data) {
        return new ApiResponse<>(apiEnum.getCode(), apiEnum.getMessage(), data);
    }
}