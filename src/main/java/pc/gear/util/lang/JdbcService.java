package pc.gear.util.lang;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import pc.gear.dto.CreateStatementDto;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.util.Constants;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class JdbcService {


    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HikariDataSource hikariDataSource;

    @Value("${app.sql.batch-size}")
    private int batchSize;

    public void transactionForNamedParameterJdbcTemplate(String sql, List<MapSqlParameterSource> params) {
        transactionTemplate.execute(status -> {
            try {
                namedParameterJdbcTemplate.batchUpdate(sql, params.toArray(new MapSqlParameterSource[0]));
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }

    public <T> void  transactionForJdbcTemplate(String sql, List<T> list, int batchSize, ParameterizedPreparedStatementSetter<T> pss) {
        transactionTemplate.execute(status -> {
            try {
                jdbcTemplate.batchUpdate(sql, list, batchSize, pss);
            } catch (Exception e) {
                status.setRollbackOnly();
                throw e;
            }
            return null;
        });
    }

    public void batchInsertCustomized(List<?>  products) throws SQLException, IllegalAccessException {
        if (CollectionUtils.isEmpty(products)) {
            return;
        }
        String sql = createStatementInsert(products.get(0).getClass());
        Connection connection = null;
        PreparedStatement ps = null;
        int counter = 0;
        try {
            connection = hikariDataSource.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            for (var product : products) {
                ps.clearParameters();
                List<Object> params = getParams(product);
                if (params.isEmpty()) {
                    log.info("Params is empty !");
                    continue;
                }
                int parameterIndex = 1;
                for (Object value : params) {
                    if (value != null) {
                        if (value instanceof String strVal) {
                            ps.setString(parameterIndex, strVal);
                        } else if (value instanceof Integer intVal) {
                            ps.setInt(parameterIndex, intVal);
                        } else if (value instanceof Long longVal) {
                            ps.setLong(parameterIndex, longVal);
                        } else if (value instanceof Double doubleVal) {
                            ps.setDouble(parameterIndex, doubleVal);
                        } else if (value instanceof Float floatVal) {
                            ps.setFloat(parameterIndex, floatVal);
                        } else if (value instanceof LocalDateTime localDateTimeVal) {
                            ps.setTimestamp(parameterIndex, DateUtil.localDatetimeToTimstamp(localDateTimeVal));
                        } else if (value instanceof LocalDate localDateVal) {
                            ps.setTimestamp(parameterIndex, DateUtil.localDateToTimstamp(localDateVal));
                        }else {
                            ps.setObject(parameterIndex, value);
                        }
                    } else {
                        ps.setObject(parameterIndex, null);
                    }
                    parameterIndex++;
                }
                ps.addBatch();

                counter++;
                if (counter % batchSize == 0 || counter == products.size()) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                connection.commit();
            }
        } catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }
    }

    public String createStatementInsert(Class<?> clazz) {
        List<String> columnNames = new ArrayList<>();
        List<String> valuePlaceholders = new ArrayList<>();
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Entity must be annotated with @Table.");
        }
        String tableName = clazz.getAnnotation(Table.class).name();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Column.class)) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String columnName = columnAnnotation.name();
                columnNames.add(columnName);
                valuePlaceholders.add("?");
            }
        }

        String columns = String.join(Constants.COMMA, columnNames);
        String values = String.join(Constants.COMMA, valuePlaceholders);

        return String.format("INSERT INTO %s(%s) VALUES (%s)", tableName, columns, values);
    }

    public List<Object> getParams(Object obj) throws IllegalAccessException {
        List<Object> params = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);
            params.add(value);
        }
        return params;
    }

}
