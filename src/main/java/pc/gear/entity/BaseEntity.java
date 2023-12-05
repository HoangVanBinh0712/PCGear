package pc.gear.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import pc.gear.config.audit.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "creataedDateTime")
    private LocalDateTime createdDateTime;

    @CreatedBy
    @Column(name = "createdBy")
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updatedDateTime")
    private LocalDateTime updatedDateTime;

    @LastModifiedBy
    @Column(name = "updatedBy")
    private String updatedBy;
}
