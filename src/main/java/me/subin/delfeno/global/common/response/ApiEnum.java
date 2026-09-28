package me.subin.delfeno.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 클라이언트에게 반환될 성공 및 에러 응답 본문의 메타 정보를 관리하는 열거형
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ApiEnum {

    /**
     * 공통 성공 응답
     */
    OK (HttpStatus.OK, 100000, "요청이 정상적으로 처리되었습니다."),
    CREATED (HttpStatus.CREATED, 101000, "새로운 리소스가 생성되었습니다."),

    /**
     * 공통 에러 응답
     */
    INVALID_REQUEST_PARAMETERS (HttpStatus.BAD_REQUEST, -800100, "요청 파라미터가 유효하지 않습니다."),
    UNSUPPORTED_HTTP_METHOD (HttpStatus.METHOD_NOT_ALLOWED, -805100, "지원하지 않는 HTTP 메소드 요청입니다."),
    INTERNAL_SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR, -900000, "내부 서버에 오류가 발생했습니다.")
    ;

    private final HttpStatus status;

    private final int code;

    private final String message;

    /**
     * HTTP 상태 코드에 따른 성공 응답 코드의 열거형 상수를 변환한다.
     *
     * @param status    HTTP 상태 코드
     * @return          HTTP 상태 코드가 {@code CREATED}인 경우 {@link ApiEnum#CREATED}를, 그렇지 않은 경우 {@link ApiEnum#OK} 반환
     */
    public static ApiEnum resolve(HttpStatus status) {
        if (status == HttpStatus.CREATED) {
            return ApiEnum.CREATED;
        }
        return ApiEnum.OK;
    }
}