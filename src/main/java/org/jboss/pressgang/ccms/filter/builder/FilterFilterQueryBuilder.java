package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.FilterFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.Filter;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterFilterQueryBuilder extends BaseFilterQueryBuilder<Filter> {
    private static final Logger LOG = LoggerFactory.getLogger(FilterFilterQueryBuilder.class);

    public FilterFilterQueryBuilder(final EntityManager entityManager) {
        super(Filter.class, new FilterFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.FILTER_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("filterId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.FILTER_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "filterName");
        } else if (fieldName.equals(CommonFilterConstants.FILTER_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "filterDescription");
        } else if (fieldName.equals(CommonFilterConstants.FILTER_TYPE_FILTER_VAR)) {
            addEqualsCondition("filterClassType", (Integer) field.getData());
        } else {
            super.processField(field);
        }
    }
}
