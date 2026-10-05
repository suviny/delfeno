package me.subin.delfeno.global.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 프로젝트 전역에서 사용될 도메인에 대한 상태값 처리를 위해 서로 다른 타입의 값들을 상응되는 의미별로 정의 및 관리하는 열거형
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public enum StatusValue {

    YES (1, "Y", true),
    NO (0, "N", false)
    ;

    private final int number;

    private final String string;

    private final boolean bool;

    /**
     * 전달된 정수와 일치하는 값을 지닌 상수의 {@code boolean} 타입의 상태값을 반환한다.
     *
     * @param source    논리형 상태값으로 변환하고자 하는 정수
     * @return          전달된 값이 {@code 1}일 경우 {@code true}, {@code 0}일 경우 {@code false} 반환
     */
    public static boolean booleanOf(int source) {
        for (StatusValue e : values()) {
            if (e.getNumber() == source) {
                return e.isBool();
            }
        }
        throw new IllegalArgumentException(
                "No constant found for " + source + ".");
    }

    /**
     * 전달된 문자열과 일치하는 값을 지닌 상수의 {@code boolean} 타입 상태값을 반환한다.
     *
     * @param source    논리형 상태값으로 변환하고자 하는 문자열
     * @return          전달된 값이 {@code "Y"}일 경우 {@code true}, {@code "N"}일 경우 {@code false} 반환
     */
    public static boolean booleanOf(String source) {
        for (StatusValue e : values()) {
            if (e.getString().equals(source)) {
                return e.isBool();
            }
        }
        throw new IllegalArgumentException(
                "No constant found for " + source + ".");
    }
}