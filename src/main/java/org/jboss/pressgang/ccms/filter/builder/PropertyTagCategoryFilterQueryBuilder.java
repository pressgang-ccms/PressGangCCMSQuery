package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.PropertyTagCategoryFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class PropertyTagCategoryFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTagCategory> {
    public PropertyTagCategoryFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTagCategory.class, new PropertyTagCategoryFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("propertyTagCategoryId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagCategoryName");
        } else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagCategoryDescription");
        } else {
            super.processField(field);
        }
    }
}
