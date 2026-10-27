package me.subin.delfeno.global.infra.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * {@link CodeEnum} 인터페이스의 구현체인 열거형과 데이터베이스 코드값 간의 SQL 작업 전후 처리를 위한 상호 변환 타입 핸들러
 *
 * @param <E> 타입 핸들러의 처리 대상인 {@link CodeEnum}의 구현체인 열거형 타입
 *
 * @author 박 수 빈
 * @version 1.0
 */
@MappedTypes(CodeEnum.class)
public class CodeEnumTypeHandler<E extends Enum<E> & CodeEnum> extends BaseTypeHandler<E> {

    private final Class<E> type;

    private final E[] enums;

    public CodeEnumTypeHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null.");
        }
        this.type = type;
        this.enums = type.getEnumConstants();
        if (!type.isInterface() && this.enums == null) {
            throw new IllegalArgumentException(type.getSimpleName() + " does not represent an enum type.");
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return rs.wasNull() ? null : lookup(code);
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return rs.wasNull() ? null : lookup(code);
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return cs.wasNull() ? null : lookup(code);
    }

    private E lookup(String code) {
        for (E e : this.enums) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException(
                "Unknown code '" + code + "' for this '" + type.getSimpleName() + "'.");
    }
}