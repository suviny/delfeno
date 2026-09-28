package me.subin.delfeno.global.infra.audit;

import me.subin.delfeno.global.common.model.BaseDateTime;
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
 * {@code INSERT} 또는 {@code UPDATE} 쿼리 처리시, 생성 일시와 변경 일시 정보를 자동 주입하는 마이바티스 인터셉터
 *
 * @author 박 수 빈
 * @version 1.0
 */
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BaseDateTimeAuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs(); // 메소드 호출 시 전달된 인자들을 가져온다.
        MappedStatement statement = (MappedStatement) args[0]; // 실행될 매퍼 태그 및 쿼리문 정보를 가져온다.
        Object parameter = args[1]; // 쿼리문에 전달될 실제 파라미터 정보를 가져온다.

        /* @Param 어노테이션을 이용하거나 다중 파라미터 형태로 전달되었는지 확인한다. */
        if (parameter instanceof MapperMethod.ParamMap<?> map) {
            // 서로 다른 키값으로 동일한 객체가 중복 바인딩 될 경우를 방지하기 위한 Set
            Set<Object> audited = new HashSet<>();
            for (Object value : map.values()) {
                if (value != null && audited.add(value)) {
                    dispatchParameter(value, statement);
                }
            }
        } else {
            dispatchParameter(parameter, statement);
        }

        return invocation.proceed();
    }

    private void dispatchParameter(Object parameter, MappedStatement statement) {
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        if (parameter instanceof Collection<?> collection) {
            for (Object object : collection) {
                setAuditDateTime(object, sqlCommandType);
            }
        } else {
            setAuditDateTime(parameter, sqlCommandType);
        }
    }

    private void setAuditDateTime(Object parameter, SqlCommandType sqlCommandType) {
        if (parameter instanceof BaseDateTime baseDateTime) {
            LocalDateTime now = LocalDateTime.now();
            if (SqlCommandType.INSERT.equals(sqlCommandType)) {
                baseDateTime.beforeInsert(now);
            } else if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
                baseDateTime.beforeUpdate(now);
            }
        }
    }
}