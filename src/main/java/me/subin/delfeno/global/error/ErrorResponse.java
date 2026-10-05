package me.subin.delfeno.global.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import me.subin.delfeno.global.common.response.ApiEnum;
import me.subin.delfeno.global.common.response.BaseResponse;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.*;

/**
 * 잘못된 요청 또는 서버의 예외 발생으로 인해 에러가 발생했을 경우 반환될 에러 응답 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
public class ErrorResponse extends BaseResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FieldErrorDetail> errors;

    private ErrorResponse(int code, String message, List<FieldErrorDetail> errors) {
        super(code, message);
        this.errors = errors == null ? Collections.emptyList() : errors;
    }

    private ErrorResponse(int code, String message) {
        super(code, message);
    }

    /**
     * 사용자의 잘못된 요청 파라미터로 인해 발생된 필드 에러에 대한 실패 응답 객체를 생성한다.
     *
     * @param apiEnum   API 응답의 메타 정보를 정의한 열거형 상수
     * @param errors    응답 본문에 담길 에러에 대한 정보를 담은 리스트
     * @return          생성된 에러 응답 객체 반환
     */
    public static ErrorResponse fail(ApiEnum apiEnum, List<FieldErrorDetail> errors) {
        return new ErrorResponse(apiEnum.getCode(), apiEnum.getMessage(), errors);
    }

    /**
     * 발생된 예외에 대한 에러 응답 객체를 생성한다.
     *
     * @param apiEnum   API 응답의 메타 정보를 정의한 열거형 상수
     * @return          생성된 에러 응답 객체 반환
     */
    public static ErrorResponse error(ApiEnum apiEnum) {
        return new ErrorResponse(apiEnum.getCode(), apiEnum.getMessage());
    }


    /**
     * 입력 데이터 검증 실패로 인해 발생된 필드 에러에 대한 정보를 담은 내부 클래스
     */
    @Getter
    public static class FieldErrorDetail {

        private final String field;

        private final String value;

        private final String reason;

        private FieldErrorDetail(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        /**
         * 특정 필드에 의해 에러가 발생했을 경우 해당 에러에 대한 정보를 리스트로 생성한다.
         *
         * @param field     에러가 발생한 필드명
         * @param value     사용자 입력값
         * @param reason    에러가 발생한 원인
         * @return          생성된 에러 정보 리스트 반환
         */
        public static List<FieldErrorDetail> of(String field, String value, String reason) {
            return List.of(new FieldErrorDetail(field, value, reason));
        }

        /**
         * {@code @ModelAttribute} 또는 {@code @RequestBody}으로 전달된 데이터가 {@code @Valid} 또는 {@code @Validated}에 의한 검증에 실패 시
         * {@link org.springframework.validation.BindException} 또는 {@link org.springframework.web.bind.MethodArgumentNotValidException}
         * 발생할 경우 에러 정보를 리스트로 생성한다.
         *
         * @param bindingResult     바인딩 및 데이터 유효성 검증 실패 정보를 담고 있는 객체
         * @return                  생성된 에러 정보 리스트 반환
         */
        public static List<FieldErrorDetail> of(BindingResult bindingResult) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            return errors.stream()
                    .map(error -> new FieldErrorDetail(
                            error.getField(),
                            Objects.toString(error.getRejectedValue(), ""),
                            error.getDefaultMessage())).toList();
        }

        /**
         * 클래스 레벨의 {@code @Validated} 또는 {@code Validator}에 의한 검증에 실패 시
         * {@link jakarta.validation.ConstraintViolationException}이 발생할 경우 제약 조건 위반 정보를 리스트로 생성한다.
         *
         * @param violations    자바 Bean Validation 제약 조건을 위반시 발생되는 에러 정보를 담고 있는 객체
         * @return              생성된 에러 정보 리스트 반환
         */
        public static List<FieldErrorDetail> of(Set<ConstraintViolation<?>> violations) {
            List<ConstraintViolation<?>> errors = new ArrayList<>(violations);
            return errors.stream()
                    .map(error -> new FieldErrorDetail(
                            violatedField(error.getPropertyPath().toString()),
                            Objects.toString(error.getInvalidValue(), ""),
                            error.getMessage())).toList();
        }

        private static String violatedField(String path) {
            return (path == null) ? "" : path.substring(path.lastIndexOf('.') + 1);
        }
    }
}
