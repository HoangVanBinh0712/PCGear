package pc.gear.repository.custom.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pc.gear.config.ColumnMapper;
import pc.gear.repository.custom.ProductCustomRepository;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;
import pc.gear.service.BaseService;
import pc.gear.util.CollectionUtil;
import pc.gear.util.JwtUtil;
import pc.gear.util.StringUtil;
import pc.gear.util.type.Role;

@Repository
public class ProductCustomRepositoryImpl extends BaseService implements ProductCustomRepository {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

}
