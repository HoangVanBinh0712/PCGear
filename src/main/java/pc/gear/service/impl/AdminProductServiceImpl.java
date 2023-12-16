package pc.gear.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.entity.Product;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.AdminProductService;
import pc.gear.service.BaseService;
import pc.gear.validator.ProductValidator;

@Service
public class AdminProductServiceImpl extends BaseService implements AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public void create(CreateProductRequest request) {
        productValidator.validateProductCreate(request);
        Product product = modelMapper.map(request, Product.class);
        product.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        productRepository.save(product);
    }

    @Override
    public void update(UpdateProductRequest request) {
        Product p = productValidator.validateProductUpdate(request);
        // Map data
        modelMapper.map(request, p);
        // Set category
        p.setCategory(categoryRepository.getReferenceById(request.getCategoryId()));
        productRepository.save(p);
    }

    @Override
    public void delete(String projectCode) {
        Product p = productRepository.findByProductCode(projectCode);
        if (p == null) return;
        if (Boolean.TRUE.equals(p.getDeleteFlag())) {
            productRepository.delete(p);
        } else {
            p.setDeleteFlag(Boolean.TRUE);
            productRepository.save(p);
        }
    }
}
