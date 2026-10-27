package me.subin.delfeno.global.infra.interceptor;

import me.subin.delfeno.global.common.model.BaseTime;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * {@code INSERT} 또는 {@code UPDATE} 처리 시, 생성 일시와 변경 일시를 자동 주입하는 마이바티스 인터셉터
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BaseTimeAuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 메소드 호출시 전달된 인자들을 가져온다.
        Object[] args = invocation.getArgs();
        // 실행될 매퍼 태그 및 쿼리문에 대한 정보를 가져온다.
        MappedStatement ms = (MappedStatement) args[0];
        // 쿼리문에 전달될 실제 파라미터 정보들을 가져온다.
        Object parameter = args[1];
        
        /* @Param 어노테이션을 통해 파라미터가 전달되는 경우 */
        if (parameter instanceof MapperMethod.ParamMap<?> paramMap) {
            Set<Object> audited = new HashSet<>();
            for (Object value : paramMap.values()) {
                if (value != null && audited.add(value)) {
                    resolveArguments(value, ms);
                }
            }
        } else {
            resolveArguments(parameter, ms);
        }
        
        return invocation.proceed();
    }

    private void resolveArguments(Object parameter, MappedStatement ms) {
        SqlCommandType commandType = ms.getSqlCommandType();
        /* 여러 파라미터 객체가 리스트 또는 컬렉션으로 전달되는 경우 */
        if (parameter instanceof Collection<?> collection) {
            for (Object value : collection) {
                setAuditingTime(value, commandType);
            }
        } else {
            setAuditingTime(parameter, commandType);
        }
    }

    private void setAuditingTime(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof BaseTime baseTime) {
            LocalDateTime now = LocalDateTime.now();
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                baseTime.preInsert(now);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                baseTime.preUpdate(now);
            }
        }
    }
}