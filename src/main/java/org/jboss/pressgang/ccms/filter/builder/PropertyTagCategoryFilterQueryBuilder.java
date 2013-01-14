package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.PropertyTagCategory;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class PropertyTagCategoryFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTagCategory> {
    public PropertyTagCategoryFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTagCategory.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("propertyTagCategoryId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("propertyTagCategoryName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.PROP_CATEGORY_DESCRIPTION_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("propertyTagCategoryDescription", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
