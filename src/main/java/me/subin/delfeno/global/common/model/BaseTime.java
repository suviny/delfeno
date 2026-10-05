package me.subin.delfeno.global.common.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 생성 일시와 수정 일시 필드를 공통적으로 관리하는 추상 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
public abstract class BaseTime {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * {@code INSERT} 실행 전 생성 일시와 수정 일시를 초기화한다.
     *
     * @param dateTime  데이터 생성 시점의 일시 정보
     */
    public void beforeInsert(LocalDateTime dateTime) {
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }

    /**
     * {@code UPDATE} 실행 전 수정 일시를 현재 시점의 일시 정보로 갱신한다.
     *
     * @param dateTime  데이터 변경 시점의 일시 정보
     */
    public void beforeUpdate(LocalDateTime dateTime) {
        this.updatedAt = dateTime;
    }
}