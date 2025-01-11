package pc.gear.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pc.gear.repository.custom.impl.ProductCustomRepositoryImpl;
import pc.gear.response.product.GetProductByCodeResponse;
import pc.gear.util.lang.JwtUtil;
import pc.gear.util.type.Role;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ut-local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductCustomRepositoryImplTest {


    @Autowired
    ProductCustomRepositoryImpl productCustomRepository;

    @Test
    @Sql(scripts = {"classpath:sql/testGetProductCode1.sql"},
    config = @SqlConfig(encoding = "utf-8"))
    void testGetProductByCode() {

        try (MockedStatic<JwtUtil> jwtUtilMockedStatic = Mockito.mockStatic(JwtUtil.class)) {
            jwtUtilMockedStatic.when(JwtUtil::getCurrentUserRole).thenReturn(Role.ADMIN);
            GetProductByCodeResponse productByCode = productCustomRepository.getProductByCode("CPU-INTEL-CORE-I5-14400F-NEW-BOX-(-10-NHÂN-16-LUỒNG-/-1.8---4.7-GHZ-/-20MB-)\n" +
                    "-4df8cac2-456c-4ecc-b2e3-e2782059f77a");
            Assertions.assertNotNull(productByCode);
        }
    }
}
