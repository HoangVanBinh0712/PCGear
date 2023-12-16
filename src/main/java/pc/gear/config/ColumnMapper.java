package pc.gear.config;

import jakarta.persistence.Column;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColumnMapper<T> extends BeanPropertyRowMapper<T> {

    private Map<String, String> columnMap;

    public ColumnMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    @Override
    protected void initialize(Class<T> mappedClass) {
        columnMap = new HashMap<>();
        for (Field field : mappedClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (StringUtils.isNotBlank(column.name())) {
                    columnMap.put(field.getName(), column.name());
                }
            }
        }
        super.initialize(mappedClass);
    }

    @Override
    protected String underscoreName(String name) {
        if (columnMap.containsKey(name)) {
            return columnMap.get(name);
        }
        return super.underscoreName(name);
    }

    @Override
    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        if (Number.class.isAssignableFrom(pd.getPropertyType())) {
            Object value = JdbcUtils.getResultSetValue(rs, index, String.class);
            if (ObjectUtils.isNotEmpty(value)) {
                String valueString = value.toString();
                if (NumberUtils.isCreatable(valueString)) {
                    return valueString;
                }
                return null;
            }
        }
        return super.getColumnValue(rs,index,pd);
    }

    public static <T> ColumnMapper<T> newInstance(Class<T> mappedClass) {
        return new ColumnMapper<>(mappedClass);
    }
}
