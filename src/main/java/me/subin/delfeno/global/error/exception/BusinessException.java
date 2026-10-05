package me.subin.delfeno.global.error.exception;

import lombok.Getter;
import me.subin.delfeno.global.common.response.ApiEnum;
import me.subin.delfeno.global.error.ErrorResponse.FieldErrorDetail;

import java.util.List;

/**
 * 비즈니스 로직 처리를 위해 발생시키는 예외에 대한 상위 추상 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
public abstract class BusinessException extends RuntimeException {

    private ApiEnum apiEnum;

    private List<FieldErrorDetail> errors;

    public BusinessException(String message, ApiEnum errorCode) {
        super(message);
        this.apiEnum = errorCode;
    }

    public BusinessException(ApiEnum errorCode) {
        this.apiEnum = errorCode;
    }

    public BusinessException(ApiEnum errorCode, List<FieldErrorDetail> errors) {
        this.apiEnum = errorCode;
        this.errors = errors;
    }
}