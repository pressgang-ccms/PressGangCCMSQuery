package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.model.StringConstants;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class StringConstantFilterQueryBuilder extends BaseFilterQueryBuilder<StringConstants> {
    public StringConstantFilterQueryBuilder(final EntityManager entityManager) {
        super(StringConstants.class, entityManager);
    }

    @Override
    public void processFilterString(final String fieldName, final String fieldValue) {
        if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_IDS_FILTER_VAR)) {
            if (fieldValue.trim().length() != 0 && fieldValue.matches("^((\\s)*\\d+(\\s)*,?)*((\\s)*\\d+(\\s)*)$")) {
                addIdInCommaSeparatedListCondition("stringConstantsId", fieldValue);
            }
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_NAME_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("constantName", fieldValue);
        } else if (fieldName.equals(CommonFilterConstants.STRING_CONSTANT_VALUE_FILTER_VAR)) {
            addLikeIgnoresCaseCondition("constantValue", fieldValue);
        } else {
            super.processFilterString(fieldName, fieldValue);
        }
    }
}
