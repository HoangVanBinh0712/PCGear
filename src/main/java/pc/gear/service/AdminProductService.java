package pc.gear.service;

import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.ImportProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;

import java.io.IOException;
import java.sql.SQLException;

public interface AdminProductService {

    void create(CreateProductRequest request);

    void update(UpdateProductRequest request);

    void delete(String projectCode);

    void importProduct(ImportProductRequest request) throws IOException, IllegalAccessException, SQLException;

}
