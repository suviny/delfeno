package me.subin.delfeno.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 성공 및 에러 응답 본문 구성시 사용될 메타 정보들을 관리하는 열거형
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum ApiEnum {

    /**
     * 성공 응답 코드
     */
    OK (HttpStatus.OK, 100000, "요청이 정상적으로 처리되었습니다."),
    CREATED (HttpStatus.CREATED, 101000, "리소스가 성공적으로 생성되었습니다."),

    /**
     * 에러 응답 코드
     */
    INVALID_REQUEST_PARAMETER (HttpStatus.BAD_REQUEST, -800100, "요청 파라미터가 유효하지 않습니다."),
    UNSUPPORTED_HTTP_METHOD (HttpStatus.METHOD_NOT_ALLOWED, -805100, "지원하지 않는 HTTP 메소드 요청입니다."),
    INTERNAL_SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR, -900000, "내부 서버에 오류가 발생했습니다."),

    /**
     * 사용자 에러 응답 코드
     */
    AUTHENTICATION_FAILED (HttpStatus.UNAUTHORIZED, -801310, "사용자 인증에 실패했습니다."),
    AUTHENTICATION_REQUIRED (HttpStatus.UNAUTHORIZED, -801311, "사용자 인증이 필요합니다."),
    ACCESS_DENIED (HttpStatus.FORBIDDEN, -803310, "요청 자원에 대한 접근 권한이 없습니다."),
    USER_NOT_FOUND (HttpStatus.NOT_FOUND, -804210, "요청하신 사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL (HttpStatus.CONFLICT, -809111, "현재 사용 중인 이메일입니다."),
    DUPLICATED_NICKNAME (HttpStatus.CONFLICT, -809113, "현재 사용 중인 닉네임입니다.")
    ;

    private final HttpStatus status;

    private final int code;

    private final String message;

    /**
     * HTTP 상태 코드에 따른 성공 응답 코드의 열거형 상수를 반환한다.
     *
     * @param status    HTTP 상태 코드
     * @return          HTTP 상태 코드가 {@code CREATED}인 경우 {@link ApiEnum#CREATED}, 그외의 경우 {@link ApiEnum#OK} 반환
     */
    public static ApiEnum from(HttpStatus status) {
        return (status == HttpStatus.CREATED) ? ApiEnum.CREATED : ApiEnum.OK;
    }
}