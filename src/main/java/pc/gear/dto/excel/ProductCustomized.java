package pc.gear.dto.excel;


import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class ProductCustomized {

    @Column(name = "category_id")
    private Long category_id;

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

    @Column(name = "delete_Fg")
    private Boolean deleteFlag;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "created_datetime")
    protected LocalDateTime createdDateTime;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "updated_datetime")
    protected LocalDateTime updatedDateTime;

    @Column(name = "updated_by")
    protected String updatedBy;
}
