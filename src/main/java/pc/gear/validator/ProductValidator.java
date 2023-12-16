package pc.gear.validator;

import pc.gear.entity.Product;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;

public interface ProductValidator {
    void validateProductCreate(CreateProductRequest request);

    Product validateProductUpdate(UpdateProductRequest request);
}
