package pc.gear.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import pc.gear.config.ColumnMapper;
import pc.gear.repository.custom.ProductCustomRepository;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.util.CollectionUtil;
import pc.gear.util.JwtUtil;
import pc.gear.util.type.Role;

@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public GetProductByCodeResponse getProductByCode(String productCode) {
        StringBuilder sql = new StringBuilder("""
                select product.product_code,
                       product.title,
                       product.description,
                       product.price,
                       product.stock,
                       product.discount,
                       product.discount_from,
                       product.discount_to,
                       product.image,
                       product.delete_fg,
                       product.updated_datetime,
                       category.category_cd,
                       category.name as category_name
                from product
                         inner join category
                                    on product.category_id = category.category_id and product.product_code = :productCode
                """);
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValue("productCode", productCode);
        // If not admin
        if (!Role.ADMIN.equals(JwtUtil.getCurrentUserRole())) {
            // Add delete flag
            sql.append("""
                    and category.delete_fg != 1 and product.delete_fg != 1
                    """);
        }
        return CollectionUtil.getFirstFormCollection(
                jdbcTemplate.query(sql.toString(), param, ColumnMapper.newInstance(GetProductByCodeResponse.class)));
    }
}
