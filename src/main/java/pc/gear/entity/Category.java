package pc.gear.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
@Builder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_cd")
    private String categoryCd;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "delete_Fg")
    private Boolean deleteFlag;

    @PrePersist
    private void prePersist() {
        if (this.deleteFlag == null) {
            this.deleteFlag = Boolean.FALSE;
        }
    }


}
