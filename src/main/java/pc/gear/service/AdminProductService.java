package pc.gear.service;

import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;

public interface AdminProductService {

    void create(CreateProductRequest request);

    void update(UpdateProductRequest request);

    void delete(String projectCode);
}
