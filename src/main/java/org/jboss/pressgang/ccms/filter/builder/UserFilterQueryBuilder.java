package org.jboss.pressgang.ccms.filter.builder;

import javax.persistence.EntityManager;

import java.util.List;

import org.jboss.pressgang.ccms.filter.UserFieldFilter;
import org.jboss.pressgang.ccms.filter.base.BaseFilterQueryBuilder;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldDataBase;
import org.jboss.pressgang.ccms.filter.structures.FilterFieldStringData;
import org.jboss.pressgang.ccms.model.User;
import org.jboss.pressgang.ccms.utils.constants.CommonFilterConstants;

public class UserFilterQueryBuilder extends BaseFilterQueryBuilder<User> {

    public UserFilterQueryBuilder(final EntityManager entityManager) {
        super(User.class, new UserFieldFilter(), entityManager);
    }

    @Override
    public void processField(final FilterFieldDataBase<?> field) {
        final String fieldName = field.getBaseName();

        if (fieldName.equals(CommonFilterConstants.USER_IDS_FILTER_VAR)) {
            addIdInCollectionCondition("userId", (List<Integer>) field.getData());
        } else if (fieldName.equals(CommonFilterConstants.USER_NAME_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "userName");
        } else if (fieldName.equals(CommonFilterConstants.USER_DESCRIPTION_FILTER_VAR)) {
            processStringField((FilterFieldStringData) field, "userDescription");
        } else {
            super.processField(field);
        }
    }
}
