package pc.gear.response.product;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProductByCodeResponse {

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "discount_from")
    private LocalDateTime discountFrom;

    @Column(name = "discount_to")
    private LocalDateTime discountTo;

    @Column(name = "image")
    private String image;

    @Column(name = "delete_fg")
    private Boolean deleteFlag;

    @Column(name = "category_cd")
    private String categoryCode;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

}
