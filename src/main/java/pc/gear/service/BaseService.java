package pc.gear.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pc.gear.config.exception.PcGearException;
import pc.gear.config.exception.PcGearNotFoundException;
import pc.gear.dto.SearchRequest;
import pc.gear.dto.interfaces.ISearchRequest;
import pc.gear.util.CollectionUtil;
import pc.gear.util.Constants;
import pc.gear.util.MessageConstants;
import pc.gear.util.response.ApiError;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class BaseService {

    @Autowired
    private MessageSource messageSource;

    protected void throwError(String messageCode, Object... args) {
        throw new PcGearException(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()));
    }

    protected String getMessage(String messageCode, Object... args) {
        return messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale());
    }

    protected ApiError getBadRequestError(String messageCode, Object... args) {
        return new ApiError(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()), Constants.BAD_REQUEST_CODE);
    }

    protected void throwErrorNotFound(String messageCode, Object... args) {
        throw new PcGearNotFoundException(messageSource.getMessage(messageCode, args, LocaleContextHolder.getLocale()));
    }

    protected void validateSearchRequest(ISearchRequest searchRequest, String sortFields, String sortFieldsMessageCode) {
        validatePageSizeAndPageNumber(searchRequest);
        validateSortFields(searchRequest, List.of(this.getMessage(sortFields).split(Constants.COMMA)), sortFieldsMessageCode);
    }


    /**
     * Validate the Sort fields and the sort directions.
     *
     * @param searchRequest ISearchRequest, sortFields List<String>, sortFieldsMessageCode String
     * @author BinhSenpai
     */
    private void validateSortFields(ISearchRequest searchRequest, List<String> sortFields, String sortFieldsMessageCode) {
        if (StringUtils.isBlank(searchRequest.getSortDirections())) {
            this.throwError(MessageConstants.NOT_BLANK, this.getSortDirectionsMessage());
        }

        if (StringUtils.isBlank(searchRequest.getSortFields())) {
            this.throwError(MessageConstants.NOT_BLANK, this.getSortFieldsMessage());
        }

        List<String> sortDirectionsRequest = List.of(searchRequest.getSortDirections().split(Constants.COMMA));
        if (!CollectionUtil.allContains(Constants.SORT_DIRECTIONS, sortDirectionsRequest)) {
            this.throwError(MessageConstants.MUST_BE_IN, this.getSortDirectionsMessage(), Constants.SORT_DIRECTIONS.toString());
        }

        List<String> sortFieldsRequest = List.of(searchRequest.getSortFields().split(Constants.COMMA));
        if (!CollectionUtil.allContains(sortFields, sortFieldsRequest)) {
            this.throwError(MessageConstants.MUST_BE_IN, this.getSortFieldsMessage(), this.getMessage(sortFieldsMessageCode));
        }

        if (sortFieldsRequest.size() != sortDirectionsRequest.size()) {
            this.throwError(MessageConstants.AMOUNT_ITEM_NOT_MATCH, this.getSortDirectionsMessage(), this.getSortFieldsMessage());
        }
    }

    /**
     * Validate the Page Size and Page Number
     *
     * @param searchRequest ISearchRequest
     * @author BinhSenpai
     */
    private void validatePageSizeAndPageNumber(ISearchRequest searchRequest) {
        if (searchRequest.getPageNumber() == null) {
            this.throwError(MessageConstants.NOT_NULL, this.getPageNumberMessage());
        }

        if (searchRequest.getPageSize() == null) {
            this.throwError(MessageConstants.NOT_NULL, this.getPageSizeMessage());
        }

        if (searchRequest.getPageNumber() < Constants.NUMBER_ONE) {
            this.throwError(MessageConstants.MUST_BE_GREATER_THAN, this.getPageNumberMessage(), Constants.NUMBER_ONE);
        }

        if (searchRequest.getPageSize() < Constants.NUMBER_ONE) {
            this.throwError(MessageConstants.MUST_BE_GREATER_THAN, this.getPageSizeMessage(), Constants.NUMBER_ONE);
        }
    }

    private String getSortDirectionsMessage() {
        return this.getMessage(MessageConstants.SORT_DIRECTIONS);
    }

    private String getSortFieldsMessage() {
        return this.getMessage(MessageConstants.SORT_FIELDS);
    }

    private String getPageNumberMessage() {
        return this.getMessage(MessageConstants.PAGE_NUMBER);
    }

    private String getPageSizeMessage() {
        return this.getMessage(MessageConstants.PAGE_SIZE);
    }

    /**
     * Add pagination for native query
     *
     * @param sql           StringBuilder
     * @param searchRequest SearchRequest
     * @author BinhSenpai
     */
    protected void addPagination(StringBuilder sql, SearchRequest searchRequest) {
        String[] sortFields = searchRequest.getSortFields().split(Constants.COMMA);
        String[] sortDirections = searchRequest.getSortDirections().split(Constants.COMMA);
        int len = sortFields.length;
        // Sort
        StringBuilder query = new StringBuilder(" order by ");
        for (int i = 0; i < len; i++) {
            query.append(sortFields[i]).append(Constants.BLANK).append(sortDirections[i]);
            // Not append comma for last item
            if (i != len - 1) {
                query.append(Constants.COMMA);
            }
        }
        // Paging
        query.append(" limit ");
        query.append(caculateOffset(searchRequest)).append(Constants.COMMA).append(searchRequest.getPageSize());
        sql.append(query);
    }

    private int caculateOffset(SearchRequest searchRequest) {
        Integer pageNumber = searchRequest.getPageNumber();
        Integer pageSize = searchRequest.getPageSize();
        if (Constants.NUMBER_ONE.equals(pageNumber)) {
            return Constants.NUMBER_ZERO;
        } else {
            return (pageNumber - Constants.NUMBER_ONE) * pageSize;
        }
    }

    /**
     * Validate a search request (class that implements ISearchRequest or extends SearchRequest).
     * Will validate page number, page size, sort fields, sort directions that allow
     * After that return a Pageable for Pagination
     *
     * @param request               ISearchRequest
     * @param sortFields            String
     * @param sortFieldsMessageCode String
     * @return Pageable
     * @author BinhSenpai
     */
    protected Pageable addPaginationAndValidate(ISearchRequest request, String sortFields, String sortFieldsMessageCode) {
        this.validateSearchRequest(request, sortFields, sortFieldsMessageCode);
        // Generate pageable
        List<Sort.Order> orders = new ArrayList<>();
        String[] sortDirections = request.getSortDirections().split(Constants.COMMA);
        String[] sortFieldValues = request.getSortFields().split(Constants.COMMA);
        int l = sortDirections.length;
        for (int i = 0; i < l; i++) {
            orders.add(new Sort.Order(Sort.Direction.valueOf(sortDirections[i]), sortFieldValues[i]));
        }
        Sort sort = Sort.by(orders);
        Pageable pageable = PageRequest.of(request.getPageNumber() - 1, request.getPageSize(), sort);
        return pageable;
    }

    protected void setPreparedStatement(PreparedStatement ps, Object[] paramsArray) throws SQLException {
        for (int i = 0; i < paramsArray.length; i++) {
            ps.setObject(i + 1, paramsArray[i]);
        }
    }
}
