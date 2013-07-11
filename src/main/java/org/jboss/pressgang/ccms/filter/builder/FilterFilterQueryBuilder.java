package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFilterQueryBuilder extends BaseFilterQueryBuilder<Filter> {
    private static final Logger LOG = LoggerFactory.getLogger(FilterFilterQueryBuilder.class);

    public FilterFilterQueryBuilder(final EntityManager entityManager) {
        super(Filter.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.FILTER_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches(ID_REGEX)) {
                addIdInCommaSeparatedListCondition("filterId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.FILTER_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("filterName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("filterDescription", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.FILTER_TYPE_FILTER_VAR)) {
            try {
                final Integer typeId = Integer.parseInt(fieldValue);
                addEqualsCondition("filterClassType", typeId);
            } catch (final NumberFormatException ex) {
                LOG.debug("Malformed Filter query parameter for the \"{}\" parameter. Value = {}", fieldName, fieldValue);
            }
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
