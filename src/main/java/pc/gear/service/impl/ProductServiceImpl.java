package pc.gear.service.impl;

import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pc.gear.config.ColumnMapper;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;
import pc.gear.service.BaseService;
import pc.gear.service.ProductService;
import pc.gear.util.MessageConstants;
import pc.gear.validator.ProductValidator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public GetProductByCodeResponse getByProductCode(String productCode) {
        // throw error if blank code
        if (StringUtils.isBlank(productCode)) {
            this.throwError(MessageConstants.NOT_BLANK, this.getMessage(MessageConstants.PRODUCT_CODE));
        }
        GetProductByCodeResponse response = productRepository.getProductByCode(productCode);
        if (response == null) {
            // throw error 404
            this.throwErrorNotFound(MessageConstants.DATA_NOT_FOUND, this.getMessage(MessageConstants.PRODUCT));
        }
        return response;
    }

    @Override
    public ProductSearchResponse search(ProductSearchRequest request) {
        this.validateSearchRequest(request, "productApi.ProductSearchRequest.sortFields.value",
                "productApi.ProductSearchRequest.sortFields.message");
        return productRepository.search(request);
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object testJdbcTemplate(ProductSearchRequest request) {
            StringBuilder sql = new StringBuilder("SELECT * FROM product WHERE 1 = 1 ");
        List<Object> params = new ArrayList<>();

        if (request.getPriceFrom() != null) {
            sql.append(" AND price >= ?");
            params.add(request.getPriceFrom());
        }
        if (request.getPriceTo() != null) {
            sql.append(" AND price <= ?");
            params.add(request.getPriceTo());
        }
        Object[] paramsArray = params.toArray();

        return jdbcTemplate.query(sql.toString(), ps -> setPreparedStatement(ps, paramsArray), ColumnMapper.newInstance(ProductSearchResponse.ProductSearchItem.class));
    }

    private void setPreparedStatement(PreparedStatement ps, Object[] paramsArray) throws SQLException {
        for (int i = 0; i < paramsArray.length; i++) {
            ps.setObject(i + 1, paramsArray[i]);
        }
    }
}
