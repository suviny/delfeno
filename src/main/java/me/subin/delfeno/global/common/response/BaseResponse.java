package me.subin.delfeno.global.common.response;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 성공 및 에러 응답 클래스가 공통적으로 가져야 할 기본 속성들을 정의한 상위 추상 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
public abstract class BaseResponse {

    private final LocalDateTime timestamp;

    private final int code;

    private final String message;

    protected BaseResponse(int code, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = code;
        this.message = message;
    }
}