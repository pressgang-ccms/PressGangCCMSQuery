package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.StringConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class StringConstantFilterQueryBuilder extends BaseFilterQueryBuilder<StringConstants> {
    public StringConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(StringConstants.class, new StringConstantFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("stringConstantsId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantName");
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantValue");
        } else {
            super.processField(field);
        }
    }
}
