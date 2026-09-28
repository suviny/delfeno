package me.subin.delfeno.global.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
public class JacksonConfig {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder
                .modules(new JavaTimeModule())
                /* 객체 및 데이터의 직렬화 설정 */
                .serializers(
                    new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                /* 클라이언트로부터 전달된 JSON 데이터 역직렬화 설정 */
                .deserializers(
                    new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_FORMAT)),
                    new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
                /* 날짜 데이터의 타임 스탬프 변환 설정 비활성화 */
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}