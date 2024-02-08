package pc.gear.repository.custom.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pc.gear.config.ColumnMapper;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.repository.custom.ProductCustomRepository;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;
import pc.gear.service.BaseService;
import pc.gear.util.lang.CollectionUtil;
import pc.gear.util.lang.DateUtil;
import pc.gear.util.lang.JdbcService;
import pc.gear.util.lang.JwtUtil;
import pc.gear.util.lang.StringUtil;
import pc.gear.util.type.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCustomRepositoryImpl extends BaseService implements ProductCustomRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JdbcService jdbcService;

    @Autowired
    private HikariDataSource hikariDataSource;

    @Value("${app.sql.batch-size}")
    private int batchSize;

    @Override
    public GetProductByCodeResponse getProductByCode(String productCode) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        StringBuilder sql = getSelectProduct();
        sql.append("""
                    where p.product_code = :productCode
                """);
        param.addValue("productCode", productCode);
        // If not admin
        addConditionDeletedItem(sql);
        return CollectionUtil.getFirstFormCollection(namedParameterJdbcTemplate.query(sql.toString(), param, ColumnMapper.newInstance(GetProductByCodeResponse.class)));
    }

    @Override
    public ProductSearchResponse search(ProductSearchRequest request) {
        MapSqlParameterSource param = new MapSqlParameterSource();
        StringBuilder sql = getSelectProduct();
        StringBuilder sqlCount = getCountProduct();
        addCondition(request, sql, param);
        addCondition(request, sqlCount, param);
        addConditionDeletedItem(sql);
        addConditionDeletedItem(sqlCount);
        addPagination(sql, request);
        String query = sql.toString();
        return ProductSearchResponse.builder()
                .totalRow(namedParameterJdbcTemplate.queryForObject(sqlCount.toString(), param, Integer.class))
                .contents(namedParameterJdbcTemplate.query(query, param, ColumnMapper.newInstance(ProductSearchResponse.ProductSearchItem.class))).build();
    }

    private void addConditionDeletedItem(StringBuilder sql) {
        // If not admin
        if (!Role.ADMIN.equals(JwtUtil.getCurrentUserRole())) {
            // Add delete flag
            sql.append("""
                    and c.delete_fg != 1 and p.delete_fg != 1
                    """);
        }
    }

    private void addCondition(ProductSearchRequest request, StringBuilder sql, MapSqlParameterSource param) {
        sql.append(" where 1 = 1 ");
        if (StringUtils.isNotEmpty(request.getTextSearch())) {
            sql.append(" and (p.title like :textSearch or p.title like :textSearch) ");
            param.addValue("textSearch", StringUtil.addQueryContains(request.getTextSearch()));
        }

        if (StringUtils.isNotEmpty(request.getCategoryCode())) {
            sql.append(" and c.category_cd = :categoryCode ");
            param.addValue("categoryCode", request.getCategoryCode());
        }
        if (Boolean.TRUE.equals(request.getIsDiscount())) {
            sql.append("""
                      and (p.discount > 0)
                      and (p.discount_from is null or p.discount_from <= CURRENT_TIMESTAMP)
                      and (p.discount_to is null or p.discount_to >= CURRENT_TIMESTAMP)
                    """);
        }
        if (request.getPriceFrom() != null) {
            sql.append(" and p.price >= :priceFrom ");
            param.addValue("priceFrom", request.getPriceFrom());
        }
        if (request.getPriceTo() != null) {
            sql.append(" and p.price <= :priceTo ");
            param.addValue("priceTo", request.getPriceTo());
        }
    }

    private StringBuilder getSelectProduct() {
        return new StringBuilder("""
                select p.product_code,
                       p.title,
                       p.description,
                       p.price,
                       p.stock,
                       p.discount,
                       p.discount_from,
                       p.discount_to,
                       p.image,
                       p.delete_fg,
                       p.updated_datetime,
                       c.category_cd,
                       c.name as category_name
                from product p
                         left join category c
                                    on p.category_id = c.category_id
                """);
    }

    private StringBuilder getCountProduct() {
        return new StringBuilder("""
                select count(1)
                from product p
                    left join category c
                                        on p.category_id = c.category_id
                """);
    }

    @Override
    public void jdbcBatchInsert(List<ImportProductDto> products) {
        LocalDateTime now = DateUtil.currentDate();
        String sql = """
                insert into product(product_code, title, description, price, stock, discount, discount_from, discount_to,
                                    delete_fg, created_by, created_datetime, category_id) value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        ParameterizedPreparedStatementSetter<ImportProductDto> pss = (ps, product) -> {
            setPreparedStatement(ps, product, now);
        };
        jdbcService.transactionForJdbcTemplate(sql, products, batchSize, pss);

    }

    private static void setPreparedStatement(PreparedStatement ps, ImportProductDto product, LocalDateTime now) throws SQLException {
        String productCode = StringUtil.generateCode(product.getTitle());
        LocalDateTime discountFrom = DateUtil.parseToLocalDatetime(product.getDiscountFrom(), DateUtil.DATE_TIME_IMPORT_PATTERN);
        LocalDateTime discountTo = DateUtil.parseToLocalDatetime(product.getDiscountTo(), DateUtil.DATE_TIME_IMPORT_PATTERN);
        ps.setString(1, productCode);
        ps.setString(2, product.getTitle());
        ps.setString(3, product.getDescription());
        ps.setBigDecimal(4, product.getPrice());
        ps.setBigDecimal(5, product.getStock());
        ps.setBigDecimal(6, product.getDiscount());
        ps.setTimestamp(7, DateUtil.localDatetimeToTimstamp(discountFrom));
        ps.setTimestamp(8, DateUtil.localDatetimeToTimstamp(discountTo));
        ps.setBoolean(9, Boolean.FALSE);
        ps.setObject(10, JwtUtil.getCurrentUserId());
        ps.setTimestamp(11, DateUtil.localDatetimeToTimstamp(now));
        ps.setObject(12, product.getCategoryEntity().getCategoryId());
    }


    @Override
    public void namedParameterJdbcBatchInsert(List<ImportProductDto> products) {
        LocalDateTime now = DateUtil.currentDate();
        String sql = """
                insert into product(product_code, title, description, price, stock, discount, discount_from, discount_to,
                                    delete_fg, created_by, created_datetime, category_id) value
                                    (:product_code, :title, :description, :price, :stock, :discount, :discount_from, :discount_to,
                                     :delete_fg, :created_by, :created_datetime, :category_id)
                """;
        CollectionUtil.splitList(products, batchSize)
                .forEach(productChilds -> {
                    List<MapSqlParameterSource> params = new ArrayList<>();
                    productChilds.forEach(product -> {
                        String productCode = StringUtil.generateCode(product.getTitle());
                        LocalDateTime discountFrom = DateUtil.parseToLocalDatetime(product.getDiscountFrom(), DateUtil.DATE_TIME_IMPORT_PATTERN);
                        LocalDateTime discountTo = DateUtil.parseToLocalDatetime(product.getDiscountTo(), DateUtil.DATE_TIME_IMPORT_PATTERN);
                        MapSqlParameterSource param = new MapSqlParameterSource();
                        param.addValue("product_code", productCode);
                        param.addValue("title", product.getTitle());
                        param.addValue("description", product.getDescription());
                        param.addValue("price", product.getPrice());
                        param.addValue("stock", product.getStock());
                        param.addValue("discount", product.getDiscount());
                        param.addValue("discount_from", DateUtil.localDatetimeToTimstamp(discountFrom));
                        param.addValue("discount_to", DateUtil.localDatetimeToTimstamp(discountTo));
                        param.addValue("delete_fg", Boolean.FALSE);
                        param.addValue("created_by", JwtUtil.getCurrentUserId());
                        param.addValue("created_datetime", DateUtil.localDatetimeToTimstamp(now));
                        param.addValue("category_id", product.getCategoryEntity().getCategoryId());
                        params.add(param);
                    });
                    jdbcService.transactionForNamedParameterJdbcTemplate(sql, params);
                });
    }

    @Override
    public void batchInsertUsingConnection(List<ImportProductDto> products) throws SQLException {
        LocalDateTime now = DateUtil.currentDate();
        String sql = """
                insert into product(product_code, title, description, price, stock, discount, discount_from, discount_to,
                                    delete_fg, created_by, created_datetime, category_id) value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection connection = null;
        PreparedStatement ps = null;
        int counter = 0;
        try {
            connection = hikariDataSource.getConnection();
            connection.setAutoCommit(false);
            ps = connection.prepareStatement(sql);
            for(var product : products){
                ps.clearParameters();
                setPreparedStatement(ps, product, now);
                ps.addBatch();
                counter++;
                if (counter % batchSize == 0 || counter == products.size()) {
                    ps.executeBatch();
                    ps.clearBatch();
                }
                connection.commit();
            }
        } catch (SQLException e) {
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
}
