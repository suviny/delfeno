package me.subin.delfeno.global.common.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 생성과 변경 및 삭제 등의 작업 처리 일시 정보를 관리하는 추상 클래스
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Getter
public abstract class BaseDateTime {

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * {@code INSERT} 실행 전, 생성 일시와 변경 일시를 초기화한다.
     * 
     * @param dateTime  데이터 생성 시점의 일시 정보
     */
    public void beforeInsert(LocalDateTime dateTime) {
        this.createdAt = dateTime;
        this.updatedAt = dateTime;
    }

    /**
     * {@code UPDATE} 실행 전, 변경 일시를 현재 시점의 일시 정보로 최신화한다.
     * 
     * @param dateTime  데이터 변경 시점의 일시 정보
     */
    public void beforeUpdate(LocalDateTime dateTime) {
        this.updatedAt = dateTime;
    }
}