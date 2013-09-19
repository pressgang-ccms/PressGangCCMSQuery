package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.PropertyTagFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.PropertyTag;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class PropertyTagFilterQueryBuilder extends BaseFilterQueryBuilder<PropertyTag> {
    public PropertyTagFilterQueryBuilder(final EntityManager entityManager) {
        super(PropertyTag.class, new PropertyTagFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.PROP_TAG_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("propertyTagId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.PROP_TAG_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagName");
        } else if (fieldName.equals(CommonFilterConstants.PROP_TAG_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "propertyTagDescription");
        } else {
            super.processField(field);
        }
    }
}
