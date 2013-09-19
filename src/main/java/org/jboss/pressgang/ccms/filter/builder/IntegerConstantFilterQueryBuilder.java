package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.IntegerConstantFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class IntegerConstantFilterQueryBuilder extends BaseFilterQueryBuilder<IntegerConstants> {
    public IntegerConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(IntegerConstants.class, new IntegerConstantFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("integerConstantsId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "constantName");
        } else if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_VALUE_FILTER_VAR)) {
            addEqualsCondition("constantValue", (Integer) field.getData());
        } else {
            super.processField(field);
        }
    }
}
