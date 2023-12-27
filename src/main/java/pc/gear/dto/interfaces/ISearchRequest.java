package pc.gear.dto.interfaces;

public interface ISearchRequest {
    Integer getPageNumber();

    Integer getPageSize();

    String getSortFields();

    String getSortDirections();
}
