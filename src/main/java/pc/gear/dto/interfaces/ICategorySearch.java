package pc.gear.dto.interfaces;

import java.time.LocalDateTime;

public interface ICategorySearch {
    Long getCategoryId();

    void setCategoryId(Long categoryId);

    String getCategoryCd();

    void setCategoryCd(String categoryCd);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    Boolean getDeleteFlag();

    void setDeleteFlag(Boolean deleteFlag);

    LocalDateTime getCreatedDateTime();

    void setCreatedDateTime(LocalDateTime createdDateTime);

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    LocalDateTime getUpdatedDateTime();

    void setUpdatedDateTime(LocalDateTime updatedDateTime);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);
}
