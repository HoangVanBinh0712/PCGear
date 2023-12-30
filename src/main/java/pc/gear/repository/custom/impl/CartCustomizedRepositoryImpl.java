package pc.gear.repository.custom.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pc.gear.config.ColumnMapper;
import pc.gear.repository.custom.CartCustomizedRepository;
import pc.gear.response.Cart.GetCartResponse;
import pc.gear.util.JwtUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartCustomizedRepositoryImpl implements CartCustomizedRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<GetCartResponse.CartItem> getCartItems() {
        Long userId = JwtUtil.getCurrentUserId();
        StringBuilder sql = new StringBuilder("""
                SELECT c.cart_id,
                       c.quantity,
                       c.updated_datetime as cart_updated_datetime,
                       p.product_code,
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
                       category.category_cd,
                       category.name as category_name
                FROM cart c
                         inner join product p on p.product_id = c.product_id
                         left join category on p.category_id = category.category_id
                WHERE c.customer_id = ?
                """);
        List<Object> params = new ArrayList<>();
        params.add(userId);
        return jdbcTemplate.query(sql.toString(), ps -> setPreparedStatement(ps, params.toArray()),                ColumnMapper.newInstance(GetCartResponse.CartItem.class));
    }

    private void setPreparedStatement(PreparedStatement ps, Object[] paramsArray) throws SQLException {
        for (int i = 0; i < paramsArray.length; i++) {
            ps.setObject(i + 1, paramsArray[i]);
        }
    }
}
