package pc.gear.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pc.gear.config.exception.PcGearException;
import pc.gear.entity.Category;
import pc.gear.repository.CategoryRepository;
import pc.gear.repository.ProductRepository;
import pc.gear.request.admin.product.CreateProductRequest;
import pc.gear.service.common.ExcelService;
import pc.gear.service.impl.AdminProductServiceImpl;
import pc.gear.util.MessageConstants;
import pc.gear.validator.ProductValidator;
import pc.gear.validator.impl.ProductValidatorImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Locale;

@ExtendWith(SpringExtension.class)
public class AdminProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductValidator productValidator;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExcelService excelService;

    private BaseService baseService;

    private AdminProductServiceImpl adminProductServic;

    private MessageSource messageSource;

    public MessageSource messageSource() {

        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages/vi/messages",
                "classpath:messages/vi/system",
                "classpath:messages/en/messages",
                "classpath:messages/en/system");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.US);
        return messageSource;
    }

    @BeforeEach
    void setup() {
        baseService = new BaseService();
        this.messageSource = messageSource();
        ModelMapper modelMapper = new ModelMapper();
        productValidator = Mockito.spy(new ProductValidatorImpl(productRepository, categoryRepository));
        ReflectionTestUtils.setField(productValidator, "messageSource", messageSource);
        ReflectionTestUtils.setField(baseService, "messageSource", messageSource);

        adminProductServic = AdminProductServiceImpl.builder()
                .productRepository(productRepository)
                .modelMapper(modelMapper)
                .productValidator(productValidator)
                .categoryRepository(categoryRepository)
                .excelService(excelService)
                .baseService(baseService)
                .build();
        Mockito.lenient().doCallRealMethod().when(productValidator).validateProductCreate(Mockito.any());

    }

    @Test
    @DisplayName("TestCreate01")
    void testCreate01() {
        // run success
        setup();
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setTitle("Product");
        createProductRequest.setDescription("Product");
        createProductRequest.setPrice(BigDecimal.TEN);
        createProductRequest.setStock(10);
        createProductRequest.setDiscount(BigDecimal.TEN);
        createProductRequest.setDiscountFrom(LocalDateTime.MIN);
        createProductRequest.setDiscountTo(LocalDateTime.MAX);
        createProductRequest.setImage("SomeUrl");
        createProductRequest.setCategoryId(1L);
        Mockito.lenient().when(categoryRepository.getReferenceById(Mockito.any()))
                .thenReturn(Category.builder().categoryId(1L).categoryCd("1").build());
        Mockito.lenient().when(categoryRepository.existsByCategoryIdAndDeleteFlagNot(Mockito.any(), Mockito.anyBoolean()))
                        .thenReturn(true);
        Assertions.assertDoesNotThrow(() -> adminProductServic.create(createProductRequest));
    }

    @Test
    @DisplayName("TestCreate02")
    void testCreate02() {
        // Category not exist
        setup();
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setTitle("Product");
        createProductRequest.setDescription("Product");
        createProductRequest.setPrice(BigDecimal.TEN);
        createProductRequest.setStock(10);
        createProductRequest.setDiscount(BigDecimal.TEN);
        createProductRequest.setDiscountFrom(LocalDateTime.MIN);
        createProductRequest.setDiscountTo(LocalDateTime.MAX);
        createProductRequest.setImage("SomeUrl");
        createProductRequest.setCategoryId(1L);
        Mockito.lenient().when(categoryRepository.existsByCategoryIdAndDeleteFlagNot(Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(false);
        String message = baseService.getMessage(MessageConstants.CATEGORY);
        PcGearException pcGearException = new PcGearException(messageSource.getMessage(MessageConstants.DATA_NOT_FOUND, new String[]{message}, LocaleContextHolder.getLocale()));
        PcGearException pcGearExceptionActual = Assertions.assertThrows(PcGearException.class, () -> adminProductServic.create(createProductRequest));
        Assertions.assertEquals(pcGearExceptionActual.getMessage(), pcGearException.getMessage());
    }


    @Test
    @DisplayName("TestCreate03")
    void testCreate03() {
        // DiscountFrom is after DiscountTo
        setup();
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setTitle("Product");
        createProductRequest.setDescription("Product");
        createProductRequest.setPrice(BigDecimal.TEN);
        createProductRequest.setStock(10);
        createProductRequest.setDiscount(BigDecimal.TEN);
        createProductRequest.setDiscountFrom(LocalDateTime.MAX);
        createProductRequest.setDiscountTo(LocalDateTime.MIN);
        createProductRequest.setImage("SomeUrl");
        createProductRequest.setCategoryId(1L);
        Mockito.lenient().when(categoryRepository.existsByCategoryIdAndDeleteFlagNot(Mockito.any(), Mockito.anyBoolean()))
                .thenReturn(true);
        String param1 = baseService.getMessage("AdminProductApi.createProductRequest.discountTo");
        String param2 = baseService.getMessage("AdminProductApi.createProductRequest.discountFrom");
        PcGearException pcGearException = new PcGearException(messageSource.getMessage(MessageConstants.MUST_BE_GREATER_THAN, new String[]{param1, param2}, LocaleContextHolder.getLocale()));
        PcGearException pcGearExceptionActual = Assertions.assertThrows(PcGearException.class, () -> adminProductServic.create(createProductRequest));
        Assertions.assertEquals(pcGearExceptionActual.getMessage(), pcGearException.getMessage());
    }

}
