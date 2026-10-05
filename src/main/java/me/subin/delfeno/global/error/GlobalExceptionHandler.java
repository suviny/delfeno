package me.subin.delfeno.global.error;

import me.subin.delfeno.global.common.response.ApiEnum;
import me.subin.delfeno.global.error.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import me.subin.delfeno.global.error.ErrorResponse.FieldErrorDetail;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ApiEnum apiEnum = e.getApiEnum();
        return ResponseEntity
                .status(apiEnum.getStatus())
                .body(ErrorResponse.fail(apiEnum, e.getErrors()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ApiEnum apiEnum = ApiEnum.INVALID_REQUEST_PARAMETER;
        return ResponseEntity
                .status(apiEnum.getStatus())
                .body(ErrorResponse.fail(apiEnum, FieldErrorDetail.of(e.getBindingResult())));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        ApiEnum apiEnum = ApiEnum.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(apiEnum.getStatus())
                .body(ErrorResponse.error(apiEnum));
    }
}