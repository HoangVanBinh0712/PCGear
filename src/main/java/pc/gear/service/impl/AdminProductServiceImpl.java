package pc.gear.service.impl;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pc.gear.config.exception.ViolationListException;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.entity.Product;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.ImportProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.AdminProductService;
import pc.gear.service.BaseService;
import pc.gear.service.common.ExcelService;
import pc.gear.util.lang.ExcelUtil;
import pc.gear.util.response.ApiError;
import pc.gear.validator.ProductValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class AdminProductServiceImpl implements AdminProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductValidator productValidator;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private BaseService baseService;

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

    @Override
    public void importProduct(ImportProductRequest request) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(request.getFile().getInputStream())) {
            Map<String, Sheet> sheetMap = ExcelUtil.workbookToMap(workbook);
            List<ImportProductDto> products = new ArrayList<>();
            List<ApiError> errors = new ArrayList<>();
            for (int rowIndex = 8; rowIndex < 16; rowIndex++) {
                ImportProductDto product = new ImportProductDto();
                excelService.readObject(sheetMap, product, rowIndex, errors);
                products.add(product);
            }
            if (!errors.isEmpty()) {
                throw new ViolationListException(errors);
            }
            products.forEach(System.out::println);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
