package me.subin.delfeno.global.infra.audit;

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
 * {@code INSERT} 또는 {@code UPDATE} 쿼리 처리시, 생성 일시와 수정 일시를 자동 주입하는 마이바티스 인터셉터
 *
 * @author 박 수 빈
 * @version 1.0
 * @see me.subin.delfeno.global.common.model.BaseTime
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BaseTimeAuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 메소드 호출시 전달된 인자들을 가져온다.
        Object[] args = invocation.getArgs();
        // 실행될 매퍼 태그 및 쿼리문 정보를 가져온다.
        MappedStatement mappedStatement = (MappedStatement) args[0];
        // 쿼리문에 전달될 실제 파라미터 정보를 가져온다.
        Object parameter = args[1];

        /* @Param 어노테이션을 이용하거나 다중 파라미터 형태로 전달되었다면 */
        if (parameter instanceof MapperMethod.ParamMap<?> map) {
            // 서로 다른 키값으로 동일한 값이 중복 바인딩될 경우를 방지하기 위한 Set<>
            Set<Object> audited = new HashSet<>();
            for (Object value : map.values()) {
                if (value != null && audited.add(value)) {
                    resolveArguments(value, mappedStatement);
                }
            }
        } else {
            resolveArguments(parameter, mappedStatement);
        }
        return invocation.proceed();
    }

    private void resolveArguments(Object parameter, MappedStatement statement) {
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        if (parameter instanceof Collection<?> collection) {
            for (Object value : collection) {
                setAuditTime(value, sqlCommandType);
            }
        } else {
            setAuditTime(parameter, sqlCommandType);
        }
    }

    private void setAuditTime(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof BaseTime baseTime) {
            LocalDateTime now = LocalDateTime.now();
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                baseTime.beforeInsert(now);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                baseTime.beforeUpdate(now);
            }
        }
    }
}