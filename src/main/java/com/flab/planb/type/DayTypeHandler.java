package com.flab.planb.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(Day.class)
@MappedJdbcTypes(JdbcType.INTEGER)
public class DayTypeHandler implements TypeHandler<Day> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Day day, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, day.getCode());
    }

    @Override
    public Day getResult(ResultSet rs, String columnName) throws SQLException {
        return Day.getDay(rs.getInt(columnName));
    }

    @Override
    public Day getResult(ResultSet rs, int columnIndex) throws SQLException {
        return Day.getDay(rs.getInt(columnIndex));
    }

    @Override
    public Day getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Day.getDay(cs.getInt(columnIndex));
    }
}
