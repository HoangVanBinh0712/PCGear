package pc.gear.repository.custom;

import pc.gear.dto.excel.ImportProductDto;
import pc.gear.request.product.ProductSearchRequest;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.response.product.ProductSearchResponse;

import java.sql.SQLException;
import java.util.List;

public interface ProductCustomRepository {
    GetProductByCodeResponse getProductByCode(String productCode);

    ProductSearchResponse search(ProductSearchRequest request);

    void jdbcBatchInsert(List<ImportProductDto> products);

    void namedParameterJdbcBatchInsert(List<ImportProductDto> products);

    void batchInsertUsingConnection(List<ImportProductDto> products) throws SQLException;

}
