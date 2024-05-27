package pc.gear.service.impl;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import pc.gear.config.exception.ViolationListException;
import pc.gear.dto.excel.ImportProductDto;
import pc.gear.dto.excel.ProductCustomized;
import pc.gear.entity.Category;
import pc.gear.entity.Product;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.request.admin.product.ExportProductRequest;
import pc.gear.request.admin.product.ImportProductRequest;
import pc.gear.request.admin.product.UpdateProductRequest;
import pc.gear.service.AdminProductService;
import pc.gear.service.BaseService;
import pc.gear.service.common.ExcelService;
import pc.gear.util.Constants;
import pc.gear.util.lang.DateUtil;
import pc.gear.util.lang.ExcelUtil;
import pc.gear.util.lang.JdbcService;
import pc.gear.util.lang.JwtUtil;
import pc.gear.util.lang.StringUtil;
import pc.gear.util.response.ApiError;
import pc.gear.validator.ProductValidator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
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

    @Autowired
    private JdbcService jdbcService;

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
    @Transactional
    public void importProduct(ImportProductRequest request) throws IOException, IllegalAccessException, SQLException {
        List<Category> categories = categoryRepository.findByDeleteFlagIsNullOrDeleteFlagEquals(Boolean.FALSE);
        try (Workbook workbook = new XSSFWorkbook(request.getFile().getInputStream())) {
            // Find the range to loop
            Sheet sheetImport = workbook.getSheet(Constants.PRODUCT_SHEET);
            int readTo = 0;
            for (int i = 0; i < Constants.MAX_ROW_READ; i++) {
                String val = ExcelUtil.getDataFromCell(sheetImport, i, ExcelUtil.getColIndex("D"));
                if (Constants.END.equals(val)) {
                    readTo = i - 1;
                }
            }
            Map<String, Sheet> sheetMap = ExcelUtil.workbookToMap(workbook);
            List<ImportProductDto> products = new ArrayList<>();
            List<ApiError> errors = new ArrayList<>();
            for (int rowIndex = 8; rowIndex < readTo; rowIndex++) {
                ImportProductDto product = new ImportProductDto();
                excelService.readObjectImportProduct(sheetMap, product, rowIndex, errors, categories);
                products.add(product);
            }
            if (!errors.isEmpty()) {
                throw new ViolationListException(errors);
            }
            for (int i = 1; i < 10000; i++) {
                ImportProductDto productDto = new ImportProductDto();
                productDto.setTitle("Title " + i);
                productDto.setCategoryEntity(categories.get(0));
                productDto.setPrice(BigDecimal.valueOf(10000));
                productDto.setStock(BigDecimal.valueOf(1000));
                products.add(productDto);
            }
            StopWatch stopWatch = new StopWatch();
//            stopWatch.start();
//            List<Product> productEntityList = products.stream().map(p -> {
//                Product product = new Product();
//                product.setTitle(p.getTitle());
//                product.setDescription(p.getDescription());
//                product.setDiscount(p.getDiscount());
//                product.setPrice(p.getPrice());
//                product.setStock(p.getStock() != null ? p.getStock().intValue() : 0);
//                product.setDiscountFrom(DateUtil.parseToLocalDatetime(p.getDiscountFrom(), DateUtil.DATE_TIME_IMPORT_PATTERN));
//                product.setDiscountTo(DateUtil.parseToLocalDatetime(p.getDiscountTo(), DateUtil.DATE_TIME_IMPORT_PATTERN));
//                product.setCategory(p.getCategoryEntity());
//                return product;
//            }).toList();
//            productRepository.saveAllAndFlush(productEntityList);
//            stopWatch.stop();
//            log.info("Jpa save all: " + stopWatch.lastTaskInfo().getTimeMillis());
//            stopWatch.start();
//            productRepository.jdbcBatchInsert(products);
//            stopWatch.stop();
//            log.info("JDBC save all: " + stopWatch.lastTaskInfo().getTimeMillis());
//            stopWatch.start();
//            productRepository.namedParameterJdbcBatchInsert(products);
//            stopWatch.stop();
//            log.info("Named parameter JDBC save all: " + stopWatch.lastTaskInfo().getTimeMillis());
//            stopWatch.start();
//            productRepository.batchInsertUsingConnection(products);
//            stopWatch.stop();
//            log.info("Connection save all: " + stopWatch.lastTaskInfo().getTimeMillis());
            stopWatch.start();
            List<ProductCustomized> productCustomizedList = products.stream().map(p -> {
                ProductCustomized product = new ProductCustomized();
                product.setTitle(p.getTitle());
                product.setProductCode(StringUtil.generateCode(p.getTitle()));
                product.setDescription(p.getDescription());
                product.setDiscount(p.getDiscount());
                product.setPrice(p.getPrice());
                product.setStock(p.getStock() != null ? p.getStock().intValue() : 0);
                product.setDiscountFrom(DateUtil.parseToLocalDatetime(p.getDiscountFrom(), DateUtil.DATE_TIME_IMPORT_PATTERN));
                product.setDiscountTo(DateUtil.parseToLocalDatetime(p.getDiscountTo(), DateUtil.DATE_TIME_IMPORT_PATTERN));
                product.setCategory_id(p.getCategoryEntity().getCategoryId());
                product.setDeleteFlag(Boolean.FALSE);
                product.setCreatedBy(JwtUtil.getCurrentUsername());
                product.setCreatedDateTime(DateUtil.now());
                product.setUpdatedBy(JwtUtil.getCurrentUsername());
                product.setUpdatedDateTime(DateUtil.now());
                return product;
            }).toList();
            jdbcService.batchInsertCustomized(productCustomizedList);
            stopWatch.stop();
            log.info("Customized JDBC save all: " + stopWatch.lastTaskInfo().getTimeMillis());
        }
    }

    @Override
    public void exportProduct(ExportProductRequest request) {
        String filePath = "C:\\Users\\ASUS\\OneDrive\\Máy tính\\pcgear\\Product_Export_Template.xlsx";
        String filePathOutPut = "C:\\Users\\ASUS\\OneDrive\\Máy tính\\pcgear\\Product_Export_Template_1.xlsx";
        int sourceRowIndex = 8;
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(fis)) {
            Sheet sheet = workbook.getSheet(Constants.PRODUCT_SHEET);
            if (sheet != null) {
                ExcelUtil.writeDataFromListObject(sheet, sourceRowIndex, request.getProducts());
                // Write the changes back to the workbook
                try (FileOutputStream fos = new FileOutputStream(filePathOutPut)) {
                    workbook.write(fos);
                }
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
