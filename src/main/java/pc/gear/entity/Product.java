package pc.gear.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pc.gear.util.Constants;
import pc.gear.util.lang.StringUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
@Builder
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

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

    @PrePersist
    public void prePersist() {
        // Replace spaces with hyphens
        this.productCode = StringUtil.generateCode(this.title);
        this.deleteFlag = this.deleteFlag != null ? this.deleteFlag : Boolean.FALSE;
    }
}
