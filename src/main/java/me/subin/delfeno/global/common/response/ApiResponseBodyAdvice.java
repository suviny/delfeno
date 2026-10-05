package me.subin.delfeno.global.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 컨트롤러에서 {@link ResponseEntity}를 사용해 반환되는 응답 데이터를 가로채 {@link ApiResponse}로 래핑을 수행하는 응답 전처리 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@RestControllerAdvice(basePackages = "me.subin.delfeno.domain")
@RequiredArgsConstructor
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.hasMethodAnnotation(ExceptionHandler.class)) {
            return false;
        }
        return ResponseEntity.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        HttpStatus status = resolveStatus(response);
        ApiEnum apiEnum = ApiEnum.from(status);
        ApiResponse<?> payload = ApiResponse.success(apiEnum, body);
        if (body instanceof String) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(payload);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize response body to JSON.", e);
            }
        }
        return payload;
    }

    private HttpStatus resolveStatus(ServerHttpResponse response) {
        if (response instanceof ServletServerHttpResponse serverHttpResponse) {
            int status = serverHttpResponse.getServletResponse().getStatus();
            HttpStatus resolved = HttpStatus.resolve(status);
            if (resolved != null) {
                return resolved;
            }
        }
        return HttpStatus.OK;
    }
}