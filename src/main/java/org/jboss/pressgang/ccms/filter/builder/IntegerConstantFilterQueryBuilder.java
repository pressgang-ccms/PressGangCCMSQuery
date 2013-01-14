package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.IntegerConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class IntegerConstantFilterQueryBuilder extends BaseFilterQueryBuilder<IntegerConstants> {
    public IntegerConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(IntegerConstants.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.INTEGER_CONSTANT_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeperatedListCondition("integerConstantsId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("constantName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR)) {
            addFieldCondition(getCriteriaBuilder().equal(getRootPath().get("constantValue"), fieldValue));
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
